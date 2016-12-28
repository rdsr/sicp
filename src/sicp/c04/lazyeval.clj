(ns sicp.c04.lazyeval
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
            [sicp.c04.elv.let :as l]
            [sicp.c04.elv.procedure :as p]
            [sicp.c04.elv.env :as env]
            [sicp.c04.elv.util :as u]
            [sicp.c04.elv.begin :refer [eval-sequence]]
            [sicp.c04.elv.env :refer [extend-env]])
  (:import [java.io StringReader PushbackReader]
           (clojure.lang IDeref)))

;; -- thunk
(defn mk-thunk [exp env] ['thunk exp env])
(defn thunk? [exp] (u/tagged-list? exp 'thunk))
(defn thunk-exp [obj] (nth obj 1))
(defn thunk-env [obj] (nth obj 2))

(declare eval force-it)

;; -- force/delay

(defn actual-value [op env]
  (force-it (eval op env)))

(defn force-it [obj]
  (if (thunk? obj)
    (actual-value
      (thunk-exp obj) (thunk-env obj))
    obj))

(defn delay-it [exp env]
  (mk-thunk exp env))

;; --

;; -- evaluated thunk
(defrecord Thunk [exp env res])
(defn mk-thunk [exp env] (atom (Thunk. exp env nil)))
(defn thunk? [exp] (and (instance? IDeref exp) (instance? Thunk @exp)))
(defn evaluated-thunk? [obj]
  (and (thunk? obj)
       (not (nil? (:res @obj)))))
(defn thunk-exp [obj] (:exp @obj))
(defn thunk-res [obj] (:res @obj))
(defn thunk-env [obj] (:env @obj))
(defn update-thunk! [obj res]
  (swap! obj (fn [_ res] (Thunk. nil nil res)) res))

;; -- force/delay when evaluated thunk could be in use
(defn force-it [obj]
  (cond
    (evaluated-thunk? obj) (thunk-res obj)
    (thunk? obj)
    (let [res (actual-value
                (thunk-exp obj)
                (thunk-env obj))]
      (update-thunk! obj res)
      res)
    :else obj))

(defn delay-it [exp env]
  (mk-thunk exp env))
;; --


;; -- driver

(defn display [s]
  (newline)
  (println s)
  (newline))

(defn user-print [object]
  (if (p/compound-procedure? object)
    (display [:compound-procedure
              :args (p/procedure-parameters object)
              :body (p/procedure-body object)
              '<procedure-env>])
    (display object)))

(defn driver [s]
  (with-open [pbr (-> s StringReader. PushbackReader.)]
    (binding [*read-eval* false]
      (loop [e (read pbr false nil)]
        (when-not (nil? e)
          ;; force eval if result is a thunk
          (let [r (force-it (eval e env/global-env))]
            (user-print r))
          (recur (read pbr false nil)))))))

;; --


(defn eval-if [exp env]
  (if (s/true? (actual-value (f/if-predicate exp) env))
    (eval (f/if-consequent exp) env)
    (eval (f/if-alternative exp) env)))

(defn list-of-arg-values [args env]
  (if (app/no-operands? args)
    ()
    (cons (actual-value
            (app/first-operand args)
            env)
          (list-of-arg-values
            (app/rest-operands args)
            env))))

(defn list-of-delayed-arg-values [args env]
  (if (app/no-operands? args)
    ()
    (cons (delay-it
            (app/first-operand args)
            env)
          (list-of-delayed-arg-values
            (app/rest-operands args)
            env))))

(defn apply [f args env]
  (cond
    (p/primitive-procedure? f)
    (p/apply-primitive-procedure
      f
      (list-of-arg-values args env))

    (p/compound-procedure? f)
    (b/eval-sequence
      eval
      (p/procedure-body f)
      (extend-env
        (p/procedure-parameters f)
        (list-of-delayed-arg-values args env)
        env))

    :else (throw (Error. "Unknown procedure type -- APPLY" f))))


(defn eval [exp env]
  (cond
    (s/self-evaluating? exp) exp
    (s/variable? exp) (e/lookup-variable-value exp env)
    (q/quoted? exp) (q/text-of-quotation exp)
    (a/assignment? exp) (a/eval-assignment eval exp env)
    (d/definition? exp) (d/eval-definition eval exp env)
    (l/let? exp) (eval (l/let->combination exp) env)
    (f/if? exp) (eval-if exp env)
    (lm/lambda? exp) (p/mk-procedure
                       (lm/lambda-parameters exp)
                       (lm/lambda-body exp)
                       env)
    (b/begin? exp) (b/eval-sequence eval (b/begin-actions exp) env)
    (c/cond? exp) (eval (c/cond->if exp) env)
    (app/application? exp) (apply
                             (actual-value (app/operator exp) env)
                             (app/operands exp)
                             env)

    :else (throw (Error. (str "Unknown expression type: " exp)))))


