(ns sicp.c04.elv.let_test
  (:require [clojure.test :refer :all])
  (:require [sicp.c04.elv.let :refer [let->combination]])
  (:refer-clojure :exclude [eval apply true? false?]))

(deftest testing-let-exps
  (testing "let->cobination"
    (is (= (let->combination
             '(let ((a 1)
                     (b 2))
                (+ a b)))
           '((lambda (a b) (+ a b)) 1 2)))

    (is (= (let->combination
             '(let sum ((a 1)
                         (b 2))
                       (sum a b)))
           '(begin
              (define sum (lambda (a b) (sum a b)))
              (sum 1 2))))))
