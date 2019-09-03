;; a list with 0 elements
;; (define list0 the-emptylst)
(define list0 empty)

;; a list with 1 element
;; (define list1 (make-lst 'a the-emptylst))
(define list1 (cons 'a empty))

;; a list with 2 elements
;; (define list2 (make-lst 'a 
;;               (make-lst 'b the-emptylst)))
(define list2 (cons 'a (cons 'b empty)))

;; get the 2nd element from list2
;; (lst-first (lst-rest list2)) is 'b
(check-expect (first (rest list2)) 'b)