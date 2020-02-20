;; count-change: number -> number
;; determines the number of ways how the given amount
;; of many can be changed with coins
;; example: (count-change 15) = 22
(define (count-change amount)
  (cc amount 6))

;; Tests
(check-expect (count-change 3) 2)
(check-expect (count-change 15) 22)

;; cc: number number -> number
;; determine the number of ways how the given amount
;; can be changed using only the lowerst kinds-of-coins
;; coin types
;; example: (cc 3 1) is 1
(define (cc amount kinds-of-coins)
  (cond [(= amount 0) 1]
        [(or (< amount 0) (= kinds-of-coins 0)) 0]
        [else 
         (+ 
          (cc amount (- kinds-of-coins 1))
          (cc 
           (- amount (denomination kinds-of-coins))
           kinds-of-coins))]))

;; Tests
(check-expect (cc 3 1) 1)
(check-expect (cc 3 2) 2)
(check-expect (cc 15 1) 1)
(check-expect (cc 15 2) 8)

;; denomination: number -> number
;; returns the value of the "n-th" coin
;; example: (denomination 3) = 5
(define (denomination coin-number)
  (cond [(= coin-number 1) 1]
        [(= coin-number 2) 2]
        [(= coin-number 3) 5]     
        [(= coin-number 4) 10]
        [(= coin-number 5) 20]
        [(= coin-number 6) 50]
        [else (error 'denomination "undefined count value")]))

;; Tests (for the Euro system)
(check-expect (denomination 1) 1)
(check-expect (denomination 2) 2)
(check-expect (denomination 3) 5)
(check-expect (denomination 4) 10)
(check-expect (denomination 5) 20)
(check-expect (denomination 6) 50)
(check-error (denomination 7) "denomination: undefined count value")