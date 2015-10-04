(ns sicp.c04.elv.util)

(defn tagged-list? [exp tag]
  (and (list? exp)
       (= (first exp) tag)))
