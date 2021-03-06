(ns sicp.c04.ex4-9
  (:require [sicp.c04.elv.begin :as b]
            [sicp.c04.elv.definition :as d]
            [sicp.c04.elv.if :as i]
            [sicp.c04.elv.util :as u]
            [sicp.c04.elv.lambda :as l]))

(defn while? [exp] (u/tagged-list? exp 'while))
(defn while-pred [exp] (second exp))
(defn while-body [exp] (drop 2 exp))

(defn- expand-while [pred body]
  (list (d/mk-definition
          'while
          (l/mk-lambda
            ()
            ;; wrap if in a vec, since lambda expects body to be a seq
            [(i/mk-if pred
                      ;; we want the seq to be (exp1, exp2.. (while))
                      ;; hence wrapping '(while) in a vec
                      (b/sequence->exp (concat body ['(while)]))
                      false)]))
        '(while)))

(defn while->combination [exp]
  (let [pred (while-pred exp)
        body (while-body exp)]
    (expand-while pred body)))

