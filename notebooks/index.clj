^:kindly/hide-code
(ns index
  (:require [scicloj.kindly.v4.kind :as kind]
            [scicloj.kindly.v4.api :as kindly]))

^:kindly/hide-code
(def md (comp kindly/hide-code
              kind/md))

(md "
# Preface

This book documents the ecosystem of tools and libraries around the [Kindly](https://scicloj.github.io/kindly-noted/kindly) convention.

## In this book

- [Kindly intro](./kindly.html)

- [Catalogue of visualization kinds](./kinds.html)

- [Kind compatibility matrix](./kind_compatibility.html)
     
- [Kindly-advice](./kindly_advice.html) - to help tools support Kindly
     


 ")
