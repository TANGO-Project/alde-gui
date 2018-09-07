;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.tabs.applications
  (:require [restapi.testbeds :as restapi]
            [re-frame.core :as re-frame]
            [gui.subs :as subs]
            [reagent.core :as reagent]
            [gui.globals :as VARS]
            [gui.apps.newp :as panel-new]
            [gui.apps.updatep :as panel-update]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION:
(defn panel-component []
  (let [selected-app      (re-frame/subscribe [::subs/selected-app])
        selected-app-id   (re-frame/subscribe [::subs/selected-app-id])
        selected-app-type (re-frame/subscribe [::subs/selected-app-type])]
    (fn []
      [:div
        [:div.row
          [:div.col-sm-8
            [:h5
              [:span.badge.badge-pill.badge-primary {:style {:color "#ffff99"}} "Total applications: " (str (VARS/get-total-apps))]
              " - "
              (cond
                (= @selected-app-type "app")
                  [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (str "Application id: " @selected-app-id ", Name: " (@selected-app :name))]
                :else
                  [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (str @selected-app-id)])
              ]]
          [:div.col-sm-4
            [:h5

              (when (= @selected-app-type "app")
                ;; update node TODO
                [:button.badge.badge-pill.btn-sm.btn-warning {:title "update selected application"
                  :data-toggle "collapse" :data-target "#collapseUpdate" :aria-expanded "false" :aria-controls "collapseUpdate"
                  :on-click #(restapi/add-node-to-testbed (fn[] (- 1 1)) {
                                                                      :name "node_3",
                                                                      :information_retrieved false
                                                                    })} "update"])
              (when (= @selected-app-type "app")
                ;; delete node TODO
                [:button.badge.badge-pill.btn-sm.btn-secondary {:data-toggle "tooltip" :data-placement "top" :title "delete selected application"
                  :disabled true
                  :on-click #(restapi/add-node-to-testbed (fn[] (- 1 1)) {
                                                                      :name "node_3",
                                                                      :information_retrieved false
                                                                    })} "delete"])
              ;; add new node TODO
              [:button.badge.badge-pill.btn-sm.btn-success {:title "add a new application"
                :data-toggle "collapse" :data-target "#collapseNew" :aria-expanded "false" :aria-controls "collapseNew"
                :on-click #(restapi/add-node-to-testbed (fn[] (- 1 1)) {
                                                                    :name "node_3",
                                                                    :information_retrieved false
                                                                  })} "new application"]
            ]]]

        [panel-update/panel]
        [panel-new/panel]

        [:div {:id "apps-graph-parent" :style {:width "auto"}}
          [:div
            {:id "apps-graph" :style {:id "tab-testbeds" :width "auto" :height "600px" :border "1px solid lightgray"
             :background-color "#D1E1EF" :margin-top "5px"}}]]])))
