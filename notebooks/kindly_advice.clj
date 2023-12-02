(ns kindly-advice
  (:require [scicloj.kindly-advice.v1.api :as kindly-advice]
            [scicloj.kindly.v4.kind :as kind]))

;; # Kindly-advice

;; # Goal

;; - provide **tools** with the necessary information to support Kindly

;; - have sensible defaults

;; - be user-extensible


;; # Asking for advice

(kindly-advice/advise
 {:value [0 1 2 3]})

(kindly-advice/advise
 {:form '(range 4)})

(kindly-advice/advise
 {:form '[:p "hello"]})

(kindly-advice/advise
 {:form '(kind/hiccup
          [:p "hello"])})

;; # Extending

(kindly-advice/add-advisor!
 (fn [{:keys [value]}]
   (if (and (vector? value)
            (-> value first (= :div)))
     [[:kind/hiccup]])))

(kindly-advice/advise
 {:form '[:div [:p "hello"]]})
