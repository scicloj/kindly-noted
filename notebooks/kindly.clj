;; # Kindly

;; WIP documentation for [kindly](https://github.com/scicloj/kindly)

(ns kindly
  (:require [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [scicloj.clay.v2.api :as clay]
            [tablecloth.api :as tc]))

;; # Why

;; * Different tools have different ways of writing notes. For example:
;;   * [Anglican tutorials](https://probprog.github.io/anglican/examples/index.html) ([source](https://bitbucket.org/probprog/anglican-examples/src/master/worksheets/)) - written in [Gorilla REPL](https://github.com/JonyEpsilon/gorilla-repl)
;;   * [thi-ng/geom viz examples](https://github.com/thi-ng/geom/blob/feature/no-org/org/examples/viz/demos.org)  ([source](https://raw.githubusercontent.com/thi-ng/geom/feature/no-org/org/examples/viz/demos.org)) - written in [Org-babel-clojure](https://orgmode.org/worg/org-contrib/babel/languages/ob-doc-clojure.html)
;;   * [Clojure2d docs](https://github.com/Clojure2D/clojure2d#Usage) ([source1](https://github.com/Clojure2D/clojure2d/blob/master/src/clojure2d), [source2](https://github.com/Clojure2D/clojure2d/blob/master/metadoc/clojure2d/)) - written in [Codox](https://github.com/weavejester/codox) and [Metadoc](https://github.com/generateme/metadoc)
;;   * [Tablecloth API docs](https://scicloj.github.io/tablecloth/index.html) ([source](https://github.com/scicloj/tablecloth/blob/master/docs/index.Rmd)) - written in [rmarkdown-clojure](https://github.com/genmeblog/rmarkdown-clojure)
;;   * [R interop ClojisR examples](https://github.com/scicloj/clojisr-examples) ([source](https://github.com/scicloj/clojisr-examples/tree/master/src/clojisr_examples)) - written in [Notespace v2](https://github.com/scicloj/notespace/blob/master/doc/v2.md)
;;   * [Bayesian optimization tutorial](https://nextjournal.com/generateme/bayesian-optimization) ([source](https://nextjournal.com/generateme/bayesian-optimization)) - written in [Nextjournal](https://nextjournal.com/)
;;   * [scicloj.ml tutorials](https://github.com/scicloj/scicloj.ml-tutorials#tutorials-for-sciclojml) ([source](https://github.com/scicloj/scicloj.ml-tutorials/tree/main/src/scicloj/ml)) - written in [Notespace v3](https://github.com/scicloj/notespace/blob/master/doc/v3.md)
;;   * [Clojure2d color tutorial](https://clojure2d.github.io/clojure2d/docs/notebooks/index.html#/notebooks/color.clj) ([source](https://github.com/Clojure2D/clojure2d/blob/master/notebooks/color.clj)) - written in [Clerk](https://github.com/nextjournal/)
;;   * [Viz.clj](https://scicloj.github.io/viz.clj/) ([source](https://github.com/scicloj/viz.clj/blob/master/notebooks/intro.clj)) - written in Kindly using [Clay](https://scicloj.github.io/viz.clj/)
;;   * ...

;; # Goal

;; * Have one way to express data visualizations
;; * for blog posts, books, slideshows, reports, dashboards, and interactive analyses,
;; * that just will work across different tools,
;; * without mentioning those tools in the code.

;; # Status

;; * Supported by Clay & Claykind
;;
;; * Has adapters for Portal (kind-portal) & Clerk (kind-clerk)
;;
;; * Ready to explore on other tools
                                        ;
;; # Example

(kind/md
 "hello *hello* **hello**")

;; # The set of kinds

kindly/known-kinds

;; # How to use Kinds?

;; ## Attaching metadata to forms
^:kind/md
["hello *hello* **hello**"]

^kind/md
["hello *hello* **hello**"]

;; ## Attaching metadata to values
(-> ["hello *hello* **hello**"]
    kind/md)

(-> ["hello *hello* **hello**"]
    kind/md
    meta
    :kindly/kind)

;; ## Attaching metadata to values - cont.

(-> "hello *hello* **hello**"
    kind/md
    meta
    :kindly/kind)

;; Values that cannot have metadata are wrapped in a vector before attaching metadata.

(-> "hello *hello* **hello**"
    kind/md
    kind/pprint)

(-> "hello *hello* **hello**"
    kind/md
    meta)

;; ## Using values annotated by libraries

(defn my-library-function-for-big-big-text [text]
  (kind/hiccup
   [:big {:style {:background "#ccddcc"}}
    [:big text]]))

(-> "hello"
    my-library-function-for-big-big-text)

(-> "hello"
    my-library-function-for-big-big-text
    (assoc-in [1 :style]
              {:background "#ddccdd"}))


;; ## Automatically-inferred kinds
;; In certain situations, kinds are inferred without annotation. For example, images:

(def clj-image
  (->  "https://upload.wikimedia.org/wikipedia/commons/e/eb/Ash_Tree_-_geograph.org.uk_-_590710.jpg"
       (java.net.URL.)
       (javax.imageio.ImageIO/read)))

clj-image

(require '[tablecloth.api :as tc])

(tc/dataset {:x (range 3)
             :y (repeatedly 3 rand)})

;; # Catalogue of visualisations (WIP)

;; ## Markdown - `:kind/md`
(-> "hello *hello* **hello**"
    kind/md)

;; ## Code

(-> "(defn f [x] (+ x 9))"
    kind/code)

;; ## Vega-Lite

(def my-plot
  (-> {:encoding
       {:y {:field "y", :type "quantitative"},
        :size {:value 400},
        :x {:field "x", :type "quantitative"}},
       :mark {:type "circle", :tooltip true},
       :width 400,
       :background "floralwhite",
       :height 100,
       :data {:values "x,y\n1,1\n2,-4\n3,9\n", :format {:type "csv"}}}
      kind/vega-lite))

my-plot

;;; ## Images
clj-image

;;; ## Datasets
(def my-dataset
  (-> {:x (range 3)}
      tc/dataset
      (tc/map-columns :y
                      [:x]
                      (fn [x] (* x x)))))

my-dataset

;;; ## Reagent

(kind/reagent
 ['(fn [data]
     [:div {:style {:background "#ccddcc"}}
      [:big (count data)]])
  (vec (range 99))])


;; ## Pretty printing
(->> (range 30)
     (apply array-map)
     kind/pprint)


;; ## Hidden
(->> {:x 9}
     kind/hidden)

;; ## Plain data structures

;; Plain Clojure data structures have recursive kind semantics:
;; * Each tool has its own way to represent them visually
;; (e.g., Clay just uses text, while Portal has a hierarchical navigation UI).

(list 1 "A" :B 'C)

[1 "A" :B 'C]

#{1 "A" :B 'C}

{1 "A" :B 'C}


;; ## Plain data structures - nesting

;; * If the values inside them have kind information, they are handled accordingly.

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

;; ## Hiccup
(-> [:div {:style
           {:background-color "floralwhite"}}
     [:p "hello"]]
    kind/hiccup)

;; ## Hiccup - nesting
(-> [:div {:style
           {:background-color "floralwhite"
            :border-style "solid"}}
     [:p {:style {:background-color "#ccddcc"
                  :border-style "solid"}}
      "hello"]
     (kind/md
      "hello *hello* **hello**")
     (kind/code
      "(defn f [x] (+ x 9))")
     my-plot]
    kind/hiccup)

;; ## Portal

(-> [(kind/hiccup [:p {:style {:background-color "#ccddcc"
                               :border-style "solid"}}
                   "hello"])
     (kind/md
      "hello *hello* **hello**")
     (kind/code
      "(defn f [x] (+ x 9))")
     my-plot]
    kind/portal)
