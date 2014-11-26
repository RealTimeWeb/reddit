#lang racket
(require "reddit-service.rkt")
(connect-reddit-service)
(define FIRST (first (get-posts "all" "top")))
(define COMMENTS (get-comments "all" (post-id FIRST) "top"))

(define (print-comments comments)
  (cond [(empty? (comment-replies comments)) (comment-body comments)]
        [else (map print-comments (comment-replies comments))]))
;(comment-replies (first (comment-replies (first (comment-replies (first COMMENTS))))))