;; algorithms on family trees

;; a family tree node (ftn) is either
;; 1. empty
;; or 2. (make-child father mother name year eyes),
;; where father and mother are of type ftn,
;; name and eyes are symbols, and year is a number

;; a "child" consists of father and mother, both of
;; which are also a child, a symbol for the name,
;; a number of the year of birth, and a symbol
;; for the eye color
(define-struct child (father mother name year eyes))

;; Oldest Generation:
(define Carl (make-child empty empty 'Carl 1926 'green))
(define Bettina (make-child empty empty 'Bettina 1926 'green))

;; Middle Generation:
(define Adam (make-child Carl Bettina 'Adam 1950 'yellow))
(define Dave (make-child Carl Bettina 'Dave 1955 'black))
(define Eva (make-child Carl Bettina 'Eva 1965 'blue))
(define Fred (make-child empty empty 'Fred 1966 'pink))

;; Youngest Generation: 
(define Gustav (make-child Fred Eva 'Gustav 1988 'brown))

;; blue-eyed-ancestor? : ftn  ->  boolean
;; to determine whether a-ftree contains a
;; structure with 'blue in the eyes field
;; example: (blue-eyed-ancestor? Gustav) = true
(define (blue-eyed-ancestor? a-ftree)
  (cond
    [(empty? a-ftree) false]
    [else (or (symbol=? (child-eyes a-ftree) 'blue)
              (or (blue-eyed-ancestor? (child-father a-ftree))
                  (blue-eyed-ancestor? (child-mother a-ftree))))]))

;; Tests
(check-expect (blue-eyed-ancestor? Carl) false)
(check-expect (blue-eyed-ancestor? Bettina) false)
(check-expect (blue-eyed-ancestor? Adam) false)
(check-expect (blue-eyed-ancestor? Dave) false)
(check-expect (blue-eyed-ancestor? Eva) true)
(check-expect (blue-eyed-ancestor? Fred) false)
(check-expect (blue-eyed-ancestor? Gustav) true)