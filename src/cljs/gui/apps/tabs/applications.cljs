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
          [:div.col-sm-7
            [:h5
              [:span
                [:span.badge.badge-pill.badge-primary (str "Applications: " (str (VARS/get-total-apps)))]]
              " "
              (cond
                (= @selected-app-type "app")
                  [:span.badge.badge-pill.badge-info {:style {:color "#ffff99"}} (str "Application id: " @selected-app-id ", Name: " (@selected-app :name))]
                (= @selected-app-type "exec")
                  [:span
                    [:span.badge.badge-pill.badge-info (str "Executable")]
                    [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (str @selected-app-id)]]
                (= @selected-app-type "conf")
                  [:span
                    [:span.badge.badge-pill.badge-info (str "Execution Configuration")]
                    [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} @selected-app-id]]
                :else
                  [:span.badge.badge-pill.badge-info {:style {:color "#ffff99"}} (str @selected-app-id)])
              ]]
          [:div.col-sm-5
            [:h5
              [:button.badge.badge-pill.btn-sm.btn-success {:title "add a new application"
                :on-click #(re-frame/dispatch [:modal { :show? true
                                                        :child [panel-new/panel]
                                                        :size :large}])} "new application"]
              ;(when (or (= @selected-app-type "app") (= @selected-app-type "exec"))
              ;  ;; update node TODO
              ;  [:button.badge.badge-pill.btn-sm.btn-warning {:title "add a new execution configuration"
              ;    :on-click #(re-frame/dispatch [:modal { :show? true
              ;                                            :child [panel-update/panel]
              ;                                            :size :large}])} "new execution configuration"])
              (when (= @selected-app-type "app")
                ;; delete node TODO
                [:button.badge.badge-pill.btn-sm.btn-danger {
                  :on-click #(when (.confirm js/window (str "Are you sure you want to delete the application [" @selected-app-id " - " (@selected-app :name) "]?"))
                              (restapi/rem-application @selected-app-id))} "delete"])
              ;(when (= @selected-app-type "conf")
              ;  ;; launch TODO
              ;  [:button.badge.badge-pill.btn-sm.btn-primary {:title "launch application"
              ;    :on-click #(when (.confirm js/window (str "Are you sure you want to launch the selected [" @selected-app-id " - " (subs @selected-app-id 5) "] execution configuration?"))
              ;                (restapi/rem-application (subs @selected-app-id 5)))} "launch"])


            ]]]

        [:div {:style {:width "auto"}}
          [:div {:id "apps-graph" :style {:width "auto" :height "750px" :border "1px solid lightgray"
                 :background-color "#888888" :margin-top "5px"}}]]])))
