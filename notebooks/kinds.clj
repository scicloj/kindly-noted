(ns kinds
  (:require [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [tablecloth.api :as tc]))

;; # Catalogue of visualization kinds

;; ## Plain values

;; Values with no kind are displayed the default way each tool would display them.
;; In Clay, they are simply pretty-printed.

(+  4 5)

(str "abcd" "efgh")

;; ## Plain data structures

;; By default (according to `kindly/advice`), plain Clojure data structures: vectors, other sequentials (lists/seqs/ranges/etc.), sets, and maps, are assigned the kinds `kind/vector`, `kind/seq`, , `kind/set`, and `kind/map`, respectively.

;; Each tool may have its own way to display these kinds.
;; For example, Clay just uses text,
;; while Portal has a hierarchical navigation UI.

(list 1 "A" :B 'C)

(range 9)

[1 "A" :B 'C]

#{1 "A" :B 'C}

{1 "A" :B 'C}

;; These kinds have recursive kind semantics:
;; if the values inside them have kind information,
;; they should be handled accordingly.

;; Here is a vector of things of different kinds inside:

[(kind/hiccup
  [:div {:style
         {:background-color "floralwhite"}}
   [:p "hello"]])
 (kind/md
  "hello *hello* **hello**")
 (kind/code
  "(defn f [x] (+  x 9))")]

;; And here is a map:

{:x  (kind/md
      "**hello**")
 (kind/md
  "**hello**") :x}

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
 "(defn f [x] {:y (+  x 9)})")

;; ## Edn

;; (will be documented soon)

;; ## Hiccup

;; Values of `kind/hiccup` should be displayed as the HTML this value defines according to [Hiccup](https://github.com/weavejester/hiccup) notation.

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

;; This kind has recursive semantics:
;; if the values inside them have kind information,
;; they should be handled accordingly.

;; Foe example:

(kind/hiccup
 [:div {:style
        {:background-color "#eeddee"
         :border-style "solid"}}
  [:p {:style {:background-color "#ccddcc"
               :border-style "solid"}}
   "hello"]
  (kind/md
   "hello *hello* **hello**")
  (kind/code
   "(defn f [x] (+  x 9))")])

;; ## Reagent

;; Values of `kind/reagent` express [Reagent](https://reagent-project.github.io/) components.

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

;; The `:reagent/deps` option can be used to provide additional dependencies.
;; This should be documented better soon.

;; ## HTML

;; Values of `kind/html` are displayed as raw html.

(kind/html
 "<div style='height:40px; width:40px; background:purple'></div> ")

(kind/html
 "
<svg height=100 width=100>
<circle cx=50 cy=50 r=40 stroke='purple' stroke-width=3 fill='floralwhite' />
</svg> ")

;; ## Vega-Lite

(def vega-lite-plot
  (kind/vega-lite
   {:encoding
    {:y {:field "y", :type "quantitative"},
     :size {:value 400},
     :x {:field "x", :type "quantitative"}},
    :mark {:type "circle", :tooltip true},
    :width 400,
    :background "floralwhite",
    :height 100,
    :data {:values "x,y\n1,1\n2,-4\n3,9\n", :format {:type "csv"}}}))

vega-lite-plot

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
 (let [n 20
       walk (fn [bias]
              (->> (repeatedly n #(-> (rand)
                                      (- 0.5)
                                      (+ bias)))
                   (reductions +)))]
   {:data [{:x (walk 1)
            :y (walk -1)
            :z (map #(* % %)
                    (walk 2))
            :type :scatter3d
            :mode :lines+markers
            :opacity 0.2
            :line {:width 10}
            :marker {:size 20
                     :colorscale :Viridis}}]}))

;; ## Observable

;; [Observable](https://observablehq.com/) visualizations can be written as Javascript. Some of us are working on a Clojure DSL to express the same.


;; Examples from [Quarto's Observable documentation](https://quarto.org/docs/interactive/ojs/):

(kind/observable
 "athletes = FileAttachment('notebooks/datasets/athletes.csv').csv({typed: true})")

(kind/observable
 "athletes")

(kind/observable
 "Inputs.table(athletes)")

(kind/observable
 "
Plot.plot({
  grid: true,
  facet: {
    data: athletes,
    y: 'sex'
  },
  marks: [
    Plot.rectY(
      athletes,
      Plot.binX({y: 'count'}, {x: 'weight', fill: 'sex'})
    ),
    Plot.ruleY([0])
  ]
})
")

(kind/observable
 "population = FileAttachment('notebooks/datasets/population.json').json()")

(kind/observable
 "population")

(kind/observable
 " import { chart } with { population as data } from '@d3/zoomable-sunburst'
 chart")


(kind/observable
 "
//| panel: input
viewof bill_length_min = Inputs.range(
                                      [32, 50],
                                      {value: 35, step: 1, label: 'Bill length (min):'}
                                      )
viewof islands = Inputs.checkbox(
                                 ['Torgersen', 'Biscoe', 'Dream'],
                                 { value: ['Torgersen', 'Biscoe'],
                                  label: 'Islands:'
                                  }
                                 )

Plot.rectY(filtered,
            Plot.binX(
                      {y: 'count'},
                      {x: 'body_mass_g', fill: 'species', thresholds: 20}
                      ))
 .plot({
        facet: {
                data: filtered,
                x: 'sex',
                y: 'species',
                marginRight: 80
                },
        marks: [
                Plot.frame(),
                ]
        }
       )
Inputs.table(filtered)
data = FileAttachment('notebooks/datasets/palmer-penguins.csv').csv({ typed: true })
filtered = data.filter(function(penguin) {
                                           return bill_length_min < penguin.bill_length_mm &&
                                           islands.includes(penguin.island);
                                           })
")

;; ## Video

;; Values of `kind/video` are specifications for embedded videos.
;; See, e.g.,  [HTML Youtube Videos](https://www.w3schools.com/html/html_youtube.asp) on w3schools, for the relevant options.

(kind/video
 {:youtube-id "DAQnvAgBma8"})

(kind/video
 {:youtube-id "DAQnvAgBma8"
  :allowfullscreen false})

(kind/video
 {:youtube-id "DAQnvAgBma8"
  :iframe-width 480
  :iframe-height 270})

(kind/video
 {:youtube-id "DAQnvAgBma8"
  :embed-options {:mute 1
                  :controls 0}})


;; ## Image

;; By default (according to `kindly/advice`), `BufferedImage` objects
;; are inferred to be of `kind/image`.

(defonce tree-image
  (->  "https://upload.wikimedia.org/wikipedia/commons/e/eb/Ash_Tree_-_geograph.org.uk_-_590710.jpg"
       (java.net.URL.)
       (javax.imageio.ImageIO/read)))

(type tree-image)

tree-image

;; ## ML models

;; By default (according to `kindly/advice`), a machine learning model of the [Smile](https://haifengl.github.io/) library is inferred to be of `kind/smile-model`.

(smile.regression.OLS/fit
 (smile.data.formula.Formula/lhs "y")
 (smile.data.DataFrame/of (into-array [(double-array [1 1 2])
                                       (double-array [2 4 5])
                                       (double-array [3 9 13])
                                       (double-array [4 16 19])])
                          (into-array ["w" "x" "y"])))

(defonce marketing-dataset
  (tc/dataset "https://github.com/scicloj/datarium-CSV/raw/main/data/marketing.csv.gz"
              {:key-fn keyword}))


(require '[scicloj.noj.v1.stats :as noj.stats]
         '[scicloj.metamorph.ml :as ml])

(def marketing-model
  (-> marketing-dataset
      (noj.stats/linear-regression-model :sales
                                         [:youtube
                                          :facebook
                                          :newspaper])
      ml/thaw-model))

(type marketing-model)

marketing-model

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




;; A value of `kind/table` should be displayed as a table
;; (using the tool's UI for tables, if any).

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

;; A sequence of vectors without column-names information:
(kind/table
 {:row-vectors (->> (range 25)
                    (map (fn [x]
                           [x
                            (* x x)])))})

;; A sequence of maps and column-names information:
(kind/table
 {:row-maps (->> (range 25)
                 (map (fn [x]
                        {:x x
                         :y (* x x)})))
  :column-names [:x :y]})

;; Some tools support [datatables](https://datatables.net/)
;; for displaying tables.
;; This can be expressed using the `:use-datatables` option.

(-> squares-dataset
    (kind/table {:use-datatables true}))

;; In addition, the `:datatables` option can be used to control
;; [datatables options](https://datatables.net/manual/options)
;; (see [the full list](https://datatables.net/reference/option/)).

(-> squares-dataset
    (kind/table {:use-datatables true
                 :datatables {:scrollY 200}}))

;; and in this case the user may specify [datatables options](https://datatables.net/manual/options)
;; (see [the full list](https://datatables.net/reference/option/)).

;; The `kind/table` has recursive semantics:
;; if the values inside them have kind information,
;; they should be handled accordingly.

(kind/table
 {:column-names [(kind/code ":x")
                 (kind/code ":y")]
  :row-vectors [[(kind/md "*some text* **some more text**")
                 (kind/code "{:x (1 2 [3 4])}")]
                [(tc/dataset {:x (range 3)
                              :y (map inc (range 3))})
                 vega-lite-plot]
                [(kind/hiccup [:div {:style {:height 200}}
                               tree-image])
                 (kind/md "$x^2$")]]})


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

;; ## Portal

;; Values of `kind/portal` are displayed using an embedded
;; [Portal](https://github.com/djblue/portal) viewer.

(kind/portal
 {:x (range 3)})

;; This kind has recursive semantics:
;; if the values inside them have kind information,
;; they should be handled accordingly.

;; Note that `kind/portal` applies the [kind-portal](https://github.com/scicloj/kind-portal) adapter to nested kinds.
(kind/portal
 [(kind/hiccup [:img {:height 50 :width 50
                      :src "https://clojure.org/images/clojure-logo-120b.png"}])
  (kind/hiccup [:img {:height 50 :width 50
                      :src "https://raw.githubusercontent.com/djblue/portal/fbc54632adc06c6e94a3d059c858419f0063d1cf/resources/splash.svg"}])])

(kind/portal
 [(kind/hiccup [:big [:big "a plot"]])
  vega-lite-plot])

(kind/portal
 [(kind/hiccup [:p {:style {:background-color "#ccddcc"
                            :border-style "solid"}}
                "hello"])
  (kind/md
   "hello *hello* **hello**")
  (kind/code
   "(defn f [x] (+  x 9))")
  vega-lite-plot])


;; ## Fragment

;; `kind/fragment` is a special kind. It expects a sequential value and generates multiple items, of potentially multiple kinds, from its elements.

(->> ["purple" "darkgreen" "goldenrod"]
     (mapcat (fn [color]
               [(kind/md (str "### subsection: " color))
                (kind/hiccup [:div {:style {:background-color color
                                            :color "lightgrey"}}
                              [:big [:p color]]])]))
     kind/fragment)
