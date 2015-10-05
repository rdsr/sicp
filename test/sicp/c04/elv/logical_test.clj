(ns sicp.c04.elv.logical-test
  (:require [clojure.test :refer :all])
  (:require [sicp.c04.elv.logical :refer :all])
  (:refer-clojure :exclude [eval apply true? false?]))

(deftest testing-exps
  (testing "and->if"
    (is (= (and->if '(and 1 0 (+ 2 3)))
           '(if 1 (if 0 (+ 2 3) false) false)))
    (is (or->if '(or 1 0 (+ 2 3)))
        '(if 1 1 (if 0 0 (+ 2 3))))))

