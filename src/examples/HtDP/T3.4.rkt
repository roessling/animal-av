(define-struct lst (first rest))
(define-struct emptylst ())
(define the-emptylst (make-emptylst))

;; a list with 0 elements
(define list0 the-emptylst)

;; a list with 1 element
(define list1 (make-lst 'a the-emptylst))

;; a list with 2 elements
(define list2 (make-lst 'a 
              (make-lst 'b 
               the-emptylst)))

;; get the 2nd element from list2
(check-expect (lst-first (lst-rest list2)) 'b)