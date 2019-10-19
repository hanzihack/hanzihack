(ns hanzihack.marilyn.final.db
  (:require
   [honeysql.core :as sql]
   [honeysql.helpers :as hh]
   [hanzihack.db.core :refer [*db*]]
   [clojure.java.jdbc :as jdbc]
   [db-helper.db :refer [equal]]))


(defn get-list [{:keys [user-id]}]
  (let [query (sql/format
                (->
                  {:select [:f.id
                            :f.sound
                            :f.pinyin
                            :f.pinyin_compact
                            [:l.id :location_id]
                            [(sql/call :row_to_json :l.*) :location]]
                   :from [[:marilyn.finals :f]]
                   :left-join [[{:select [:l.* :a.areas]
                                 :where [:= :l.user_id user-id]
                                 :from [[:marilyn.locations :l]]
                                 :left-join [[{:select [:location_id
                                                        [(sql/call :json_agg :marilyn.areas.*) :areas]]
                                               :from [:marilyn.areas]
                                               :group-by [:location_id]} :a] [:= :l.id :a.location_id]]} :l]
                               [:= :f.id :l.final_id]]}))]
                   

    (prn (sql/format query))
    (jdbc/query *db* query)))

(comment (get-list {:user-id 1}))

