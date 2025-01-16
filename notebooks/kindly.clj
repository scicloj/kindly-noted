;; # Kindly

;; Kindly is a standard for requesting data visualizations in Clojure.

;; It specifies in what kinds of way Clojure forms and values should be displayed.

(ns kindly
  (:require [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [tablecloth.api :as tc]))

;; ![Kindly logo](notebooks/images/Kindly.svg.png)

;; ## Why

;; Different tools have had different ways of writing notes. For example:

;; - [Anglican tutorials](https://probprog.github.io/anglican/examples/index.html) ([source](https://bitbucket.org/probprog/anglican-examples/src/master/worksheets/)) - written in [Gorilla REPL](https://github.com/JonyEpsilon/gorilla-repl)
;; - [thi-ng/geom viz examples](https://github.com/thi-ng/geom/blob/feature/no-org/org/examples/viz/demos.org)  ([source](https://raw.githubusercontent.com/thi-ng/geom/feature/no-org/org/examples/viz/demos.org)) - written in [Org-babel-clojure](https://orgmode.org/worg/org-contrib/babel/languages/ob-doc-clojure.html)
;; - [Clojure2d docs](https://github.com/Clojure2D/clojure2d#Usage) ([source1](https://github.com/Clojure2D/clojure2d/blob/master/src/clojure2d), [source2](https://github.com/Clojure2D/clojure2d/blob/master/metadoc/clojure2d/)) - written in [Codox](https://github.com/weavejester/codox) and [Metadoc](https://github.com/generateme/metadoc)
;; - [Tablecloth API docs](https://scicloj.github.io/tablecloth/index.html) ([source](https://github.com/scicloj/tablecloth/blob/master/docs/index.Rmd)) - written in [rmarkdown-clojure](https://github.com/genmeblog/rmarkdown-clojure)
;; - [R interop ClojisR examples](https://github.com/scicloj/clojisr-examples) ([source](https://github.com/scicloj/clojisr-examples/tree/master/src/clojisr_examples)) - written in [Notespace v2](https://github.com/scicloj/notespace/blob/master/doc/v2.md)
;; - [Bayesian optimization tutorial](https://nextjournal.com/generateme/bayesian-optimization) ([source](https://nextjournal.com/generateme/bayesian-optimization)) - written in [Nextjournal](https://nextjournal.com/)
;; - [scicloj.ml tutorials](https://github.com/scicloj/scicloj.ml-tutorials#tutorials-for-sciclojml) ([source](https://github.com/scicloj/scicloj.ml-tutorials/tree/main/src/scicloj/ml)) - written in [Notespace v3](https://github.com/scicloj/notespace/blob/master/doc/v3.md)
;; - [Clojure2d color tutorial](https://clojure2d.github.io/clojure2d/docs/notebooks/index.html#/notebooks/color.clj) ([source](https://github.com/Clojure2D/clojure2d/blob/master/notebooks/color.clj)) - written in [Clerk](https://github.com/nextjournal/)
;; - [Tablecloth documentation](https://scicloj.github.io/tablecloth) ([source](https://github.com/scicloj/tablecloth/blob/master/notebooks/index.clj)) - written in Kindly using [Clay](https://scicloj.github.io/clay)
;; - ...

;; ## Goal

;; * Have a standard way to request data visualizations
;; * for blog posts, books, slideshows, reports, dashboards, and interactive analyses,
;; * that just will work across different tools,
;; * without even mentioning those tools in the code.
;; * we aim for copy/paste compatibility of visualisation code accross different tools
;;   * as it is a given for normal Clojure code producing textual output in a text oriented repl
;;   * visualisation code working in one tool should produce the same or very similar result in all compatible tools 


;; ## Status

;; * supported by [Clay](https://scicloj.github.io/clay/)
;; * implemented a partially working adapter for Portal ([kind-portal](https://github.com/scicloj/kind-portal))
;; * implemented a partially working adapter for Clerk ([kind-clerk](https://github.com/scicloj/kind-clerk))
;; * actively working to support other tools such as Cursive, Calva, and Clojupyter  

;; ## Existing book/notebook projects using Kindly

;; - [Tablecloth documentation](https://scicloj.github.io/tablecloth/)
;; - [Fastmath 3 documentation](https://generateme.github.io/fastmath/clay/)
;; - [ClojisR documentation](https://scicloj.github.io/clojisr/)
;; - [Wolframite documentation](https://scicloj.github.io/wolframite)
;; - [Clay documentation](https://scicloj.github.io/clay/)
;; - [Kindly-noted](https://scicloj.github.io/kindly-noted/) - documenting the ecosystem around Kindly - WIP
;; - [Noj documentation](https://scicloj.github.io/noj/) - WIP
;; - [Clojure Tidy Tuesdays](https://kiramclean.github.io/clojure-tidy-tuesdays/) data-science explorations
;; - [Clojure Data Tutorials](https://scicloj.github.io/clojure-data-tutorials)
;; - [Clojure Data Scrapbook](https://scicloj.github.io/clojure-data-scrapbook/)
;; - [LLMs tutorial](https://kpassapk.github.io/llama.clj/llama.html) (in spanish) by Kyle Passarelli
;; - [Statistical Computing in Clojure: Functional Approaches to Unsupervised Learning](https://github.com/adabwana/f24-cs7300-final-project/) by Jaryt Salvo


                                        ;
;; ## Example

;; Here is how one may request something of `kind/md`, which means Markdown:

(kind/md
 "hello *hello* **hello**")

;; ## The set of kinds

(sort kindly/known-kinds)

;; You can find more (details and examples of using these kinds)[/kindly-noted/kinds.html] in the Kindly book.

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

;; To hide the the code of a given form and only show the output, here are a few options:

;; 1. Add the metadata `:kindly/hide-code true` to the form (e.g., by preceding it with `^:kindly/hide-code`).

;; 2. Add the metadata `:kindly/hide-code true` to the value (e.g., using `vary-meta`).

;; 3. Some tools such as Clay [allow](https://scicloj.github.io/clay/#hiding-code) the user to globally define certain kinds (e.g., `:kind/md`, `:kind/hiccup`) to always hide code

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
