#lang racket

; Provide the external structs
(provide (struct-out post))
(provide (struct-out comment))

; Provide the external functions
(provide get-posts)
(provide get-posts/string)
(provide get-posts/json)
(provide get-comments)
(provide get-comments/string)
(provide get-comments/json)
(provide disconnect-reddit-service)
(provide connect-reddit-service)

; Load the internal libraries
(require net/url)
(require srfi/19)
(require srfi/6)
(require racket/port)
(require json)
(require net/uri-codec)

; Define the structs
(define-struct post (ups downs created subreddit id title author is-self is-nsfw content permalink))

(define-struct comment (ups downs created subreddit id author body body-html replies))

(define (json->post jdata)
	(make-post (hash-ref (hash-ref jdata 'data) 'ups)
			(hash-ref (hash-ref jdata 'data) 'downs)
			(hash-ref (hash-ref jdata 'data) 'created)
			(hash-ref (hash-ref jdata 'data) 'subreddit)
			(hash-ref (hash-ref jdata 'data) 'id)
			(hash-ref (hash-ref jdata 'data) 'title)
			(hash-ref (hash-ref jdata 'data) 'author)
			(hash-ref (hash-ref jdata 'data) 'is_self)
			(hash-ref (hash-ref jdata 'data) 'over_18)
                        (if (hash-ref (hash-ref jdata 'data) 'is_self)
                            (hash-ref (hash-ref jdata 'data) 'selftext)
                            (hash-ref (hash-ref jdata 'data) 'url))
			(hash-ref (hash-ref jdata 'data) 'permalink)))

(define (json->comment jdata)
	(make-comment (hash-ref (hash-ref jdata 'data) 'ups)
			(hash-ref (hash-ref jdata 'data) 'downs)
			(hash-ref (hash-ref jdata 'data) 'created)
			(hash-ref (hash-ref jdata 'data) 'subreddit)
			(hash-ref (hash-ref jdata 'data) 'id)
			(hash-ref (hash-ref jdata 'data) 'author)
			(hash-ref (hash-ref jdata 'data) 'body)
			(hash-ref (hash-ref jdata 'data) 'body_html)
                        (if (equal? "" (hash-ref (hash-ref jdata 'data) 'replies))
                            empty
			(map json->comment (drop-right (hash-ref (hash-ref (hash-ref (hash-ref jdata 'data) 'replies) 'data) 'children) 1)))))


; Handle connections
(define CONNECTION true)
(define (disconnect-reddit-service)
	(set! CONNECTION false))
(define (connect-reddit-service)
	(set! CONNECTION true))

; Build Client Store
(define CLIENT_STORE (read-json (open-input-file "cache.json")))

(define (boolean->string a-boolean)
	(if a-boolean
		"true"
		"false"))
(define (string->boolean a-string)
	(string=? a-string "true"))
(define (key-value pair)
	(string-append (symbol->string (car pair)) "=" (cdr pair)))
(define (convert-post-args data)
	(string->bytes/utf-8 (alist->form-urlencoded data)))
(define (convert-get-args url data)
	(string-append url "?" (string-join (map key-value data) "&")))
(define (hash-request url data)
	(string-append url "%{" (string-join (map key-value data) "}%{") "}"))
(define (post->json url full-data index-data)
	(if CONNECTION
		(port->string (post-pure-port (string->url url) (convert-post-args full-data)))
		(hash-ref CLIENT_STORE (hash-request url index-data) "")))
(define (get->json url full-data index-data)
	(if CONNECTION
		(port->string (get-pure-port (string->url (convert-get-args url full-data))))
		(hash-ref CLIENT_STORE (hash-request url index-data) "")))

; Define the services, and their helpers
(define (get-posts subreddit sort-mode)
  (local [(define DATA (get-posts/json subreddit sort-mode))]
    (if (or (number? DATA) (and (hash? DATA) (hash-has-key? DATA 'error)))
             empty
             (map json->post (hash-ref (hash-ref (get-posts/json subreddit sort-mode) 'data) 'children)))))

(define (get-posts/json subreddit sort-mode)
	(string->jsexpr (get-posts/string subreddit sort-mode)))

(define (get-posts/string subreddit sort-mode)
	(get->json (string-append "http://www.reddit.com/r/" subreddit "/" sort-mode ".json") 
	 	(list) 
	 	(list (cons 'sort_mode sort-mode) (cons 'subreddit subreddit))))

(define (get-comments id sort-mode)
  (local [(define DATA (get-comments/json id sort-mode))]
    (if (and (hash? DATA) (hash-has-key? DATA 'error))
        empty
        (map json->comment 
         (drop-right (hash-ref (hash-ref (second DATA) 'data) 'children) 1)))
    ))

(define (get-comments/json id sort-mode)
  (string->jsexpr (get-comments/string id sort-mode)))

(define (get-comments/string id sort-mode)
	(get->json (string-append "http://www.reddit.com/r/all/comments/" id "/" sort-mode ".json") 
	 	(list) 
	 	(list (cons 'id id) (cons 'sort_mode sort-mode))))

