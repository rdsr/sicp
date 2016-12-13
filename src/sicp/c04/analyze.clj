(ns sicp.c04.elv.analyze
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
            [sicp.c04.elv.let :as l]))

(declare analyze)

(defn analyze-self-evaluating [exp] (fn [env] exp))

(defn analyze-quoted [exp]
  (fn [env] (q/text-of-quotation exp)))

(defn analyze-variable [exp]
  (fn [env] (e/lookup-variable-value exp env)))

(defn analyze-assignment [exp]
  (let [var (a/assignemt-variable exp)
        vp (analyze (a/assignemt-value exp))]
    (fn [env] (e/set-variable-value!
                var (vp env)
                env))))

(defn analyze-definition [exp]
  (let [var (d/definition-variable exp)
        vp (analyze (d/definition-value exp))]
    (fn [env] (e/define-variable!
                var
                (vp env)
                env))))

(defn analyze-if [exp]
  (let [pp (analyze (i/if-predicate exp))
        cp (analyze (i/if-consequent exp))
        ap (analyze (i/if-alternative exp))]
    (fn [env]
      (if (s/true? (pp env))
        (cp env)
        (ap env)))))

(defn analyze-sequence [exp]
  (let [ps (map analyze exp)]
    (cond
      (or (nil? ps) (empty? ps))
      (throw (Error. "Empty sequence -- ANALYZE ")))
    (= (count ps) 1) ps
    :else (reduce (fn [p1 p2]
                    (fn [env] (p1 env) (p2 env)))
                  ps)))

(defn analyze-lambda [exp]
  (let [varsp (lm/lambda-parameters exp)
        bodyp (analyze-sequence (lm/lambda-body exp))]
    (fn [env]
      (p/mk-procedure varsp
                      bodyp
                      env))))

(defn exec-application [fn args]
  (cond
    (p/primitive-procedure? fn)
    (p/apply-primitive-procedure fn args)

    (p/compound-procedure? fn)
    ((p/procedure-body fn)
      (e/extend-env (p/procedure-parameters fn)
                    args
                    (p/procedure-env fn)))))

(defn analyze-application [exp]
  (let [fnp (analyze (app/operator exp))
        argps (map analyze (app/operands exp))]
    (fn [env]
      (exec-application (fnp env)
                        (map (fn [ap] (ap env)) argps)))))

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

(defn eval [exp env]
  ((analyze exp) env))


