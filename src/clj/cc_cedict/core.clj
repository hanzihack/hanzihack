(ns cc-cedict.core
  (:require [clojure.string :as s]
            [cheshire.core :as json]))


;; file can be download from here https://www.mdbg.net/chinese/dictionary?page=cc-cedict

(def cedict (-> (slurp "./resources/cedict.u8") s/split-lines))

(defn split-cols [line]
  (let [trad-idx (s/index-of line \space)
        sim-idx  (s/index-of line \[ (inc trad-idx))
        pron-idx (s/index-of line \] (inc sim-idx))
        start-mean-idx (s/index-of line \/ (inc sim-idx))
        trad     (s/trim (subs line 0 trad-idx))
        sim      (s/trim (subs line trad-idx sim-idx))
        pron     (->
                   (s/trim (subs line (inc sim-idx) pron-idx))
                   (s/split #" "))
        means    (->>
                   (s/split (subs line start-mean-idx) #"/")
                   (remove empty?))]
    {:trad trad
     :sim sim
     :pron pron
     :means means}))

(defn u8->json-file [cols-lines filepath]
  (spit filepath ;"./resources/cedict.json"
    (json/generate-string
      cols-lines
      {:pretty true})))


(comment
  (let [lines (->> cedict
                   (drop 30))
        line  (->> lines
                  (take 3)
                  last)]

    (map split-cols (take 3 lines))))
    ;(u8->json-file "./resources/cedict.json")))
