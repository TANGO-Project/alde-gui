;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.execs.tabs.executions
  (:require [restapi.testbeds :as restapi]
            [reagent.core :as r]
            [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.subs :as subs]
            [gui.globals :as VARS]
            [utils.logs :as logs]))


(def EXECUTIONS_LOADED (r/atom false))
(def EXECUTIONS (r/atom [:tr]))
(def TOTAL_EXECUTIONS (r/atom 0))
(def TOTAL_SHOWN (r/atom 0))
(def SHOW (r/atom "COMPLETED")) ;COMPLETED"))
(def SHOW_EXEC_TYPE (r/atom "ALL"))
(def SHOW_TESTBED (r/atom "ALL"))
(def SHOW_ALL (r/atom false))


;; FUNCTION: draw-row
(defn- draw-row ""
  [x]
  (when-not (and (not @SHOW_ALL) (nil? (x :execution_configuration)))
    (swap! TOTAL_SHOWN inc)
    ^{:key (str "k-exec-" (random-uuid))}
    [:tr
      [:th.table-dark {:scope "row"} (str (x :id))]
      (if-not (nil? (x :execution_configuration))
        [:td (str ((x :execution_configuration) :testbed_id))]
        [:td "-"])
      [:td
        (case (x :status)
          "COMPLETED" {:class "table-success"}
          "FAILED"    {:class "table-danger"}
          "SUBMITTED" {:class "table-primary"}
          "RUNNING"   {:class "table-info"}
          "CANCELLED" {:class "table-warning"}
          {:class "table-secondary"})
        (str (x :status))]
      [:td (str (x :execution_type))]
      (if-not (nil? (x :execution_configuration))
        [:td
          [:table.table-sm.table-hover ;.table-dark
            [:tbody
              [:tr
                [:td [:b "COMPSS config."]]
                [:td (str ((x :execution_configuration) :compss_config))]]
              [:tr
                [:td [:b "Command"]]
                [:td (str ((x :execution_configuration) :command))]]
              [:tr
                [:td [:b "Profile file"]]
                [:td (str ((x :execution_configuration) :profile_file))]]]]]
        [:td "-"])]))


;; FUNCTION: f-execs
(defn- f-execs "Parse execs result"
  [res]
  (when-not @EXECUTIONS_LOADED
    (do
      (reset! VARS/TAB_LOADING true)
      (logs/info "Executing f-execs [EXECUTIONS_LOADED="  @EXECUTIONS_LOADED "]...")
      (reset! TOTAL_SHOWN 0)
      (if (and (or (= @SHOW "ALL") (nil? @SHOW) (= @SHOW "")) (or (= @SHOW_EXEC_TYPE "ALL") (nil? @SHOW_EXEC_TYPE) (= @SHOW_EXEC_TYPE "")))
        ; SHOW ALL
        (re-frame/dispatch [::events/set-executions (doall (for [x (res :objects)] (draw-row x)))])
        (if (or (= @SHOW_EXEC_TYPE "ALL") (nil? @SHOW_EXEC_TYPE) (= @SHOW_EXEC_TYPE ""))
          ; SHOW BY STATUS
          (re-frame/dispatch [::events/set-executions (doall (for [x (res :objects)]
                                                        (when (= (x :status) @SHOW)
                                                          (draw-row x))))])
          ; SHOW BY EXECUTION TYPE
          (if (or (= @SHOW "ALL") (nil? @SHOW) (= @SHOW ""))
            (re-frame/dispatch [::events/set-executions (doall (for [x (res :objects)]
                                                          (when (= (x :execution_type) @SHOW_EXEC_TYPE)
                                                            (draw-row x))))])
          ; SHOW BY STATUS AND EXECUTION TYPE
          (re-frame/dispatch [::events/set-executions (doall (for [x (res :objects)]
                                                        (when (and (= (x :status) @SHOW) (= (x :execution_type) @SHOW_EXEC_TYPE))
                                                          (draw-row x))))]))))
      (reset! TOTAL_EXECUTIONS (res :num_results))))
  (reset! VARS/TAB_LOADING false)
  (reset! EXECUTIONS_LOADED true))



;; FUNCTION: show-by-testbed
(defn- show-by-testbed [t]
  (reset! EXECUTIONS_LOADED false)
  (reset! SHOW_TESTBED t)
  (reset! VARS/TAB_LOADING true)
  (restapi/get-execs f-execs))


;; FUNCTION: show-by-status
(defn- show-by-status [status]
  (reset! EXECUTIONS_LOADED false)
  (reset! SHOW status)
  (reset! VARS/TAB_LOADING true)
  (restapi/get-execs f-execs))


;; FUNCTION: show-by-exec-type
(defn- show-by-exec-type [exec-type]
  (reset! EXECUTIONS_LOADED false)
  (reset! SHOW_EXEC_TYPE exec-type)
  (reset! VARS/TAB_LOADING true)
  (restapi/get-execs f-execs))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION:
(defn panel-component []
  (let [res-execs   (do (logs/info "Call to f-execs (panel-component)") (restapi/get-execs f-execs))
        t-execs     (re-frame/subscribe [::subs/executions])]
    [:div
      [:div.row
        [:div.col-sm-3
          [:h5
            [:span.badge.badge-pill.badge-primary "Total executions: "]
            [:span.badge.badge-pill.badge-secondary (str @TOTAL_EXECUTIONS)]]]
        [:div.col-sm-9
          [:div.form-check
            [:input.form-check-input {:type "checkbox" :id "chkShowAll" :checked @SHOW_ALL
                :on-click #(do (reset! SHOW_ALL (not @SHOW_ALL)) (reset! VARS/TAB_LOADING true) (reset! EXECUTIONS_LOADED false) (restapi/get-execs f-execs))}]
            [:label.form-check-label {:for "chkShowAll"} "Show executions with empty 'execution_configuration'"]]]]

      [:div.row {:style {:margin-top "-10px"}}
        [:div.col-sm-7
          [:span.badge.badge-pill.badge-primary "Showing... "]
          [:span.badge.badge-pill.badge-secondary (str @TOTAL_SHOWN)]
          " "
          [:span.badge.badge-pill.badge-dark "testbeds"]
          [:span.badge.badge-pill.badge-secondary @SHOW_TESTBED]
          " "
          [:span.badge.badge-pill.badge-dark "status"]
          (if-not (= @SHOW "ALL")
            [:span.badge.badge-pill.badge-info @SHOW]
            [:span.badge.badge-pill.badge-secondary @SHOW])
          " "
          [:span.badge.badge-pill.badge-dark "execution type"]
          [:span.badge.badge-pill.badge-secondary @SHOW_EXEC_TYPE]]
        [:div.col-sm-1
          [:span.badge.badge-pill.badge-primary "Filters:"]]]

      [:div.row {:style {:margin-bottom "5px"}}
        [:div.col-sm-7 ]
        [:div.col-sm-1
          [:div.dropdown.show
            [:a.badge.badge-pill.badge-dark.dropdown-toggle {:href "#" :role "button" :id "dropdownMenuLink"
                :data-toggle "dropdown" :aria-haspopup "true" :aria-expanded "false"} "Status"]
            [:div.dropdown-menu {:aria-labelledby "dropdownMenuLink"}
              ;[:a.dropdown-item {:href "#/execs" :on-click #(show-by-status "ALL")} "ALL"]
              [:a.dropdown-item {:href "#/execs" :on-click #(show-by-status "COMPLETED")} "COMPLETED"]
              [:a.dropdown-item {:href "#/execs" :on-click #(show-by-status "RUNNING")} "RUNNING"]
              [:a.dropdown-item {:href "#/execs" :on-click #(show-by-status "CANCELLED")} "CANCELLED"]
              [:a.dropdown-item {:href "#/execs" :on-click #(show-by-status "FAILED")} "FAILED"]
              [:a.dropdown-item {:href "#/execs" :on-click #(show-by-status "SUBMITTED")} "SUBMITTED"]
              [:a.dropdown-item {:href "#/execs" :on-click #(show-by-status "UNKNOWN")} "UNKNOWN"]
              [:a.dropdown-item {:href "#/execs" :on-click #(show-by-status "RESTART")} "RESTARTED"]
              [:a.dropdown-item {:href "#/execs" :on-click #(show-by-status "TIMEOUT")} "TIMEOUT"]]]]
        [:div.col-sm-2
          [:div.dropdown.show
            [:a.badge.badge-pill.badge-dark.dropdown-toggle {:href "#" :role "button" :id "dropdownMenuLink2"
                :data-toggle "dropdown" :aria-haspopup "true" :aria-expanded "false"} "Execution type"]
            [:div.dropdown-menu {:aria-labelledby "dropdownMenuLink2"}
              [:a.dropdown-item {:href "#/execs" :on-click #(show-by-exec-type "ALL")} "ALL"]
              [:a.dropdown-item {:href "#/execs" :on-click #(show-by-exec-type "SINGULARITY:PM")} "SINGULARITY:PM"]
              [:a.dropdown-item {:href "#/execs" :on-click #(show-by-exec-type "SLURM:SBATCH")} "SLURM:SBATCH"]]]]]

      [:div
        [:table.table.table-sm.table-hover ;.table-bordered
          [:thead.thead-dark
            [:tr
              [:th {:scope "col"} "#"]
              [:th {:scope "col"} "TESTBED"]
              [:th {:scope "col"} "STATUS"]
              [:th {:scope "col"} "EXEC TYPE"]
              [:th {:scope "col"} "EXECUTION CONFIG"]]]
          [:tbody
            @t-execs]]]]))
