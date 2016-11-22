(ns sicp.c04.ex4-16
  (:require [sicp.c04.elv.definition :as d]
            [sicp.c04.elv.env :as e]
            [sicp.c04.elv.env.frames :as f]
            [sicp.c04.elv.let :as l]))

(defn lookup-variable-value [var e]
  (if (e/empty-env? e)
    (throw (Error. (str "Unbound variable -- lookup-variable-value " var)))
    (let [f (e/first-frame e)]
      (if (f/has-binding? @f var)
        (let [v (f/read-binding-value @f var)]
          (if (= v '*unassigned*)
            (throw (Error. (str "Unassigned variable -- lookup-variable-value" var)))
            v))
        (recur var
               (e/enclosing-env e))))))

(defn scan-out-defines [procedure-body]
  (let [{definitions true body false}
        (group-by (fn [exp] (d/definition? exp)) procedure-body)]
    (if (empty? definitions)
      procedure-body
      (let [names (for [exp definitions] (d/definition-variable exp))
            sentinals (for [name names] (list name '*unassigned*))
            set-values (for [definition definitions]
                         (list 'set!
                               (d/definition-variable definition)
                               (d/definition-value definition)))]
        (l/mk-let sentinals
                  (concat set-values body))))))

;; it is better to install 'scan-out-defines in mk-procedure
;; since it will be called only once, compared to apply
;; where it will be called multiple times
(defn mk-procedure [parameters body env]
  (list 'procedure parameters (scan-out-defines body) env))
