import requests
import json
import threading
from cache import _recursively_convert_unicode_to_str, lookup
from comment import Comment
from post import Post
import raw_json
def connect():
	"""
	
	:param : _
	:type : _
	:returns:
	"""
	raw_json.connect()
def disconnect():
	"""
	
	:param : _
	:type : _
	:returns:
	"""
	raw_json.disconnect()
def get_posts(subreddit, sort_mode):
	"""
	
	:param subreddit: _
	:type subreddit: _
	:param sort_mode: _
	:type sort_mode: _
	:returns:
	"""
	"""
	Retrieves all the top posts
	"""
	return _recursively_convert_unicode_to_str(json.loads(raw_json.get_posts(subreddit, sort_mode)))

def get_posts_async(callback, error_callback, subreddit, sort_mode):
	"""
	
	:param callback: _
	:type callback: _
	:param error_callback: _
	:type error_callback: _
	:param subreddit: _
	:type subreddit: _
	:param sort_mode: _
	:type sort_mode: _
	:returns:
	"""
	"""
	Asynchronous version of get_posts
	"""
	def server_call(subreddit, sort_mode):
		"""
		
		:param subreddit: _
		:type subreddit: _
		:param sort_mode: _
		:type sort_mode: _
		:returns:
		"""
		try:
			callback(get_posts(subreddit, sort_mode))
		except Exception, e:
			error_callback(e)
	threading.Thread(target=server_call, args = (subreddit, sort_mode)).start()

def get_comments(subreddit, id, sort_mode):
	"""
	
	:param subreddit: _
	:type subreddit: _
	:param id: _
	:type id: _
	:param sort_mode: _
	:type sort_mode: _
	:returns:
	"""
	"""
	Retrieves comments for a post
	"""
	return _recursively_convert_unicode_to_str(json.loads(raw_json.get_comments(subreddit, id, sort_mode)))

def get_comments_async(callback, error_callback, subreddit, id, sort_mode):
	"""
	
	:param callback: _
	:type callback: _
	:param error_callback: _
	:type error_callback: _
	:param subreddit: _
	:type subreddit: _
	:param id: _
	:type id: _
	:param sort_mode: _
	:type sort_mode: _
	:returns:
	"""
	"""
	Asynchronous version of get_comments
	"""
	def server_call(subreddit, id, sort_mode):
		"""
		
		:param subreddit: _
		:type subreddit: _
		:param id: _
		:type id: _
		:param sort_mode: _
		:type sort_mode: _
		:returns:
		"""
		try:
			callback(get_comments(subreddit, id, sort_mode))
		except Exception, e:
			error_callback(e)
	threading.Thread(target=server_call, args = (subreddit, id, sort_mode)).start()

