;; T5.13
;; squared>? : number number  ->  boolean 
;; determines if x squared is greater than c
(define (squared>? x c)
  (> (* x x) c)) 

;; filter1: (X X -> boolean) (listof X) X -> (listof X)
;; places all elements in the list "alon" into the output
;; list for which "rel-op" returns true when working on
;; a list element and the parameter t
;; example: (filter1 < '(1 2 3) 3) is '(1 2)
(define (filter1 rel-op alon t) 
   (cond 
      [(empty? alon) empty] 
      [else 
         (cond
            [(rel-op (first alon) t) 
                     (cons (first alon) 
                           (filter1 rel-op (rest alon) t))] 
            [else 
               (filter1 rel-op (rest alon) t)])])) 

;; Tests
(check-expect (squared>? 3 9) false)
(check-expect (squared>? 3 8.9) true)
(check-expect (filter1 squared>? '(1 2 3 4 5) 10) '(4 5))
(check-expect (filter1 squared>? '(1 2 3 4 5) 30) empty)