(ns sicp.c04.elv.letstart-test
  (:require [clojure.test :refer :all])
  (:require [sicp.c04.elv.letstar :as ls])
  (:refer-clojure :exclude [eval apply true? false?]))

(deftest testing-let*-exps
  (testing "let*->nested-let"
    (is (= (ls/let*->nested-let
             '(let ((a 1)
                     (b (+ a 2)))
                (+ a b)))
           '(let ((a 1))
              (let ((b (+ a 2)))
                (+ a b)))))))
