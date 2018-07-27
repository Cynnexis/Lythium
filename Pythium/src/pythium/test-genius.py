# -*- coding: utf-8 -*-
import sys
import json

from rauth.service import OAuth2Service
import select
import socket
import webbrowser
import urllib
import urllib.request
import re

with open("../../../res/misc/genius-client-id.txt", 'r') as f:
	GENIUS_CLIENT_ID = f.read().replace('\n', '')

with open("../../../res/misc/genius-client-secret.txt", 'r') as f:
	GENIUS_CLIENT_SECRET = f.read().replace('\n', '')

GENIUS_REDIRECT_URI = "http://localhost:6814"

genius = OAuth2Service(
	client_id=GENIUS_CLIENT_ID,
	client_secret=GENIUS_CLIENT_SECRET,
	name="genius",
	authorize_url="https://api.genius.com/oauth/authorize",
	access_token_url="https://api.genius.com/oauth/token",
	base_url="https://api.genius.com/",

)

params = {
#	"scope": "",
	"response_type": "code",
	"redirect_uri": GENIUS_REDIRECT_URI
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
webbrowser.open_new_tab(genius.get_authorize_url(**params))
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

print("code = {}".format(code))


# Create a decoder
def decoder(payload):
	results = json.loads(payload.decode('utf-8'))
	if "access_token" in results.keys():
		print("Access token: {}".format(results["access_token"]))
	return results


data = {
	"code": code,
	"grant_type": "authorization_code",
	"redirect_uri": GENIUS_REDIRECT_URI
}
session = genius.get_auth_session(data=data, decoder=decoder)

search_query = {
	'q': "Ocean Seven Lions" # To change
}
search_query = urllib.parse.urlencode(search_query).replace('+', '%20')
results = session.get("search?{}".format(search_query)).json()

result_hits = results["response"]["hits"]

first_result = None
for h in result_hits:
	if h["type"] == "song":
		first_result = h
		break

first_result_path = first_result["result"]["api_path"]

results = session.get(first_result_path).json()
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
