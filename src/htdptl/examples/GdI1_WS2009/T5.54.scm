;; T5.54
;; my-fold: Y (X Y -> Y) (listof X) -> Y
;; folds a list using an initial value (init) and a
;; combining operation (combine-op)
;; example: (my-fold 0 + '(1 2 3)) is 6
(define (my-fold init combine-op lst) 
  (cond 
    [(empty? lst) init] 
    [else 
      (combine-op (first lst) 
                  (my-fold init combine-op (rest lst)))]))

;; Tests
(check-expect (my-fold 0 + '(1 2 3)) 6)
(check-expect (my-fold 1 * '(1 4 3)) 12)

;; sum: (listof number) -> number
;; sums all elements in a list
;; example: (sum '(1 2 3)) is 6
(define (sum lst) 
  (my-fold 0 + lst))

;; product: (listof number) -> number
;; multiples all elements in a list
;; example: (product '(1 2 3) is 6
(define (product lst) 
  (my-fold 1 * lst)) 

;; Tests
(check-expect (sum '(1 2 3)) 6)
(check-expect (sum '(1 4 3)) 8)
(check-expect (product '(1 2 3)) 6)
(check-expect (product '(1 4 3)) 12)
