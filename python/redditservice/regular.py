import requests
import json
import threading
from _cache import _recursively_convert_unicode_to_str, lookup
from comment import Comment
from post import Post
import structured
def connect():
    """
    Connect to the online data source in order to get up-to-date information.
    
    :returns: void
    """
    structured.connect()
def disconnect():
    """
    Connect to the local cache, so no internet connection is required.
    
    :returns: void
    """
    structured.disconnect()
def get_posts(subreddit, sort_mode):
    """
    Retrieves all the top posts
    
    :param subreddit: The subreddit that Posts will be returned from. Use "all" to return results from all subreddits.
    :type subreddit: string
    :param sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
    :type sort_mode: string
    :returns: listof Post
    """
    return map(Post._from_json, structured.get_posts(subreddit, sort_mode)['data']['children'])

def get_posts_async(callback, error_callback, subreddit, sort_mode):
    """
    Asynchronous version of get_posts
    
    :param callback: Function that consumes the data (a listof Post) returned on success.
    :type callback: function
    :param error_callback: Function that consumes the exception returned on failure.
    :type error_callback: function
    :param subreddit: The subreddit that Posts will be returned from. Use "all" to return results from all subreddits.
    :type subreddit: string
    :param sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
    :type sort_mode: string
    :returns: void
    """
    def server_call(callback, error_callback, subreddit, sort_mode):
        """
        Internal closure to thread this call.
        
        :param callback: Function that consumes the data (a listof Post) returned on success.
        :type callback: function
        :param error_callback: Function that consumes the exception returned on failure.
        :type error_callback: function
        :param subreddit: The subreddit that Posts will be returned from. Use "all" to return results from all subreddits.
        :type subreddit: string
        :param sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
        :type sort_mode: string
        :returns: void
        """
        try:
            callback(get_posts(subreddit, sort_mode))
        except Exception, e:
            error_callback(e)
    threading.Thread(target=server_call, args = (subreddit, sort_mode)).start()

def get_comments(id, sort_mode):
    """
    Retrieves comments for a post
    
    :param id: The unique id of a Post from which Comments will be returned.
    :type id: string
    :param sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
    :type sort_mode: string
    :returns: listof Comment
    """
    return map(Comment._from_json, structured.get_comments(subreddit, id, sort_mode)[1]['data']['children'][:-1])

def get_comments_async(callback, error_callback, id, sort_mode):
    """
    Asynchronous version of get_comments
    
    :param callback: Function that consumes the data (a listof Comment) returned on success.
    :type callback: function
    :param error_callback: Function that consumes the exception returned on failure.
    :type error_callback: function
    :param id: The unique id of a Post from which Comments will be returned.
    :type id: string
    :param sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
    :type sort_mode: string
    :returns: void
    """
    def server_call(callback, error_callback, id, sort_mode):
        """
        Internal closure to thread this call.
        
        :param callback: Function that consumes the data (a listof Comment) returned on success.
        :type callback: function
        :param error_callback: Function that consumes the exception returned on failure.
        :type error_callback: function
        :param id: The unique id of a Post from which Comments will be returned.
        :type id: string
        :param sort_mode: The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).
        :type sort_mode: string
        :returns: void
        """
        try:
            callback(get_comments(id, sort_mode))
        except Exception, e:
            error_callback(e)
    threading.Thread(target=server_call, args = (id, sort_mode)).start()

