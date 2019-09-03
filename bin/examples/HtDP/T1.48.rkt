;; reply: symbol -> symbol
;; purpose: give a decent reply to a greeting or questions
;; example: (reply 'GoodMorning) is 'Hi
(define (reply s)
  (cond
    [(symbol=? s 'GoodMorning) 'Hi]
    [(symbol=? s 'HowAreYou?) 'Fine]
    [(symbol=? s 'GoodAfternoon) 'INeedANap]
    [(symbol=? s 'GoodEvening) 'BoyAmITired]
    [else 'Error_in_reply:unknown_case] ))

;; Tests
(check-expect (reply 'GoodMorning) 'Hi)
(check-expect (reply 'HowAreYou?) 'Fine)
(check-expect (reply 'GoodAfternoon) 'INeedANap)
(check-expect (reply 'GoodEvening) 'BoyAmITired)
(check-expect (reply 'goodEvening) 'Error_in_reply:unknown_case)
(check-expect (reply 'Hallo) 'Error_in_reply:unknown_case)