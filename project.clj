(defproject sicp "0.0.1"
  :description "Solutions to SICP exercises"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/core.typed "0.3.22"]
                 [org.clojure/clojure "1.8.0"]]
  :injections [(require 'clojure.core.typed)
               (clojure.core.typed/install)])
