(ns hanzihack.marilyn.actor.svc
  (:require [clojure.java.jdbc :as jdbc]
            [hanzihack.db.core :refer [*db*]]
            [clojure.tools.logging :as log]))


(defn create [{:keys [name note image initial_id user_id] :as actor}]
  (try
    (first (jdbc/insert! *db* :marilyn.actors actor))
    (catch Exception e
      (log/infof "cant create new actor - $s %s" actor e)
      (jdbc/update! *db* :marilyn.actors
                    (select-keys actor [:name :note :image])
                    ["initial_id=? AND user_id=?" initial_id user_id])
      (first (jdbc/query *db*
                         ["SELECT * FROM marilyn.actors WHERE initial_id=? AND user_id=?" initial_id user_id])))))
