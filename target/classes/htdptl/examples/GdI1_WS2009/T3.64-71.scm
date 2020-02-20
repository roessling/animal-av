;; Type 1: treat one recursive argument as atomic

;; append-to-list : list list -> list
;; to construct a new list by replacing empty 
;; in alon1 with alon2
(define (append-to-list alon1 alon2)
  (cond
    [(empty? alon1) alon2]
    [else (cons (first alon1) 
                (append-to-list (rest alon1) alon2))]))

;; Tests
(check-expect (append-to-list empty empty) empty)
(check-expect (append-to-list empty '(1 2 3)) '(1 2 3))
(check-expect (append-to-list '(1 3 6) empty) '(1 3 6))
(check-expect (append-to-list '(1 3 6) '(1 2 5)) '(1 3 6 1 2 5))


;; Type 2: step through all recursive arguments synchronously

;; add-list: (listof number) (listof number) -> (listof number)
;; constructs a new list of numbers where the i-th number
;; is the sum of the i-th number of alon1 and the i-th 
;; number of alon2
;; Example: (add-list (list 1 2) (list 7 8 9)) is (list 8 10)
(define (add-list alon1 alon2)
  (cond
    [(or (empty? alon1) (empty? alon2)) empty]
    [else (cons
           (+ (first alon1) (first alon2))
           (add-list (rest alon1) (rest alon2)))]))

;; Tests
(check-expect (add-list empty '(1 3 5)) empty)
(check-expect (add-list '(1 3 5) empty) empty)
(check-expect (add-list '(1 3 5) '(2 5 9)) '(3 8 14))
(check-expect (add-list (list 1 2) (list 7 8 9)) (list 8 10))

;; Type 3: treat all cases separately

;; list-pick : (listof symbol) N[>= 1] -> symbol
;; to determine the nth symbol from alos, counting from 1;
;; signals an error if there is no n-th item
;; example: (list-pick '(a c f) 3) is 'f
(define (list-pick alos n)
  (cond
    [(empty? alos) (error 'list-pick "list too short")]
    [(= n 1) (first alos)]
    [(> n 1) (list-pick (rest alos) (- n 1))]))

;; Tests
(check-error (list-pick empty 1) "list-pick: list too short")
(check-expect (list-pick (cons 'a empty) 1) 'a)
(check-error (list-pick empty 3) "list-pick: list too short")
(check-error (list-pick (cons 'a empty) 3) "list-pick: list too short")
(check-expect (list-pick '(a c f g) 3) 'f)