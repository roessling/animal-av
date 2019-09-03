;; T5.9-12
;; less-than: (listof number) number -> (listof number) 
;; to construct a list of those numbers 
;; in alon that are less than threshold t
;; example: (less-than '(1 3 8 2) 4) is '(1 3 2)
(define (less-than alon t) 
   (cond 
       [(empty? alon) empty] 
       [else 
           (cond 
              [(< (first alon) t) 
               (cons (first alon) 
                     (less-than (rest alon) t))] 
              [else  (less-than (rest alon) t)])])) 


;; greater-than: (listof number) number -> (listof number) 
;; to construct a list of those numbers 
;; in alon that are greater than threshold t
;; example: (greater-than '(1 3 8 2) 4) is '(8)
(define (greater-than alon t) 
   (cond 
       [(empty? alon) empty] 
       [else 
           (cond 
              [(> (first alon) t) 
               (cons (first alon) 
                     (greater-than (rest alon) t))] 
              [else  (greater-than (rest alon) t)])])) 

;; filter1: (X number -> boolean) (listof X) number -> (listof X)
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

;; less-than1: (listof number) number -> (listof number)
;; to construct a list of those numbers 
;; in alon that are less than threshold t
;; example: (less-than1 '(1 3 8 2) 4) is '(1 3 2)
(define (less-than1 alon t) 
  (filter1 < alon t)) 

;; greater-than1: (listof number) number -> (listof number) 
;; to construct a list of those numbers 
;; in alon that are greater than threshold t
;; example: (greater-than1 '(1 3 8 2) 2) is '(3 8)
(define (greater-than1 alon t)
  (filter1 > alon t)) 

;; less-or-equal: (listof number) number -> (listof number) 
;; to construct a list of those numbers 
;; in alon that are less than threshold t
;; example: (less-or-equal: '(1 3 8 2) 3) is '(1 3 2)
(define (less-or-equal alon t)
  (filter1 <= alon t))

;; Tests
(define dummy-list-1 '(1 2 3 4 5))
(define dummy-list-2 '(1 2 4 8 16))
(define dummy-list-3 '(1 4 9 16 3))
(define dummy-list-4 '(1 5 2 3 1))
(check-expect (less-than dummy-list-1 2) '(1))
(check-expect (less-than dummy-list-2 4) '(1 2))
(check-expect (less-than dummy-list-3 5) '(1 4 3))
(check-expect (less-than dummy-list-4 3) '(1 2 1))

(check-expect (greater-than dummy-list-1 2) '(3 4 5))
(check-expect (greater-than dummy-list-2 4) '(8 16))
(check-expect (greater-than dummy-list-3 5) '(9 16))
(check-expect (greater-than dummy-list-4 3) '(5))

(check-expect (filter1 < dummy-list-1 2) (less-than dummy-list-1 2))
(check-expect (filter1 < dummy-list-2 4) (less-than dummy-list-2 4))
(check-expect (filter1 < dummy-list-3 5) (less-than dummy-list-3 5))
(check-expect (filter1 < dummy-list-4 3) (less-than dummy-list-4 3))

(check-expect (filter1 > dummy-list-1 2) (greater-than dummy-list-1 2))
(check-expect (filter1 > dummy-list-2 4) (greater-than dummy-list-2 4))
(check-expect (filter1 > dummy-list-3 5) (greater-than dummy-list-3 5))
(check-expect (filter1 > dummy-list-4 3) (greater-than dummy-list-4 3))

(check-expect (less-than1 dummy-list-1 2) (less-than dummy-list-1 2))
(check-expect (less-than1 dummy-list-2 4) (less-than dummy-list-2 4))
(check-expect (less-than1 dummy-list-3 5) (less-than dummy-list-3 5))
(check-expect (less-than1 dummy-list-4 3) (less-than dummy-list-4 3))

(check-expect (greater-than1 dummy-list-1 2) (greater-than dummy-list-1 2))
(check-expect (greater-than1 dummy-list-2 4) (greater-than dummy-list-2 4))
(check-expect (greater-than1 dummy-list-3 5) (greater-than dummy-list-3 5))
(check-expect (greater-than1 dummy-list-4 3) (greater-than dummy-list-4 3))
              
(check-expect (less-or-equal dummy-list-1 2) '(1 2))
(check-expect (less-or-equal dummy-list-2 4) '(1 2 4))
(check-expect (less-or-equal dummy-list-3 5) '(1 4 3))
(check-expect (less-or-equal dummy-list-4 3) '(1 2 3 1))
