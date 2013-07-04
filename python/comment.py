class Comment(object):
	"""
	
	"""
	def __init__(self, ups, downs, created, subreddit, id, author, body, body_html, replies):
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
		:param author: _
		:type author: _
		:param body: _
		:type body: _
		:param body_html: _
		:type body_html: _
		:param replies: _
		:type replies: _
		:returns:
		"""
		self.ups = ups
		self.downs = downs
		self.created = created
		self.subreddit = subreddit
		self.id = id
		self.author = author
		self.body = body
		self.body_html = body_html
		self.replies = replies
	
	@staticmethod
	def _from_json(json_data):
		"""
		
		:param json_data: _
		:type json_data: _
		:returns:
		"""
		if json_data['data']['replies'] != "" and json_data['data']['replies']['kind'] == "t1":
			replies = map(Comment._from_json, json_data['data']['replies']['data']['children'][:-1])
		else:
			replies = []
		return Comment(json_data['data']['ups'],
					json_data['data']['downs'],
					json_data['data']['created'],
					json_data['data']['subreddit'],
					json_data['data']['id'],
					json_data['data']['author'],
					json_data['data']['body'],
					json_data['data']['body_html'],
					replies)