;; T5.64-65
;; filter1: (X Y -> boolean) (listof X) Y -> (listof X)
;; filters all elements from alon for which (rel-op elem t)
;; returns false
;; example: (filter1 < '(2 3 4 5) 4) is '(2 3)
(define (filter1 rel-op alon t) 
  (cond 
    [(empty? alon) empty] 
    [else 
      (cond 
        [(rel-op (first alon) t) 
                     (cons (first alon) 
                     (filter1 rel-op (rest alon) t))] 
        [else (filter1 rel-op (rest alon) t)])]))


;; filter2: (X Y -> boolean) (listof X) Y -> (listof X)
;; filters all elements from alon for which (rel-op elem t)
;; returns false
;; example: (filter2 < '(2 3 4 5) 4) is '(2 3)
;; Note: removed folded "cond"
(define (filter2 rel-op alon t) 
   (cond 
      [(empty? alon) empty] 
      [(rel-op (first alon) t) 
         (cons (first alon) 
               (filter2 rel-op (rest alon) t))] 
      [else (filter2 rel-op (rest alon) t)])) 

;; Tests
(check-expect (filter1 < '(2 3 4 5) 4) '(2 3))
(check-expect (filter1 symbol=? '(A b D F) 'F) '(F))
(check-expect (filter2 < '(2 3 4 5) 4) '(2 3))
(check-expect (filter2 symbol=? '(A b D F) 'F) '(F))
