;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.modal
  (:require [re-frame.core :refer [dispatch subscribe]]
            [utils.logs :as logs]
            [cljs.pprint :as pprint]
            [reagent.core :as r]
            [cljs.pprint :as pprint]))


(def TXT_APP (r/atom {:id "-not-defined-" :name "-not-defined-"}))
(def TXT_EXEC (r/atom {:id "-not-defined-" :name "-not-defined-"}))
(def TXT_CONF (r/atom {:id "-not-defined-" :name "-not-defined-"}))
(def TXT_EXECUTION (r/atom {:id "-not-defined-" :name "-not-defined-"}))


;; COMPONENT: modal-panel
(defn modal-panel
  [{:keys [child size show?]}]
  [:div.modal-wrapper
    [:div.modal-backdrop
      {:on-click (fn [event]
                      (do
                        (dispatch [:modal {:show? (not show?)
                                           :child nil
                                           :size :default}])
                        (.preventDefault event)
                        (.stopPropagation event)))}]
    [:div.modal-child
      {:style {:width (case size
               :extra-small "15%"
               :small "30%"
               :large "70%"
               :extra-large "85%"
               "50%")}} child]])

(defn modal []
  (let [modal (subscribe [:modal])]
    (fn []
      [:div
       (if (:show? @modal)
         [modal-panel @modal])])))


(defn- close-modal []
  (dispatch [:modal {:show? false :child nil}]))

;;
(defn- conf []
  [:div.modal_exec_conf {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
    ;; header
    [:div.row
      [:div.col-sm-12
        [:h5 {:style {:margin-top "-5px" :text-align "left"}}
          [:span.badge.badge-pill.badge-success "Execution Configuration"]
          [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (str "conf_" (@TXT_CONF :id))]]]]
    ;; content
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT_CONF :id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Execution Type:"]]
      [:div.col-sm-8
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_CONF :execution_type))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Command:"]]
      [:div.col-sm-10
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_CONF :command))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "COMPSs configuration:"]]
      [:div.col-sm-10
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_CONF :compss_config))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Profile file:"]]
      [:div.col-sm-10
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_CONF :profile_file))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "executable_id:"]]
      [:div.col-sm-3
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_CONF :executable_id))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "exec time:"]]
      [:div.col-sm-3
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_CONF :exec_time))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "cpus per node:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_CONF :num_cpus_per_node))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "gpus per node:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_CONF :num_gpus_per_node))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "num nodes:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_CONF :num_nodes))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "srun config:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_CONF :srun_config))}]]]

    [:button {:type "button" :title "Cancel" :class "close" :on-click #(close-modal)
              :style {:margin-bottom "35px" :margin-top "-15px"}} "Close"]])

;;
(defn- executable []
  [:div.modal_exec {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
    ;; header
    [:div.row
      [:div.col-sm-12
        [:h5 {:style {:margin-top "-5px" :text-align "left"}}
          [:span.badge.badge-pill.badge-primary "Executable"]
          [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (str "exec_" (@TXT_EXEC :id))]]]]
    ;; content
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT_EXEC :id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Satus:"]]
      [:div.col-sm-4
        [:input.form-control.input-sm.text-left {:type "text" :placeholder "---" :read-only true :style
          (if (= (@TXT_EXEC :status) "COMPILED")
            {:background-color "#58FA58"}
            (if (= (@TXT_EXEC :status) "NOT_COMPILED")
              {:background-color "#FF0000"}
              {:background-color "#FFFFFF"}))
          :defaultValue (@TXT_EXEC :status)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Compilation Type:"]]
      [:div.col-sm-8
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_EXEC :compilation_type))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Compilation Script:"]]
      [:div.col-sm-8
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_EXEC :compilation_script))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Executable File:"]]
      [:div.col-sm-8
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_EXEC :executable_file))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Singularity application folder:"]]
      [:div.col-sm-8
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_EXEC :singularity_app_folder))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Singularity image file:"]]
      [:div.col-sm-8
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_EXEC :singularity_image_file))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Source code file:"]]
      [:div.col-sm-8
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_EXEC :source_code_file))}]]]

    [:button {:type "button" :title "Cancel" :class "close" :on-click #(close-modal)
              :style {:margin-bottom "35px" :margin-top "-15px"}} "Close"]])

;;
(defn- application []
  [:div.modal_application {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
    ;; header
    [:div.row
      [:div.col-sm-12
        [:h5 {:style {:margin-top "-5px" :text-align "left"}}
          [:span.badge.badge-pill.badge-primary "Application"]
          [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@TXT_APP :name)]]]]
    ;; content
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT_APP :id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Name:"]]
      [:div.col-sm-8
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT_APP :name)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Executables:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (count (@TXT_APP :executables)))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Execution Configurations:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (count (@TXT_APP :execution_configurations)))}]]]

    [:button {:type "button" :title "Cancel" :class "close" :on-click #(close-modal)
              :style {:margin-bottom "35px" :margin-top "-15px"}} "Close"]])


;;
(defn- execution []
  [:div.modal_exec {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
    ;; header
    [:div.row
      [:div.col-sm-12
        [:h5 {:style {:margin-top "-5px" :text-align "left"}}
          [:span.badge.badge-pill.badge-primary "Execution"]
          [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} "exe"]]]]
    ;; content
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT_EXECUTION :id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Status:"]]
      [:div.col-sm-8
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT_EXECUTION :status)}]]]
    (if (= (@TXT_EXECUTION :status) "COMPLETED")
      [:div.row {:style {:margin-top "5px"}}
        [:label.col-sm-2.control-label.text-right [:b "energy output:"]]
        [:div.col-sm-8
          [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
            :defaultValue (str (@TXT_EXECUTION :energy_output) " J")}]]]
      [:div.row {:style {:margin-top "5px"}}
        [:label.col-sm-2.control-label.text-right [:b "energy output:"]]
        [:div.col-sm-8
          [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
            :defaultValue "-"}]]])
    (if (= (@TXT_EXECUTION :status) "COMPLETED")
      [:div.row {:style {:margin-top "5px"}}
        [:label.col-sm-2.control-label.text-right [:b "runtime output:"]]
        [:div.col-sm-8
          [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
            :defaultValue (str (@TXT_EXECUTION :runtime_output) " s")}]]]
      [:div.row {:style {:margin-top "5px"}}
        [:label.col-sm-2.control-label.text-right [:b "runtime output:"]]
        [:div.col-sm-8
          [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
            :defaultValue "-"}]]])
    (if (= (@TXT_EXECUTION :status) "COMPLETED")
      [:div.row {:style {:margin-top "5px"}}
        [:label.col-sm-2.control-label.text-right [:b "watts:"]]
        [:div.col-sm-8
          [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
            :defaultValue (str (pprint/cl-format nil  "~,4f" (/ (@TXT_EXECUTION :energy_output) (@TXT_EXECUTION :runtime_output))) " W")}]]]
      [:div.row {:style {:margin-top "5px"}}
        [:label.col-sm-2.control-label.text-right [:b "watts:"]]
        [:div.col-sm-8
          [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
            :defaultValue "-"}]]])

    [:button {:type "button" :title "Cancel" :class "close" :on-click #(close-modal)
              :style {:margin-bottom "35px" :margin-top "-15px"}} "Close"]])
