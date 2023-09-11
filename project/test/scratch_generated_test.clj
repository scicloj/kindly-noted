(ns scratch-generated-test
  (:require
   [clojure.test :refer [deftest is]]
   [scicloj.note-to-test.v1.api :as note-to-test]))

(deftest test-everything

  (is (= (note-to-test/represent-value-with-meta
          (+ 1 4 9))
       {:value 14, :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          (* 9 10))
       {:value 90, :meta nil})))
