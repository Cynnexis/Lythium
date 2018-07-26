# -*- coding: utf-8 -*-
import json

from rauth.service import OAuth2Service
import select
import socket
import webbrowser

with open("../../../res/misc/spotify-client-id.txt", 'r') as f:
	SPOTIFY_CLIENT_ID = f.read().replace('\n', '')

with open("../../../res/misc/spotify-client-secret.txt", 'r') as f:
	SPOTIFY_CLIENT_SECRET = f.read().replace('\n', '')

SPOTIFY_REDIRECT_URI = "http://localhost:6814"

spotify = OAuth2Service(
	client_id=SPOTIFY_CLIENT_ID,
	client_secret=SPOTIFY_CLIENT_SECRET,
	name="spotify",
	authorize_url="https://accounts.spotify.com/authorize",
	access_token_url="https://accounts.spotify.com/api/token",
	base_url="https://api.spotify.com/v1/",
	
)

params = {
	"scope": "user-read-currently-playing",
	"response_type": "code",
	"redirect_uri": SPOTIFY_REDIRECT_URI
}

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
webbrowser.open_new_tab(spotify.get_authorize_url(**params))
(clientsocket, address) = serversocket.accept()
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

clientsocket.sendto(("HTTP/1.1 200 OK\n" +
                     "Content-Type: text/html\n" +
                     "\n" +
                     template +
                     "\n").encode("utf-8"), address)
try:
	clientsocket.shutdown(socket.SHUT_WR)
except OSError:
	pass
clientsocket.close()
serversocket.close()

# Search code in 'data'
code = None
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

# Create a decoder
def decoder(payload):
	return json.loads(payload.decode('utf-8'))

data = {
	"code": code,
	"grant_type": "authorization_code",
	"redirect_uri": SPOTIFY_REDIRECT_URI
}
session = spotify.get_auth_session(data=data, decoder=decoder)
results = session.get("me/player/currently-playing").json()

artists = ""
for a in results["item"]["artists"]:
	if artists == "":
		artists = a["name"]
	else:
		artists = artists + ", " + a["name"]

print("You're listening to {} by {}.".format(results["item"]["name"], artists))





