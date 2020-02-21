;; T7.32-33

;; exponent: number number -> number
;; returns b^n
;; example: (expt 2 3) is 8
;; note: linear complexity O( n )
(define (exponent b n)
  (cond 
    [(= n 0) 1]
    [else (* b (exponent b (- n 1)))]))

;; fast-expt number number -> number
;; returns b^n
;; example: (fast-expt 2 3) is 8
(define (fast-expt b n)
  (cond [(= n 0) 1]
        [(even? n) (sqr (fast-expt b (/ n 2)))]
        [else (* b (fast-expt b (- n 1)))]))

;; Tests
(check-expect (exponent 2 3) 8)
(check-expect (exponent 3 2) 9)
(check-expect (fast-expt 2 3) 8)
(check-expect (fast-expt 3 2) 9)