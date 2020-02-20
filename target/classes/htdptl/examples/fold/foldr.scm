;; This is a generic example program using foldr
;; 
;; Contract for foldr:
;; (foldr f base (list x-1 ... x-n)) = (f x-1 ... (f x-n base))
;;
;; You can replace + by other binary functions, like:
;; - * / max min   
;;
(foldr + 0 (list 1 2 3))