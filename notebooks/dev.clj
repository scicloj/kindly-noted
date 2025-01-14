(ns dev
  (:require [scicloj.clay.v2.api :as clay]))

(clay/make! {:format [:quarto :html]
             :base-source-path "notebooks"
             :source-path ["index.clj"
                           "kind_compatibility.clj"
                           "kindly.clj"
                           "kinds.clj"
                           "emmy_viewers.clj"
                           "kindly_advice.clj"]
             :base-target-path "docs"
             :book {:title "Kindly Noted"}
             :clean-up-target-dir true})
