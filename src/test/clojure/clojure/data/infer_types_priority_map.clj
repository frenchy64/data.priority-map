(ns clojure.data.infer-types-priority-map
  (:use clojure.test)
  (:require [clojure.core.typed :as t]
            [clojure.core.typed.runtime-infer :as infer]))

(t/install #{:load})

(defn delete-anns [nss]
  (doseq [ns nss]
    (infer/delete-generated-annotations
      ns
      {:ns ns})))

(defn infer-anns [nss]
  (doseq [ns nss]
    (t/runtime-infer :ns ns)))

(def infer-files
  '[clojure.data.priority-map
    ])

;; FIXME shouldn't need this, but some types
;; don't compile
(delete-anns infer-files)

(def tests 
  '[clojure.data.test-priority-map
    ])

;; FIXME need to forcibly reload the :lang'd file. Why?
(apply require (conj tests :reload-all))
(apply run-tests tests)

(infer-anns infer-files)
