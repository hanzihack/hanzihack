(ns hanzihack.env
  (:require
    [selmer.parser :as parser]
    [clojure.tools.logging :as log]
    [hanzihack.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init
   (fn []
     (parser/cache-off!)
     (log/info "\n-=[hanzihack started successfully using the development profile]=-"))
   :stop
   (fn []
     (log/info "\n-=[hanzihack has shut down successfully]=-"))
   :middleware wrap-dev})
