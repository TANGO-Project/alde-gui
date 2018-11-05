;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license.
;; Please, refer to the LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.testbeds.modal
  (:require [re-frame.core :refer [dispatch subscribe]]
            [utils.logs :as logs]
            [reagent.core :as r]))


(def TXT (r/atom {:id "-not-defined-" :name "-not-defined-"}))
(def TXT_NODE (r/atom {:id "-not-defined-" :name "-not-defined-"}))
(def TXT_NODE_ELEM (r/atom {:id "-not-defined-" :name "-not-defined-"}))


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
(defn- node []
  [:div.modal_node {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
    ;; header
    [:div.row
      [:div.col-sm-10
        [:h5 {:style {:margin-top "-5px" :text-align "left"}}
          [:span.badge.badge-pill.badge-primary " Node "]
          [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@TXT_NODE :name)]]]]
    ;; content
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE :id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Name:"]]
      [:div.col-sm-4
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE :name)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Information retrieved?"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE :information_retrieved)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Testbed Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE :testbed_id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "cpus:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :read-only true
          :defaultValue (str (count (@TXT_NODE :cpus)))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "gpus:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :read-only true
          :defaultValue (str (count (@TXT_NODE :gpus)))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "memories:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :read-only true
          :defaultValue (str (count (@TXT_NODE :memories)))}]]]
    [:button {:type "button" :title "Cancel" :class "close" :on-click #(close-modal)
              :style {:margin-bottom "35px" :margin-top "-15px"}} "Close"]])

;;
(defn- testbed []
  [:div.modal_testbed {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
    ;; header
    [:div.row
      [:div.col-sm-12
        [:h5 {:style {:margin-top "-5px" :text-align "left"}}
          [:span.badge.badge-pill.badge-primary " Testbed "]
          [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@TXT :name)]]]]
    ;; content
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT :id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Name:"]]
      [:div.col-sm-4
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT :name)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Category:"]]
      [:div.col-sm-4
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT :category)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Endpoint:"]]
      [:div.col-sm-6
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT :endpoint)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Protocol:"]]
      [:div.col-sm-4
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT :protocol)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Nodes:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :read-only true
          :defaultValue (str (count (@TXT :nodes)))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Package Formats:"]]
      [:div.col-sm-8
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT :package_formats))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Extra Configuration:"]]
      [:div.col-sm-10
        [:textarea.form-control.input-sm.text-left {:rows "2" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT :extra_config))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Online?"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT :online)}]]]

    [:button {:type "button" :title "Cancel" :class "close" :on-click #(close-modal)
              :style {:margin-bottom "35px" :margin-top "-15px"}} "Close"]])


;;
(defn- node_elem_cpu []
  [:div.modal_node_elem {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
    ;; header
    [:div.row
      [:div.col-sm-12
        [:h5 {:style {:margin-top "-5px" :text-align "left"}}
          [:span.badge.badge-pill.badge-primary " CPU "]
          [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@TXT_NODE_ELEM :model_name)]]]]
    ;; content
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Model Name:"]]
      [:div.col-sm-10
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :model_name)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Vendor Id:"]]
      [:div.col-sm-6
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :vendor_id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Model:"]]
      [:div.col-sm-4
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :model)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Architecture"]]
      [:div.col-sm-4
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :arch)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Cores:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_NODE_ELEM :cores))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Speed:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_NODE_ELEM :speed))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Flags:"]]
      [:div.col-sm-10
        [:textarea.form-control.input-sm.text-left {:rows "2" :style {:background-color "#FFFFFF"} :read-only true
          :defaultValue (@TXT_NODE_ELEM :flags)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Bogomips:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_NODE_ELEM :bogomips))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Cache:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_NODE_ELEM :cache))}]]]
    [:button {:type "button" :title "Cancel" :class "close" :on-click #(close-modal)
              :style {:margin-bottom "35px" :margin-top "-15px"}} "Close"]])


;;
(defn- node_elem_gpu []
  [:div.modal_node_elem {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
    ;; header
    [:div.row
      [:div.col-sm-12
        [:h5 {:style {:margin-top "-5px" :text-align "left"}}
          [:span.badge.badge-pill.badge-primary " GPU "]
          [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@TXT_NODE_ELEM :model_name)]]]]
    ;; content
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Model Name:"]]
      [:div.col-sm-10
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :model_name)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Vendor Id:"]]
      [:div.col-sm-6
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :vendor_id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Node Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :node_id)}]]]
    [:button {:type "button" :title "Cancel" :class "close" :on-click #(close-modal)
              :style {:margin-bottom "35px" :margin-top "-15px"}} "Close"]])

;;
(defn- node_elem_mem []
  [:div.modal_node_elem {:style {:padding "16px" :border-radius "6px" :text-align "center"}}
    ;; header
    [:div.row
      [:div.col-sm-10
        [:h5 {:style {:margin-top "-5px" :text-align "left"}}
          [:span.badge.badge-pill.badge-primary " Memory "]
          [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} (@TXT_NODE_ELEM :id)]]]]
    ;; content
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :id)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Memory Type:"]]
      [:div.col-sm-10
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_NODE_ELEM :memory_type))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Size:"]]
      [:div.col-sm-6
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (str (@TXT_NODE_ELEM :size))}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Units:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFFF"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :units)}]]]
    [:div.row {:style {:margin-top "5px"}}
      [:label.col-sm-2.control-label.text-right [:b "Node Id:"]]
      [:div.col-sm-2
        [:input.form-control.input-sm.text-left {:type "text" :style {:background-color "#FFFFCC"} :placeholder "---" :read-only true
          :defaultValue (@TXT_NODE_ELEM :node_id)}]]]
    [:button {:type "button" :title "Cancel" :class "close" :on-click #(close-modal)
              :style {:margin-bottom "35px" :margin-top "-15px"}} "Close"]])
