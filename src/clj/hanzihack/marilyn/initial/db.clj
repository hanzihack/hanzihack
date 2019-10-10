(ns hanzihack.marilyn.initial.db
  (:require
   [honeysql.core :as sql]
   [hanzihack.db.core :refer [*db*]]
   [clojure.java.jdbc :as jdbc]
   [db-helper.db :refer [where equal]]))


(defn get-list [{:keys [user-id sound group]}]
  (let [query (sql/format 
                (where
                  {:select [:i.* [(sql/call :row_to_json :a.*) :actor]]
                   :from [[:marilyn.initials :i]]
                   :left-join [[:marilyn.actors :a] [:and
                                                     [:= :i.id :a.initial_id]
                                                     [:= :a.user-id user-id]]]}
                  [(equal :i.sound sound)
                   (equal (sql/raw "i.\"group\"") group)]))]

     (jdbc/query *db* query)))

