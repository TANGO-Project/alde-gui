;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.execs.tabs.exec_graphs
  (:require [gui.globals :as VARS]
            [utils.logs :as logs]
            [cljsjs.chartjs :as charts]
            [reagent.core :as reagent]))



(def not-nil? (complement nil?))


(defn show-revenue-chart
  []
  (let [context (.getContext (.getElementById js/document "rev-chartjs") "2d")
        chart-data {:type "bar"
                    :data {:labels ["2012" "2013" "2014" "2015" "2016"]
                           :datasets [{:data [5 10 15 20 25]
                                       :label "Time in seconds"
                                       :backgroundColor "#90EE90"}
                                      {:data [3 6 9 12 15]
                                       :label "Energy in joules"
                                       :backgroundColor "#F08080"}]}}]
      (js/Chart. context (clj->js chart-data))))




(defn show-revenue-chart-3-1
  []
  (let [execs-1 (filter #(and (= (% :status) "COMPLETED")
                              (= (% :execution_configuration_id) 20)
                              (not-nil? (% :energy_output))
                              (not-nil? (% :runtime_output))) @VARS/ALDE_EXECS)
       labels   (for [x execs-1] (x :id))
       data1    (for [x execs-1] (x :runtime_output))
       context (.getContext (.getElementById js/document "rev-chartjs") "2d")
      chart-data {:type "bar"
                    :data {:labels labels
                           :datasets [{:data data1
                                       :label "Time in seconds"
                                       :backgroundColor "#90EE90"}]}}]
      (js/Chart. context (clj->js chart-data))))


(defn show-revenue-chart-3-2
  []
  (let [execs-1 (filter #(and (= (% :status) "COMPLETED")
                              (= (% :execution_configuration_id) 20)
                              (not-nil? (% :energy_output))
                              (not-nil? (% :runtime_output))) @VARS/ALDE_EXECS)
       labels   (for [x execs-1] (x :id))
       data2    (for [x execs-1] (x :energy_output))
       context (.getContext (.getElementById js/document "rev-chartjs-2") "2d")
        chart-data {:type "bar"
                    :data {:labels labels
                           :datasets [{:data data2
                                       :label "Energy in joules"
                                       :backgroundColor "#F08080"}]}}]
      (js/Chart. context (clj->js chart-data))))



(defn panel-component
  []
  (reagent/create-class
    {:component-did-mount #(do (show-revenue-chart-3-1) (show-revenue-chart-3-2))
     :display-name        "chartjs-component"
     :reagent-render      (fn []
                            [:div
                              [:canvas {:id "rev-chartjs" :width "auto" :height "200px"}]
                              [:canvas {:id "rev-chartjs-2" :width "auto" :height "200px"}]])}))
