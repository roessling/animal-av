;; absolute-if: number -> number
;; determines the absolute value of x (which will always be positive)
;; example: (absolute-if -17.3) is 17.3
(define (absolute-if x)
  (if (< x 0)
      (- x)
      x))

;; Tests
(check-expect (absolute-if 0) 0)
(check-expect (absolute-if 17.323) 17.323)
(check-expect (absolute-if -81.42) 81.42)

;; absolute-cond: number -> number
;; determines the absolute value of x (which will always be positive)
;; example: (absolute-cond -17.3) is 17.3
(define (absolute-cond x)
  (cond [(> x 0) x]
        [(= x 0) 0]
        [else (- x)]))

;; Tests
(check-expect (absolute-cond 0) 0)
(check-expect (absolute-cond 17.323) 17.323)
(check-expect (absolute-cond -81.42) 81.42)