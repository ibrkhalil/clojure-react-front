(ns clojure-react-front.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      [cljs.core.async :refer [<!]] [cljs-http.client :as http]
    
      )
        (:require-macros [cljs.core.async.macros :refer [go]]))

;; -------------------------
;; Views
(defn data-list []
[:ul
  [:li "woah"]
]
)

(defn handler [response]
  (.log js/console response))
(defn home-page []
  [:div [:h1 "Data"
  ] [data-list]])


 
  
  
  



;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
  
