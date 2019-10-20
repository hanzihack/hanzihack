(ns hanzihack.auth.facebook.svc
  (:require [clj-http.client :as client]))


(def graph-url "https://graph.facebook.com/v4.0/")


(defn retrieve [access-token]
  (:body (client/get (str graph-url "/me")
                     {:as :json
                      :query-params {:fields "id,name,email,picture{url}"
                                     :access_token access-token}})))


