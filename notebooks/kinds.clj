(ns kinds
  (:require [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [tablecloth.api :as tc]))

;; # Catalogue of visualization kinds

;; **WIP**

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
(def clj-image
  (->  "https://upload.wikimedia.org/wikipedia/commons/e/eb/Ash_Tree_-_geograph.org.uk_-_590710.jpg"
       (java.net.URL.)
       (javax.imageio.ImageIO/read)))

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
