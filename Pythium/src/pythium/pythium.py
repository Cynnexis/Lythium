# -*- coding: utf-8  -*-

import os
import sys
import argparse
import json

sys.path.append(os.getcwd())
sys.path.append("/".join(os.path.abspath(os.path.dirname(__file__)).split("/")[:-1]))

import spotipy
import spotipy.util as util


# Use argparse to get the files which contain the client ID, client secret and the redirect URL:
parser = argparse.ArgumentParser()
parser.add_argument("username", help="User name on Spotify", type=str)
parser.add_argument("-i", "--client-id", help="File where the client ID is stored", type=str,
                    default="spotify-client-id.txt")
parser.add_argument("-s", "--client-secret", help="File where the client secret is stored", type=str,
                    default="spotify-client-secret.txt")
parser.add_argument("-r", "--redirect-uri", help="File where the redirect URI is stored", type=str,
                    default="spotify-redirect-uri.txt")
args = parser.parse_args()

with open(args.client_id, 'r') as f:  # "../../res/misc/spotify-client-id.txt"
	SPOTIPY_CLIENT_ID = f.read()

with open(args.client_secret, 'r') as f:
	SPOTIPY_CLIENT_SECRET = f.read()

with open(args.redirect_uri, 'r') as f:
	SPOTIPY_REDIRECT_URI = f.read()

os.environ["SPOTIPY_CLIENT_ID"] = SPOTIPY_CLIENT_ID
os.environ["SPOTIPY_CLIENT_SECRET"] = SPOTIPY_CLIENT_SECRET
os.environ["SPOTIPY_REDIREC" \
           "T_URI"] = SPOTIPY_REDIRECT_URI

if len(sys.argv) > 1:
	username = args.username
else:
	print("<ERROR>")
	sys.exit()

scope = 'user-read-currently-playing'
token = util.prompt_for_user_token(username=username, scope=scope, client_id=SPOTIPY_CLIENT_ID,
                                   client_secret=SPOTIPY_CLIENT_SECRET, redirect_uri=SPOTIPY_REDIRECT_URI)

if token:
	sp = spotipy.Spotify(auth=token)
	sp.trace = False
	results = sp.current_user_playing_track()
	print(results)
	with open("current_user_playing_track.tmp.json", 'w') as f:
		f.write(json.dumps(results))
else:
	print("<ERROR>")
