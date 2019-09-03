;; contains-doll?: (listof symbol) -> boolean
;; returns true if the list passed in contains 'doll
;; example: (contains-doll? '(barbie ken doll)) is true
(define (contains-doll? a-list-of-symbols)
  (cond
    [(empty? a-list-of-symbols) false]
    [(cons? a-list-of-symbols) 
     (cond
       [(symbol=? (first a-list-of-symbols) 'doll) true]
       [else (contains-doll? (rest a-list-of-symbols))])
     ]))

;; Tests
(check-expect (contains-doll? '(barbie ken doll)) true)
(check-expect (contains-doll? '(barbie ken)) false)