;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.execs.tabs.exec_failed
  (:require [restapi.testbeds :as restapi]
            [reagent.core :as r]
            [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.subs :as subs]
            [gui.globals :as VARS]
            [utils.logs :as logs]
            [cljs.pprint :as pprint]))


(def EXECUTIONS_LOADED (r/atom false))
(def TOTAL_SHOWN (r/atom 0))

;; APPS
(def SHOW_APP_RESULTS (r/atom "ALL"))
(def COMPLETED_APP (r/atom {}))
(def COMPLETED_APP_NAME (r/atom ""))
(def COMPLETED_APP_ID (r/atom "ALL"))
;; CONFS
(def COMPLETED_APP_CONF (r/atom ""))
(def COMPLETED_APP_CONF_ID (r/atom ""))

;; NOT NIL
(def not-nil? (complement nil?))


;; FUNCTION: draw-row
(defn- draw-row ""
  [x]
  (when-not (and (nil? (x :execution_configuration)) (not (empty? x)))
    (swap! TOTAL_SHOWN inc)
    ^{:key (str "k-exec-" (random-uuid))}
    [:tr
      [:th.table-dark {:scope "row"} (str (x :id))]
      (if-not (nil? (x :execution_configuration))
        [:td (str ((x :execution_configuration) :testbed_id))]
        [:td "-"])
      [:td (str (x :execution_type))]
      [:td (str (x :execution_configuration_id))]
      [:td (str (x :slurm_sbatch_id))]
      (if-not (nil? (x :execution_configuration))
        [:td
          [:table.table-sm.table-hover ;.table-dark
            [:tbody
              (when-not (nil? ((x :execution_configuration) :compss_config))
                [:tr
                  [:td [:b "COMPSS config."]]
                  [:td (str ((x :execution_configuration) :compss_config))]])
              (when-not (nil? ((x :execution_configuration) :command))
                [:tr
                  [:td [:b "Command"]]
                  [:td (str ((x :execution_configuration) :command))]])
              (when-not (nil? ((x :execution_configuration) :profile_file))
                [:tr
                  [:td [:b "Profile file"]]
                  [:td (str ((x :execution_configuration) :profile_file))]])]]]
        [:td "-"])]))


;; FUNCTION: show-all
(defn- show-all ""
  []
  (logs/info "Showing ALL failed executions...")
  (reset! SHOW_APP_RESULTS "ALL")
  (reset! COMPLETED_APP {})
  (reset! COMPLETED_APP_NAME "")
  (reset! COMPLETED_APP_ID "ALL")
  (reset! COMPLETED_APP_CONF "")
  (reset! COMPLETED_APP_CONF_ID "")
  (when-not @EXECUTIONS_LOADED
    (do
      (reset! TOTAL_SHOWN 0)
      (reset! VARS/TAB_LOADING true)
      (re-frame/dispatch [::events/set-exec-tr (doall (for [x @VARS/ALDE_EXECS_FAILED] (draw-row x)))])
      ))
  (reset! VARS/TAB_LOADING false)
  (reset! EXECUTIONS_LOADED true))


;; FUNCTION: show-by-app
(defn- show-by-app ""
  [id-configs]
  (logs/info "Showing failed executions by app [" @COMPLETED_APP_ID "] (ids: " (str id-configs) ") ...")
  (when-not @EXECUTIONS_LOADED
    (do
      (reset! TOTAL_SHOWN 0)
      (reset! VARS/TAB_LOADING true)
      (let [filtered-execs (filter #(contains? id-configs (% :execution_configuration_id)) @VARS/ALDE_EXECS_FAILED)]
        (re-frame/dispatch [::events/set-exec-tr (doall (for [x filtered-execs] (draw-row x)))]))))
  (reset! VARS/TAB_LOADING false)
  (reset! EXECUTIONS_LOADED true))


;; FUNCTION: show-by-app-conf
(defn- show-by-app-conf ""
  [id-config]
  (logs/info "Showing failed executions by app [" @COMPLETED_APP_ID "] configuration [" (str id-config) "] ...")
  (when-not @EXECUTIONS_LOADED
    (do
      (reset! TOTAL_SHOWN 0)
      (reset! VARS/TAB_LOADING true)
      (let [filtered-execs (filter #(= id-config (% :execution_configuration_id)) @VARS/ALDE_EXECS_FAILED)]
        (re-frame/dispatch [::events/set-exec-tr (doall (for [x filtered-execs] (draw-row x)))]))))
  (reset! VARS/TAB_LOADING false)
  (reset! EXECUTIONS_LOADED true))

;;;;;;;;;;;;;;;;;;;;;;;;;
;; APPS

;; FUNCTION:
(defn- select-app [id]
  (reset! EXECUTIONS_LOADED false)

  ;; select
  (reset! COMPLETED_APP_CONF "")
  (reset! COMPLETED_APP_CONF_ID "")
  (if-not (= id "ALL")
    (let [res (filter #(= id (% :id)) @VARS/ALDE_APPS)
          res (first res)]
      (reset! SHOW_APP_RESULTS (res :name))
      (reset! COMPLETED_APP res)
      (reset! COMPLETED_APP_NAME (res :name))
      (reset! COMPLETED_APP_ID (res :id)))
    (show-all))

  ;; filtered results
  (when-not (= id "ALL")
    ;; get configs and filter results
    (let [configs   (into #{}
                      (for [x (@COMPLETED_APP :execution_configurations)]
                        (x :id)))
          res-logs  (logs/info "config apps: " (str configs))]
      (show-by-app configs))))


;; FUNCTION:
(defn- gen-apps-cbo ""
  []
  (for [x @VARS/ALDE_APPS]
    ^{:key (str "k-apps-cbo-item-" (random-uuid))}
    [:a.dropdown-item {:href "#/execs" :on-click #(select-app (x :id))} (str (x :id) "-" (x :name))]))


;;;;;;;;;;;;;;;;;;;;;;;;;
;; CONFS

;; FUNCTION:
(defn- select-app-conf [id]
  (when-not (= id "ALL")
    (let [res (filter #(= id (% :id)) @VARS/ALDE_APPS)
          res (first res)]
      (reset! COMPLETED_APP_CONF (str "conf_" id))
      (reset! COMPLETED_APP_CONF_ID id)
      (reset! EXECUTIONS_LOADED false)
      (show-by-app-conf id))))


;; FUNCTION:
(defn- gen-apps-conf-cbo ""
  []
  (for [x (@COMPLETED_APP :execution_configurations)]
    ^{:key (str "k-apps-conf-cbo-item-" (random-uuid))}
    [:a.dropdown-item {:href "#/execs" :on-click #(select-app-conf (x :id))} (str (x :id) "-" (x :executable_id) "/" (x :execution_type))]))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION:
(defn panel-component []
  (let [total-execs           (re-frame/subscribe [::subs/total-execs])
        total-execs-failed    (re-frame/subscribe [::subs/total-execs-failed])
        t-execs               (re-frame/subscribe [::subs/exec-tr])]
    [:div
      [:div.row
        [:div.col-sm-3
          [:h5
            [:span.badge.badge-pill.badge-primary "Total executions: "]
            [:span.badge.badge-pill.badge-secondary (str @total-execs)]]]
        [:div.col-sm-3
          [:h5
            [:span.badge.badge-pill.badge-warning "Failed: "]
            [:span.badge.badge-pill.badge-secondary @total-execs-failed]]]
        [:div.col-sm-4 ]
        [:div.col-sm-2
          [:button.badge.badge-pill.btn-sm {:title "Reload executions"
            :on-click   #(do
                          (logs/info "Refreshing failed executions...")
                          (restapi/get-executions-failed VARS/update-execs-failed)
                          (reset! EXECUTIONS_LOADED false)
                          (show-all))} "refresh"]]]

      [:div.row {:style {:margin-top "-5px"}}
        [:div.col-sm-8
          [:span.badge.badge-pill.badge-primary "Showing... "]
          [:span.badge.badge-pill.badge-secondary (str @TOTAL_SHOWN)]
          " "
          [:span.badge.badge-pill.badge-light  {:style {:color "lightgray"}}
            [:i "Failed executions with no information about the execution configuration are not showed"]]]]

      [:div.row {:style {:margin-bottom "5px"}}
        [:div.col-sm-2
          [:div.dropdown.show
            [:a.badge.badge-pill.badge-dark.dropdown-toggle {:href "#" :role "button" :id "dropdownMenuLink3"
                :data-toggle "dropdown" :aria-haspopup "true" :aria-expanded "false"} "Applications"]
            [:div.dropdown-menu {:aria-labelledby "dropdownMenuLink3"}
              ^{:key (str "k-apps-cbo-item-" (random-uuid))} [:a.dropdown-item {:href "#/execs" :on-click #(select-app "ALL")} "ALL"]
              (gen-apps-cbo)]]
          [:span.badge.badge-pill.badge-secondary @COMPLETED_APP_NAME]
          [:span.badge.badge-pill.badge-secondary @COMPLETED_APP_ID]]
        (when-not (= @COMPLETED_APP_ID "")
          [:div.col-sm-2
            [:div.dropdown.show
              [:a.badge.badge-pill.badge-dark.dropdown-toggle {:href "#" :role "button" :id "dropdownMenuLink3"
                  :data-toggle "dropdown" :aria-haspopup "true" :aria-expanded "false"} "Configurations"]
              [:div.dropdown-menu {:aria-labelledby "dropdownMenuLink3"}
                (gen-apps-conf-cbo)]]
            [:span.badge.badge-pill.badge-secondary @COMPLETED_APP_CONF]
            [:span.badge.badge-pill.badge-secondary @COMPLETED_APP_CONF_ID]])]

      [:div
        [:table.table.table-sm.table-hover ;.table-bordered
          [:thead.thead-dark
            [:tr
              [:th {:scope "col"} "ID"]
              [:th {:scope "col"} "TESTBED"]
              [:th {:scope "col"} "EXEC TYPE"]
              [:th {:scope "col"} "CFG"]
              [:th {:scope "col"} "BATCH ID"]
              [:th {:scope "col"} "EXECUTION CONFIG"]]]
          [:tbody
            @t-execs]]]]))


;; FUNCTION:
(defn panel [l-testbeds]
  (r/create-class
    {;; Mounting
     :component-did-mount     #(do
                                (reset! EXECUTIONS_LOADED false)
                                (show-all)
                                (logs/info "EXEC-FAILED: component-did-mount"))
     ;; Unmounting
     :component-will-unmount  #(do
                                (logs/info "EXEC-FAILED: component-will-unmount")
                                (re-frame/dispatch [::events/set-exec-tr [:tr]]))
     ;; render
     :reagent-render          #(panel-component)}))
