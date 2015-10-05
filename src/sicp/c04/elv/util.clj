(ns sicp.c04.elv.util
  (:refer-clojure :exclude [eval apply true? false?]))

(defn tagged-list? [exp tag]
  (and (list? exp)
       (= (first exp) tag)))
