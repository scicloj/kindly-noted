(ns kindly-generated-test
  (:require
   [clojure.test :refer [deftest is]]
   [scicloj.kindly.v4.api :as kindly]
   [scicloj.kindly.v4.kind :as kind]
   [scicloj.note-to-test.v1.api :as note-to-test]
   [tablecloth.api :as tc]
   [scicloj.noj.v1.vis :as vis]
   [scicloj.noj.v1.datasets :as datasets]))

(deftest test-everything

  (is (= (note-to-test/represent-value-with-meta
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
                                    (update-vals note-to-test/represent-value)))}]))
       {:value [:ok], :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          (kind/md
           "hello *hello* **hello**"))
       {:value ["hello *hello* **hello**"], :meta #:kindly{:kind :kind/md}}))

  (is (= (note-to-test/represent-value-with-meta
          (-> "hello *hello* **hello**"
              kind/md
              meta))
       {:value #:kindly{:kind :kind/md}, :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          kindly/known-kinds)
       {:value
        #{:kind/code
          :kind/vega
          :kind/image
          :kind/echarts
          :kind/map
          :kind/test
          :kind/dataset
          :kind/vega-lite
          :kind/cytoscape
          :kind/set
          :kind/reagent
          :kind/var
          :kind/hidden
          :kind/hiccup
          :kind/md
          :kind/seq
          :kind/pprint
          :kind/table
          :kind/vector},
        :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          ^:kind/md
          ["hello *hello* **hello**"])
       {:value ["hello *hello* **hello**"], :meta #:kind{:md true}}))

  (is (= (note-to-test/represent-value-with-meta
          (-> "^:kind/md
          [\"hello *hello* **hello**\"]
          "
              read-string
              meta))
       {:value #:kind{:md true}, :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          (-> "^kind/md
          [\"hello *hello* **hello**\"]
          "
              read-string
              meta))
       {:value {:tag "symbol kind/md"}, :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          (kind/md
           ["hello *hello* **hello**"]))
       {:value ["hello *hello* **hello**"], :meta #:kindly{:kind :kind/md}}))

  (is (= (note-to-test/represent-value-with-meta
          (kind/md
           "hello *hello* **hello**"))
       {:value ["hello *hello* **hello**"], :meta #:kindly{:kind :kind/md}}))

  (is (= (note-to-test/represent-value-with-meta
          (kind/md
           "hello *hello* **hello**"))
       {:value ["hello *hello* **hello**"], :meta #:kindly{:kind :kind/md}}))

  (is (= (note-to-test/represent-value-with-meta
          ^:kind/md
          ["hello *hello* **hello**"])
       {:value ["hello *hello* **hello**"], :meta #:kind{:md true}}))

  (is (= (note-to-test/represent-value-with-meta
          (require '[scicloj.noj.v1.vis :as vis]
                   '[scicloj.noj.v1.datasets :as datasets]))
       {:value nil, :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          (-> datasets/iris
              (vis/hanami-histogram :sepal-width
                                    {:nbins 10})
              (assoc :height 100)
              meta))
       {:value #:kindly{:kind :kind/vega-lite}, :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          (def clj-image
            (->  "https://clojure.org/images/clojure-logo-120b.png"
                 (java.net.URL.)
                 (javax.imageio.ImageIO/read))))
       [:var]))

  (is (= (note-to-test/represent-value-with-meta
          clj-image)
       {:value :image, :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          (->> (range 30)
               (apply array-map)
               kind/pprint))
       {:value
        {0 1,
         20 21,
         24 25,
         4 5,
         22 23,
         6 7,
         28 29,
         12 13,
         2 3,
         14 15,
         26 27,
         16 17,
         10 11,
         18 19,
         8 9},
        :meta #:kindly{:kind :kind/pprint}}))

  (is (= (note-to-test/represent-value-with-meta
          (->> {:x 9}
               kind/hidden))
       {:value {:x 9}, :meta #:kindly{:kind :kind/hidden}}))

  (is (= (note-to-test/represent-value-with-meta
          (kind/hiccup
           [:div {:style
                  {:background-color "floralwhite"}}
            [:p "hello"]]))
       {:value [:div {:style {:background-color "floralwhite"}} [:p "hello"]],
        :meta #:kindly{:kind :kind/hiccup}}))

  (is (= (note-to-test/represent-value-with-meta
          (kind/md
           "hello *hello* **hello**"))
       {:value ["hello *hello* **hello**"], :meta #:kindly{:kind :kind/md}}))

  (is (= (note-to-test/represent-value-with-meta
          (kind/code
           "(defn f [x] (+ x 9))"))
       {:value ["(defn f [x] (+ x 9))"], :meta #:kindly{:kind :kind/code}}))

  (is (= (note-to-test/represent-value-with-meta
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
       {:value
        {:encoding
         {:y {:field "y", :type "quantitative"},
          :size {:value 400},
          :x {:field "x", :type "quantitative"}},
         :mark {:type "circle", :tooltip true},
         :width 400,
         :background "floralwhite",
         :height 100,
         :data {:values "x,y\n1,1\n2,-4\n3,9\n", :format {:type "csv"}}},
        :meta #:kindly{:kind :kind/vega-lite}}))

  (is (= (note-to-test/represent-value-with-meta
          clj-image)
       {:value :image, :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          (-> {:x (range 3)}
              tc/dataset
              (tc/map-columns :y
                              [:x]
                              (fn [x] (* x x)))))
       {:value {:x [0 1 2], :y [0 1 4]}, :meta {:name "_unnamed"}}))

  (is (= (note-to-test/represent-value-with-meta
          '(1 "A" :B 'C))
       {:value [1 "A" :B ["symbol quote" "symbol C"]], :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          [1 "A" :B 'C])
       {:value [1 "A" :B "symbol C"], :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          #{1 "A" :B 'C})
       {:value #{1 :B "A" "symbol C"}, :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          {1 "A" :B 'C})
       {:value {1 "A", :B "symbol C"}, :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          [(kind/hiccup
            [:div {:style
                   {:background-color "floralwhite"}}
             [:p "hello"]])
           (kind/md
            "hello *hello* **hello**")
           (kind/code
            "(defn f [x] (+ x 9))")])
       {:value
        [[:div {:style {:background-color "floralwhite"}} [:p "hello"]]
         ["hello *hello* **hello**"]
         ["(defn f [x] (+ x 9))"]],
        :meta nil}))

  (is (= (note-to-test/represent-value-with-meta
          {:x  (kind/md
                "**hello**")
           (kind/md
            "**hello**") :x})
       {:value {:x ["**hello**"], ["**hello**"] :x}, :meta nil})))
