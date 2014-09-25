import requests
import json

def _recursively_convert_unicode_to_str(input):
    if isinstance(input, dict):
        return {_recursively_convert_unicode_to_str(key): _recursively_convert_unicode_to_str(value) for key, value in input.iteritems()}
    elif isinstance(input, list):
        return [_recursively_convert_unicode_to_str(element) for element in input]
    elif isinstance(input, unicode):
        return input.encode('utf-8')
    else:
        return input

def _from_json(data):
    return _recursively_convert_unicode_to_str(json.loads(data))
        
_CACHE = {}
_CACHE_COUNTER = {}
_CONNECTED = False
def connect():
    """
    Connect to the online data source in order to get up-to-date information.
    :returns: void
    """
    _CONNECTED = True
def disconnect(filename="cache.json"):
    """
    Connect to the local cache, so no internet connection is required.
    :returns: void
    """
    _CACHE = _recursively_convert_unicode_to_str(json.load(open(filename, r)))
    for key in CACHE.keys():
        _CACHE_COUNTER[key] = 0
        _CACHE_PATTERN[key] = _CACHE[key][0]
        _CACHE_DATA[key] = _CACHE[key][1:]
    _CONNECTED = False
def lookup(key):
    """
    Internal method that looks up a key in the local cache.
    :param key: Get the value based on the key from the cache.
    :type key: string
    :returns: void
    """
    if _CACHE_COUNTER[key] >= len(_CACHE[key][1:]):
        if _CACHE[key][0] == "empty":
            return ""
        elif _CACHE[key][0] == "repeat" and _CACHE[key][1:]:
            return _CACHE[key][-1]
        elif _CACHE[key][0] == "repeat":
            return ""
        else:
            _CACHE_COUNTER[key] = 0
    else:
        _CACHE_COUNTER[key] += 1
    if _CACHE[key]:
        return _CACHE[key][1+_CACHE_COUNTER]
    else:
        return ""
    
def _save_cache(filename="cache.json"):
    json.dump(_CACHE, filename)
    

class Comment(object):
	"""
	
	"""
	def __init__(self, created, downs, author, subreddit, content, replies, id, link_id, ups):
		"""
		Creates a new Comment.
        
        :param self: This object
        :type self: Comment
        :param created: 
        :type created: int
        :param downs: 
        :type downs: int
        :param author: 
        :type author: str
        :param subreddit: name for sub reddit
        :type subreddit: str
        :param content: 
        :type content: str
        :param replies: 
        :type replies: Commentlisting
        :param id: id of the post
        :type id: str
        :param link_id: 
        :type link_id: str
        :param ups: 
        :type ups: int
        :returns: Comment
		"""
        self.created = created
        self.downs = downs
        self.author = author
        self.subreddit = subreddit
        self.content = content
        self.replies = replies
        self.id = id
        self.link_id = link_id
        self.ups = ups
        
	
	@staticmethod
	def _from_json(json_data):
		"""
		Creates a Comment from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Comment
		"""
		return Comment(json_data['data']['created'],
                       json_data['data']['downs'],
                       json_data['data']['author'],
                       json_data['data']['subreddit'],
                       json_data['data']['body'],
                       Commentlisting._from_json(json_data['data']['replies']),
                       json_data['data']['id'],
                       json_data['data']['link_id'],
                       json_data['data']['ups'])

class Post(object):
	"""
	
	"""
	def __init__(self, content, permalink, title, downs, author, subreddit, is_nsfw, created, id, is_self, ups):
		"""
		Creates a new Post.
        
        :param self: This object
        :type self: Post
        :param content: 
        :type content: str
        :param permalink: 
        :type permalink: str
        :param title: 
        :type title: str
        :param downs: 
        :type downs: int
        :param author: 
        :type author: str
        :param subreddit: name for sub reddit
        :type subreddit: str
        :param is_nsfw: 
        :type is_nsfw: boolean
        :param created: 
        :type created: int
        :param id: id of the post
        :type id: str
        :param is_self: 
        :type is_self: boolean
        :param ups: 
        :type ups: int
        :returns: Post
		"""
        self.content = content
        self.permalink = permalink
        self.title = title
        self.downs = downs
        self.author = author
        self.subreddit = subreddit
        self.is_nsfw = is_nsfw
        self.created = created
        self.id = id
        self.is_self = is_self
        self.ups = ups
        
	
	@staticmethod
	def _from_json(json_data):
		"""
		Creates a Post from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Post
		"""
		return Post(json_data['data']['selftext'],
                       json_data['data']['permalink'],
                       json_data['data']['title'],
                       json_data['data']['downs'],
                       json_data['data']['author'],
                       json_data['data']['subreddit'],
                       json_data['data']['over_18'],
                       json_data['data']['created'],
                       json_data['data']['id'],
                       json_data['data']['is_self'],
                       json_data['data']['ups'])

class Commentlisting(object):
	"""
	
	"""
	def __init__(self, comments):
		"""
		Creates a new Commentlisting.
        
        :param self: This object
        :type self: Commentlisting
        :param comments: This will be in the fields docstring.
        :type comments: list of Comment
        :returns: Commentlisting
		"""
        self.comments = comments
        
	
	@staticmethod
	def _from_json(json_data):
		"""
		Creates a Commentlisting from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Commentlisting
		"""
		return Commentlisting(map(Comment._from_json, json_data['data']['children']))

class Postlisting(object):
	"""
	
	"""
	def __init__(self, posts):
		"""
		Creates a new Postlisting.
        
        :param self: This object
        :type self: Postlisting
        :param posts: 
        :type posts: list of Post
        :returns: Postlisting
		"""
        self.posts = posts
        
	
	@staticmethod
	def _from_json(json_data):
		"""
		Creates a Postlisting from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Postlisting
		"""
		return Postlisting(map(Post._from_json, json_data['data']['children']))

class Listing(object):
	"""
	
	"""
	def __init__(self, commentlisting, postlisting):
		"""
		Creates a new Listing.
        
        :param self: This object
        :type self: Listing
        :param commentlisting: 
        :type commentlisting: Commentlisting
        :param postlisting: 
        :type postlisting: Postlisting
        :returns: Listing
		"""
        self.commentlisting = commentlisting
        self.postlisting = postlisting
        
	
	@staticmethod
	def _from_json(json_data):
		"""
		Creates a Listing from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Listing
		"""
		return Listing(Commentlisting._from_json(json_data[1]),
                       Postlisting._from_json(json_data[0]))

    

def _getcommentlisting_request(subreddit,id,sortmode):
    """
    Used to build the request string used by :func:`getcommentlisting`.
    
    
    :param subreddit: 
    :type subreddit: str
    
    :param id: 
    :type id: str
    
    :param sortmode: 
    :type sortmode: str
    :returns: str
    """
    key = "http://www.reddit.com/r/{}/comments/{}/{}.json".format(subreddit,id,sortmode)
    key += "?" + "".join([])
    return key

def _getcommentlisting_string(subreddit,id,sortmode):
    """
    Like :func:`getcommentlisting` except returns the raw data instead.
    
    
    :param subreddit: 
    :type subreddit: str
    
    :param id: 
    :type id: str
    
    :param sortmode: 
    :type sortmode: str
    :returns: str
    """
    if _CONNECTED:
        key = "http://www.reddit.com/r/{}/comments/{}/{}.json".format(subreddit,id,sortmode)
        result = requests.get(key, params = { ) }).text
    else:
        key = _getcommentlisting_request(subreddit,id,sortmode)
        result = lookup(key)
    return result

def getcommentlisting(subreddit,id,sortmode):
    """
    get listing
    
    
    :param subreddit: 
    :type subreddit: str
    
    :param id: 
    :type id: str
    
    :param sortmode: 
    :type sortmode: str
    :returns: Listing
    """
    result = _getcommentlisting_string(subreddit,id,sortmode)
    
    return Listing._from_json(_from_json(result))
    

def _getpostlisting_request(subreddit,sortmode):
    """
    Used to build the request string used by :func:`getpostlisting`.
    
    
    :param subreddit: The subreddit that Posts will be returned from (without the "r/" preceeding it). Use "all" to return results from all subreddits.
    :type subreddit: str
    
    :param sortmode:  
    :type sortmode: str
    :returns: str
    """
    key = "http://www.reddit.com/r/{}/{}.json".format(subreddit,sortmode)
    key += "?" + "".join([])
    return key

def _getpostlisting_string(subreddit,sortmode):
    """
    Like :func:`getpostlisting` except returns the raw data instead.
    
    
    :param subreddit: The subreddit that Posts will be returned from (without the "r/" preceeding it). Use "all" to return results from all subreddits.
    :type subreddit: str
    
    :param sortmode:  
    :type sortmode: str
    :returns: str
    """
    if _CONNECTED:
        key = "http://www.reddit.com/r/{}/{}.json".format(subreddit,sortmode)
        result = requests.get(key, params = { ) }).text
    else:
        key = _getpostlisting_request(subreddit,sortmode)
        result = lookup(key)
    return result

def getpostlisting(subreddit,sortmode):
    """
    Retrieves all the top posts
    
    
    :param subreddit: The subreddit that Posts will be returned from (without the "r/" preceeding it). Use "all" to return results from all subreddits.
    :type subreddit: str
    
    :param sortmode:  
    :type sortmode: str
    :returns: Postlisting
    """
    result = _getpostlisting_string(subreddit,sortmode)
    
    return Postlisting._from_json(_from_json(result))
    
