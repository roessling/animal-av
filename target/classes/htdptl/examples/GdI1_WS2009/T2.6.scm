(define-struct student (last first teacher))

;; Template:
;; (define (subst-teacher a-student a-teacher) 
;; ... (student-last a-student) ...
;; ... (student-first a-student) ...
;; ... (student-teacher a-student) ...)
;; Definition: 
(define (subst-teacher a-student a-teacher) 
  (cond
    [(symbol=? (student-teacher a-student) 'Fritz) 
     (make-student (student-last a-student)
                   (student-first a-student)
		      a-teacher)]
    [else a-student]))
;; Test 1:
(subst-teacher (make-student 'Fit 'Matthew 'Fritz) 'Elise)
;; expected value:
(make-student 'Fit 'Matthew 'Elise)
;; Test 2:
(subst-teacher (make-student 'Smith 'John 'Bill) 'Elise)
;; expected value: 
(make-student 'Smith 'John 'Bill)