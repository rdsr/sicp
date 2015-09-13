(ns sicp.c04.base
  (:refer-clojure :exclude [eval apply true? false?]))

(declare eval apply set-variable-value! define-variable! lookup-variable-value text-of-quotation
         mk-procedure primitive-procedure? apply-primitive-procedure compound-procedure?
         procedure-body extend-environment procedure-parameters procedure-environment)

(defn false? [x] (= x false))
(defn true? [x] (not (false? x)))

(defn self-evaluating? [exp]
  (cond
    (number? exp) true
    (string? exp) true
    :else false))

(defn variable? [exp]
  (symbol? exp))

(defn tagged-list? [exp tag]
  (and (list? exp)
       (= (first exp) tag)))

(defn quoted? [exp]
  (tagged-list? exp 'quote))

(defn assignemt? [exp]
  (tagged-list? exp 'set!))
(defn assignemt-variable [exp]
  (second exp))
(defn assignemt-value [exp]
  (nth exp 2))
(defn eval-assignment [exp env]
  (set-variable-value! (assignemt-variable exp)
                       (eval (assignemt-value exp) env)))


(defn lambda? [exp] (tagged-list? exp 'lambda))
(defn lambda-parameters [exp] (second exp))
(defn lambda-body [exp] (nth exp 2))
(defn mk-lambda [args body] (list 'lambda args body))

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
      (nth exp 2)
      (mk-lambda (rest (second exp))
                 (nth exp 2)))))

(defn mk-definition [variable value]
  (list 'define variable value))

(defn eval-definition [exp env]
  (define-variable! (definition-variable exp)
                    (eval (definition-value exp) env)))


(defn if? [exp]
  (tagged-list? exp 'if))
(defn if-predicate [exp]
  (second exp))
(defn if-consequent [exp]
  (nth exp 2))
(defn if-alternative [exp]
  (if (= (count exp) 4)
    (nth exp 3)
    'false))
(defn mk-if [predicate consequent alternative]
  (list 'if predicate consequent alternative))

(defn eval-if [exp env]
  (if (true? (eval (if-predicate exp) env))
    (eval (if-consequent exp) env)
    (eval (if-alternative exp) env)))

(defn begin? [exp] (tagged-list? exp 'begin))
(defn begin-actions [exp] (rest exp))
(defn last-exp? [sq] (-> sq rest empty?))
(defn first-exp [sq] (first sq))
(defn rest-exps [sq] (rest sq))
(defn mk-begin [sq] (cons 'begin sq))

(defn sequence->exp [sq]
  (cond
    (nil? sq) sq
    (last-exp? sq) (first-exp sq)
    :else (mk-begin sq)))

(defn eval-sequence [sq env]
  (cond
    (last-exp? sq) (eval (first-exp sq) env)
    :else (do (eval (first-exp sq) env)
              (eval-sequence (rest-exps sq) env))))

;; a procedure application is not one of the above exps.
(defn application? [exp] (list? exp))
(defn operator [exp] (first exp))
(defn operands [exp] (rest exp))
(defn no-operands? [ops] (empty? ops))
(defn first-operand [ops] (first ops))
(defn rest-operands [ops] (rest ops))

(defn mk-procedure [parameters body env]
  (list 'procedure parameters body env))
(defn compound-procedure? [p] (tagged-list? p 'procedure))
(defn procedure-parameters [p] (second p))
(defn procedure-body [p] (nth p 2))
(defn procedure-env [p] (nth p 3))

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

(defn list-of-values [exps env]
  (if (no-operands? exps)
    '()
    (cons (eval (first-operand exps) env)
          (list-of-values (rest-operands exps) env))))

;; eval
(defn eval [exp env]
  (cond
    (self-evaluating? exp) exp
    (variable? exp) (lookup-variable-value exp env)
    (quoted? exp) (text-of-quotation exp)
    (assignemt? exp) (eval-assignment exp env)
    (definition? exp) (eval-definition exp env)
    (if? exp) (eval-if exp env)
    (lambda? exp) (mk-procedure (lambda-parameters exp)
                                (lambda-body exp)
                                env)
    (begin? exp) (eval-sequence (begin-actions exp) env)
    (cond? exp) (eval (cond->if exp) env)
    (application? exp) (apply (eval (operator exp) env)
                              (list-of-values (operands exp) env))
    :else (Error. (str "Unknown expression type -- eval" exp))))

(defn apply [procedure arguments]
  (cond
    (primitive-procedure? procedure) (apply-primitive-procedure procedure arguments)
    (compound-procedure? procedure) (eval-sequence
                                      (procedure-body procedure)
                                      (extend-environment
                                        (procedure-parameters procedure)
                                        arguments
                                        (procedure-environment procedure)))
    :else (Error. (str "Unknown procedure type -- apply " procedure))))
