(ns hanzihack.marilyn.location.svc
  (:require [clojure.java.jdbc :as jdbc]
            [hanzihack.db.core :refer [*db*]]
            [clojure.tools.logging :as log]))


(defn create [{:keys [name note image final_id user_id] :as location}]
  (try
    (first (jdbc/insert! *db* :marilyn.locations location))
    (catch Exception e
      (log/infof "cant create new location - %s %s" location e)
      (jdbc/update! *db* :marilyn.locations
                    (select-keys location [:name :image])
                    ["final_id=? AND user_id=?" final_id user_id])
      (first (jdbc/query *db*
                         ["SELECT * FROM marilyn.locations WHERE final_id=? AND user_id=?" final_id user_id])))))
