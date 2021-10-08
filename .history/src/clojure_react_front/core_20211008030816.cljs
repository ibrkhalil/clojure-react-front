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
(def current-temp (r/atom false))
(def by-12-hr (r/atom false))
(def by-24-hr (r/atom false))
(def by-3-days (r/atom false))



;; -------------------------
;; Views


(defn by-current-temp [x y]
  ;; :current-temp values sorted in increasing order because x and y
  (
  ;; if flag of current-temp is true then sort asc otherwise desc
  if(== true (deref current-temp))
  (let [c (compare (:current-temp x) (:current-temp y))]
    (if (not= c 0)
      c
      ))

    (let [c (compare (:current-temp y) (:current-temp x))]
    (if (not= c 0)
    c
    ))
      ))

(defn by-12-hr-fn [x y]
  ;; :by-12-hr values sorted in increasing order because x and y
  (
  ;; if flag of by-12-hr is true then sort asc otherwise desc
  if(== true (deref by-12-hr))
  (let [c (compare (:temp-in-12-hrs x) (:temp-in-12-hrs y))]
    (if (not= c 0)
      c
      ))

    (let [c (compare (:temp-in-12-hrs y) (:temp-in-12-hrs x))]
    (if (not= c 0)
    c
    ))
      ))

(defn by-24-hr-fn [x y]
  ;; :by-24-hr values sorted in increasing order because x and y
  (
  ;; if flag of by-24-hr is true then sort asc otherwise desc
  if(== true (deref by-24-hr))
  (let [c (compare (:temp-in-24-hrs x) (:temp-in-24-hrs y))]
    (if (not= c 0)
      c
      ))

    (let [c (compare (:temp-in-24-hrs y) (:temp-in-24-hrs x))]
    (if (not= c 0)
    c
    ))
      ))



(defn data-list []
[:table
[:tbody
  [:tr
  [:th "Country"]
  [:th "Capital"]
  [:th {:on-click #(let [old-data @data old-temp @current-temp] (reset! current-temp (if (= old-temp true) false true)) (reset! data (sort by-current-temp old-data)) )} "current temperature"]
  [:th {:on-click #(let [old-data @data old-by-12-hr @by-12-hr] (reset! by-12-hr (if (= old-by-12-hr true) false true)) (reset! data (sort by-12-hr-fn old-data)) )} "temperature after 12 hours"]
  [:th {:on-click #(let [old-data @data old-by-24-hr @by-24-hr] (reset! by-24-hr (if (= old-by-24-hr true) false true)) (reset! data (sort by-24-hr-fn old-data)) )} "temperature after 24 hours"]
  [:th {:on-click #(let [old-data @data old-by-3-days @by-3-days] (reset! by-3-days (if (= old-by-3-days true) false true)) (reset! data (sort by-3-days-fn old-data)) )} "temperature after 3 days"]
  [:th {:on-click #(let [old-data @data old-by-12-hr @by-12-hr] (reset! by-12-hr (if (= old-by-12-hr true) false true)) (reset! data (sort by-12-hr-fn old-data)) )}"temperature after 7 days"]]

  ;; Map random temperatures to the table and drop the first record cause it's already filled in the table's heading
   (drop 1(map (fn [item] ^{:key item} [:tr [:td (:country item)] [:td (:capital item)] [:td (:current-temp item) " Celsius"] [:td (:temp-in-12-hrs item) " Celsius"] [:td (:temp-in-24-hrs item) " Celsius"] [:td (:temp-in-3-days item) " Celsius"] [:td (:temp-in-7-days item) " Celsius"]]) @data))
  
]]

)

;; Reading CSV
(go (let [response (<! (http/get "https://raw.githubusercontent.com/icyrockcom/country-capitals/master/data/country-list.csv" {:with-credentials? false :type "text/csv; charset=utf-8"}))]
  (reset! data (drop 1(seq (map (fn [item] {:country (first item) :capital  (second item) :current-temp (.floor js/Math(* 100 (.random js/Math))) :temp-in-12-hrs (.floor js/Math(* 100 (.random js/Math)))  :temp-in-24-hrs (.floor js/Math(* 100 (.random js/Math))) :temp-in-3-days (.floor js/Math(* 100 (.random js/Math))) :temp-in-7-days (.floor js/Math(* 100 (.random js/Math)))} ) (csv/parse (:body response))))))))
  


(defn home-page []
  
   [data-list])


 
  
  
  



;; -------------------------
;; Initialize app

(defn mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export init! []
  (mount-root))
  
