# -*- coding: utf-8  -*-

import os
import re
import select
import sys
import argparse
import json
import socket
import time
import urllib
import webbrowser

from rauth import OAuth2Service

from pythium.local_server import LocalServer
from pythium.saver import Saver

redirect_uri = "http://localhost:6814"


def create_local_server_for_code(authorize_url: str):
	# Read the HTML template
	template = "<html><body>The program successfully received the code! You can now close this tab.</body></html>"
	try:
		with open("../../res/html/index.html") as f:
			template = f.read()
	except IOError:
		pass
	
	# Create a server
	serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	serversocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	serversocket.bind(("localhost", 6814))
	serversocket.listen(5)  # number of max connections
	data = ""
	code = None
	continue_server = True
	webbrowser.open_new_tab(authorize_url)
	while continue_server:
		# Block the program until a connection is received
		(clientsocket, address) = serversocket.accept()
		
		# Get the raw data
		while True:
			clientsocket.setblocking(0)
			ready = select.select([clientsocket], [], [], 5)
			if ready[0]:
				datum = clientsocket.recv(4096)
				data = data + datum.decode("utf-8")
				if not datum or datum == b'' or datum == '':
					break
			else:
				break
		
		# Send an answer to the client
		clientsocket.sendto(("HTTP/1.1 200 OK\n" +
		                     "Content-Type: text/html\n" +
		                     "\n" +
		                     template +
		                     "\n").encode("utf-8"), address)
		# Close the connection with the client
		try:
			clientsocket.shutdown(socket.SHUT_WR)
		except OSError:
			pass
		clientsocket.close()
		
		# Search code in 'data'
		if data is None or data == "" or data == b'':
			code = None
		else:
			code = data.split('&code=')
			if len(code) == 1:
				code = data.split('?code=')
			
			if len(code) == 1:
				code = code[0]
			else:
				code = code[1]
			code = code.split('&')[0].split(' ')[0]
			continue_server = False
	
	serversocket.close()
	return code


server = LocalServer(port=6814)
server.start(sync=False)

# GETTING CREDENTIAL #

# Spotify API
with open("../../../res/misc/spotify-client-id.txt", 'r') as f:
	SPOTIFY_CLIENT_ID = f.read().replace('\n', '')

with open("../../../res/misc/spotify-client-secret.txt", 'r') as f:
	SPOTIFY_CLIENT_SECRET = f.read().replace('\n', '')

saver = Saver(".cache-rauth-spotify.json")

spotify = OAuth2Service(
	client_id=SPOTIFY_CLIENT_ID,
	client_secret=SPOTIFY_CLIENT_SECRET,
	name="spotify",
	authorize_url="https://accounts.spotify.com/authorize",
	access_token_url="https://accounts.spotify.com/api/token",
	base_url="https://api.spotify.com/v1/",
)

# Search if there is a save:
if saver.get("code", None) is not None and saver.get("code_timestamp", None) is not None and int(time.time()) - int(saver["code_timestamp"]) <= 3600:
	spotify_code = str(saver["code"])
else:
	params = {
		"scope": "user-read-currently-playing",
		"response_type": "code",
		"redirect_uri": redirect_uri
	}
	# Launch server
	spotify_code = None
	
	def spotify_on_data_received(data):
		global spotify_code
		# Search code in 'data'
		if data is None or data == "" or data == b'':
			spotify_code = None
		else:
			spotify_code = data.split('&code=')
			if len(spotify_code) == 1:
				spotify_code = data.split('?code=')
			
			if len(spotify_code) == 1:
				spotify_code = spotify_code[0]
			else:
				spotify_code = spotify_code[1]
			spotify_code = spotify_code.split('&')[0].split(' ')[0].split('\r')[0].split('\r\n')[0].split('\n')[0]
	server.on_data_received = spotify_on_data_received
	webbrowser.open_new_tab(spotify.get_authorize_url(**params))
	while spotify_code is None:
		pass
	server.on_data_received = None
	print("Spotify code = {}".format(spotify_code))
	#spotify_code = create_local_server_for_code(authorize_url=spotify.get_authorize_url(**params))
	saver["code"] = spotify_code
	saver["code_timestamp"] = int(time.time())


# Create a decoder
def decoder_spotify(payload):
	global saver
	results = json.loads(payload.decode('utf-8'))
	if "access_token" in results.keys():
		saver["access_token"] = results["access_token"]
		saver["access_token_timestamp"] = int(time.time())
		print("Access token: {}".format(results["access_token"]))
	if "refresh_token" in results.keys():
		saver["refresh_token"] = results["refresh_token"]
	if "token_type" in results.keys():
		saver["token_type"] = results["token_type"]
	if "expires_in" in results.keys():
		saver["expires_in"] = results["expires_in"]
	return results


# If the access and refresh tokens are already saved, use them
if {"access_token", "access_token_timestamp", "refresh_token", "token_type", "expires_in"}.issubset(saver.keys()) and saver.get("access_token_timestamp", None) is not None and int(time.time()) - int(saver["access_token_timestamp"]) <= 3600:
	spotify_access_token = saver["access_token"]
	print("Access token: {}".format(spotify_access_token))
	spotify_refresh_token = saver["refresh_token"]
	spotify_token_type = saver["token_type"]
	spotify_expires_in = saver["expires_in"]
else:
	spotify_access_token = spotify_refresh_token = spotify_token_type = spotify_expires_in = None

if spotify_access_token is not None and spotify_access_token != "":
	spotify_session = spotify.get_session(token=spotify_access_token)
else:
	spotify_data = {
		"code": spotify_code,
		"grant_type": "authorization_code",
		"redirect_uri": redirect_uri
	}
	spotify_session = spotify.get_auth_session(data=spotify_data, decoder=decoder_spotify)

# Genius API

with open("../../../res/misc/genius-client-id.txt", 'r') as f:
	GENIUS_CLIENT_ID = f.read().replace('\n', '')

with open("../../../res/misc/genius-client-secret.txt", 'r') as f:
	GENIUS_CLIENT_SECRET = f.read().replace('\n', '')

genius = OAuth2Service(
	client_id=GENIUS_CLIENT_ID,
	client_secret=GENIUS_CLIENT_SECRET,
	name="genius",
	authorize_url="https://api.genius.com/oauth/authorize",
	access_token_url="https://api.genius.com/oauth/token",
	base_url="https://api.genius.com/",
)

genius_params = {
	"response_type": "code",
	"redirect_uri": redirect_uri
}

genius_code = None

def genius_on_data_received(data):
	global genius_code
	# Search code in 'data'
	if data is None or data == "" or data == b'':
		genius_code = None
	else:
		genius_code = data.split('&code=')
		if len(genius_code) == 1:
			genius_code = data.split('?code=')
		
		if len(genius_code) == 1:
			genius_code = genius_code[0]
		else:
			genius_code = genius_code[1]
		genius_code = genius_code.split('&')[0].split(' ')[0].split('\r')[0].split('\r\n')[0].split('\n')[0]

server.on_data_received = genius_on_data_received
webbrowser.open_new_tab(genius.get_authorize_url(**genius_params))
while genius_code is None:
	pass
server.on_data_received = None
#server.stop()
#genius_code = create_local_server_for_code(authorize_url=genius.get_authorize_url(**genius_params))
print("Genius code = {}".format(genius_code))


# Create a decoder
def decoder_genius(payload):
	return json.loads(payload.decode('utf-8'))


genius_data = {
	"code": genius_code,
	"grant_type": "authorization_code",
	"redirect_uri": redirect_uri
}
print("Genius data = {}".format(genius_data))
genius_session = genius.get_auth_session(data=genius_data, decoder=decoder_genius)


# GETTING CONTENT #

# Spotify API: Fetch the current music

results = spotify_session.get("me/player/currently-playing").json()

artists = ""
for a in results["item"]["artists"]:
	if artists == "":
		artists = a["name"]
	else:
		artists = artists + ", " + a["name"]

music_name = results["item"]["name"]
print("You're listening to {} by {}.".format(music_name, artists))


# Genius API

search_query = {
	'q': "{} {}".format(music_name, artists.split(',')[0])
}
print(search_query)
search_query = urllib.parse.urlencode(search_query).replace('+', '%20')
results = genius_session.get("search?{}".format(search_query)).json()

result_hits = results["response"]["hits"]

first_result = None
for h in result_hits:
	if h["type"] == "song":
		first_result = h
		break

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
content = content.replace("<p>", "")
content = content.replace("</p>", "")
content = content.replace("<br>", "")
content = content.replace("<br/>", "")
content = content.replace("<br />", "")
content = content.replace("</br>", "")
content = content.replace("</a>", "")
content = re.sub(r"<a[^>]*>", "", content)
print(content)

server.stop()
