# -*- coding: utf-8  -*-

import os
import sys

import spotipy
import spotipy.util as util

with open("../../res/misc/spotify-client-id.txt", 'r') as f:
	SPOTIPY_CLIENT_ID = f.read()

with open("../../res/misc/spotify-client-secret.txt", 'r') as f:
	SPOTIPY_CLIENT_SECRET = f.read()

with open("../../res/misc/spotify-redirect-uri.txt", 'r') as f:
	SPOTIPY_REDIRECT_URI = f.read()

os.environ["SPOTIPY_CLIENT_ID"] = SPOTIPY_CLIENT_ID
os.environ["SPOTIPY_CLIENT_SECRET"] = SPOTIPY_CLIENT_SECRET
os.environ["SPOTIPY_REDIRECT_URI"] = SPOTIPY_REDIRECT_URI

if len(sys.argv) > 1:
	username = sys.argv[1]
else:
	print("Usage: %s username" % (sys.argv[0],))
	sys.exit()

scope = 'user-read-currently-playing'
token = util.prompt_for_user_token(username=username, scope=scope, client_id=SPOTIPY_CLIENT_ID,
                                   client_secret=SPOTIPY_CLIENT_SECRET, redirect_uri=SPOTIPY_REDIRECT_URI)

if token:
	sp = spotipy.Spotify(auth=token)
	sp.trace = False
	results = sp.current_user_playing_track()
	print(results)
else:
	print("Can't get token for", username)
