(ns user
  (:require [scicloj.clay.v2.api :as clay]))

;; Start Clay.
(clay/start!)

(clay/swap-options!
 assoc
 :remote-repo {:git-url "https://github.com/scicloj/kindly-noted"
               :branch "main"}
 :quarto {:format {:html {:toc true
                          :theme :spacelab
                          :embed-resources true}}
          :highlight-style :solarized
          :code-block-background true
          :embed-resources true
          :execute {:freeze true}})
