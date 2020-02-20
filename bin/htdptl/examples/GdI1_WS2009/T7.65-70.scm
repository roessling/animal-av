;; T7.65-70

;; vector-sum-for-3: (vectorof number) -> number
;; returns the sum of the first three elements in the vector
;; example: (vector-sum-for-3 (vector 1 2 3)) is 6
(define (vector-sum-for-3 v)
  (+ (vector-ref v 0)
     (vector-ref v 1)
     (vector-ref v 2)))

;; vector-sum: vector -> number
;; returns the sum of the elements in the vector
;; example: (vector-sum (vector 1 2 3)) is 6
(define (vector-sum v) 
  (vector-sum-aux v (vector-length v)))

;; vector-sum-aux: (vectorof number) number -> number
;; to sum the numbers in v with index in [0, i)
;; example: (vector-sum (vector 1 2 3) 2) is 3
(define (vector-sum-aux v i) 
  (cond
    [(zero? i) 0]
    [else (+ (vector-ref v (- i 1)) 
             (vector-sum-aux v (- i 1)))]))

;; Tests
(check-expect (vector-sum-for-3 (vector 1 2 3)) 6)
(check-expect (vector-sum-for-3 (vector 1 2 3 4 5)) 6)
(check-expect (vector-sum-for-3 (vector 2 4 6)) 12)
(check-expect (vector-sum (vector 1 2 3)) 6)
(check-expect (vector-sum (vector 1 2 3 4 5)) 15)
(check-expect (vector-sum (vector 2 4 6)) 12)
