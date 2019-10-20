(ns hanzihack.auth.facebook.graphql
  (:require [hanzihack.auth.facebook.svc :as svc]))

(defn login [ctx {:keys [access_token]} _]
  (let [result (svc/retrieve access_token)]
    (assoc result
      :photo (get-in result [:picture :data :url]))))


