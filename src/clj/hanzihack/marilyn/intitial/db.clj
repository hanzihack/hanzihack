(ns hanzihack.marilyn.initial.db
  (:require
   [honeysql.core :as sql]
   [hanzihack.db.core :refer [*db*]]
   [clojure.java.jdbc :as jdbc]))


(defn get-list [{:keys [sound group limit offset]}]
  (let [query (sql/format {:select [:*]
                           :from [:marilyn.initials]
                           :limit limit
                           :offset offset
                           :where [:and
                                   [:= :sound sound]
                                   #_[:= :group group]]})]
     (jdbc/query *db* query)))

