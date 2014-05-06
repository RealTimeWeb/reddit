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

# To populate the cache:  

Say you want to add today's posts on the virginiatech subreddit to the cache.


>>> reddit._start_editing()
>>> reddit.get_posts('virginiatech')
>>> reddit._save_cache()
>>>

Now the "cache.json" file will have an entry for "virginiatech", and
you can use that as an input to the function when disconnected.

You can also specify the name of a cache file in the _save_cache() and the disconnect() methods.  For example, this populates a 
cache file called "udel.json" with posts from the udel subreddit:

>>> reddit._start_editing()
>>> posts = reddit.get_posts("udel")
>>> reddit._save_cache("udel.json")

And this accesses that cache for offline use:

>>> reddit.disconnect("udel.json")
>>> reddit.get_posts("udel")
[<Post 1jgbl1>, <Post 24swcy>, <Post 24qk8j>, <Post 24n1w5>, <Post 24dhcz>, <Post 24azkf>, <Post 24b47e>, <Post 24820j>, <Post 248i88>, <Post 244nh4>, <Post 242w3k>, <Post 23zpf6>, <Post 23q2n8>, <Post 23q1f8>, <Post 23q2yp>, <Post 23of8k>, <Post 23ly53>, <Post 23fbxr>, <Post 23gl7u>, <Post 23bbah>, <Post 23aec5>, <Post 236pwf>, <Post 23135x>, <Post 2320mx>, <Post 22z7zb>]
>>>

# To run the unit tests from the command line:

>>> python -m tests.test


Further documentation is available in the `docs/_builds/index.html` file.

Potential changes available:

* The created field currently returns an epoch time - it is trivial to change this to a human readable string
* Only the raw text of a field is returned, but it's possible to get the formatted HTML too
* Only the first page of a subreddit is currently grabbed; it would not be hard to get further pages.
