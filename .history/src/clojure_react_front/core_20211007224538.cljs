(ns clojure-react-front.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      ;; [goog.labs.format.csv :as csv]
      [clojure_csv/core.clj :as csv]
      [cljs.core.async :refer [<!]] [cljs-http.client :as http])
      (:require-macros [cljs.core.async.macros :refer [go]]))

;; -------------------------
;; Views
(defn data-list []
[:ul
  [:li "woah"]
]
)

;; Reading CSV
(go (let [response (<! (http/get "https://raw.githubusercontent.com/icyrockcom/country-capitals/master/data/country-list.csv" {:with-credentials? false :type "text/csv"}))]
  (prn (:status response))
  (prn ((csv/parse :body response false \n )))))


(defn home-page []
  [:div [:h1 "Data"
  ] [data-list]])


 
  
  
  



;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
  
