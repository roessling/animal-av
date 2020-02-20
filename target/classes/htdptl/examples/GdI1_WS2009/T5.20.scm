;; T.20
;; length: (listof X) -> number 
;; to compute the length of a list
;; example: (length '(1 2 3 4 5)) is 5
(define (my-length alox) 
    (cond 
       [(empty? alox) 0] 
       [else (+ (length (rest alox)) 1)])) 

;; Tests
(check-expect (my-length '(1 2 3 4 5)) 5)
(check-expect (my-length empty) 0)
(check-expect (my-length '(a b c d)) 4)
(check-expect (my-length (list 1 2 "hi" 'bill 5)) 5)
