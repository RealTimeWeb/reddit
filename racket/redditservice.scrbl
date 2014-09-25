#lang scribble/manual
 
@title{ redditservice }
@author{+email "Peeratham Techapalokul" "tpeera4@vt.edu"}

@section{Structs}
 
An example service that shows generally how the spec file is structured.


@defproc[(make-comment
    [created integer?]
    [downs integer?]
    [author string?]
    [subreddit string?]
    [content string?]
    [replies commentlisting?]
    [id string?]
    [link-id string?]
    [ups integer?]
    comment]{
        
        @itemlist[
            @item{@racket[created] ---  }
            @item{@racket[downs] ---  }
            @item{@racket[author] ---  }
            @item{@racket[subreddit] --- name for sub reddit }
            @item{@racket[content] ---  }
            @item{@racket[replies] ---  }
            @item{@racket[id] --- id of the post }
            @item{@racket[link-id] ---  }
            @item{@racket[ups] ---  }
            
        ]}

@defproc[(make-post
    [content string?]
    [permalink string?]
    [title string?]
    [downs integer?]
    [author string?]
    [subreddit string?]
    [is-nsfw boolean?]
    [created integer?]
    [id string?]
    [is-self boolean?]
    [ups integer?]
    post]{
        
        @itemlist[
            @item{@racket[content] ---  }
            @item{@racket[permalink] ---  }
            @item{@racket[title] ---  }
            @item{@racket[downs] ---  }
            @item{@racket[author] ---  }
            @item{@racket[subreddit] --- name for sub reddit }
            @item{@racket[is-nsfw] ---  }
            @item{@racket[created] ---  }
            @item{@racket[id] --- id of the post }
            @item{@racket[is-self] ---  }
            @item{@racket[ups] ---  }
            
        ]}

@defproc[(make-commentlisting
    [comments (listof comment?]
    commentlisting]{
        
        @itemlist[
            @item{@racket[comments] --- This will be in the fields docstring. }
            
        ]}

@defproc[(make-postlisting
    [posts (listof post?]
    postlisting]{
        
        @itemlist[
            @item{@racket[posts] ---  }
            
        ]}

@defproc[(make-listing
    [commentlisting commentlisting?]
    [postlisting postlisting?]
    listing]{
        
        @itemlist[
            @item{@racket[commentlisting] ---  }
            @item{@racket[postlisting] ---  }
            
        ]}


@section{Functions}

@defproc[(disconnect-redditservice ) void]{
        Establishes that data will be retrieved locally.
        @itemlist[
            @item{@racket[filename] --- A cache file to use. Defaults to @racket{"cache.json"}.
		]}

@defproc[(disconnect-redditservice ) void]{
        Establishes that data will be accessed online.
        @itemlist[]}


@defproc[(getcommentlisting  [subreddit string?] [id string?] [sortmode string?]) 
    listing?
    ]{
    get listing
    @itemlist[
    @item{@racket[subreddit] --- }]}
    @item{@racket[id] --- }]}
    @item{@racket[sortmode] --- }]}
    ]}

@defproc[(getpostlisting  [subreddit string?] [sortmode string?]) 
    postlisting?
    ]{
    Retrieves all the top posts
    @itemlist[
    @item{@racket[subreddit] --- The subreddit that Posts will be returned from (without the "r/" preceeding it). Use "all" to return results from all subreddits.}]}
    @item{@racket[sortmode] ---  }]}
    ]}
