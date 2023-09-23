;; # Kindly

(ns kindly
  (:require [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [scicloj.note-to-test.v1.api :as note-to-test]
            [scicloj.clay.v2.api :as clay]
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

;; ## How to use Kinds?

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

;; ### Automatically-inferred kinds
;; In certain situations, kinds are inferred without annotation. For example, images:

(def clj-image
  (->  "https://clojure.org/images/clojure-logo-120b.png"
       (java.net.URL.)
       (javax.imageio.ImageIO/read)))

clj-image

;; ## Catalogue of visualisations

;; ### Markdown - `:kind/md`
(-> "hello *hello* **hello**"
    kind/md)

(-> "hello *hello* **hello**"
    kind/md
    clay/in-portal)

;; ### Code

(-> "(defn f [x] (+ x 9))"
    kind/code)

(-> "(defn f [x] (+ x 9))"
    kind/code
    clay/in-portal)

;; ### Vega-Lite

(def my-plot
  {:encoding
   {:y {:field "y", :type "quantitative"},
    :size {:value 400},
    :x {:field "x", :type "quantitative"}},
   :mark {:type "circle", :tooltip true},
   :width 400,
   :background "floralwhite",
   :height 100,
   :data {:values "x,y\n1,1\n2,-4\n3,9\n", :format {:type "csv"}}})

(-> my-plot
    kind/vega-lite)

(-> my-plot
    kind/vega-lite
    clay/in-portal)

;; ### Coming soon
;; Vega, Cytoscape, ECharts, Plotly, 3Dmol

;;; ### Images
clj-image

(-> clj-image
    clay/in-portal)

;;; ### Datasets
(def my-dataset
  (-> {:x (range 3)}
      tc/dataset
      (tc/map-columns :y
                      [:x]
                      (fn [x] (* x x)))))

my-dataset

(-> my-dataset
    clay/in-portal)

;; ### Hiccup
(-> [:div {:style
           {:background-color "floralwhite"}}
     [:p "hello"]]
    kind/hiccup)

(-> [:div {:style
           {:background-color "floralwhite"}}
     [:p "hello"]]
    kind/hiccup
    clay/in-portal)

;;; ### Reagent
;; coming soon: `kind/reagent`

#_(kind/reagent
   ['(fn [data]
       [:div [:h1 (count data)]])
    (vec (range 99))])


;; ### Pretty printing
(->> (range 30)
     (apply array-map)
     kind/pprint)

(->> (range 30)
     (apply array-map)
     kind/pprint
     clay/in-portal)

;; ### Hidden
(->> {:x 9}
     kind/hidden)

;; ### Plain data structures

;; Plain Clojure data structures have recursive kind semantics:
;; - Each tool has its own way to represent them visually
;; (e.g., Clay just uses text, while Portal has a hierarchical navigation UI).
;; - If the values inside them have kind information, they are handled accordingly.
;;;
;; Examples:

(list 1 "A" :B 'C)

(clay/in-portal
 (list 1 "A" :B 'C))

[1 "A" :B 'C]

(clay/in-portal
 [1 "A" :B 'C])

#{1 "A" :B 'C}

(clay/in-portal
 #{1 "A" :B 'C})

{1 "A" :B 'C}

(clay/in-portal
 {1 "A" :B 'C})

[(kind/hiccup
  [:div {:style
         {:background-color "floralwhite"}}
   [:p "hello"]])
 (kind/md
  "hello *hello* **hello**")
 (kind/code
  "(defn f [x] (+ x 9))")]

(clay/in-portal
 [(kind/hiccup
   [:div {:style
          {:background-color "floralwhite"}}
    [:p "hello"]])
  (kind/md
   "hello *hello* **hello**")
  (kind/code
   "(defn f [x] (+ x 9))")])

{:x  (kind/md
      "**hello**")
 (kind/md
  "**hello**") :x}

(clay/in-portal
 {:x  (kind/md
       "**hello**")
  (kind/md
   "**hello**") :x})

;; ### Tables

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






;; ## Old notes

;; - Different tools have different ways of writing notes. For example:
;;   - [Anglican tutorials](https://probprog.github.io/anglican/examples/index.html) ([source](https://bitbucket.org/probprog/anglican-examples/src/master/worksheets/)) - written in [Gorilla REPL](https://github.com/JonyEpsilon/gorilla-repl)
;;   - [thi-ng/geom viz examples](https://github.com/thi-ng/geom/blob/feature/no-org/org/examples/viz/demos.org)  ([source](https://raw.githubusercontent.com/thi-ng/geom/feature/no-org/org/examples/viz/demos.org)) - written in [Org-babel-clojure](https://orgmode.org/worg/org-contrib/babel/languages/ob-doc-clojure.html)
;;   - [Clojure2d docs](https://github.com/Clojure2D/clojure2d#Usage) ([source1](https://github.com/Clojure2D/clojure2d/blob/master/src/clojure2d), [source2](https://github.com/Clojure2D/clojure2d/blob/master/metadoc/clojure2d/)) - written in [Codox](https://github.com/weavejester/codox) and [Metadoc](https://github.com/generateme/metadoc)
;;   - [Tablecloth API docs](https://scicloj.github.io/tablecloth/index.html) ([source](https://github.com/scicloj/tablecloth/blob/master/docs/index.Rmd)) - written in [rmarkdown-clojure](https://github.com/genmeblog/rmarkdown-clojure)
;;   - [R interop ClojisR examples](https://github.com/scicloj/clojisr-examples) ([source](https://github.com/scicloj/clojisr-examples/tree/master/src/clojisr_examples)) - written in [Notespace v2](https://github.com/scicloj/notespace/blob/master/doc/v2.md)
;;   - [Bayesian optimization tutorial](https://nextjournal.com/generateme/bayesian-optimization) ([source](https://nextjournal.com/generateme/bayesian-optimization)) - written in [Nextjournal](https://nextjournal.com/)
;;   - [scicloj.ml tutorials](https://github.com/scicloj/scicloj.ml-tutorials#tutorials-for-sciclojml) ([source](https://github.com/scicloj/scicloj.ml-tutorials/tree/main/src/scicloj/ml)) - written in [Notespace v3](https://github.com/scicloj/notespace/blob/master/doc/v3.md)
;;   - [Clojure2d color tutorial](https://clojure2d.github.io/clojure2d/docs/notebooks/index.html#/notebooks/color.clj) ([source](https://github.com/Clojure2D/clojure2d/blob/master/notebooks/color.clj)) - written in [Clerk](https://github.com/nextjournal/)
;;   - [Viz.clj](https://scicloj.github.io/viz.clj/) ([source](https://github.com/scicloj/viz.clj/blob/master/notebooks/intro.clj)) - written in Kindly using [Clay](https://scicloj.github.io/viz.clj/)
;;   - ...
;
