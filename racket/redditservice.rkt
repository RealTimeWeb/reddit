#lang racket

; Load the internal libraries
(require htdp/error)
(require json)
(require racket/port)
(require net/url)
(require "sticky-web.rkt")

; Provide the external structs
(provide
    (struct-out comment)
    (struct-out post)
    (struct-out commentlisting)
    (struct-out postlisting)
    (struct-out listing)
    getcommentlisting
    getpostlisting
    connect-redditservice
    disconnect-redditservice)

; Define the structs

(define-struct comment
    ( created downs author subreddit content replies id link-id ups))

(define-struct post
    ( content permalink title downs author subreddit is-nsfw created id is-self ups))

(define-struct commentlisting
    ( comments))

(define-struct postlisting
    ( posts))

(define-struct listing
    ( commentlisting postlisting))



(define (json->comment jdata)
    (make-comment
        (hash-ref (hash-ref jdata 'data) 'created)
        (hash-ref (hash-ref jdata 'data) 'downs)
        (json->string (hash-ref (hash-ref jdata 'data) 'author))
        (json->string (hash-ref (hash-ref jdata 'data) 'subreddit))
        (json->string (hash-ref (hash-ref jdata 'data) 'body))
        (json->commentlisting (hash-ref (hash-ref jdata 'data) 'replies))
        (json->string (hash-ref (hash-ref jdata 'data) 'id))
        (json->string (hash-ref (hash-ref jdata 'data) 'link_id))
        (hash-ref (hash-ref jdata 'data) 'ups)
        )

(define (json->post jdata)
    (make-post
        (json->string (hash-ref (hash-ref jdata 'data) 'selftext))
        (json->string (hash-ref (hash-ref jdata 'data) 'permalink))
        (json->string (hash-ref (hash-ref jdata 'data) 'title))
        (hash-ref (hash-ref jdata 'data) 'downs)
        (json->string (hash-ref (hash-ref jdata 'data) 'author))
        (json->string (hash-ref (hash-ref jdata 'data) 'subreddit))
        (hash-ref (hash-ref jdata 'data) 'over_18)
        (hash-ref (hash-ref jdata 'data) 'created)
        (json->string (hash-ref (hash-ref jdata 'data) 'id))
        (hash-ref (hash-ref jdata 'data) 'is_self)
        (hash-ref (hash-ref jdata 'data) 'ups)
        )

(define (json->commentlisting jdata)
    (make-commentlisting
        (map json->comment (hash-ref (hash-ref jdata 'data) 'children))
        )

(define (json->postlisting jdata)
    (make-postlisting
        (map json->post (hash-ref (hash-ref jdata 'data) 'children))
        )

(define (json->listing jdata)
    (make-listing
        (json->commentlisting (list-ref jdata 1))
        (json->postlisting (list-ref jdata 0))
        )


(define connect-redditservice connect)
(define disconnect-redditservice disconnect)

; Define the services, and their helpers

(define (getcommentlisting ) 
    (json->listing (getcommentlisting/json )))
    
(define (getcommentlisting/json )
    (string->jsexpr (getcommentlisting/string )))
    
(define (getcommentlisting/string )
    (get->string 
        (format "http://www.reddit.com/r/~s/comments/~s/~s.json"  subreddit id sortmode)
        (list)
        (list)
        ))


(define (getpostlisting ) 
    (json->postlisting (getpostlisting/json )))
    
(define (getpostlisting/json )
    (string->jsexpr (getpostlisting/string )))
    
(define (getpostlisting/string )
    (get->string 
        (format "http://www.reddit.com/r/~s/~s.json"  subreddit sortmode)
        (list)
        (list)
        ))

