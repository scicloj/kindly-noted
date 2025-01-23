;; # Kind compatibility matrix

^:kindly/hide-code
(ns kind-compatibility
  (:require [scicloj.kindly.v4.api :as kindly]
            [scicloj.kindly.v4.kind :as kind]
            [tablecloth.api :as tc]
            [clojure.string :as str]
            
            ))
^:kindly/hide-code
(def kind-status
  [
   {:clojupyter :n, :description :kind/edn, :clay :c, :clerk :u, :kind :kind/edn}
   {:clojupyter :r, :description :kind/code, :clay :c, :clerk :u, :kind :kind/code}
   {:clojupyter :c, :description :kind/vega, :clay :c, :clerk :u, :kind :kind/vega}
   {:clojupyter :n, :description :kind/smile-model, :clay :c, :clerk :u, :kind :kind/smile-model}
   {:clojupyter :c, :description :kind/image, :clay :c, :clerk :u, :kind :kind/image}
   {:clojupyter :c, :description :kind/plotly, :clay :c, :clerk :u, :kind :kind/plotly}
   {:clojupyter :c, :description :kind/echarts, :clay :c, :clerk :u, :kind :kind/echarts}
   {:clojupyter :r, :description :kind/map, :clay :c, :clerk :u, :kind :kind/map}
   {:clojupyter :n, :description :kind/portal, :clay :c, :clerk :u, :kind :kind/portal}
   {:clojupyter :r, :description :kind/test, :clay :c, :clerk :u, :kind :kind/test}
   {:clojupyter :c, :description :kind/dataset, :clay :c, :clerk :u, :kind :kind/dataset}
   {:clojupyter :c, :description :kind/vega-lite, :clay :c, :clerk :u, :kind :kind/vega-lite}
   {:clojupyter :c, :description :kind/html, :clay :c, :clerk :u, :kind :kind/html}
   {:clojupyter :c, :description :kind/cytoscape, :clay :c, :clerk :u, :kind :kind/cytoscape}
   {:clojupyter :r, :description :kind/set, :clay :c, :clerk :u, :kind :kind/set}
   {:clojupyter :c, :description :kind/reagent, :clay :c, :clerk :u, :kind :kind/reagent}
   {:clojupyter :c, :description :kind/var, :clay :c, :clerk :u, :kind :kind/var}
   {:clojupyter :c, :description :kind/hidden, :clay :c, :clerk :u, :kind :kind/hidden}
   {:clojupyter :c, :description :kind/hiccup, :clay :c, :clerk :u, :kind :kind/hiccup}
   {:clojupyter :c, :description :kind/md, :clay :c, :clerk :u, :kind :kind/md}
   {:clojupyter :r, :description :kind/tex, :clay :c, :clerk :u, :kind :kind/tex}
   {:clojupyter :r, :description :kind/seq, :clay :c, :clerk :u, :kind :kind/seq}
   {:clojupyter :n, :description :kind/htmlwidgets-plotly, :clay :c, :clerk :u, :kind :kind/htmlwidgets-plotly}
   {:clojupyter :r, :description :kind/video, :clay :c, :clerk :u, :kind :kind/video}
   {:clojupyter :n, :description :kind/observable, :clay :c, :clerk :u, :kind :kind/observable}
   {:clojupyter :n, :description :kind/emmy-viewers, :clay :c, :clerk :u, :kind :kind/emmy-viewers}
   {:clojupyter :r, :description :kind/pprint, :clay :c, :clerk :u, :kind :kind/pprint}
   {:clojupyter :c, :description :kind/highcharts, :clay :c, :clerk :u, :kind :kind/highcharts}
   {:clojupyter :r, :description :kind/table, :clay :c, :clerk :u, :kind :kind/table}
   {:clojupyter :c, :description :kind/fn, :clay :c, :clerk :u, :kind :kind/fn}
   {:clojupyter :r, :description :kind/vector, :clay :c, :clerk :u, :kind :kind/vector}
   {:clojupyter :n, :description :kind/htmlwidgets-ggplotly, :clay :c, :clerk :u, :kind :kind/htmlwidgets-ggplotly}
   {:clojupyter :n, :description :kind/fragment, :clay :c, :clerk :u, :kind :kind/fragment}
   {:clojupyter :c, :description :kind/scittle, :clay :c, :clerk :u, :kind :kind/scittle}
   {:clojupyter :r, :description :kind/test-last, :clay :c, :clerk :u, :kind :kind/test-last}


   ])

^:kindly/hide-code
(defn status->upper [m k]
  (update m k (fn [v] 
                (case v 
                  :c  (kind/hiccup [:div {:style "background-color:green"} (-> v name str/upper-case)])
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
                (update :kind (fn [v] (str v))))
           kind-status)
      (tc/dataset)
      (tc/reorder-columns [:kind :description :clay :clojupyter :clerk])
      (tc/order-by [:kind])  
      #_(tech.v3.dataset.print/print-range  :all)
      (tc/rows)))

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
  :column-names [:kind :description :clay :clojupyter :clerk]})
