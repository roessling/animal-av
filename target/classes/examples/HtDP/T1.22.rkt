;; lohn: num -> num
;; determines the wages for a worker who
;; has worked 'stundenanzahl' hours for 12$ per hour
;; example: (lohn 10)
(define (lohn stundenanzahl)
  (* 12 stundenanzahl))

;; Test
(check-expect (lohn 40) 480)