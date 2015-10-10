(ns sicp.c04.elv.env-test
  (:require [clojure.test :refer :all]
            [sicp.c04.elv.env :refer :all])
  (:refer-clojure  :exclude [eval apply]))

(deftest env-manipulation
  (testing "extend-env"
    (let [env (extend-env '(k) '(v) empty-env)]
      (is (= (lookup-variable-value 'k env) 'v))
      (is (= (lookup-variable-value 'k1
                                    (extend-env '(k1) '(v1) env))
             'v1))
      ;; todo can I do away with exceptions?
      (is (thrown? Error (lookup-variable-value 'k1 env)))))

  (testing "define var"
    (let [env (extend-env '(k) '(v) empty-env)]
      (define-variable! 'x 1 env)
      (is (= (lookup-variable-value 'x env)
             1))))

  (testing "set var"
    (let [env (extend-env '(x) '(1) empty-env)]
      (is (thrown? Error (set-variable-value! 'y 1 env)))
      (define-variable! 'y 1 env)
      (set-variable-value! 'y 2 env)
      (is (= (lookup-variable-value 'y env) 2))))

  (testing "mk-unbound"
    (let [env (extend-env '(x) '(1) empty-env)]
      (mk-unbound! 'x env)
      (is (thrown? Error (lookup-variable-value 'x env))))))

