;; # Clay demo

;; ## Setup

(ns daslu.example1
  (:require [scicloj.clay.v2.api :as clay]
            [scicloj.viz.api :as viz]
            [tablecloth.api :as tc]
            [scicloj.kindly.v3.api :as kindly]
            [scicloj.kindly.v3.kind :as kind]))


;; ## Useful commands

(clay/start!)

^:kindly/hide-code?^:kindly/hide-code?^:kindly/hide-code?
(comment
  (do (clay/show-doc! "notebooks/daslu/example1.clj"
                      {:toc? true})
      (clay/write-html! "docs/daslu/example1.html")))
      

;; ## Examples

;; ### Plain values

(+ 1 2)

(def people-as-maps
  (->> (range 29)
       (mapv (fn [i]
               {:preferred-language (["clojure" "clojurescript" "babashka"]
                                     (rand-int 3))
                :age (rand-int 100)}))))

(def people-as-vectors
  (->> people-as-maps
       (mapv (juxt :preferred-language :age))))

people-as-maps

people-as-vectors

;; ### Pretty printing

(kind/pprint people-as-maps)

(kind/pprint people-as-vectors)

;; ### Tables

(kind/table
 {:column-names [:preferred-language :age]
  :row-vectors people-as-vectors})

(kind/table
 {:column-names [:preferred-language :age]
  :row-maps people-as-maps})

[:p {:style ; https://www.htmlcsscolor.com/hex/7F5F3F
     {:color "#7F5F3F"}}
 "hello"]

;; ### Images

(import java.awt.image.BufferedImage
        java.awt.Color
        sun.java2d.SunGraphics2D)

(defn a-piece-of-random-art [n]
  (let [bi (BufferedImage. n n BufferedImage/TYPE_INT_RGB)
        g  (-> (.createGraphics ^BufferedImage bi))]
    (dotimes [t 100]
      (->> #(rand-int n)
           (repeatedly 4)
           (apply #(.drawLine ^SunGraphics2D g %1 %2 %3 %4))))
    bi))

(a-piece-of-random-art (+ 40 (rand-int 90)))

;; ### Datasets

(require '[tablecloth.api :as tc])

(-> {:x (range 6)
     :y [:A :B :C :A :B :C]}
    tc/dataset)

;; #### Known issues

;; With the current Markdown implementation, used by Clay (based on [Cybermonday](https://github.com/kiranshila/cybermonday)), brackets inside datasets cells are not visible.

(-> {:x [1 [2 3] 4]
     :y [:A :B :C]}
    tc/dataset)

;; For now, cases of this kind can be handled by the user by switching to the `:kind/pprint` kind.

(-> {:x [1 [2 3] 4]
     :y [:A :B :C]}
    tc/dataset
    kind/pprint)

;; ### [Vega](https://vega.github.io/vega/) and [Vega-Lite](https://vega.github.io/vega-lite/)

(defn vega-point-plot [data]
  (-> {:data {:values data},
       :mark "point"
       :encoding
       {:size {:field "w" :type "quantitative"}
        :x {:field "x", :type "quantitative"},
        :y {:field "y", :type "quantitative"},
        :fill {:field "z", :type "nominal"}}}
      kind/vega))

(defn random-data [n]
  (->> (repeatedly n #(- (rand) 0.5))
       (reductions +)
       (map-indexed (fn [x y]
                      {:w (rand-int 9)
                       :z (rand-int 9)
                       :x x
                       :y y}))))

(defn random-vega-plot [n]
  (-> n
      random-data
      vega-point-plot))

(random-vega-plot 9)

;; ### [Cytoscape.js](https://js.cytoscape.org/)

(def cytoscape-example
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

(kind/cytoscape cytoscape-example)

(kind/cytoscape [cytoscape-example
                 {:style {:height 100
                          :width 100}}])

;; ### [Apache Echarts](https://echarts.apache.org/)

(kind/echarts
 {:xAxis {:data ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"]}
  :yAxis {}
  :series [{:type "bar"
            :color ["#7F5F3F"]
            :data [23 24 18 25 27 28 25]}]})

;; ### Delays

(delay
  (Thread/sleep 500)
  (+ 1 2))

(delay
  [:div [:big "hi......."]])

;; ### Tests

(-> 2
    (+ 3)
    (clay/check = 4))

(-> 2
    (+ 3)
    (clay/check = 5))

;; ### Viz.clj

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type :point)
    (viz/mark-size 200)
    (viz/color :x)
    viz/viz)

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/viz :viz/type :point
             :COLOR "x"
             :MSIZE 200))

:bye

;; ## Examples

;; ### Plain values

(+ 1 2)

(def people-as-maps
  (->> (range 29)
       (mapv (fn [i]
               {:preferred-language (["clojure" "clojurescript" "babashka"]
                                     (rand-int 3))
                :age (rand-int 100)}))))

(def people-as-vectors
  (->> people-as-maps
       (mapv (juxt :preferred-language :age))))

people-as-maps

people-as-vectors

;; ### Pretty printing

(kind/pprint people-as-maps)

(kind/pprint people-as-vectors)

;; ### Tables

(kind/table
 {:column-names [:preferred-language :age]
  :row-vectors people-as-vectors})

(kind/table
 {:column-names [:preferred-language :age]
  :row-maps people-as-maps})

[:p {:style ; https://www.htmlcsscolor.com/hex/7F5F3F
     {:color "#7F5F3F"}}
 "hello"]

;; ### Images

(import java.awt.image.BufferedImage
        java.awt.Color
        sun.java2d.SunGraphics2D)

(defn a-piece-of-random-art [n]
  (let [bi (BufferedImage. n n BufferedImage/TYPE_INT_RGB)
        g  (-> (.createGraphics ^BufferedImage bi))]
    (dotimes [t 100]
      (->> #(rand-int n)
           (repeatedly 4)
           (apply #(.drawLine ^SunGraphics2D g %1 %2 %3 %4))))
    bi))

(a-piece-of-random-art (+ 40 (rand-int 90)))

;; ### Datasets

(require '[tablecloth.api :as tc])

(-> {:x (range 6)
     :y [:A :B :C :A :B :C]}
    tc/dataset)

;; #### Known issues

;; With the current Markdown implementation, used by Clay (based on [Cybermonday](https://github.com/kiranshila/cybermonday)), brackets inside datasets cells are not visible.

(-> {:x [1 [2 3] 4]
     :y [:A :B :C]}
    tc/dataset)

;; For now, cases of this kind can be handled by the user by switching to the `:kind/pprint` kind.

(-> {:x [1 [2 3] 4]
     :y [:A :B :C]}
    tc/dataset
    kind/pprint)

;; ### [Vega](https://vega.github.io/vega/) and [Vega-Lite](https://vega.github.io/vega-lite/)

(defn vega-point-plot [data]
  (-> {:data {:values data},
       :mark "point"
       :encoding
       {:size {:field "w" :type "quantitative"}
        :x {:field "x", :type "quantitative"},
        :y {:field "y", :type "quantitative"},
        :fill {:field "z", :type "nominal"}}}
      kind/vega))

(defn random-data [n]
  (->> (repeatedly n #(- (rand) 0.5))
       (reductions +)
       (map-indexed (fn [x y]
                      {:w (rand-int 9)
                       :z (rand-int 9)
                       :x x
                       :y y}))))

(defn random-vega-plot [n]
  (-> n
      random-data
      vega-point-plot))

(random-vega-plot 9)

;; ### [Cytoscape.js](https://js.cytoscape.org/)

(def cytoscape-example
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

(kind/cytoscape cytoscape-example)

(kind/cytoscape [cytoscape-example
                 {:style {:height 100
                          :width 100}}])

;; ### [Apache Echarts](https://echarts.apache.org/)

(kind/echarts
 {:xAxis {:data ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"]}
  :yAxis {}
  :series [{:type "bar"
            :color ["#7F5F3F"]
            :data [23 24 18 25 27 28 25]}]})

;; ### Delays

(delay
  (Thread/sleep 500)
  (+ 1 2))

(delay
  [:div [:big "hi......."]])

;; ### Tests

(-> 2
    (+ 3)
    (clay/check = 4))

(-> 2
    (+ 3)
    (clay/check = 5))

;; ### Viz.clj

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type :point)
    (viz/mark-size 200)
    (viz/color :x)
    viz/viz)

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/viz :viz/type :point
             :COLOR "x"
             :MSIZE 200))

:bye

;; ## Examples

;; ### Plain values

(+ 1 2)

(def people-as-maps
  (->> (range 29)
       (mapv (fn [i]
               {:preferred-language (["clojure" "clojurescript" "babashka"]
                                     (rand-int 3))
                :age (rand-int 100)}))))

(def people-as-vectors
  (->> people-as-maps
       (mapv (juxt :preferred-language :age))))

people-as-maps

people-as-vectors

;; ### Pretty printing

(kind/pprint people-as-maps)

(kind/pprint people-as-vectors)

;; ### Tables

(kind/table
 {:column-names [:preferred-language :age]
  :row-vectors people-as-vectors})

(kind/table
 {:column-names [:preferred-language :age]
  :row-maps people-as-maps})

[:p {:style ; https://www.htmlcsscolor.com/hex/7F5F3F
     {:color "#7F5F3F"}}
 "hello"]

;; ### Images

(import java.awt.image.BufferedImage
        java.awt.Color
        sun.java2d.SunGraphics2D)

(defn a-piece-of-random-art [n]
  (let [bi (BufferedImage. n n BufferedImage/TYPE_INT_RGB)
        g  (-> (.createGraphics ^BufferedImage bi))]
    (dotimes [t 100]
      (->> #(rand-int n)
           (repeatedly 4)
           (apply #(.drawLine ^SunGraphics2D g %1 %2 %3 %4))))
    bi))

(a-piece-of-random-art (+ 40 (rand-int 90)))

;; ### Datasets

(require '[tablecloth.api :as tc])

(-> {:x (range 6)
     :y [:A :B :C :A :B :C]}
    tc/dataset)

;; #### Known issues

;; With the current Markdown implementation, used by Clay (based on [Cybermonday](https://github.com/kiranshila/cybermonday)), brackets inside datasets cells are not visible.

(-> {:x [1 [2 3] 4]
     :y [:A :B :C]}
    tc/dataset)

;; For now, cases of this kind can be handled by the user by switching to the `:kind/pprint` kind.

(-> {:x [1 [2 3] 4]
     :y [:A :B :C]}
    tc/dataset
    kind/pprint)

;; ### [Vega](https://vega.github.io/vega/) and [Vega-Lite](https://vega.github.io/vega-lite/)

(defn vega-point-plot [data]
  (-> {:data {:values data},
       :mark "point"
       :encoding
       {:size {:field "w" :type "quantitative"}
        :x {:field "x", :type "quantitative"},
        :y {:field "y", :type "quantitative"},
        :fill {:field "z", :type "nominal"}}}
      kind/vega))

(defn random-data [n]
  (->> (repeatedly n #(- (rand) 0.5))
       (reductions +)
       (map-indexed (fn [x y]
                      {:w (rand-int 9)
                       :z (rand-int 9)
                       :x x
                       :y y}))))

(defn random-vega-plot [n]
  (-> n
      random-data
      vega-point-plot))

(random-vega-plot 9)

;; ### [Cytoscape.js](https://js.cytoscape.org/)

(def cytoscape-example
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

(kind/cytoscape cytoscape-example)

(kind/cytoscape [cytoscape-example
                 {:style {:height 100
                          :width 100}}])

;; ### [Apache Echarts](https://echarts.apache.org/)

(kind/echarts
 {:xAxis {:data ["Mon" "Tue" "Wed" "Thu" "Fri" "Sat" "Sun"]}
  :yAxis {}
  :series [{:type "bar"
            :color ["#7F5F3F"]
            :data [23 24 18 25 27 28 25]}]})

;; ### Delays

(delay
  (Thread/sleep 500)
  (+ 1 2))

(delay
  [:div [:big "hi......."]])

;; ### Tests

(-> 2
    (+ 3)
    (clay/check = 4))

(-> 2
    (+ 3)
    (clay/check = 5))

;; ### Viz.clj

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/type :point)
    (viz/mark-size 200)
    (viz/color :x)
    viz/viz)

(-> [{:x 1 :y 2}
     {:x 2 :y 4}
     {:x 3 :y 9}]
    viz/data
    (viz/viz :viz/type :point
             :COLOR "x"
             :MSIZE 200))

:bye
