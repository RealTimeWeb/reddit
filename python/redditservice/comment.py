class Comment(object):
	"""
	A Comment on either a Post or another Comment.
	"""
	def __init__(self, ups, downs, created, subreddit, id, author, body, body_html, replies):
		"""
		Creates a new Comment
        
        :param self: This object
        :type self: Comment
        :param ups: The number of upvotes associated with this Comment.
        :type ups: int
        :param downs: The number of downvotes associated with this Comment.
        :type downs: int
        :param created: The date that this Comment was created.
        :type created: int
        :param subreddit: The subreddit that this Comment was made in.
        :type subreddit: string
        :param id: A unique ID for this Comment. A combination of letters, numbers, and dashes.
        :type id: string
        :param author: The username of the author of this Post.
        :type author: string
        :param body: The text of this post, without any markup.
        :type body: string
        :param body_html: The HTML text of this post.
        :type body_html: string
        :param replies: A list of comments that are in reply to this one.
        :type replies: listof Comment
        :returns: Comment
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
		Creates a Comment from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Comment
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