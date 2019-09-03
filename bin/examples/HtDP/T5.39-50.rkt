;; T5.39-50
;; my-map: (X -> Y) (listof X) -> (listof Y)
;; applies the function f to all elements in the list
;; and returns a list of the output results
;; example: (my-map odd? '(1 2 3 4)) is (list true false true false)
(define (my-map f alox) 
 (cond 
   [(empty? alox) empty] 
   [else 
     (cons 
      (f (first alox)) 
      (my-map f (rest alox)))]))

;; Tests
(check-expect (my-map odd? '(1 2 3 4)) (list true false true false))
(check-expect (my-map sqr '(2 4 6 8)) '(4 16 36 64))

;; C->F: number -> number
;; converts a temperature from Celsius into Fahrenheit
;; example: (C->F 0) is 32
(define (C->F temperature)
  (+ 32 (* temperature (/ 9 5))))

;; convertCF-from-map: (listof number) -> (listof number) 
(define (convertCF-from-map alon) 
  (my-map C->F alon)) 

;; Structure for Inventory Records
;; name: string - name of the inventory record
;; price: number - price for the inventory record
(define-struct ir (name price)) 

;; names-from-map: (listof ir) -> (listof symbol) 
(define (names-from-map aloIR) 
  (my-map ir-name aloIR)) 

;; list-of-squares: (listof number) -> (listof number) 
;; constructs the list of squares of the elements of alon 
(define (list-of-squares alon)
  (cond 
    [(empty? alon) empty]
    [else (cons (sqr (first alon))
           (list-of-squares (rest alon)))]))

;; list-of-squares-map: (listof number) -> (listof number) 
;; constructs the list of squares of the elements of alon 
(define (list-of-squares-map list) 
  (my-map sqr list))

(check-expect (list-of-squares-map '(2 4 6 8)) '(4 16 36 64))