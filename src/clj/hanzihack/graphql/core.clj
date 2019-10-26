(ns hanzihack.graphql.core
  (:require
    [com.walmartlabs.lacinia.util :refer [attach-resolvers]]
    [com.walmartlabs.lacinia.schema :as schema]
    [com.walmartlabs.lacinia :as lacinia]
    [com.walmartlabs.lacinia.resolve :as resolve]
    [clojure.data.json :as json]
    [clojure.edn :as edn]
    [clojure.java.io :as io]
    [ring.util.http-response :as res]
    [mount.core :refer [defstate]]
    [graphql-query.core :refer [graphql-query]]
    [hanzihack.marilyn.initial.graphql :as initial]
    [hanzihack.marilyn.final.graphql :as final]
    [hanzihack.marilyn.actor.graphql :as actor]
    [hanzihack.marilyn.location.graphql :as location]
    [hanzihack.user.graphql :as user]
    [clojure.tools.logging :as log]))


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
                         :final/resolve-list   final/get-list

                         ; actor
                         :actor/resolve-list   list-actor
                         :actor/create   actor/create

                         ; location
                         :location/resolve-list list-location
                         :location/create  location/create

                         ; user
                         :facebook/login user/fblogin
                         :user/me        user/me})

      schema/compile))

(defn execute
  ([query]
   (execute query nil nil))
  ([query vars]
   (execute query vars nil))
  ([query vars context]
   (lacinia/execute compiled-schema query vars context)))


(defn execute-request [req]
  (let [query  (-> req :body slurp)
        vars nil
        session (:session req)
        context {:session (merge session)}]

    (let [result   (execute query vars context)]
      ;; TODO: I dont know how to update a session in graphql resolver yet T^T
      ;; TODO: don't check for facebook_login on gql-result and the set a session
      (let [user (into {} (get-in result [:data :facebook_login]))
            new-session (if-not (empty? user)
                          (do
                            (log/debug :fb-login user)
                            (assoc session :identity user))
                          session)]
        (-> {:status 200
             :headers {}
             :session new-session
             :body (json/write-str result)}
            (res/header "X-Wow-Value" "wow"))))))


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

