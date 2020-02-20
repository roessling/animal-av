;; enumerate-tree: (treeof number) -> (listof number)
;; enumerates all elements of the tree into a list
;; example: (emumerate-tree (list 1 (list 2 (list 3 4) (list 5 6)) 7)) is '(1 2 3 4 5 6 7)
(define (enumerate-tree tree)
  (cond 
    [(empty? tree) empty]
    [(not (cons? tree)) (list tree)]
    [else
       (append (enumerate-tree (first tree))
               (enumerate-tree (rest tree)))])
 )

;; Tests
(check-expect (enumerate-tree (list 1 (list 2 (list 3 4) (list 5 6)) 7)) '(1 2 3 4 5 6 7))

;; variant of sum-of-odd-squares using fold
;; sum-of-odd-squares-fold: (treeof number) -> number
;; sum the square value of all odd numbers in the tree
;; example: (sum-of-odd-squares (list 1 (list 2 (list 3 4) (list 5 6)) 7)) is 84
(define (sum-of-odd-squares-fold tree)
  (foldl
   +
   0
   (map sqr
        (filter odd?
                (enumerate-tree tree)
                )
        )
   )
)


;; Tests
(check-expect (sum-of-odd-squares-fold (list 1 (list 2 (list 3 4) (list 5 6)) 7)) 84)
(check-expect (sum-of-odd-squares-fold (list 1 (list 3 (list 5 6) (list 8 10)) 12)) 35)
