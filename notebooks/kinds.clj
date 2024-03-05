(ns kinds
  (:require [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [tablecloth.api :as tc]))

;; # Catalogue of visualization kinds

;; ## Plain values

;; Values with no kind are displayed the default way each tool would display them.
;; In Clay, they are simply pretty-printed.

(+ 4 5)

(str "abcd" "efgh")

;; ## Hidden

;; Values of `kind/hidden` are simply not displayed.

(kind/hidden
 {:x 9})

;; ## Markdown

;; Values of `kind/md` are rendered as Markdown.

(kind/md
 ["
* This is [markdown](https://www.markdownguide.org/).
  * *Isn't it??*"
  "
* Here is **some more** markdown."])

;; Ideally, tools should support LaTeX inside Markown.

(kind/md
 "If $x$ equals 9, then $$x^2+9=90$$")

;; ## Code

;; Values of `kind/code` are rendered as Clojure code.

(kind/code
 "(defn f [x] {:y (+ x 9)})")

;; ## Hiccup
(def hello-hiccup
  (kind/hiccup
   [:ul
    [:li [:p "hi"]]
    [:li [:big
          [:big
           [:p {:style
                ;; https://www.htmlcsscolor.com/hex/7F5F3F
                {:color "#7F5F3F"}}
            "hello"]]]]]))

hello-hiccup

;; ## Reagent

(kind/reagent
 ['(fn [{:keys [initial-value
                background-color]}]
     (let [*click-count (reagent.core/atom initial-value)]
       (fn []
         [:div {:style {:background-color background-color}}
          "The atom " [:code "*click-count"] " has value: "
          @*click-count ". "
          [:input {:type "button" :value "Click me!"
                   :on-click #(swap! *click-count inc)}]])))
  {:initial-value 9
   :background-color "#d4ebe9"}])

;; ## HTML

;; Values of `kind/html` are displayed as raw html.

(kind/html
 "<div style='height:40px; width:40px; background:purple'></div> ")

(kind/html
 "
<svg height=100 width=100>
<circle cx=50 cy=50 r=40 stroke='purple' stroke-width=3 fill='floralwhite' />
</svg> ")

;; ## Images

;; By default (according to `kindly/advice`), `BufferedImage` objects
;; are inferred to be of `kind/image`.

(defonce tree-image
  (->  "https://upload.wikimedia.org/wikipedia/commons/e/eb/Ash_Tree_-_geograph.org.uk_-_590710.jpg"
       (java.net.URL.)
       (javax.imageio.ImageIO/read)))

tree-image

;; ## ML models

;; By default (according to `kindly/advice`), a machine learning model of the [Smile](https://haifengl.github.io/) library is inferred to be of `kind/smile-model`.

(-> datasets/iris
    (noj.stats/linear-regression-model :sepal-length
                                       [:sepal-width
                                        :petal-width
                                        :petal-length])
    ml/thaw-model)

;; This kind is displayed by printing the value displaying it as code.

;; ## Datasets

;; By default (according to `kindly/advice`), [tech.ml.dataset](https://github.com/techascent/tech.ml.dataset) / [Tablecloth](https://scicloj.github.io/tablecloth) datasets are inferred to be of `kind/dataset`.

;; This kind should be printed and rendered as Markdown,
;; possibly with some tool-specific table styling.

(def squares-dataset
  (-> {:x (range 25)}
      tc/dataset
      (tc/map-columns :y
                      [:x]
                      (fn [x]
                        (* x x)))))

;; Datasets can have various printable values inside:

(tc/dataset
 {:x [1 [2 3] 4]
  :y [:A :B :C]})

;; Some elements might be missing:

(tc/dataset
 [{:x 1 :y 2 :z 3}
  {:y 4 :z 5}])

;; ## Tables

;; A value of `kind/table` should be displayed as a table.

;; The table contents can be specified in different ways.

;; A dataset:
(kind/table squares-dataset)

;; A sequence of vectors and column-names information:
(kind/table
 {:row-vectors (->> (range 25)
                    (map (fn [x]
                           [x
                            (* x x)])))
  :column-names [:x :y]})

;; A sequence of maps and column-names information:
(kind/table
 {:row-maps (->> (range 25)
                 (map (fn [x]
                        {:x x
                         :y (* x x)})))
  :column-names [:x :y]})

;; ## Pretty printing

;; Values of kind `kind/pprint` should be pretty-printed.

(->> (range 30)
     (apply array-map)
     kind/pprint)

;; For some tool like Clay, this is the default
;; when there is no kind information.

(->> (range 30)
     (apply array-map))

;; Still, it can be is useful to ensure the same behaviour
;; across different tools.

;; It can also be useful to override other kinds previously
;; specified or automatically inferred.

(kind/pprint
 hello-hiccup)

(kind/pprint
 tree-image)

(kind/pprint
 kind/dataset)

;; ## Vega-Lite

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

;; ## Cytoscape

(kind/cytoscape
 {:elements {:nodes [{:data {:id "a" :parent "b"} :position {:x 215 :y 85}}
                     {:data {:id "b"}}
                     {:data {:id "c" :parent "b"} :position {:x 300 :y 85}}
                     {:data {:id "d"} :position {:x 215 :y 175}}
                     {:data {:id "e"}}
                     {:data {:id "f" :parent "e"} :position {:x 300 :y 175}}]
             :edges [{:data {:id "ad" :source "a" :target "d"}}
                     {:data {:id "eb" :source "e" :target "b"}}]}
  :style [{:selector "node"
           :css {:content "data(id)"
                 :text-valign "center"
                 :text-halign "center"}}
          {:selector "parent"
           :css {:text-valign "top"
                 :text-halign "center"}}
          {:selector "edge"
           :css {:curve-style "bezier"
                 :target-arrow-shape "triangle"}}]
  :layout {:name "preset"
           :padding 5}})

;; ## ECharts

;; This example is taken from Apache ECharts' [Getting Started](https://echarts.apache.org/handbook/en/get-started/).

(kind/echarts
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

;; ## Plotly

(kind/plotly
 {:data [{:x [0 1 3 2]
          :y [0 6 4 5]
          :z [0 8 9 7]
          :type :scatter3d
          :mode :lines+markers
          :opacity 0.5
          :line {:width 5}
          :marker {:size 4
                   :colorscale :Viridis}}]})




;; ## Smile Regression models

;; A [Smile](https://haifengl.github.io/) regression model is displayed
;; by turning it into a String
;; and higlighting the output as printed Clojure.

(smile.regression.OLS/fit
 (smile.data.formula.Formula/lhs "y")
 (smile.data.DataFrame/of (into-array [(double-array [1 1 2])
                                       (double-array [2 4 5])
                                       (double-array [3 9 13])
                                       (double-array [4 16 19])])
                          (into-array ["w" "x" "y"])))


(require '[scicloj.noj.v1.datasets :as datasets]
         '[scicloj.noj.v1.stats :as noj.stats]
         '[scicloj.ml.core :as ml])

(-> datasets/iris
    (noj.stats/linear-regression-model :sepal-length
                                       [:sepal-width
                                        :petal-width
                                        :petal-length])
    ml/thaw-model)

;; ## Table

;; A table uses the tool's UI for tables, if any.

(kind/table squares-dataset)

;; Some tools support [datatables](https://datatables.net/) for displaying tables.

(-> squares-dataset
    (kind/table {:use-datatables true}))

;; and in this case the user may specify [datatables options](https://datatables.net/manual/options)
;; (see [the full list](https://datatables.net/reference/option/)).

(-> squares-dataset
    (kind/table {:use-datatables true
                 :datatables {:scrollY 200}}))


;; ## Plain data structures

;; Plain Clojure data structures have recursive kind semantics:
;; * Each tool has its own way(range 20) to represent them visually
;; (e.g., Clay just uses text, while Portal has a hierarchical navigation UI).

(list 1 "A" :B 'C)

[1 "A" :B 'C]

#{1 "A" :B 'C}

{1 "A" :B 'C}


;; ## Plain data structures - nesting other kinds

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

;; ## Hiccup - nesting other kinds
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
