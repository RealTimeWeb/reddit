Welcome to Reddit Library's documentation!
==========================================

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

To populate the cache:  

Say you want to add today's posts on the virginiatech subreddit to the cache.


>>> reddit._start_editing()
>>> reddit.get_posts('virginiatech')
[<reddit.Post object at 0x7f50d2627990>, <reddit.Post object at 0x7f50d26279d0>, <reddit.Post object at 0x7f50d2627a10>, <reddit.Post object at 0x7f50d2627a50>, <reddit.Post object at 0x7f50d2627a90>, <reddit.Post object at 0x7f50d2627ad0>, <reddit.Post object at 0x7f50d2627b10>, <reddit.Post object at 0x7f50d2627b50>, <reddit.Post object at 0x7f50d2627b90>, <reddit.Post object at 0x7f50d2627bd0>, <reddit.Post object at 0x7f50d2627c10>, <reddit.Post object at 0x7f50d2627c50>, <reddit.Post object at 0x7f50d2627c90>, <reddit.Post object at 0x7f50d2627cd0>, <reddit.Post object at 0x7f50d2627d10>, <reddit.Post object at 0x7f50d2627d50>, <reddit.Post object at 0x7f50d2627d90>, <reddit.Post object at 0x7f50d2627dd0>, <reddit.Post object at 0x7f50d2627e10>, <reddit.Post object at 0x7f50d2627e50>, <reddit.Post object at 0x7f50d2627e90>, <reddit.Post object at 0x7f50d2627ed0>, <reddit.Post object at 0x7f50d2627f10>, <reddit.Post object at 0x7f50d2627f50>, <reddit.Post object at 0x7f50d2627f90>]
>>> reddit._save_cache("cache.json")
>>>

Now the "cache.json" file will have an entry for "virginiatech", and
you can use that as an input to the function when disconnected.


To run the unit tests from the command line:

>>> python -m tests.test


Further documentation is available in the `docs/_builds/index.html` file.

Potential changes available:

* The created field currently returns an epoch time - it is trivial to change this to a human readable string
* Only the raw text of a field is returned, but it's possible to get the formatted HTML too
* Only the first page of a subreddit is currently grabbed; it would not be hard to get further pages.
