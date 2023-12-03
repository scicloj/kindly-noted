(ns draft)

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
                          (update-vals note-to-test/represent-value)))}])

;; # Topics in discussion

;; - hiding code
;; - handling styles around data visualizations
;; - compatibility with [Emmy-Viewers](https://emmy-viewers.mentat.org/)
;; - backwards compatibility of kind inference


^:kindly/hide-code?
(comment
  (note-to-test/gentest! "notebooks/kindly.clj"
                         {:accept true
                          :verbose true}))
