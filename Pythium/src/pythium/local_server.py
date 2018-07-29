# -*- coding: utf-8 -*-
import select
import socket
from enum import Enum
from threading import Thread


class LocalServer:
	class State(Enum):
		INITIALIZING = 0,
		STARTED = 1,
		PAUSED = 2,
		STOPPED = 3
	
	def __init__(self, port: int = 8080, max_connection: int = 5, on_state_changed=None, on_server_initialized=None):
		self.server = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
		self.server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
		self.port = port
		self.max_connection = max_connection
		self.__state = LocalServer.State.INITIALIZING
		self.on_state_changed = on_state_changed
		self.on_server_initialized = on_server_initialized
		self.template = "<html><body>The program successfully received the code! You can now close this tab.</body></html>"
		try:
			with open("../../res/html/index.html") as f:
				self.template = f.read()
		except IOError:
			pass
	
	def start(self):
		self.server.bind(("localhost", self.port))
		self.server.listen(self.max_connection)  # number of max connections
		self.state = LocalServer.State.STARTED
		if self.on_server_initialized is not None and callable(self.on_server_initialized):
			try:
				self.on_server_initialized()
			except TypeError:
				pass
		
	def pause(self):
		self.state = LocalServer.State.PAUSED
	
	def stop(self):
		self.state = LocalServer.State.STOPPED
		self.server.close()
	
	def wait_for_connection(self, timeout_s: float = 1.) -> str:
		(client, address) = self.server.accept()
		
		data = ""
		# Get the raw data
		while True:
			client.setblocking(0)
			ready = select.select([client], [], [], timeout_s)
			if ready[0]:
				datum = client.recv(4096)
				data = data + datum.decode("utf-8")
				if not datum or datum == b'' or datum == '':
					break
			else:
				break
		
		# Send an answer to the client
		client.sendto(("HTTP/1.1 200 OK\n" +
		               "Content-Type: text/html\n" +
		               "\n" +
		               self.template +
		               "\n").encode("utf-8"), address)
		# Close the connection with the client
		try:
			client.shutdown(socket.SHUT_WR)
		except OSError:
			pass
		client.close()
		return data
	
	# GETTERS & SETTERS #
	
	def get_state(self) -> State:
		return self.__state
	
	def set_state(self, state: State) -> None:
		def iter_on_state_changed(on_state_changed, state):
			for f in on_state_changed:
				if f is not None and callable(f):
					try:
						f(state)
					except TypeError:
						pass
		
		if self.__state != state:
			self.__state = state
			try:
				if self.on_state_changed is not None:
					if callable(self.on_state_changed):
						self.on_state_changed(self.__state)
					elif isinstance(self.on_state_changed, list) or isinstance(self.on_state_changed,
					                                                           tuple) or isinstance(
							self.on_state_changed, set):
						iter_on_state_changed(self.on_state_changed, self.__state)
					else:
						# Check if iterable
						try:
							iterator = iter(self.on_state_changed)
							iter_on_state_changed(self.on_state_changed, self.__state)
						except TypeError:
							pass
			except TypeError:
				pass
	
	state = property(get_state, set_state)
