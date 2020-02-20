;; T3.74-82
;; Data definition:
;; An arithmetic expression aexp is either
;;  1. a number
;;  2. (make-add l r) where l and r are aexp
;;  3. (make-mul l r) where l and r are aexp
;; 4. (make-sub l r) where l and r are aexp

(define-struct add (left right))
(define-struct mul (left right))
(define-struct sub (left right))

;; parse :: deep-list-of symbol -> aexp
;; converts a deep list of symbols into a corresponding
;; aexp, if possible
;; 
;; Example: 
;; (parse '(+ 3 (* (+ 3 5) (* 1 2))))
;; = (make-add 3 (make-mul (make-add 3 5) (make-mul 1 2)))
(define (parse sexp)
 (cond
   [(number? sexp) sexp]
   [(cons? sexp) 
     (cond 
       [(symbol=? (first sexp) '+) 
	  (make-add (parse (second sexp)) 
                   (parse (third sexp)))]
       [(symbol=? (first sexp) '*) 
         (make-mul (parse (second sexp)) 
                   (parse (third sexp)))]
       [(symbol=? (first sexp) '-) 
          (make-sub (parse (second sexp)) 
                    (parse (third sexp)))])]))

;; Tests
(check-expect (parse '(+ 3 (* (+ 3 5) (* 1 2))))
              (make-add 3 (make-mul (make-add 3 5) (make-mul 1 2))))
(check-expect (parse '(+ 3 (* (- 3 5) (* 1 2))))
              (make-add 3 (make-mul (make-sub 3 5) (make-mul 1 2))))

;; calc :: aexp -> number
;; calculates the number represented by 
;; the arithmetic expression exp
;;
;; Example:
;; (calc (make-add 3 (make-mul (make-add 3 5) (make-mul 1 2))))
;;  = 19
(define (calc exp)
  (cond 
     [(number? exp) exp]
     [(add? exp) (+ 
                    (calc (add-left exp)) 
                    (calc (add-right exp)))]
     [(mul? exp) (* 
                    (calc (mul-left exp)) 
                    (calc (mul-right exp)))]
     [(sub? exp) (- 
                    (calc (sub-left exp)) 
                    (calc (sub-right exp)))]))
;; Tests
(check-expect (calc (parse '(+ 3 (* (+ 3 5) (* 1 2))))) 19)
(check-expect (calc (parse '(+ 3 (* (- 3 5) (* 1 2))))) -1)

;; swap+* :: aexp -> aexp
;;
;; generates new aexp within which
;; every addition is replaced by multiplication
;; and vice versa
;;
;; Example:
;; (swap+* (make-add 3 (make-mul 5 7)))
;;  = (make-mul 3 (make-add 5 7))
(define (swap+* exp)
  (cond 
     [(number? exp) exp]
     [(add? exp) (make-mul 
                    (swap+* (add-left exp)) 
                    (swap+* (add-right exp)))]
     [(mul? exp) (make-add 
                  (swap+* (mul-left exp)) 
                  (swap+* (mul-right exp)))]
     [(sub? exp) exp]))

;; Tests
(check-expect (swap+* (parse '(+ 3 (* (+ 3 5) (* 1 2)))))
              (make-mul 3 (make-add (make-mul 3 5) (make-add 1 2))))
(check-expect (swap+* (parse '(+ 3 (* (- 3 5) (* 1 2)))))
              (make-mul 3 (make-add (make-sub 3 5) (make-add 1 2))))
