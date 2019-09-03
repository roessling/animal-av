;; wage : number  ->  number
;; to compute the total wage (at $12 per hour)
;; of someone who worked for h hours
;; example: (wage 5) is 60
(define (wage h)
  (* 12 h))

;; hours->wages : (listof number) -> (listof number)
;; to create a list of weekly wages from 
;; a list of weekly hours (alon)
;; example: (hours->wages (list 3 5 7)) = (list 36 60 84)
(define (hours->wages alon)
  (cond
    [(empty? alon) empty]
    [else (cons 
           (wage (first alon)) 
           (hours->wages (rest alon)))]))

;; Tests
(check-expect (hours->wages empty) empty)
(check-expect (hours->wages (list 3 5 7)) (list 36 60 84))