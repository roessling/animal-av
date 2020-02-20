;; This is a generic example program using foldl
;; 
;; Contract for foldl:
;; foldl : (X Y -> Y) Y (listof X) -> Y
;; (foldl f base (list x-1 ... x-n)) = (f x-n ... (f x-1 base))
;;
;; You can replace + by other binary functions, like:
;; - * / max min   
;;
(foldl + 0 (list 1 2 3))