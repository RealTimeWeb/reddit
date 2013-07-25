class Post(object):
	"""
	A link (or self-text) that has been submitted to Reddit.
	"""
	def __init__(self, ups, downs, created, subreddit, id, title, author, is_self, is_nsfw, content, permalink):
		"""
		Creates a new Post
        :param self: This object
        :type self: Post
        :param ups: The number of upvotes associated with this Post.
        :type ups: int
        :param downs: The number of downvotes associated with this Post.
        :type downs: int
        :param created: The date that this Post was created.
        :type created: int
        :param subreddit: The subreddit that this Post was made in.
        :type subreddit: string
        :param id: A unique ID for this Post. A combination of letters, numbers, and dashes.
        :type id: string
        :param title: The title of this Post.
        :type title: string
        :param author: The username of the author of this Post.
        :type author: string
        :param is_self: Whether or not this Post was text (True), or a URL (False).
        :type is_self: boolean
        :param is_nsfw: Whether or not this Post is Not Safe for Work (NSFW).
        :type is_nsfw: boolean
        :param content: The text of the post, or a url if it is not a self Post.
        :type content: string
        :param permalink: A permanent url that directs to this Post.
        :type permalink: string
        :returns: Post
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
		Creates a Post from json data.
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Post
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