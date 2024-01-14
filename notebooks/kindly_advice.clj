(ns kindly-advice
  (:require [scicloj.kindly-advice.v1.api :as kindly-advice]
            [scicloj.kindly.v4.kind :as kind]
            [scicloj.kindly.v4.api :as kindly]))

^:kindly/hide-code
(def md (comp kindly/hide-code
              kind/md))

(md
 "
# Kindly-advice

## Goal

- provide **tools** with the necessary information to support Kindly

- have sensible defaults

- be user-extensible

## Asking for advice
")

(kindly-advice/advise {:value (kind/hiccup
                               [:div [:h1 "hello"]])})

(md "
The `:kind` field is the important one, expressing the bottom line of the inference: Kindly-advice recommends the tool handles this value as Hiccup.

The tool's job will usually be to display the `:value` field based on the `:kind` field.

In the following exmple, we are asking for advice for given form (annotated by Kindly in this example).
Kindly-advice evaluates the form and adds the resulting value to complete the context. This completion will only take place if the value is missing. It is recommended that tools will take care of evaluation themselves and pass the complete context to Kindly-advice. Doing so allows the tool to handle Exceptions better, among other things.
Kindly-advice checks both the form and value for metadata. The metadata might not be present on the value.
")

(kindly-advice/advise {:form
                       ^:kind/hiccup
                       [:div [:h1 "hello"]]})

(md "Sometimes, there is no inferred kind, as no metadata or relevant predicates say anything useful:")

(kindly-advice/advise {:form '(+ 1 2)})

(md "In some situations, the kind inferred by predicates. Kindly-advice has a list of default predicates, which can be extended by the user. In the following eexample, it recognizes a dataset created by Tablecloth.")

(require '[tablecloth.api :as tc])
(kindly-advice/advise {:value (tc/dataset {:x (range 4)})})


;; ## Extending

(kindly-advice/add-advisor!
 (fn [{:keys [value]}]
   (if (and (vector? value)
            (-> value first (= :div)))
     [[:kind/hiccup]])))

(kindly-advice/advise
 {:form '[:div [:p "hello"]]})


(md "
## Examples

Kindly-advice is used by the following projects:
* [kind-portal](https://github.com/scicloj/kind-portal)
* [kind-clerk](https://github.com/scicloj/kind-clerk)
* [read-kinds](https://github.com/scicloj/read-kinds)
* [Clay](https://scicloj.github.io/clay/)
")
