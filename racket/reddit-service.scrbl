
#lang scribble/manual
 
@title{reddit-service}
@author{author+email "" ""}

@section{Structs}
 
Get the Front Page of the internet.


@defproc[(make-post [ups number?]
			[downs number?]
			[created number?]
			[subreddit string?]
			[id string?]
			[title string?]
			[author string?]
			[is-self boolean?]
			[is-nsfw boolean?]
			[content string?]
			[permalink string?]) post]{


@itemlist[

			@item{@racket[ups] --- The number of upvotes associated with this Post.}

			@item{@racket[downs] --- The number of downvotes associated with this Post.}

			@item{@racket[created] --- The date that this Post was created.}

			@item{@racket[subreddit] --- The subreddit that this Post was made in.}

			@item{@racket[id] --- A unique ID for this Post. A combination of letters, numbers, and dashes.}

			@item{@racket[title] --- The title of this Post.}

			@item{@racket[author] --- The username of the author of this Post.}

			@item{@racket[is-self] --- Whether or not this Post was text (True), or a URL (False).}

			@item{@racket[is-nsfw] --- Whether or not this Post is Not Safe for Work (NSFW).}

			@item{@racket[content] --- The text of the post, or a url if it is not a self Post.}

			@item{@racket[permalink] --- A permanent url that directs to this Post.}]}

@defproc[(make-comment [ups number?]
			[downs number?]
			[created number?]
			[subreddit string?]
			[id string?]
			[author string?]
			[body string?]
			[body-html string?]
			[replies (listof comment?)]) comment]{


@itemlist[

			@item{@racket[ups] --- The number of upvotes associated with this Comment.}

			@item{@racket[downs] --- The number of downvotes associated with this Comment.}

			@item{@racket[created] --- The date that this Comment was created.}

			@item{@racket[subreddit] --- The subreddit that this Comment was made in.}

			@item{@racket[id] --- A unique ID for this Comment. A combination of letters, numbers, and dashes.}

			@item{@racket[author] --- The username of the author of this Post.}

			@item{@racket[body] --- The text of this post, without any markup.}

			@item{@racket[body-html] --- The HTML text of this post.}

			@item{@racket[replies] --- A list of comments that are in reply to this one.}]}



@section{Functions}

@defproc[(disconnect-reddit-service ) void]{

Establishes that data will be retrieved locally.
@itemlist[

		]}

@defproc[(connect-reddit-service ) void]{

Establishes that the online service will be used.
@itemlist[

		]}

@defproc[(get-posts [subreddit string?]
			[sort-mode string?]) (listof post?)]{

Retrieves all the top posts
@itemlist[

			@item{@racket[subreddit] --- The subreddit that Posts will be returned from. Use "all" to return results from all subreddits.}

			@item{@racket[sort-mode] --- The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).}]}

@defproc[(get-comments [id string?]
			[sort-mode string?]) (listof comment?)]{

Retrieves comments for a post
@itemlist[

			@item{@racket[id] --- The unique id of a Post from which Comments will be returned.}

			@item{@racket[sort-mode] --- The order that the Posts will be sorted by. Options are: "top" (ranked by upvotes minus downvotes), "best" (similar to top, except that it uses a more complicated algorithm to have good posts jump to the top and stay there, and bad comments to work their way down, see http://blog.reddit.com/2009/10/reddits-new-comment-sorting-system.html), "hot" (similar to "top", but weighted by time so that recent, popular posts are put near the top), "new" (posts will be sorted by creation time).}]}

