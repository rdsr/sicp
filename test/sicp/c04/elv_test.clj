(ns sicp.c04.elv-test
  (:require [clojure.test :refer :all]
            [sicp.c04.eval :refer :all]
            [sicp.c04.elv.assignment :refer :all]
            [sicp.c04.elv.definition :refer :all]
            [sicp.c04.elv.cond :refer :all]
            [sicp.c04.elv.begin :refer :all]
            [sicp.c04.elv.sym :refer :all])
  (:refer-clojure :exclude [eval apply true? false?]))

(deftest representing-exps
  (testing "self-evaluating"
    (is (self-evaluating? 1))
    (is (self-evaluating? "1")))

  (testing "variable?"
    (is (variable? 'x)))

  (testing "assignment"
    (let [v '(set! x 1)]
      (is (assignment? v))
      (is (= (assignemt-value v) 1))
      (is (= (assignemt-variable v) 'x))))

  (testing "definition"
    (let [v1 '(define x 1)
          v2 '(define (x a b c d)
                      (+ a b c d))
          v3 '(define x
                      (lambda (a b c d)
                              (+ a b c d)))]
      (is (definition? v1))
      (is (definition? v2))
      (is (definition? v3))
      (is (= (definition-variable v1) 'x))
      (is (= (definition-variable v2) 'x))
      (is (= (definition-variable v3) 'x))
      (is (= (definition-value v1) '1))
      (is (= (definition-value v2)
             (definition-value v3)))))

  (testing "begin"
    (let [v1 '(begin x1 x2)]
      (is (begin? v1))
      (is (= (begin-actions v1) '(x1 x2)))
      (is (-> v1 begin-actions rest-exps last-exp?))
      (is (= (-> v1 begin-actions sequence->exp) v1))))

  (testing "cond->if"
    (is (= (cond->if '(cond
                   ((> x 0) x)
                   ((= x 0) (display 'zero) 0)
                   (else (-x))))
           '(if (> x 0)
              x
              (if (= x 0)
                (begin (display (quote zero)) 0)
                (-x)))))
    (is (= (cond->if '(cond (else (+ x 2))))
           '(+ x 2)))
    (is (false? (cond->if '(cond))))))
