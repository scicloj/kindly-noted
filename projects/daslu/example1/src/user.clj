(ns user
  (:require [scicloj.clay.v2.api :as clay]
            [scicloj.kindly-default.v1.api :as kindly-default]))

;; Initialize Kindly's [default](https://github.com/scicloj/kindly-default).
(kindly-default/setup!)

;; Start Clay.
(clay/start!)

;; ## Useful commands

(comment
  ;; Show the whole document:
  (clay/show-doc! "notebooks/scratch.clj"))

(comment
  ;; Show the document with table-of-contents, and write it as html:
  (clay/show-doc-and-write-html! "notebooks/scratch.clj"
                                 {:toc? true}))
