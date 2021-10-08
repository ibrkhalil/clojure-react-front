(ns clojure-react-front.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      [clojure-ajax]
      ))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to Reagent"
  [:ul[:li "wow"]]]]
  (def matches (atom nil)))
(defn component []
  (let [get-stuff (fn [] (GET "https://raw.githubusercontent.com/icyrockcom/country-capitals/master/data/country-list.csv" :handler (fn [response] 
                           (reset! matches (:body response)))))]


  ))

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
  
