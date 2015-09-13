(ns sicp.c04.elv.env-test
  (:require [clojure.test :refer :all]
            [sicp.c04.elv.env :refer :all])
  (:refer-clojure :exclude [eval apply]))

(deftest env-manipulation
  (testing "extend-env"
    (let [env (extend-env empty-env '(k) '(v))]
      (is (= (lookup-var-value env 'k) 'v))
      (is (= (lookup-var-value (extend-env env '(k1) '(v1)) 'k1)
             'v1))
      ;; todo can I do away with exceptions?
      (is (thrown? Error (lookup-var-value env 'k1)))))
  (testing "define var"
    (let [env (extend-env empty-env '(k) '(v))]
      (is (= (lookup-var-value (define-variable! env 'x 1)
                               'x)
             1))))
  (testing "set var"
    (let [env (extend-env empty-env '(x) '(1))]
      (is (thrown? Error (set-variable-value! env 'y 1)))
      (let [env2 (define-variable! env 'y 1)]
        (set-variable-value! env2 'y 2)
        (is (= (lookup-var-value env2 'y) 2))))))

