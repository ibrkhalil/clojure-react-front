(ns clojure-react-front.core
    (:require
      [reagent.core :as r]
      [reagent.dom :as d]
      [clojure.string :as str]
      [goog.labs.format.csv :as csv]
      
      [cljs.core.async :refer [<!]] [cljs-http.client :as http])
      (:require-macros [cljs.core.async.macros :refer [go]]))

;; Main App State
(def app-data (r/atom {:data [] :current-temp false :current-capital false :by-12-hr false :by-24-hr false :by-3-days false :by-7-days false}))
;; -------------------------
;; Views

(defn data-list []
[:table
[:tbody
  [:tr
  [:th "Country"]
  [:th {:on-click #(let [old-data (:data @app-data) old-capital (:current-capital @app-data)] (swap! app-data assoc :current-capital (if (not= old-capital false) false true)) (swap! app-data assoc :data (vec (sort-by :capital (if (not= old-capital false) > <) old-data))) )} "Capital" ]
  [:th {:on-click #(let [old-data (:data @app-data) old-temp (:current-temp @app-data)] (swap! app-data assoc :current-temp (if (not= old-temp false) false true)) (swap! app-data assoc :data (vec (sort-by :current-temp (if (not= old-temp false) > <) old-data))) )} "current temperature"]
  [:th {:on-click #(let [old-data (:data @app-data) old-by-12-hr (:by-12-hr @app-data)] (swap! app-data assoc :by-12-hr (if (not= old-by-12-hr false) false true)) (swap! app-data assoc :data (vec (sort-by :temp-in-12-hrs (if (not= old-by-12-hr false) > <) old-data))) )} "temperature after 12 hours"]
  [:th {:on-click #(let [old-data (:data @app-data) old-by-24-hr (:by-24-hr @app-data)] (swap! app-data assoc :by-24-hr (if (not= old-by-24-hr false) false true)) (swap! app-data assoc :data (vec (sort-by :temp-in-24-hrs (if (not= old-by-24-hr false) > <) old-data))) )} "temperature after 24 hours"]
  [:th {:on-click #(let [old-data (:data @app-data) old-by-3-days (:by-3-days @app-data)] (swap! app-data assoc :by-3-days (if (not= old-by-3-days false) false true)) (swap! app-data assoc :data (vec (sort-by :temp-in-3-days (if (not= old-by-3-days false) > <) old-data))) )} "temperature after 3 days"]
  [:th {:on-click #(let [old-data (:data @app-data) old-by-7-days (:by-7-days @app-data)] (swap! app-data assoc :by-7-days (if (not= old-by-7-days false) false true)) (swap! app-data assoc :data (vec (sort-by :temp-in-7-days (if (not= old-by-7-days false) > <) old-data))) )} "temperature after 7 days"]]

  ;; Map random temperatures to the table and drop the first record cause it's already filled in the table's heading
   (drop 1(map (fn [item] ^{:key item} [:tr [:td (:country item)] [:td (:capital item)] [:td (:current-temp item) " Celsius"] [:td (:temp-in-12-hrs item) " Celsius"] [:td (:temp-in-24-hrs item) " Celsius"] [:td (:temp-in-3-days item) " Celsius"] [:td (:temp-in-7-days item) " Celsius"]]) (:data @app-data)))
  
]]

)

;; Reading CSV
(go (let [response (<! (http/get "https://raw.githubusercontent.com/icyrockcom/country-capitals/master/data/country-list.csv" {:with-credentials? false :type "text/csv; charset=utf-8"}))]
  (swap! app-data assoc :data (drop 1(seq (map (fn [item] {:country (first item) :capital  (second item) :current-temp (Math/floor (* 100 (Math/random))) :temp-in-12-hrs (Math/floor (* 100 (Math/random)))  :temp-in-24-hrs (Math/floor (* 100 (Math/random))) :temp-in-3-days (Math/floor (* 100 (Math/random))) :temp-in-7-days (Math/floor (* 100 (Math/random)))} ) (csv/parse (:body response))))))))
  


(defn home-page []
   [data-list]
   )


 
  
  
  



;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
  
