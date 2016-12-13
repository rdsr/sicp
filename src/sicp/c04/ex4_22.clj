;; Exercise 4.22.  Extend the evaluator in this section to
;; support the special form let. (See exercise 4.6.)

(ns sicp.c04.ex4-22
  (:require [sicp.c04.elv.sym :as s]
            [sicp.c04.elv.quote :as q]
            [sicp.c04.elv.assignment :as a]
            [sicp.c04.elv.definition :as d]
            [sicp.c04.elv.if :as i]
            [sicp.c04.elv.lambda :as lm]
            [sicp.c04.elv.begin :as b]
            [sicp.c04.elv.cond :as c]
            [sicp.c04.elv.application :as app]
            [sicp.c04.elv.env :as e]
            [sicp.c04.elv.procedure :as p]
            [sicp.c04.elv.let :as l])
  (:use [sicp.c04.elv.analyze]))

(defn analyze [exp]
  (cond
    (s/self-evaluating? exp) (analyze-self-evaluating exp)
    (q/quoted? exp) (analyze-quoted exp)
    (s/variable? exp) (analyze-variable exp)
    (a/assignment? exp) (analyze-assignment exp)
    (d/definition? exp) (analyze-definition exp)
    (l/let? exp) (analyze (l/let->combination exp))
    (i/if? exp) (analyze-if exp)
    (lm/lambda? exp) (analyze-lambda exp)
    (b/begin? exp) (analyze-sequence (b/begin-actions exp))
    (c/cond? exp) (analyze (c/cond->if exp))
    (app/application? exp) (analyze-application exp)
    :else (throw (Error. "Unknown expression type -- ANALYZE" exp))))
