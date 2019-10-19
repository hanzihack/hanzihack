(ns hanzihack.graphql.core
  (:require
    [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.walmartlabs.lacinia :as lacinia]
    [com.walmartlabs.lacinia.resolve :as resolve]
    [graphql-query.core :refer [graphql-query]]
    [hanzihack.marilyn.initial.graphql :as initial]
    [hanzihack.marilyn.final.graphql :as final]
    [clojure.data.json :as json]
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [ring.util.http-response :refer :all]
    [mount.core :refer [defstate]]))


(defn get-initial [{:keys [:initial_id]} args value]
  (println :get-initial args)
  {:id initial_id
   :name "a"
   :pinyin "a"})


(defn list-actor [ctx args value]
  (println :list-actor args)
  (resolve/with-context
    [{:id 1
      :name "actor"
      :initial_id 2}]
    {:initial_id 99}))

(defn list-final [ctx args value]
  [{:id 1
    :sound "(u)an"
    :pinyin "uan"}])

(defn list-location [ctx args value]
  (print :list-location)
  [{:id 1
    :name "Esplanade"
    :final_id 3}])

(defn get-actor [ctx args value]
  (println :get-actor args)
  {:id 2
   :name "get-actor"})

(defn get-final [ctx args value]
  (println :get-final args)
  {:id 2
   :sound "(u)an"
   :pinyin "uan"})

(defstate compiled-schema
  :start
  (-> "graphql/schema.edn"
      io/resource
      slurp
      edn/read-string
      (attach-resolvers {;; objects
                         :initial/resolve   get-initial
                         :actor/resolve     get-actor
                         :final/resolve     get-final
                         :location/resolve  (constantly {})
                         :area/resolve      (constantly {})
                         :character/resolve (constantly {})

                         ; queries
                         :initial/resolve-list initial/get-list
                         :actor/resolve-list   list-actor
                         :final/resolve-list   final/get-list
                         :location/resolve-list list-location})

      schema/compile))

(defn execute
  ([query]
   (execute query nil nil))
  ([query vars]
   (execute query vars nil))
  ([query vars context]
   (lacinia/execute compiled-schema query vars context)))

(defn execute-request [query]
  (let [vars nil
        ;; TODO: receive context as args, <- send from route {:user-id x}
        context {:user-id 1}]
    (-> (execute query vars context)
        (json/write-str))))


;; ----------

(comment
  (let [gqr (graphql-query {:operation {:operation/type :query
                                        :operation/name :getInitialTables}
                            :queries   [
                                        {:query/data  [:initials {:group "a"}
                                                       :fragment/initialWithActor]
                                         :query/alias :aTable}
                                        {:query/data  [:initials {:group "i"}
                                                       :fragment/initialWithActor]
                                         :query/alias :iTable}
                                        {:query/data  [:initials {:group "u"}
                                                       :fragment/initialWithActor]
                                         :query/alias :uTable}
                                        {:query/data  [:initials {:group "ü"}
                                                       :fragment/initialWithActor]
                                         :query/alias :uvTable}]
                            :fragments [{:fragment/name   :fragment/initialWithActor
                                         :fragment/type   :initial
                                         :fragment/fields [:id :sound :group :pinyin
                                                           [:actor [:id :name]]]}]})]

    (println :gqr gqr)
    (execute gqr nil {:user-id 1})))

