;; This is a generic example program using map
;;
;; Contract for foldl: 
;; map : (X -> Y) (listof X) -> (listof Y)
;;
(map sqr (list 1 2 3)) 


;; map can also be used with an arbitrary number of lists
;; e.g.
(map + (list 1 2 3) (list 2 5 8) (list 13 21 3))