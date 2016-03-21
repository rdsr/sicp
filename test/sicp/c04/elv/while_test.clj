(ns sicp.c04.elv.while-test
  (:require
    [clojure.test :refer :all]
    [sicp.c04.ex4-9 :as w]
    [sicp.c04.elv.env :as e]))

(deftest testing-while
  (testing "simple while"
    (is (= (w/while->combination
             '(while (> i 0)
                (println i) (set! i (dec i))))
           '((define while
                     (lambda
                       ()
                       (if (> i 0)
                         (begin (println i)
                                (while))
                         false)))
              (while))))))



