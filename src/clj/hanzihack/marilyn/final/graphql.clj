(ns hanzihack.marilyn.final.graphql
  (:require
    [hanzihack.marilyn.final.db :as db]))

(defn get-list [ctx args value]
  (db/get-list (merge args ctx)))
