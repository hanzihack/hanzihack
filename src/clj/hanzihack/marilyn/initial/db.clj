(ns hanzihack.marilyn.initial.db
  (:require
   [honeysql.core :as sql]
   [honeysql.helpers :as hh]
   [hanzihack.db.core :refer [*db*]]
   [clojure.java.jdbc :as jdbc]
   [db-helper.db :refer [equal]]))


(defn get-list [{:keys [user-id sound group]}]
  (let [query (sql/format
                (->
                  {:select [:i.* [(sql/call :row_to_json :a.*) :actor]]
                   :from [[:marilyn.initials :i]]
                   :left-join [[:marilyn.actors :a] [:and
                                                     [:= :i.id :a.initial_id]
                                                     [:= :a.user-id user-id]]]}
                  (hh/merge-where
                    (equal :i.sound sound)
                    (equal (sql/raw "i.\"group\"") group))))]

    (jdbc/query *db* query)))

