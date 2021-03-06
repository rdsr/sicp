(ns sicp.c04.elv.definition
  (:use [sicp.c04.elv.util])
  (:require [sicp.c04.elv.lambda :refer [mk-lambda]]
            [sicp.c04.elv.env :refer [define-variable!]])
  (:refer-clojure :exclude [eval apply true? false?]))

;; -- definition
(defn definition? [exp]
  (tagged-list? exp 'define))

(defn definition-variable [exp]
  (let [e (second exp)]
    (if (list? e)
      (first e)
      e)))

(defn definition-value [exp]
  (let [e (second exp)]
    (if (symbol? e)
      ;; var definition
      (nth exp 2)
      ;; function defintion
      (mk-lambda (rest (second exp))
                 (drop 2 exp)))))

(defn mk-definition [variable value]
  (list 'define variable value))

(defn eval-definition [eval-fn exp env]
  (define-variable!
    (definition-variable exp)
    (eval-fn (definition-value exp) env)
    env))
