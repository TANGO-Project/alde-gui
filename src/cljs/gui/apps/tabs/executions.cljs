;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.tabs.executions
  (:require [restapi.testbeds :as restapi]
            [reagent.core :as r]
            [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.subs :as subs]
            [gui.globals :as VARS]
            [utils.logs :as logs]))


(def EXECUTIONS (r/atom [:tr]))
(def TOTAL_EXECUTIONS (r/atom 0))
(def SHOW (r/atom "ALL"))
(def SHOW_EXEC_TYPE (r/atom "ALL"))


;; FUNCTION: f-execs
(defn- f-execs "Parse execs result"
  [res]
  (logs/info @SHOW)
  (if (or (= @SHOW "ALL") (nil? @SHOW) (= @SHOW ""))
    (re-frame/dispatch [::events/set-executions (for [x (res :objects)]
                                                  [:tr
                                                  (if (= (x :status) "COMPLETED")
                                                    {:class "table-success"}
                                                    (if (= (x :status) "FAILED")
                                                      {:class "table-danger"}
                                                      {:class ""}))
                                                        [:th {:scope "row"} (str (x :id))]
                                                        [:td (str (x :status))]
                                                        [:td (str (x :execution_type))]
                                                        [:td (str (x :execution_configuration))]])])
    (re-frame/dispatch [::events/set-executions (for [x (res :objects)]
                                                  (when (= (x :status) @SHOW)
                                                    [:tr
                                                    (if (= (x :status) "COMPLETED")
                                                      {:class "table-success"}
                                                      (if (= (x :status) "FAILED")
                                                        {:class "table-danger"}
                                                        {:class ""}))
                                                          [:th {:scope "row"} (str (x :id))]
                                                          [:td (str (x :status))]
                                                          [:td (str (x :execution_type))]
                                                          [:td (str (x :execution_configuration))]]))]))
  (reset! TOTAL_EXECUTIONS (res :num_results))
  (reset! VARS/TAB_LOADING false))


;; FUNCTION: show-by-status
(defn- show-by-status [status]
  (reset! SHOW status)
  (reset! VARS/TAB_LOADING true)
  (restapi/get-execs f-execs))


;; FUNCTION: show-by-exec-type
(defn- show-by-exec-type [exec-type]
  ;(reset! SHOW status)
  (reset! VARS/TAB_LOADING true)
  (restapi/get-execs f-execs))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;:;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION:
(defn panel-component []
  (let [res-execs   (restapi/get-execs f-execs)
        t-execs     (re-frame/subscribe [::subs/executions])]
    [:div
      [:div.row {:style {:margin-top "-640px"}}
        [:div.col-sm-3
          [:h5
            [:span.badge.badge-pill.badge-primary {:style {:color "#ffff99"}} "Total executions: " (str @TOTAL_EXECUTIONS)]]]

        [:div.col-sm-2
          [:div.dropdown.show
            [:a.btn-sm.btn-secondary.dropdown-toggle {:href "#" :role "button" :id "dropdownMenuLink"
                :data-toggle "dropdown" :aria-haspopup "true" :aria-expanded "false"} "Status"]
            [:div.dropdown-menu {:aria-labelledby "dropdownMenuLink"}
              [:a.dropdown-item {:href "#/apps" :on-click #(show-by-status "ALL")} "ALL"]
              [:a.dropdown-item {:href "#/apps" :on-click #(show-by-status "COMPLETED")} "COMPLETED"]
              [:a.dropdown-item {:href "#/apps" :on-click #(show-by-status "FAILED")} "FAILED"]
              [:a.dropdown-item {:href "#/apps" :on-click #(show-by-status "SUBMITTED")} "SUBMITTED"]]]
          [:span.badge.badge-pill.badge-secondary @SHOW]]

        [:div.col-sm-2
          [:div.dropdown.show
            [:a.btn-sm.btn-secondary.dropdown-toggle {:href "#" :role "button" :id "dropdownMenuLink2"
                :data-toggle "dropdown" :aria-haspopup "true" :aria-expanded "false"} "Execution type"]
            [:div.dropdown-menu {:aria-labelledby "dropdownMenuLink2"}
              [:a.dropdown-item {:href "#/apps" :on-click #(show-by-exec-type "ALL")} "ALL"]
              [:a.dropdown-item {:href "#/apps" :on-click #(show-by-exec-type "SINGULARITY:PM")} "SINGULARITY:PM"]
              [:a.dropdown-item {:href "#/apps" :on-click #(show-by-exec-type "SLURM:SBATCH")} "SLURM:SBATCH"]]]
          [:span.badge.badge-pill.badge-secondary @SHOW_EXEC_TYPE]]

        [:div.col-sm-5
          [:h5
            ;; add new node TODO
            [:button.badge.badge-pill.btn-sm.btn-success {:title "add a new execution"
              :data-toggle "collapse" :data-target "#collapseNew" :aria-expanded "false" :aria-controls "collapseNew"
              :on-click #(restapi/add-node-to-testbed (fn[] (- 1 1)) {
                                                                  :name "node_3",
                                                                  :information_retrieved false
                                                                })} "new execution"]]]]

      [:div
        [:table.table.table-sm.table-bordered
          [:thead.thead-dark
            [:tr
              [:th {:scope "col"} "#"]
              [:th {:scope "col"} "status"]
              [:th {:scope "col"} "execution_type"]
              [:th {:scope "col"} "execution_configuration"]]]
          [:tbody
            @t-execs]
          ]
        ]
  ]))
