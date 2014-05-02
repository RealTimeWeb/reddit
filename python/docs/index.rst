.. redditservice documentation master file, created by
   sphinx-quickstart on Tue Jul 23 13:06:29 2013.
   You can adapt this file completely to your liking, but it should at least
   contain the root `toctree` directive.

Welcome to redditservice's documentation!
=========================================

The Reddit library offers access to the Reddit, the "Front Page of the Internet" - in other words, a social link-sharing site. There are many different "subreddits" (categorized sections of the site), with both textual and URL content. To get an idea of what Reddit is like, I recommend you check it out at `reddit.com <http://www.reddit.com/>`_ , and then look at some of the top `subreddits <http://www.redditlist.com/>`_ .

>>> from reddit import reddit

The simplest action is to get some posts by passing in a subreddit name.

>>> posts = reddit.get_posts("virginiatech")
>>> posts
[<reddit.reddit.Post object>, <reddit.reddit.Post object>, ...]
>>> len(posts)
25

Posts have a number of useful properties:

>>> post[0].title
'Pictures from the Formula SAE test drive today.'
>>> post[0].ups - post[0].downs
-5

Given a Post, you can also get its comments:

>>> reddit.get_comments(posts[0])
[<reddit.reddit.Comment object>]
>>> len(reddit.get_comments(posts[3]))
25

Comments are threaded:

>>> comments = reddit.get_comments(posts[3])
>>> comments[0].replies # top comment to the top comment!
[<reddit.reddit.Comment object>, <reddit.reddit.Comment object>, ...]

Some threads have huge numbers of Comments, so you should limit them.

>>> comments = reddit.get_comments(posts[2], max_breadth=4, max_depth=2)
>>> len(comments)
4
>>> len(comments[0].replies)
4
>>> len(comments[0].replies[0].replies)
0

The built-in cache allows you to work online:

>>> reddit.connect() # unnecessary: default is connected

or offline:

>>> reddit.disconnect()
>>> posts = reddit.get_posts("corgis")
>>> len(posts)
25

But remember there must be data in the cache already!

>>> posts = reddit.get_posts("udel")
reddit.reddit.RedditException: No data was in the cache for this subreddit.

The cache can be configured to handle repeated calls differently. For example, if you want, you could make it return new results every time you call:

>>> len(reddit.get_posts("corgis"))
25
>>> len(reddit.get_posts("corgis"))
20
>>> len(reddit.get_posts("corgis"))
5

Methods
-------

.. autofunction:: reddit.connect

.. autofunction:: reddit.disconnect

.. autofunction:: reddit.get_posts

.. autofunction:: reddit.get_comments
   
Data Classes
------------

.. autoclass:: reddit.Post
    :members:
    :special-members: __init__
    
.. autoclass:: reddit.Comment
    :members:
    :special-members: __init__

Indices and tables
==================

* :ref:`genindex`
* :ref:`modindex`
* :ref:`search`

