(ns clojure-react-front.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]))

;; -------------------------
;; Views

(defn home-page []
  [:div [:h2 "Welcome to Reagent"
  [:ul[:li "wow"]]]]
  

  (def matches (atom nil))
(defn component []
  (let [get-stuff (fn [] (GET "/..." :handler (fn [response] 
                           (reset! matches (:body response)))))]
    (get-stuff) <-- called before component mount
    (fn []
      [:ul
        (for [m match]
          ^{:key ...} 
          [:li ...])])))
  )

;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
