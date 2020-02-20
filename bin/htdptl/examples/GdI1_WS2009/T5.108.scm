;; square: number -> number
;; returns the square value of a number
;; example: (square 3) is 9
(define (square n)
  (* n n))

;; T5.106-118
;; sum-of-odd-squares: (treeof number) -> number
;; sum the square value of all odd numbers in the tree
;; example: (sum-of-odd-squares (list 1 (list 2 (list 3 4) (list 5 6)) 7)) is 84
(define (sum-of-odd-squares tree)
  (cond 
      [(empty? tree) 0]
      [(not (cons? tree))
        (if (odd? tree) (square tree) 0)]
      [else 
        (+ (sum-of-odd-squares (first tree))
           (sum-of-odd-squares (rest tree)))]))

;; Tests
(check-expect (sum-of-odd-squares (list 1 (list 2 (list 3 4) (list 5 6)) 7)) 84)
(check-expect (sum-of-odd-squares (list 1 (list 3 (list 5 6) (list 8 10)) 12)) 35)