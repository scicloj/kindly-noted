;; # Kindly

;; Kindly is a proposed standard for requesting data visualizations in Clojure.

;; It specifies in what kinds of way Clojure forms and values should be displayed.

(ns kindly
  (:require [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [tablecloth.api :as tc]))

;; ![Kindly logo](notebooks/images/Kindly.svg.png)

;; ## Why

;; * Different tools have had different ways of writing notes. For example:
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

;; ## Goal

;; * Have a standard way to request data visualizations
;; * for blog posts, books, slideshows, reports, dashboards, and interactive analyses,
;; * that just will work across different tools,
;; * without even mentioning those tools in the code.

;; ## Status

;; * Supported by Clay & Claykind
;;
;; * Has adapters for Portal (kind-portal) & Clerk (kind-clerk)
;;
;; * Ready to explore on other tools
                                        ;
;; ## Example

;; Here is how one may request something of `kind/md`, which means Markdown:

(kind/md
 "hello *hello* **hello**")

;; ## The set of kinds

(sort kindly/known-kinds)

;; ## How to use Kinds?

;; ### Attaching metadata to forms
^:kind/md
["hello *hello* **hello**"]

^kind/md
["hello *hello* **hello**"]

;; ### Attaching metadata to values
(-> ["hello *hello* **hello**"]
    kind/md)

(-> ["hello *hello* **hello**"]
    kind/md
    meta
    :kindly/kind)

;; ### Attaching metadata to values - cont.

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

;; ### Using values annotated by libraries

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


;; ### Automatically-inferred kinds
;; In certain situations, kinds are inferred without annotation.
;; The kindly-advice library provides the default inference behaviour and an option to extend it.

;; For example, images:

(def clj-image
  (->  "https://upload.wikimedia.org/wikipedia/commons/e/eb/Ash_Tree_-_geograph.org.uk_-_590710.jpg"
       (java.net.URL.)
       (javax.imageio.ImageIO/read)))

clj-image

(require '[tablecloth.api :as tc])

(tc/dataset {:x (range 3)
             :y (repeatedly 3 rand)})


;; ## Hiding code

;; To the the code and only show the output, one may either use `:kindly/hide-code true` in the form metadata, or apply `kindly/hide-code` to the value.


;; ## Passing options

;; The functions in the `kind` namespace may recieve an additiona map argument, which is attached at the `:kindly/options` key of a value's metadata.

(def echarts-example
  {:title {:text "Echarts Example"}
   :tooltip {}
   :legend {:data ["sales"]}
   :xAxis {:data ["Shirts", "Cardigans", "Chiffons",
                  "Pants", "Heels", "Socks"]}
   :yAxis {}
   :series [{:name "sales"
             :type "bar"
             :data [5 20 36
                    10 10 20]}]})

(kind/echarts
 echarts-example)

(-> echarts-example
    (kind/echarts {:element/style
                   {:width "500px"
                    :height "200px"}}))

(-> echarts-example
    (kind/echarts {:element/style
                   {:width "500px"
                    :height "200px"}})
    meta)


;; ## Fragments

;; `kind/fragment` is a special kind. It expects a sequential value and generates multiple items, of potentially multiple kinds, from its elements.

(->> ["purple" "darkgreen" "goldenrod"]
     (mapcat (fn [color]
               [(kind/md (str "### subsection: " color))
                (kind/hiccup [:div {:style {:background-color color
                                            :color "lightgrey"}}
                              [:big [:p color]]])]))
     kind/fragment)
