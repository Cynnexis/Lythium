# -*- coding: utf-8 -*-
import json
import os


class Saver:
	
	def __init__(self, path: str, data: dict=None, try_to_load_from_path: bool=True, save_on_data_changed: bool=True):
		if data is None:
			data = {}
		
		self.__path = path
		self.__data = data
		self.__save_on_data_changed = save_on_data_changed
		
		if try_to_load_from_path:
			self.load_data(path=self.path)
	
	def save_data(self, path: str=None, data: dict=None):
		if path is not None:
			self.path = path
		
		if data is not None:
			self.data = data
		
		with open(self.path, 'w') as f:
			json.dump(self.data, f)
	
	def load_data(self, path: str=None, default_data_if_file_not_found=None):
		if path is not None:
			self.path = path
		
		if default_data_if_file_not_found is None:
			if self.data is None:
				default_data_if_file_not_found = {}
			else:
				default_data_if_file_not_found = self.data
		
		if os.path.exists(self.path):
			with open(self.path, 'r') as f:
				self.data = json.load(f)
		else:
			self.data = default_data_if_file_not_found
	
	def get(self, key, default=None):
		return self.data.get(key, default)
	
	def set(self, key, value) -> None:
		self.data[key] = value
	
	def keys(self):
		return self.data.keys()
	
	def values(self):
		return self.data.values()

	# Overrides #
	
	def __getitem__(self, item):
		return self.data[item]
	
	def __setitem__(self, key, value) -> None:
		if key not in self.data.keys():
			self.data[key] = value
			if self.save_on_data_changed:
				self.save_data()
		else:
			old_value = self.data[key]
			if old_value != value:
				self.data[key] = value
				if self.save_on_data_changed:
					self.save_data()
	
	# Getters & Setters #

	def get_path(self) -> str:
		return self.__path
	
	def set_path(self, path: str) -> None:
		if isinstance(path, str):
			self.__path = path

	path = property(get_path, set_path)

	def get_data(self) -> dict:
		return self.__data
	
	def set_data(self, data: dict) -> None:
		if isinstance(data, dict):
			self.__data = data

	data = property(get_data, set_data)

	def get_save_on_data_changed(self) -> bool:
		return self.__save_on_data_changed
	
	def set_save_on_data_changed(self, save_on_data_changed: bool) -> None:
		if isinstance(save_on_data_changed, bool):
			self.__save_on_data_changed = save_on_data_changed
		
	save_on_data_changed = property(get_save_on_data_changed, set_save_on_data_changed)
