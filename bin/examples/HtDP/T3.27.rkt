;; pred: N  ->  N
;; returns the predecessor of n; if n is 0, return 0
;; example: (pred -3) is 0
(define (pred n)
  (if (> n 0)
      (- n 1)
      0))

;; ! : N  ->  N
;; computes the faculty function
(define (! n)
  (cond
    [(zero? n) 1]
    [else (* n (! (pred n)))]))

;; Tests
(check-expect (! 3) 6)
(check-expect (! 0) 1)