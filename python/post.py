class Post(object):
	"""
	
	"""
	def __init__(self, ups, downs, created, subreddit, id, title, author, is_self, is_nsfw, content, permalink):
		"""
		
		:param self: _
		:type self: _
		:param ups: _
		:type ups: _
		:param downs: _
		:type downs: _
		:param created: _
		:type created: _
		:param subreddit: _
		:type subreddit: _
		:param id: _
		:type id: _
		:param title: _
		:type title: _
		:param author: _
		:type author: _
		:param is_self: _
		:type is_self: _
		:param is_nsfw: _
		:type is_nsfw: _
		:param content: _
		:type content: _
		:param permalink: _
		:type permalink: _
		:returns:
		"""
		self.ups = ups
		self.downs = downs
		self.created = created
		self.subreddit = subreddit
		self.id = id
		self.title = title
		self.author = author
		self.is_self = is_self
		self.is_nsfw = is_nsfw
		self.content = content
		self.permalink = permalink
	
	@staticmethod
	def _from_json(json_data):
		"""
		
		:param json_data: _
		:type json_data: _
		:returns:
		"""
		return Post(json_data['data']['ups'],
					json_data['data']['downs'],
					json_data['data']['created'],
					json_data['data']['subreddit'],
					json_data['data']['id'],
					json_data['data']['title'],
					json_data['data']['author'],
					json_data['data']['is_self'],
					json_data['data']['over_18'],
					json_data['data']['selftext'] if json_data['data']['is_self'] else json_data['data']['url'],
					json_data['data']['permalink'])