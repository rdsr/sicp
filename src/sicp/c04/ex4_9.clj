(ns sicp.c04.ex4-9
  (:require [sicp.c04.elv.begin :as b]
            [sicp.c04.elv.definition :as d]
            [sicp.c04.elv.if :as i]
            [sicp.c04.elv.util :as u]
            [sicp.c04.elv.procedure :as p]))

; (while pred exps)
;((define (while)
;   (if pred
;       (begin exps (while))))
;  (while))

(defn while? [exp] (u/tagged-list? exp 'while))
(defn while-pred [exp] (second exp))
(defn while-body [exp] (nth exp 2))

(defn- expand-while [pred body env]
  (list (d/mk-definition
          'while
          (p/mk-procedure
            ()
            (i/mk-if pred
                     (b/sequence->exp (list body '(while)))
                     false)
            env))
        '(while)))

(defn while->combination [exp env]
  (let [pred (while-pred exp)
        body (while-body exp)]
    (expand-while pred body env)))

