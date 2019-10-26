(ns hanzihack.user.graphql
  (:require
    [hanzihack.auth.facebook.svc :as fb]
    [hanzihack.user.svc :as u]))


(defn fblogin [{:keys [session]} {:keys [access_token]} _]
  (let [result (fb/retrieve access_token)
        params   (merge result
                        {:picture (get-in result [:picture :data :url])})
        user (u/create params)]
    user))


(defn me [{:keys [session]} _ _]
  (:identity session))
