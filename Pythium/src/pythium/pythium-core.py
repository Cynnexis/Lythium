# -*- coding: utf-8  -*-

import os
import sys
import argparse
import html
import json
import os
import re
import time
import urllib
import webbrowser
from threading import Thread, Event
from typing import Union, Tuple

import simplejson
from rauth import OAuth2Service, OAuth1Service

from local_server import LocalServer
from saver import Saver

# Configuring arguments
parser = argparse.ArgumentParser()
parser.add_argument("-o", "--output", help="Output filename. If none, no output file is provided.",
                    type=str, default=None)
parser.add_argument("-r", "--repository", help="Repository where the files 'spotify-client-id.txt', "
                                               "'spotify-client-secret.txt', 'genius-client-id.txt' and "
                                               "'genius-client-secret.txt' are stored. Default is working directory",
                    type=str, default="./")
parser.add_argument("-i", "--print-info", help="Print the music name, artists and lyrics in the console",
                    action="store_true")
parser.add_argument("-j", "--print-json", help="Print the json results in the console",
                    action="store_true")
parser.add_argument("-s", "--spotify-bundle", help="Output the spotify bundle as a JSON file to give in argument",
                    type=str, default=None)
parser.add_argument("-d", "--debug", help="Print debug information in the console",
                    action="store_true")
args = parser.parse_args()


def search_code_from_data(data: str) -> str:
	"""
	Search the authenticaton code in a string
	:param data: The string to parse
	:return: The code as a string
	"""
	code = None
	# Search code in 'data'
	if data is None or data == "" or data == b'':
		code = None
	else:
		# Search for the URL attribute 'code'
		code = data.split('&code=')
		# If it does not exist, change the string to search (in url, '&' and '?' has the same meaning)
		if len(code) == 1:
			code = data.split('?code=')
		
		# Get the code
		if len(code) == 1:
			code = code[0]
		else:
			code = code[1]
			# Format it a bit to avoid any bug
			code = code.split('&')[0].split(' ')[0].split('\r')[0].split('\r\n')[0].split('\n')[0]
	return code


def get_session_using_saver(saver: Saver, sess: Union[OAuth2Service, OAuth1Service], code: str,
                            default_expiration_time_second: int = 3600, name: str = "") \
		-> Tuple[Union[OAuth2Service, OAuth1Service], Union[str, None], Union[str, None], Union[str, None],
		         Union[str, int, None]]:
	global args
	
	def global_decoder(payload):
		"""
		Decoder for global API
		:param payload: The payload to decode
		:return: Return a dictionary that contains all data given by the API
		"""
		results = json.loads(payload.decode('utf-8'))
		if "access_token" in results.keys():
			saver["access_token"] = results["access_token"]
			saver["access_token_timestamp"] = int(time.time())
			if args.debug:
				print("{} access token = {}".format(name, results["access_token"]))
		if "refresh_token" in results.keys():
			saver["refresh_token"] = results["refresh_token"]
		if "token_type" in results.keys():
			saver["token_type"] = results["token_type"]
		if "expires_in" in results.keys():
			saver["expires_in"] = results["expires_in"]
		return results
	
	# If the access and refresh tokens are already saved, use them
	if {"access_token", "access_token_timestamp", "token_type"}.issubset(
			saver.keys()) and saver.get("access_token_timestamp", None) is not None and int(time.time()) - \
			int(saver["access_token_timestamp"]) <= int(saver.get("expires_in", default_expiration_time_second)):
		access_token = saver["access_token"]
		if args.debug:
			print("{} access token = {}".format(name, access_token))
		if "refresh_token" in saver.keys():
			refresh_token = saver["refresh_token"]
		else:
			refresh_token = None
		token_type = saver["token_type"]
		if "expires_in" in saver.keys():
			expires_in = saver["expires_in"]
		else:
			expires_in = None
		session = sess.get_session(token=access_token)
	else:
		access_token = refresh_token = token_type = expires_in = None
		data = {
			"code": code,
			"grant_type": "authorization_code",
			"redirect_uri": redirect_uri
		}
		if args.debug:
			print("{} payload = {}".format(name, data))
		session = sess.get_auth_session(data=data, decoder=global_decoder)
	
	return session, access_token, refresh_token, token_type, expires_in


# The default redirection URI
redirect_uri = "http://localhost:6814"

# Start a local server on the following port:
server = LocalServer(port=6814)
server.start()

# Create a saver that map "music_name|artists" -> "lyrics" as an history list and a cache at the same time. Hence, if
# the program requires lyrics from a song and that song has already been added to this saver, no need to connect to
# Genius API.
music_saver = Saver(path=".cache-music-history.json")

# GETTING CREDENTIAL #

# Spotify API
with open(os.path.join(args.repository, "spotify-client-id.txt"),
          'r') as f:  # "../../../res/misc/spotify-client-id.txt"
	SPOTIFY_CLIENT_ID = f.read().replace('\n', '')

with open(os.path.join(args.repository, "spotify-client-secret.txt"), 'r') as f:
	SPOTIFY_CLIENT_SECRET = f.read().replace('\n', '')

# Get a spotify_saver to save all the credentials to save time
spotify_saver = Saver(".cache-rauth-spotify.json")

# Create the OAuth2 service instance for Spotify API
spotify = OAuth2Service(
	client_id=SPOTIFY_CLIENT_ID,
	client_secret=SPOTIFY_CLIENT_SECRET,
	name="spotify",
	authorize_url="https://accounts.spotify.com/authorize",
	access_token_url="https://accounts.spotify.com/api/token",
	base_url="https://api.spotify.com/v1/",
)

# Search if there is a save:
if spotify_saver.get("code", None) is not None and spotify_saver.get("code_timestamp", None) is not None and \
		int(time.time()) - int(spotify_saver["code_timestamp"]) <= 3600:
	spotify_code = str(spotify_saver["code"])
else:
	# Connect to the Spotify API to get the authentication code
	params = {
		"scope": "user-read-currently-playing",
		"response_type": "code",
		"redirect_uri": redirect_uri
	}
	# Launch server
	e = Event()
	
	
	def launch_after(e: Event):
		"""
		Open the authorize URL to the Spotify API through a new tab in the default browser.
		:param e:  The event. The program will wait until the event is set to launch the URL page.
		"""
		e.wait()
		webbrowser.open_new_tab(spotify.get_authorize_url(**params))
	
	
	# Launch the webbrowser command in another thread
	Thread(target=launch_after, args=(e,)).start()
	data = None
	# Get the raw data from the connection to the Spotify API
	while data is None or data == "":
		e.set()
		data = server.wait_for_connection()
	
	# Get the code from the data
	spotify_code = search_code_from_data(data)
	spotify_saver["code"] = spotify_code
	spotify_saver["code_timestamp"] = int(time.time())

if args.debug:
	print("Spotify auth code = {}".format(spotify_code))

spotify_session, spotify_access_token, spotify_refresh_token, spotify_token_type, spotify_expires_in = get_session_using_saver(
	saver=spotify_saver, sess=spotify, code=spotify_code, name="Spotify"
)

# GETTING CONTENT #

# Spotify API: Fetch the current music

try:
	spotify_results = spotify_session.get("me/player/currently-playing").json()
except simplejson.errors.JSONDecodeError:
	print("ERROR: No data available on Spotify. Please make sure that you have accepted the link between this " +
	      "application and your Spotify account, and you are listening to a music on the same Spotify account.")
	sys.exit(1)

artists = ""
for a in spotify_results["item"]["artists"]:
	if artists == "":
		artists = a["name"]
	else:
		artists = artists + ", " + a["name"]

music_name = spotify_results["item"]["name"]

if args.print_info:
	print("You're listening to {} by {}.".format(music_name, artists))

# Before calling the Genius API, check if the music is not already in the history log:
potential_lyrics = music_saver.get("{}|{}".format(music_name, artists), None)
if potential_lyrics is None or potential_lyrics == "":
	# Genius API
	
	with open(os.path.join(args.repository, "genius-client-id.txt"), 'r') as f:  # ../../../res/misc/genius-client-id.txt
		GENIUS_CLIENT_ID = f.read().replace('\n', '')
	
	with open(os.path.join(args.repository, "genius-client-secret.txt"), 'r') as f:
		GENIUS_CLIENT_SECRET = f.read().replace('\n', '')
	
	genius_saver = Saver(".cache-rauth-genius.json")
	
	genius = OAuth2Service(
		client_id=GENIUS_CLIENT_ID,
		client_secret=GENIUS_CLIENT_SECRET,
		name="genius",
		authorize_url="https://api.genius.com/oauth/authorize",
		access_token_url="https://api.genius.com/oauth/token",
		base_url="https://api.genius.com/",
	)
	
	
	def get_genius_code():
		genius_params = {
			"response_type": "code",
			"redirect_uri": redirect_uri
		}
		global genius_code
		global genius_saver
		global server
		
		e = Event()
		
		def launch_after(e: Event):
			e.wait()
			webbrowser.open_new_tab(genius.get_authorize_url(**genius_params))
		
		Thread(target=launch_after, args=(e,)).start()
		
		data = None
		while data is None or data == "":
			e.set()
			data = server.wait_for_connection()
		genius_code = search_code_from_data(data)
		server.stop()
		server.on_data_received = lambda _: server.stop()
		
		genius_saver["code"] = genius_code
		genius_saver["code_timestamp"] = int(time.time())
	
	
	# Search if there is a save:
	if genius_saver.get("code", None) is not None and genius_saver.get("code_timestamp", None) is not None and \
			int(time.time()) - int(genius_saver["code_timestamp"]) <= 3600:
		genius_code = str(genius_saver["code"])
	else:
		# Connect to the Genius API to get the authentication code
		get_genius_code()
	
	if args.debug:
		print("Genius code = {}".format(genius_code))
	
	try:
		genius_session, genius_access_token, genius_refresh_token, genius_token_type, genius_expires_in = get_session_using_saver(
			saver=genius_saver, sess=genius, code=genius_code, default_expiration_time_second=1800, name="Genius"
		)
	except KeyError:
		get_genius_code()
		if args.debug:
			print("Genius code were wrong. New Genius code = {}".format(genius_code))
		genius_session, genius_access_token, genius_refresh_token, genius_token_type, genius_expires_in = get_session_using_saver(
			saver=genius_saver, sess=genius, code=genius_code, default_expiration_time_second=1800, name="Genius"
		)
	
	search_query = {
		'q': "{} {}".format(music_name, artists.split(',')[0])
	}
	if args.debug:
		print("Searching \"{}\" in Genius library".format(search_query['q']))
	search_query = urllib.parse.urlencode(search_query).replace('+', '%20')
	results = genius_session.get("search?{}".format(search_query)).json()
	
	result_hits = results["response"]["hits"]
	
	first_result = None
	for h in result_hits:
		if h["type"] == "song":
			first_result = h
			break
	
	content = "No lyrics found."
	if first_result is not None:
		first_result_path = first_result["result"]["api_path"]
		
		results = genius_session.get(first_result_path).json()
		path = results["response"]["song"]["path"]
		url = "https://genius.com" + path
		req = urllib.request.Request(url=url, headers={'User-Agent': 'Mozilla/5.0'})
		with urllib.request.urlopen(req) as f:
			content = f.read().decode("utf-8")
		content = content.split('<div class="lyrics">')[1]
		content = content.split('<!--sse-->')[1]
		content = content.split('<!--/sse-->')[0]
		content = re.sub(r"<\/?[^>]+>", "", content)
		content = re.sub(r"(\n|\r\n|\n\r|\s)*\0?$", "", content)
		content = re.sub(r"^(\n|\r\n|\n\r|\s)*", "", content)
		content = html.unescape(content)
	
	# Save the lyrics
	music_saver["{}|{}".format(music_name, artists)] = content
else:
	content = potential_lyrics

if args.print_info:
	print("\n{}\n".format(content))

# Finally, embed all data into a dictionary to save it as a JSON file
results = {
	"music": music_name,
	"artists": artists,
	"lyrics": content
}
if args.print_json:
	print(results)

# Save results to a file
if args.output is not None and args.output != "":
	with open(args.output, 'w') as f:
		json.dump(results, f)

# Save the spotify bundle if asked
if args.spotify_bundle is not None and args.spotify_bundle != "":
	with open(args.spotify_bundle, 'w') as f:
		json.dump(spotify_results, f)
