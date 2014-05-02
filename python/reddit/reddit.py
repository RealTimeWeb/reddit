import sys
PYTHON_3 = sys.version_info >= (3, 0)
import urllib
HEADER = { 'User-Agent' : 'RealTimeWeb Reddit library for educational purposes - Virginia Tech CS-1114 course' }
if PYTHON_3:
    import urllib.request as request
    from urllib.parse import quote_plus
    from urllib.error import HTTPError
else:
    import urllib2
    from urllib import quote_plus
    from urllib2 import HTTPError
try:
    import simplejson as json
except ImportError:
    import json
from datetime import datetime

################################################################################
# Auxilary
################################################################################

def urlencode(query, params):
    """
    Correctly convert the given query and parameters into a full query+query
    string, ensuring the order of the params.
    """
    return query + '?' + "&".join(key+'='+quote_plus(str(value)) 
                                  for key, value in params)

def _parse_int(value, default=0):
    """
    Attempt to cast *value* into an integer, returning *default* if it fails.
    """
    if value is None:
        return default
    try:
        return int(value)
    except ValueError:
        return default

def _parse_float(value, default=0):
    """
    Attempt to cast *value* into a float, returning *default* if it fails.
    """
    if value is None:
        return default
    try:
        return float(value)
    except ValueError:
        return default

def _parse_boolean(value, default=False):
    """
    Attempt to cast *value* into a bool, returning *default* if it fails.
    """
    if value is None:
        return default
    try:
        return bool(value)
    except ValueError:
        return default
        
def _get(url):
    """
    Convert a URL into it's response (a *str*).
    """
    if PYTHON_3:
        req = request.Request(url, headers=HEADER)
        response = request.urlopen(req)
        return response.read().decode('utf-8')
    else:
        req = urllib2.Request(url, headers=HEADER)
        response = urllib2.urlopen(req)
        return response.read()
        
def _recursively_convert_unicode_to_str(input):
    """
    Force the given input to only use `str` instead of `bytes` or `unicode`.
    This works even if the input is a dict, list, 
    """
    if isinstance(input, dict):
        return {_recursively_convert_unicode_to_str(key): _recursively_convert_unicode_to_str(value) for key, value in input.items()}
    elif isinstance(input, list):
        return [_recursively_convert_unicode_to_str(element) for element in input]
    elif not PYTHON_3 and isinstance(input, unicode):
        return input.encode('utf-8')
    else:
        return input

def _from_json(data):
    """
    Convert the given string data into a JSON dict/list/primitive, ensuring that
    `str` are used instead of bytes.
    """
    return _recursively_convert_unicode_to_str(json.loads(data))
################################################################################
# Cache
################################################################################

_CACHE = {}
_CACHE_COUNTER = {}
_EDITABLE = False
_CONNECTED = True
_PATTERN = "empty"
def _start_editing(pattern=_PATTERN):
    """
    Start adding seen entries to the cache. So, every time that you make a request,
    it will be saved to the cache. You must :ref:`_save_cache` to save the
    newly edited cache to disk, though!
    """
    global _EDITABLE, _PATTERN
    _EDITABLE = True
    _PATTERN = pattern
def _stop_editing():
    """
    Stop adding seen entries to the cache.
    """
    global _EDITABLE
    _EDITABLE = False
def connect():
    """
    Connect to the online data source in order to get up-to-date information.
    :returns: void
    """
    global _CONNECTED
    _CONNECTED = True
def disconnect(filename="weatherservice/cache.json"):
    """
    Connect to the local cache, so no internet connection is required.
    :returns: void
    """
    global _CONNECTED, _CACHE
    try:
        with open(filename, 'r') as f:
            _CACHE = _recursively_convert_unicode_to_str(json.load(f))['data']
    except FileNotFoundError:
        raise WeatherException("The cache file '{}' was not found.".format(filename))
    for key in _CACHE.keys():
        _CACHE_COUNTER[key] = 0
    _CONNECTED = False
def _lookup(key):
    """
    Internal method that looks up a key in the local cache.
    :param key: Get the value based on the key from the cache.
    :type key: string
    :returns: void
    """
    if key not in _CACHE:
        return ""
    if _CACHE_COUNTER[key] >= len(_CACHE[key][1:]):
        if _CACHE[key][0] == "empty":
            return ""
        elif _CACHE[key][0] == "repeat" and _CACHE[key][1:]:
            return _CACHE[key][-1]
        elif _CACHE[key][0] == "repeat":
            return ""
        else:
            _CACHE_COUNTER[key] = 1
    else:
        _CACHE_COUNTER[key] += 1
    if _CACHE[key]:
        return _CACHE[key][_CACHE_COUNTER[key]]
    else:
        return ""
def _add_to_cache(key, value):
    """
    Internal method to add a new key-value to the local cache.
    :param str key: The new url to add to the cache
    :param str value: The HTTP response for this key.
    :returns: void
    """
    if key in _CACHE:
        _CACHE[key].append(value)
    else:
        _CACHE[key] = [_PATTERN, value]
        _CACHE_COUNTER[key] = 0
        
def _save_cache(filename="weatherservice/cache.json"):
    with open(filename, 'w') as f:
        json.dump({"data": _CACHE, "metadata": ""}, f)
        
################################################################################
# Exceptions
################################################################################
class RedditException(Exception):
    pass

        
################################################################################
# Domain Objects
################################################################################
    

class Comment(object):
    """
    A Comment on either a Post or another Comment.
    """
    def __init__(self, id, author, subreddit, downs, ups, created, body, replies):
        """
        Creates a new Comment.
        
        :param str id: A unique ID for this Comment. A combination of letters, numbers, and dashes.
        :param str author: The username of the author of this Post.
        :param str subreddit: The subreddit that this Comment was made in (without the 'r/' at the front).
        :param int downs: The number of downvotes associated with this Comment.
        :param int ups: The number of upvotes associated with this Comment.
        :param int created: The date that this Comment was created.
        :param str body: The text of this post, without any markup.
        :param replies: A list of comments that are in reply to this one.
        :type replies: list of :ref:`Comment`
        :returns: Comment
        """
        self.id = id
        self.author = author
        self.subreddit = subreddit
        self.downs = downs
        self.ups = ups
        self.created = created
        self.body = body
        self.replies = replies
        
    @staticmethod
    def _from_json(json_data, post_id='', max_depth=5, depth=0, max_breadth=None):
        """
        Creates a Comment from json data.
        
        :param dict json_data: The raw json data to parse
        :returns: Comment
        """
        if 'data':
            data = json_data['data']
        else:
            raise RedditException("The data from Reddit was invalid.")
        try:
            subreddit = data.get('subreddit', '')
            if data['replies'] == "":
                replies = []
            else:
                children = data['replies']['data']['children']
                if not children or (max_depth is not None and depth >= max_depth):
                    replies = []
                elif children[-1]['kind'] == 'more':
                    rest = children[-1]['data']['children']
                    replies = [Comment._from_json(r, post_id, max_depth, depth+1, max_breadth) for r in children[:-1]][:max_breadth]
                    if max_breadth is None:
                        replies += _get_more_comments(subreddit, post_id, rest,max_depth, depth+1,max_breadth)
                    elif len(children)-1 < max_breadth:
                        replies += _get_more_comments(subreddit, post_id, rest[:max_breadth-len(replies)],max_depth, depth+1,max_breadth)
                elif max_breadth is None:
                    replies = [Comment._from_json(r, post_id, max_depth, depth+1, max_breadth) for r in children]
                else:
                    replies = [Comment._from_json(r, post_id, max_depth, depth+1, max_breadth) for r in children[:max_breadth]]
        except KeyError:
            raise RedditException("The Comment Data was invalid")
        return Comment(data.get('id', ''),
                       data.get('author', ''),
                       subreddit,
                       _parse_int(data.get('downs', '0'), 0),
                       _parse_int(data.get('ups', '0'), 0),
                       _parse_int(data.get('created', '0'), 0),
                       data.get('body', ''),
                       replies)

class Post(object):
    """
    A link (or self-text) that has been submitted to Reddit.
    """
    def __init__(self, id, author, subreddit, downs, ups, created, title, content, is_nsfw, is_url):
        """
        Creates a new Post.
        
        :param str id: A unique ID for this Post. A combination of letters, numbers, and dashes.
        :param str author: The username of the author of this Post.
        :param str subreddit: The subreddit that this Post was made in (without the 'r/' at the front)
        :param int downs: The number of downvotes associated with this Post.
        :param int ups: The number of upvotes associated with this Post.
        :param int created: The date that this Post was created.
        :param str title: The title of this Post.
        :param str content: The text of the post, or a url if it is not a self Post.
        :param bool is_nsfw: Whether or not this Post is Not Safe for Work (NSFW).
        :param bool is_url: Whether or not this Post was text (False), or a URL (True).
        :returns: Post
        """
        self.id = id
        self.author = author
        self.subreddit = subreddit
        self.downs = downs
        self.ups = ups
        self.created = created
        self.title = title
        self.content = content
        self.is_nsfw = is_nsfw
        self.is_url = is_url
    
    @staticmethod
    def _from_json(json_data):
        """
        Creates a Post from json data.
        
        :param json_data: The raw json data to parse
        :type json_data: dict
        :returns: Post
        """
        if 'data' in json_data:
            data = json_data['data']
        else:
            raise RedditException("The Post data from Reddit was invalid.")
        return Post(data.get('id', ''),
                    data.get('author', ''),
                    data.get('subreddit', ''),
                    _parse_int(data.get('downs', '0'), 0),
                    _parse_int(data.get('up', '0'), 0),
                    _parse_int(data.get('created', '0'), 0),
                    data.get('title', ''),
                    data.get('selftext', '') if data.get('is_self', False) else data.get('url', ''),
                    _parse_boolean(data.get('over_18', False), True),
                    _parse_boolean(not data.get('is_self', False), False))


SORT_MODES = ('hot', 'top', 'new', 'old', 'random', 'controversial')

def _get_posts_request(subreddit='all',sort_mode='hot'):
    """
    Used to build the request string used by :func:`get_posts`.
    
    
    :param str subreddit: The subreddit that Posts will be returned from (without the "r/" preceeding it). Use "all" to return results from all subreddits.
    :param str sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
    :returns: str
    """
    return "http://www.reddit.com/r/{}/{}.json".format(subreddit,sort_mode)

def _get_posts_string(subreddit='all', sort_mode='hot'):
    """
    Like :func:`get_posts` except returns the raw data instead.
    
    :param str subreddit: The subreddit that Posts will be returned from (without the "r/" preceeding it). Use "all" to return results from all subreddits.
    :param str sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
    :returns: str
    """
    key = _get_posts_request(subreddit, sort_mode)
    result = _get(key) if _CONNECTED else _lookup(key)
    if _CONNECTED and _EDITABLE:
        _add_to_cache(key, result)
    return result

def get_posts(subreddit='all', sort_mode='hot'):
    """
    Retrieves all the posts.
    
    :param str subreddit: The subreddit that Posts will be returned from (without the "r/" preceeding it). Use "all" to return results from all subreddits.
    :param str sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time). Finally, there is "random", which returns a single post at random from within this subreddit.
    :returns: list of Post
    """
    if sort_mode not in SORT_MODES:
        raise RedditException("Unknown sort mode: {}".format(sort_mode))
    try:
        result = _get_posts_string(subreddit, sort_mode)
    except HTTPError as e:
        if e.code == 404:
            raise RedditException("This subreddit does not exist yet.")
        else:
            raise RedditException("Internet error ({}): {}".format(e.code, e.reason))
    if result:
        try:
            if sort_mode == 'random':
                json_result = _from_json(result)[0]['data']['children']
            else:
                json_result = _from_json(result)['data']['children']
        except KeyError:
            raise RedditException("The response from the server didn't have the right fields.")
        except ValueError:
            raise RedditException("The response from the server didn't make any sense.")
        if "error" in json_result:
            raise RedditException("Error from Reddit: {}".format(json_result.get("error", "Unknown error.")))
        return list(map(Post._from_json, json_result))
    else:
        if _CONNECTED:
            raise RedditException("No response from the server.")
        else:
            raise RedditException("No data was in the cache for this subreddit.")
    

def _get_comments_request(post, sort_mode, max_depth, max_breadth):
    """
    Used to build the request string used by :func:`get_comments`.
    
    
    :param str post: The unique id of a Post from which Comments will be returned.
    :param str sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
    :returns: str
    """
    return "http://www.reddit.com/r/all/comments/{}/{}.json?max_depth={}&max_breadth={}".format(post, sort_mode, max_depth, max_breadth)

def _get_comments_string(post, sort_mode, max_depth, max_breadth):
    """
    Like :func:`get_comments` except returns the raw data instead.
    
    :param str post: The unique id of a Post from which Comments will be returned.
    :param str sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
    :returns: str
    """
    key = _get_comments_request(post, sort_mode, max_depth, max_breadth)
    result = _get(key) if _CONNECTED else _lookup(key)
    if _CONNECTED and _EDITABLE:
        _add_to_cache(key, result)
    return result

def get_comments(post, sort_mode='hot', max_depth=5, max_breadth=5):
    """
    Retrieves comments for a post.
    
    :param post: The unique id of a Post from which Comments will be returned.
    :type post: `str` or :ref:`Post`
    :param str sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
    :param int max_depth: The maximum depth that comments will be retrieved from (i.e., how many descendants from the topmost comment). To go down infinitely, use None.
    :param int max_breadth: The maximum breadth that comments will be retrieved from (i.e., how many siblings from the topmost comment). Note that this breadth applies at every subtree - in effect, it is the branching factor. To get all siblings, use None.
    :returns: list of Comment
    """
    if sort_mode not in SORT_MODES:
        raise RedditException("Unknown sort mode: {}".format(sort_mode))
    if isinstance(post, Post):
        post = post.id
    elif not isinstance(post, str):
        raise RedditException("The post parameter should be a String or a Post")
    result = _get_comments_string(post, sort_mode, max_depth, max_breadth)
    if result:
        try:
            json_result = _from_json(result)[1]['data']['children']
        except ValueError:
            raise RedditException("The response from the server didn't make any sense.")
        if "error" in json_result:
            raise RedditException("Error from Reddit: {}".format(json_result.get("error", "Unknown error.")))
        if max_breadth is None:
            return [Comment._from_json(r, post, max_depth=max_depth-1)
                    for r in json_result]
        else:
            return [Comment._from_json(r, post, max_depth=max_depth-1,
                                                max_breadth=max_breadth)
                    for r in json_result[:max_breadth]]
    else:
        if _CONNECTED:
            raise RedditException("No response from the server.")
        else:
            raise RedditException("No data was in the cache for this comment.")

def _get_more_comments_request(subreddit, post_id, comment_id, max_depth, max_breadth):
    """
    Used to build the request string used by :func:`get_comments`.
    
    
    :param str comment_id: The unique id of the items from which Comments will be returned.
    :param str subreddit: The subreddit of the items from which Comments will be returned.
    :param str post_id: The unique id of the post that will comments will be retrieved.
    :param str sort_mode: The order that the Comments will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best", "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
    :returns: str
    """
    return "http://www.reddit.com/r/{}/comments/{}/morechildrenread/{}/.json?max_depth={}&max_breadth={}".format(subreddit, post_id, comment_id, max_depth, max_breadth)
    
def _get_more_comments_string(subreddit, post_id, comment_id, max_depth, max_breadth):
    """
    Used to build the request string used by :func:`get_comments`.
    
    :param str comment_id: The unique id of the items from which Comments will be returned.
    :param str subreddit: The subreddit of the items from which Comments will be returned.
    :param str post_id: The unique id of the post that will comments will be retrieved.
    :param str sort_mode: The order that the Comments will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best", "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
    :returns: str
    """
    key = _get_more_comments_request(subreddit, post_id, comment_id, max_depth, max_breadth)
    result = _get(key) if _CONNECTED else _lookup(key)
    if _CONNECTED and _EDITABLE:
        _add_to_cache(key, result)
    return result
            
def _get_more_comments(subreddit, post_id, id_list, max_depth=5, depth=0, max_breadth=None):
    comment_lists = []
    for comment_id in id_list:
        result = _get_more_comments_string(subreddit, post_id, comment_id, max_depth, max_breadth)
        if result:
            try:
                json_result = _from_json(result)[1]['data']['children']
            except ValueError:
                raise RedditException("The response from the server didn't make any sense.")
            if json_result:
                comment_lists.append(Comment._from_json(json_result[0], post_id, max_depth, depth, max_breadth))
        else:
            if _CONNECTED:
                raise RedditException("No response from the server.")
            else:
                raise RedditException("No data was in the cache for this subreddit.")
    return comment_lists
