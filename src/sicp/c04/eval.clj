(ns sicp.c04.eval
  (:refer-clojure :exclude [eval apply true? false?])
  (:require [sicp.c04.apply :as ap]
            [sicp.c04.elv.application :as app]
            [sicp.c04.elv.assignment :as a]
            [sicp.c04.elv.begin :as b]
            [sicp.c04.elv.cond :as c]
            [sicp.c04.elv.definition :as d]
            [sicp.c04.elv.env :as e]
            [sicp.c04.elv.if :as f]
            [sicp.c04.elv.procedure :as p]
            [sicp.c04.elv.lambda :as lm]
            [sicp.c04.elv.quote :as q]
            [sicp.c04.elv.sym :as s]
            [sicp.c04.elv.let :as l]))


(defn eval [exp env]
  (cond
    (s/self-evaluating? exp) exp
    (s/variable? exp) (e/lookup-variable-value exp env)
    (q/quoted? exp) (q/text-of-quotation exp)
    (a/assignment? exp) (a/eval-assignment eval exp env)
    (d/definition? exp) (d/eval-definition eval exp env)
    (l/let? exp) (eval (l/let->combination exp) env)
    (f/if? exp) (f/eval-if eval exp env)
    (lm/lambda? exp) (p/mk-procedure (lm/lambda-parameters exp)
                                (lm/lambda-body exp)
                                env)
    (b/begin? exp) (b/eval-sequence eval (b/begin-actions exp) env)
    (c/cond? exp) (eval (c/cond->if exp) env)
    (app/application? exp) (ap/apply eval
                                     (eval (app/operator exp) env)
                                     (app/eval-operands eval (app/operands exp) env))

    :else (throw (Error. (str "Unknown expression type: " exp)))))

