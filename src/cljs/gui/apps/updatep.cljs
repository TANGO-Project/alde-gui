;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.updatep
  (:require [gui.apps.graphs :as graphs]
            [restapi.testbeds :as restapi]
            [reagent.core :as r]
            [re-frame.core :as re-frame]
            [gui.subs :as subs]
            [reagent.core :as reagent]
            [gui.globals :as VARS]
            [gui.common.modal :as modal]))


;; JSON_NEW_CONFIG
(def JSON_NEW_CONFIG (r/atom "{
  \"execution_type\":\"SINGULARITY:PM SLURM:SRUN SLURM:SBATCH\",
  \"application_id\": <APPLICATION ID>,
  \"testbed_id\": TESTBED ID (integer),
  \"executable_id\": <EXECUTABLE ID>,
  \"num_nodes\": 1,
  \"num_gpus_per_node\": 2,
  \"num_cpus_per_node\": 12,
  \"exec_time\": 10,
  \"command\": \"COMMAND (string)\",
  \"compss_config\": \"CONFIG (string)\"
}"))

(def JSON_TEMPLATE (r/atom "{
  \"execution_type\":\"SINGULARITY:PM\",
  \"application_id\": 1,
  \"testbed_id\": 1,
  \"executable_id\": 1,
  \"num_nodes\": 1,
  \"num_gpus_per_node\": 2,
  \"num_cpus_per_node\": 12,
  \"exec_time\": 10,
  \"command\": \"/apps/application/master/Matmul 2 1024 12.34 /home_nfs/home_garciad/demo_test/cpu_gpu_run_data\",
  \"compss_config\": \"--worker_in_master_cpus=12 --worker_in_master_memory=24000 --worker_working_dir=/home_nfs/home_garciad --lang=c --monitoring=1000 -d\"
}"))

;; textarea RESPONSE
(def last-resp (r/atom ""))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION: change-val
(defn- change-val ""
  [val event]
  (do
    (reset! val (-> event .-target .-value))
    (reset! JSON_NEW_CONFIG (-> event .-target .-value))))


;; FUNCTION: atom-input-text
(defn atom-input-text [value]
  [:textarea.form-control.input-sm.text-left
    {:type "text" :rows "14" :placeholder "json content" :style {:background-color "#FFFFFC"}
     :value @value
     :on-change #(reset! value (-> % .-target .-value))}])


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION: panel js/myFunction
(defn panel []
  (reset! last-resp "")
  (let [selected-app      (re-frame/subscribe [::subs/selected-app])
        selected-app-id   (re-frame/subscribe [::subs/selected-app-id])
        selected-exec-app     (re-frame/subscribe [::subs/selected-exec-app])
        selected-exec-app-id  (re-frame/subscribe [::subs/selected-exec-app-id])
        selected-exec-conf-id (re-frame/subscribe [::subs/selected-exec-conf-id])
        selected-exec-exec-id (re-frame/subscribe [::subs/selected-exec-exec-id])]
    (reset! JSON_NEW_CONFIG (clojure.string/replace @JSON_NEW_CONFIG #"<APPLICATION ID>" (str (@selected-exec-app :id))))
    (reset! JSON_NEW_CONFIG (clojure.string/replace @JSON_NEW_CONFIG #"<EXECUTABLE ID>" (str (subs @selected-app-id 5))))
    [:div.modal_exec {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
    (let [val-json (r/atom @JSON_NEW_CONFIG)]
      [:div.card.card-body
        [:div.col-sm-12
          ;; header
          [:div.row
            [:div.col-sm-12
              [:h5 {:style {:margin-top "5px" :text-align "left"}}
                [:span.badge.badge-pill.badge-primary "Application:"]
                [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@selected-exec-app :name)]
                [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@selected-exec-app :id)] " "
                [:span.badge.badge-pill.badge-primary "Executable:"]
                [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} @selected-app-id]
                [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (str (subs @selected-app-id 5))]
                " "
                [:span.badge.badge-pill.badge-success "Add a new execution configuration"]]]]
          ;; content
          [:div.row {:style {:margin-top "5px"}}
            [:label.col-sm-2.control-label.text-right [:b "Execution Configuration:"]]
            [:div.col-sm-10
              [atom-input-text val-json]]]
          ;; footer buttons
          [:div.row
            [:div.col-sm-8 " "]
            [:div.col-sm-4
              [:h5 {:style {:margin-top "5px" :text-align "left"}}
                ;; save node TODO
                [:button.badge.badge-pill.btn-sm.btn-success {:style {:margin-right "5px" :text-align "right"}
                  :data-toggle "tooltip" :data-placement "right" :title "Submit"
                  :on-click #(do (restapi/add-conf @val-json) (modal/close-modal))} "Submit"]
                ;; cancel
                [:button.badge.badge-pill.btn-sm.btn-danger {:title "Cancel operation and close panel"
                  :on-click #(do (reset! last-resp "") (modal/close-modal))} "Cancel / Close"]]]]
          ;; example
          [:div.row {:style {:margin-top "5px"}}
            [:label.col-sm-2.control-label.text-right [:b "Example:"]]]
          [:div.row
            [:div.col-sm-12
              [:textarea.form-control.input-sm.text-left
                {:type "text" :rows "14" :placeholder "json content" :style {:background-color "#FFFFCC"} :readonly true
                 :value @JSON_TEMPLATE}]]]]])]))
