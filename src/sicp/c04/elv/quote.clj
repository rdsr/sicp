(ns sicp.c04.elv.quote
  (:require [sicp.c04.elv.util :refer [tagged-list?]]))

;; -- quote
(defn quoted? [exp] (tagged-list? exp 'quote))
(defn text-of-quotation [exp] (rest exp))
