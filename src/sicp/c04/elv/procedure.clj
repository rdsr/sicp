(ns sicp.c04.elv.procedure
  (use [sicp.c04.elv.util]))

;; -- procedure
(defn mk-procedure [parameters body env]
  (list 'procedure parameters body env))
(defn compound-procedure? [p] (tagged-list? p 'procedure))
(defn procedure-parameters [p] (second p))
(defn procedure-body [p] (nth p 2))
(defn procedure-env [p] (nth p 3))


(defn primitive-procedure? [p] false)

(defn apply-primitive-procedure [p args])

(defn primitive-procedures []
  [['car first]
   ['cdr second]
   ['+ +]
   ['- -]
   ['* *]]
  )