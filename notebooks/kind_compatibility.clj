;; # Kind compatibility matrix

^:kindly/hide-code
(ns kind-compatibility
  (:require [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [tablecloth.api :as tc]
            [clojure.string :as str]))

^:kindly/hide-code
(def kind-status
  [{:clojupyter :n, :clay :c, :clerk :u, :kind :kind/edn}
   {:clojupyter :r, :clay :c, :clerk :u, :kind :kind/code}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/vega}
   {:clojupyter :n, :clay :c, :clerk :u, :kind :kind/smile-model}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/image}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/plotly}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/echarts}
   {:clojupyter :r, :clay :c, :clerk :u, :kind :kind/map}
   {:clojupyter :n, :clay :c, :clerk :u, :kind :kind/portal}
   {:clojupyter :r, :clay :c, :clerk :u, :kind :kind/test}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/dataset}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/vega-lite}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/html}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/cytoscape}
   {:clojupyter :r, :clay :c, :clerk :u, :kind :kind/set}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/reagent}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/var}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/hidden}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/hiccup}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/md}
   {:clojupyter :r, :clay :c, :clerk :u, :kind :kind/tex}
   {:clojupyter :r, :clay :c, :clerk :u, :kind :kind/seq}
   {:clojupyter :n, :clay :c, :clerk :u, :kind :kind/htmlwidgets-plotly}
   {:clojupyter :r, :clay :c, :clerk :u, :kind :kind/video}
   {:clojupyter :n, :clay :c, :clerk :u, :kind :kind/observable}
   {:clojupyter :n, :clay :c, :clerk :u, :kind :kind/emmy-viewers}
   {:clojupyter :r, :clay :c, :clerk :u, :kind :kind/pprint}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/highcharts}
   {:clojupyter :r, :clay :c, :clerk :u, :kind :kind/table}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/fn}
   {:clojupyter :r, :clay :c, :clerk :u, :kind :kind/vector}
   {:clojupyter :n, :clay :c, :clerk :u, :kind :kind/htmlwidgets-ggplotly}
   {:clojupyter :n, :clay :c, :clerk :u, :kind :kind/fragment}
   {:clojupyter :c, :clay :c, :clerk :u, :kind :kind/scittle}
   {:clojupyter :r, :clay :c, :clerk :u, :kind :kind/test-last}])


^:kindly/hide-code
(defn status->upper [m k]
  (update m k (fn [v] 
                (case v 
                  :c (kind/hiccup [:div {:style "background-color:green"} (-> v name str/upper-case)])
                  :u (kind/hiccup [:div {:style "background-color:grey"} (-> v name str/upper-case)])
                  :n (kind/hiccup [:div {:style "background-color:red"} (-> v name str/upper-case)])
                  :e (kind/hiccup [:div {:style "background-color:red"} (-> v name str/upper-case)])
                  :r (kind/hiccup [:div {:style "background-color:yellow"} (-> v name str/upper-case)])))))

^:kindly/hide-code
(def t
  (-> (map #(-> % 
                (status->upper :clay)
                (status->upper :clerk)
                (status->upper :clojupyter)
                (assoc :kind-link (kind/hiccup [:a {:href (format "https://scicloj.github.io/kindly-noted/kinds.html#%s" (name (:kind %)))}
                                                (name (:kind %))])))
           kind-status)
      (tc/dataset)
      (tc/reorder-columns [:kind-link :kind :clay :clojupyter :clerk])
      (tc/order-by [:kind])  
      (tc/drop-columns [:kind])
      (tc/rename-columns {:kind-link :kind})
      #_(tech.v3.dataset.print/print-range  :all)
      (tc/rows)
      ))

;; The following table maps the landscape of tools supporing the Kindly standard.

;; ## Notes

;; * Currently, the table does not provide any details regarding the **nesting** of kinds:
;; whether it is possible to contain a `kind/image` inside `kind/hiccup`, etc.
;; This will require a more detailed exploration.

;; ## Legend

^:kindly/hide-code
{:c :compatible
 :u :unknown
 :n :not-implemented
 :e :exception
 :r :rendering-differs-to-clay} 

;; ## Tools to be added

;; * Kindly-render (generic tool-agnostic infrastructure)
;; * Portal
;; * Cursive
;; * Calva

;; ## Details

^:kindly/hide-code
(kind/table 
 {:row-vectors t
  :column-names [:kind :clay :clojupyter :clerk]})

;; ## kind/table support in Clojupyter
;; Currently the support for `kind/table` in Clojupyter is less complete then in Clay.

