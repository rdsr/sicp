(ns sicp.c04.elv.cond
  (use [sicp.c04.elv.util])
  (:require [sicp.c04.elv.if :refer [mk-if]])
  (:require [sicp.c04.elv.begin :refer [sequence->exp]]))

;; -- cond
(defn cond? [exp] (tagged-list? exp 'cond))
(defn cond-clauses [exp] (rest exp))
(defn cond-pred-clause [clause] (first clause))
(defn cond-else-clause? [clause] (= (cond-pred-clause clause) 'else))
(defn cond-actions [clause] (rest clause))
(defn expand-clauses [clauses]
  (if (empty? clauses)
    false
    ;; assuming syntax of clauses here?
    (let [fc (first clauses)
          rc (rest clauses)]
      (if (cond-else-clause? fc)
        (if (empty? rc)
          (sequence->exp (cond-actions fc))
          (throw (Error. "else clause isn't last -- cond->if")))
        (mk-if (cond-pred-clause fc)
               (sequence->exp (cond-actions fc))
               (expand-clauses rc))))))
(defn cond->if [exp]
  (expand-clauses (cond-clauses exp)))
