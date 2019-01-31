;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.tabs.executions
  (:require [restapi.testbeds :as restapi]
            [re-frame.core :as re-frame]
            [gui.subs :as subs]
            [gui.globals :as VARS]
            [gui.apps.newp :as panel-new]
            [gui.apps.updatep :as panel-update]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION:
(defn panel-component []
  (let [selected-exec-app     (re-frame/subscribe [::subs/selected-exec-app])
        selected-exec-app-id  (re-frame/subscribe [::subs/selected-exec-app-id])
        selected-exec-conf-id (re-frame/subscribe [::subs/selected-exec-conf-id])
        selected-exec-exec-id (re-frame/subscribe [::subs/selected-exec-exec-id])
        selected-execution-id (re-frame/subscribe [::subs/selected-execution-id])
        selected-execution    (re-frame/subscribe [::subs/selected-execution])
        total-execs           (re-frame/subscribe [::subs/total-execs])
        selected-app          (re-frame/subscribe [::subs/selected-app])
        exec_cfg_execs_total          (re-frame/subscribe [::subs/exec_cfg_execs_total])
        exec_cfg_execs_completed      (re-frame/subscribe [::subs/exec_cfg_execs_completed])
        exec_cfg_execs_failed         (re-frame/subscribe [::subs/exec_cfg_execs_failed])
        exec_cfg_execs_running        (re-frame/subscribe [::subs/exec_cfg_execs_running])
        exec_cfg_execs_cancelled      (re-frame/subscribe [::subs/exec_cfg_execs_cancelled])
        exec_cfg_execs_restart        (re-frame/subscribe [::subs/exec_cfg_execs_restart])
        exec_cfg_execs_timeout        (re-frame/subscribe [::subs/exec_cfg_execs_timeout])
        exec_cfg_execs_unknown        (re-frame/subscribe [::subs/exec_cfg_execs_unknown])
        resp-executions       (re-frame/subscribe [::subs/resp-executions])
        requ-executions       (re-frame/subscribe [::subs/requ-executions])]
    (fn []
      [:div
        [:div.row
          [:div.col-sm-12
            [:h5
              [:span.badge.badge-pill.badge-primary "Applications: " (str (VARS/get-total-apps))]
              " "
              [:span.badge.badge-pill.badge-primary "Executions: " (str @total-execs)]]]]

        ;; APPS
        [:div.row {:style {:width "auto"}}
          [:div {:style {:width "50%"}}
            [:h6
              [:span.badge.badge-pill.badge-primary "Applications"] " "
              [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@selected-exec-app :name)] " "
              [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@selected-exec-app :id)] " "
              [:button.badge.badge-pill.btn-sm.btn-success {:title "add a new application"
                :on-click #(re-frame/dispatch [:modal { :show? true
                                                        :child [panel-new/panel]
                                                        :size :large}])} "new"]
              (when-not (= @selected-exec-app {})
                ;; delete node TODO
                [:button.badge.badge-pill.btn-sm.btn-danger {
                  :on-click #(when (.confirm js/window (str "Are you sure you want to delete the application [" @selected-exec-app-id " - " (@selected-app :name) "]?"))
                              (restapi/rem-application @selected-exec-app-id))} "delete"])]]

          ;; EXECS CONFS
          [:div {:style {:width "50%"}}
            [:h6
              [:span.badge.badge-pill.badge-primary "Conf. / Executables"] " "
              [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@selected-exec-app :name)] " "

              (when-not (= @selected-exec-exec-id "-")
                [:span.badge.badge-pill.badge-secondary {:style {:color "#CED8F6"}} @selected-exec-exec-id])

              (when-not (= @selected-exec-conf-id "-")
                [:span.badge.badge-pill.badge-secondary {:style {:color "#ffbb19"}} @selected-exec-conf-id])

              " "
              [:button.badge.badge-pill.btn-sm.btn-success {:title "add a new executable"
                :on-click #(re-frame/dispatch [:modal { :show? true
                                                        :child [panel-update/panel]
                                                        :size :large}])} "new executable"]

              (when-not (= @selected-exec-exec-id "-") " ")
              (when-not (= @selected-exec-exec-id "-")
                [:button.badge.badge-pill.btn-sm.btn-warning {:title "add a new execution configuration"
                  :on-click #(re-frame/dispatch [:modal { :show? true
                                                          :child [panel-update/panel]
                                                          :size :large}])} "new configuration"])

              (when-not (= @selected-exec-conf-id "-") " ")
              (when-not (= @selected-exec-conf-id "-")
                [:button.badge.badge-pill.btn-sm.btn-danger {:title "launch new execution"
                  :on-click #(when (.confirm js/window (str "Are you sure you want to launch the selected [" @selected-exec-conf-id " - " (subs @selected-exec-conf-id 5) "] execution configuration?"))
                              (restapi/launch-exec (subs @selected-exec-conf-id 5)))} "launch execution"])
          ]]]

        [:div.row {:style {:width "auto"}}
          [:div {:id "apps-graph2" :style {:width "50%" :height "350px" :border "1px solid black"
                 :background-color "#888888"}}]
          [:div {:id "apps-graph3" :style {:width "50%" :height "350px" :border "1px solid black"
                 :background-color "#aaaaaa"}}]]

        (when @VARS/TAB_EXECUTIONS_LOADING
          [:div.row [:img {:src "images/loader.gif" :width "24px" :height "24px"}] [:b "Loading executions..."]])

        [:div.row {:style {:width "auto" :margin-top "5px"}}
          [:div {:style {:width "100%"}}
            [:h6
              [:span.badge.badge-pill.badge-primary "Executions"] " "
              [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@selected-exec-app :name)]
              [:span.badge.badge-pill.badge-secondary {:style {:color "#ffbb19"}} (str @selected-exec-conf-id)] " "
              [:span.badge.badge-pill.badge-info "Total: " (str @exec_cfg_execs_total)] " "
              [:span.badge.badge-pill.badge-info {:style {:color "darkgreen"}} "Completed: " (str @exec_cfg_execs_completed)] " "
              [:span.badge.badge-pill.badge-info {:style {:color "darkred"}} "Failed: " (str @exec_cfg_execs_failed)] " "
              [:span.badge.badge-pill.badge-info {:style {:color "darkblue"}} "Running: " (str @exec_cfg_execs_running)] " "
              [:span.badge.badge-pill.badge-info "Cancelled: " (str @exec_cfg_execs_cancelled)] " "
              [:span.badge.badge-pill.badge-info "Restart: " (str @exec_cfg_execs_restart)] " "
              [:span.badge.badge-pill.badge-info "Timeout: " (str @exec_cfg_execs_timeout)] " "
              [:span.badge.badge-pill.badge-info "Unknown: " (str @exec_cfg_execs_unknown)] " "
            ]]]

        [:div.row {:style {:width "auto" :margin-top "5px"}}
          [:div {:style {:width "100%"}}
            [:h6
              (when-not (= @selected-execution-id "-")
                [:span.badge.badge-pill.badge-secondary {:style {:color "#CED8F6"}} @selected-execution-id])
              " "
              (when-not (= @selected-execution-id "-")
                [:button.badge.badge-pill.btn-sm.btn-primary {:title "stop" :on-click #(when (.confirm js/window (str "Are you sure you want to stop the selected [" @selected-execution-id "] execution?"))
                            (restapi/stop-execution @selected-execution-id))} "stop"])
              " "
              (when-not (= @selected-execution-id "-")
                [:button.badge.badge-pill.btn-sm.btn-danger {:title "cancel" :on-click #(when (.confirm js/window (str "Are you sure you want to cancel the selected [" @selected-execution-id "] execution?"))
                            (restapi/cancel-execution @selected-execution-id))} "cancel"])
              " "
              (when-not (= @selected-execution-id "-")
                [:button.badge.badge-pill.btn-sm.btn-success {:title "restart" :on-click #(when (.confirm js/window (str "Are you sure you want to restart the selected [" @selected-execution-id "] execution?"))
                            (restapi/restart-execution @selected-execution-id))} "restart"])
            ]]]

        (when-not (= @selected-exec-app-id "")
          [:div.row {:style {:width "auto"}}
            [:div {:id "apps-graph4" :style {:width "100%" :height "650px" :border "1px solid black"
                   :background-color "#cccccc"}}]])

        [:div.row {:style {:width "auto" :margin-top "250px"}}
          [:div.col-sm-12
            [:textarea.form-control.input-sm.text-left
              {:type "text" :rows "2" :placeholder "json content" :style {:background-color "black" :color "yellow"} :readonly true
               :value @requ-executions}]]]
        [:div.row {:style {:width "auto" :margin-top "1px"}}
          [:div.col-sm-12
            [:textarea.form-control.input-sm.text-left
              {:type "text" :rows "4" :placeholder "json content" :style {:background-color "black" :color "white"} :readonly true
               :value @resp-executions}]]]
        ])))
