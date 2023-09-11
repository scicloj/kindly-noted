;; # Kindly

(ns kindly
  (:require [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [scicloj.note-to-test.v1.api :as note-to-test]
            [tablecloth.api :as tc]))

^:kindly/hide-code?
(note-to-test/define-value-representations!
  [{:predicate symbol?
    :representation (partial str "symbol ")}
   {:predicate (partial instance? java.awt.image.BufferedImage)
    :representation (constantly :image)}
   {:predicate set?
    :representation (comp set (partial map note-to-test/represent-value))}
   {:predicate sequential?
    :representation (partial mapv note-to-test/represent-value)}
   {:predicate map?
    :representation (fn [m]
                      (-> m
                          (update-keys note-to-test/represent-value)
                          (update-vals note-to-test/represent-value)))}])

;; ## Example

(kind/md
 "hello *hello* **hello**")

(-> "hello *hello* **hello**"
    kind/md
    meta)

;; ## Set of kinds

kindly/known-kinds

;; ## Kind inference
;; The logic to infer kinds is defined in the [kindly-advice](./kindly-advice.html) library.

;; ### Attaching metadata to forms
^:kind/md
["hello *hello* **hello**"]

(-> "^:kind/md
[\"hello *hello* **hello**\"]
"
    read-string
    meta)

(-> "^kind/md
[\"hello *hello* **hello**\"]
"
    read-string
    meta)

;; ### Attaching metadata to values
(kind/md
 ["hello *hello* **hello**"])

;; Values that cannot have metadata are wrapped in a vector before attaching metadata.
(kind/md
 "hello *hello* **hello**")



(kind/md
 "hello *hello* **hello**")


^:kind/md
["hello *hello* **hello**"]


;; ### Using values annotated by libraries
(require '[scicloj.noj.v1.vis :as vis]
         '[scicloj.noj.v1.datasets :as datasets])

(-> datasets/iris
    (vis/hanami-histogram :sepal-width
                          {:nbins 10})
    (assoc :height 100)
    meta)

;; ### Custom inference
;; In certain situations, kinds are inferred without annotation. For example, images:

(def clj-image
  (->  "https://clojure.org/images/clojure-logo-120b.png"
       (java.net.URL.)
       (javax.imageio.ImageIO/read)))

clj-image

;; ## Kind semantics

;; ### Pretty printing
(->> (range 30)
     (apply array-map)
     kind/pprint)

;; ### Hidden
(->> {:x 9}
     kind/hidden)

;; ### Web development

;; #### Hiccup
(kind/hiccup
 [:div {:style
        {:background-color "floralwhite"}}
  [:p "hello"]])

;;; #### Reagent
;; coming soon: `kind/reagent`

#_(kind/reagent
   ['(fn [data]
       [:div [:h1 (count data)]])
    (vec (range 99))])

;; ### Visual formats

;; #### Markdown
(kind/md
 "hello *hello* **hello**")

;; #### Code
(kind/code
 "(defn f [x] (+ x 9))")

;; #### Vega-Lite
(kind/vega-lite
 {:encoding
  {:y {:field "y", :type "quantitative"},
   :size {:value 400},
   :x {:field "x", :type "quantitative"}},
  :mark {:type "circle", :tooltip true},
  :width 400,
  :background "floralwhite",
  :height 100,
  :data {:values "x,y\n1,1\n2,-4\n3,9\n", :format {:type "csv"}}})

;; #### Coming soon
;; Vega, Cytoscape, ECharts, Plotly, 3Dmol

;; ### Specific types

;;; #### Images
clj-image

;;; #### Datasets
(-> {:x (range 3)}
    tc/dataset
    (tc/map-columns :y
                    [:x]
                    (fn [x] (* x x))))

;; ### Recursive kinds
;; Some kinds are to display recursively, interpreting values as data structures whose internal values may have their own kind semantics.

;; #### Plain data structures

;; Plain Clojure data structures have recursive kind semantics:
;; - Each tool has its own way to represent them visually
;; (e.g., Clay just uses text, while Portal has a hierarchical navigation UI).
;; - If the values inside them have kind information, they are handled accordingly.
;;;
;; Examples:

'(1 "A" :B 'C)

[1 "A" :B 'C]

#{1 "A" :B 'C}

{1 "A" :B 'C}

[(kind/hiccup
  [:div {:style
         {:background-color "floralwhite"}}
   [:p "hello"]])
 (kind/md
  "hello *hello* **hello**")
 (kind/code
  "(defn f [x] (+ x 9))")]

{:x  (kind/md
      "**hello**")
 (kind/md
  "**hello**") :x}

;; #### Tables

;; Coming soon

;; ## Topics in discussion

;; - hiding code
;; - handling styles around data visualizations
;; - compatibility with [Emmy-Viewers](https://emmy-viewers.mentat.org/)
;; - backwards compatibility of kind inference


^:kindly/hide-code?
(comment
  (note-to-test/gentest! "notebooks/kindly.clj"
                         {:accept true
                          :verbose true}))
