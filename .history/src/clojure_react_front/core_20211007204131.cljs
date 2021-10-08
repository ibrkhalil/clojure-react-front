(ns clojure-react-front.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      [ajax.core :refer [GET]]))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to Reagent"
  [:ul[:li "wow"]]]]
  
   (handle-response (GET ("https://raw.githubusercontent.com/icyrockcom/country-capitals/master/data/country-list.csv")
         {:response-format :csv
          }))
  
  )

(defn handle-response [response]
    (.log js/console response))

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
  
