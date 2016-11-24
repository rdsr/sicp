(ns sicp.04.ex4-16-test
  (:require [clojure.test :refer :all]
            [sicp.c04.elv.procedure :as p]
            [sicp.c04.elv.definition :as d]
            [sicp.c04.elv.lambda :as l]
            [sicp.c04.ex4-16 :refer :all]))

(deftest test-scan-out-defines
  (testing "test variable definitions in block structure"
    (let [exp '(define (solve f y0 dt)
                       (define y (integral (delay dy) y0 dt))
                       (define dy (stream-map f y))
                       y)
          lambda (d/definition-value exp)]
      (is
        (= (scan-out-defines (l/lambda-body lambda))
           '(let (( y *unassigned*)
                   (dy *unassigned*))
              (begin
                (set! y (integral (delay dy) y0 dt))
                (set! dy (stream-map f y))
                y)))))))
