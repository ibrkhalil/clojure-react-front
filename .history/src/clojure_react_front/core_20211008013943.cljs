(ns clojure-react-front.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      [clojure.string :as str]
      [goog.labs.format.csv :as csv]
      
      [cljs.core.async :refer [<!]] [cljs-http.client :as http])
      (:require-macros [cljs.core.async.macros :refer [go]]))

;; Atoms
(def data (r/atom []))

;; -------------------------
;; Views


(defn sort-current []
(prn ("WOAH"))
)

(defn data-list []
[:table
[:tbody
  [:tr
  [:th (:country (first @data))]
  [:th (:capital (first @data))]
  [:th {:on-click ("wow")} "current temperature"]
  [:th "temperature after 12 hours"]
  [:th "temperature after 24 hours"]
  [:th "temperature after 3 days"]
  [:th "temperature after 7 days"]]

  ;; Map random temperatures to the table and drop the first record cause it's already filled in the table's heading
   (drop 1(map (fn [item] ^{:key item} [:tr [:td (:country item)] [:td (:capital item)] [:td (:current-temp item) " Celsius"] [:td (:temp-in-12-hrs item) " Celsius"] [:td (:temp-in-24-hrs item) " Celsius"] [:td (:temp-in-3-days item) " Celsius"] [:td (:temp-in-7-days item) " Celsius"]]) @data))
  
]]

)

;; Reading CSV
(go (let [response (<! (http/get "https://raw.githubusercontent.com/icyrockcom/country-capitals/master/data/country-list.csv" {:with-credentials? false :type "text/csv; charset=utf-8"}))]
  (reset! data (map (fn [item] {:country (first item) :capital  (second item) :current-temp (.floor js/Math(* 100 (.random js/Math))) :temp-in-12-hrs (.floor js/Math(* 100 (.random js/Math)))  :temp-in-24-hrs (.floor js/Math(* 100 (.random js/Math))) :temp-in-3-days (.floor js/Math(* 100 (.random js/Math))) :temp-in-7-days (.floor js/Math(* 100 (.random js/Math)))} ) (csv/parse (:body response))))))
  


(defn home-page []
  
   [data-list])


 
  
  
  



;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
  
