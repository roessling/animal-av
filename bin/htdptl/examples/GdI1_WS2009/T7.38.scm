;; T7.38

;; move: symbol symbol symbol number -> (listof (listof symbol))
;; move tower of Hanoi with height n from T1 to T2 using T3 as a helper
;; example: (move 'A 'B 'C 1) is (list (list 'A 'B))
(define (move T1 T2 T3 n)
   (cond 
      [(= n 0) empty]
      [else 
         (append 
            (move T1 T3 T2 (- n 1))
            (list (list T1 T2))
            (move T3 T2 T1 (- n 1)))
       ]
    )
)

;; Tests
(check-expect (move 'A 'B 'C 1) (list (list 'A 'B)))
(check-expect (move 'A 'B 'C 3) (list (list 'A 'B)
                                      (list 'A 'C)
                                      (list 'B 'C)
                                      (list 'A 'B)
                                      (list 'C 'A)
                                      (list 'C 'B)
                                      (list 'A 'B)))