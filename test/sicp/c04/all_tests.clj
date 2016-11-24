(ns sicp.c04.all-tests
  (:require [sicp.c04.elv.env-test]
            [sicp.c04.elv.let-test]
            [sicp.c04.elv.letstart-test]
            [sicp.c04.elv.logical-test]
            [sicp.c04.elv.while-test]
            [sicp.c04.driver-test]
            [sicp.c04.elv-test]
            [sicp.c04.ex4-16-test]
            [sicp.c04.elv.env-test]
            [clojure.test :as test]))

(test/run-all-tests #"sicp.*")
