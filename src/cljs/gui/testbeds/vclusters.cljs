;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.testbeds.vclusters
  (:require [gui.testbeds.graphs :as graphs]
            [restapi.testbeds :as restapi]
            [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.subs :as subs]
            [reagent.core :as reagent]
            [gui.globals :as VARS]
            [utils.logs :as logs]
            [gui.testbeds.modal :as modal]
            [gui.testbeds.newp :as panel-new]
            [gui.testbeds.updatep :as panel-update]
            [gui.testbeds.newtestbed :as panel-new-testbed]))


;; FUNCTION: get-sel-testbed-info
(defn- get-sel-testbed-info ""
  [id-testbed]
  (let [res (VARS/get-testbed-by-id id-testbed)]
    (when-not (nil? res)
      (do
        (re-frame/dispatch [::events/set-selected-cluster-name (res :name)])
        (str " / " (res :id))))))


;; FUNCTION: get-total-nodes
(defn- get-total-nodes ""
  [id-testbed]
  (let [res (VARS/get-testbed-by-id id-testbed)]
    (if-not (nil? res)
      (- (count (res :nodes)) 1)
      "-")))


(defn- get-node-name ""
  [n]
  (if (nil? n)
    "-"
    (str (n :name))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION:
(defn- panel-component []
  (let [id-testbed  (re-frame/subscribe [::subs/selected-cluster])
        na-testbed  (re-frame/subscribe [::subs/selected-cluster-name])
        sel-node    (re-frame/subscribe [::subs/selected-cluster-node])]
    (fn []
      [:div {:style {:width "auto"}}
        [modal/modal]

        ;; 'HEADER'
        [:div.row
          [:div.col-sm-4
            [:h5 {:style {:margin-top "-5px"}}
              [:span.badge.badge-pill.badge-primary
                {:style {:color "#ffff99"}} "Testbeds: " (str (count @VARS/ALDE_TESTBEDS))]
              " - "
              [:span.badge.badge-pill.badge-secondary
                {:style {:color "#ffff99"}} " " (str @na-testbed " " (get-sel-testbed-info @id-testbed))]]]
          [:div.col-sm-8 (when (or (= @na-testbed "-") (nil? @na-testbed)) {:style {:display "none"}})
            [:h5 {:style {:margin-top "-5px"}}
              [:span.badge.badge-pill.badge-primary {:style {:color "#ffff99"}} "Total nodes: " (str (get-total-nodes @id-testbed))]
              " - "
              [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} " Selected node: " (get-node-name @sel-node) ]]]]

        ;; TESTBEDS

        [:div.row
          [:div.col-sm-4 {:id "clusters-graph-parent" :style {:width "auto"}}
            [:div {:id "clusters-graph"
                   :style {:width "auto" :height "300px" :border "1px solid lightgray" :background-color "#E3EDF1"}}]]

          ;; NODES
          [:div.col-sm-8
          (when (or (= @na-testbed "-") (nil? @na-testbed)) {:style {:display "none"}})
            [:div {:id "nodes-graph-parent" :style {:width "auto"}}
              [:div {:id "nodes-graph"
                     :style {:width "auto" :height "300px" :border "1px solid lightgray" :background-color "#D1E1EF"}}]]]]

        ;; 'FOOTER' (TESTBEDS and NODEs)
        [:div.row
          [:div.col-sm-4
            [:h5 {:style {:margin-top "5px"}}
              ;; add new testbed TODO
              [:button.badge.badge-pill.btn-sm.btn-success {:title "add new testbed"
                :data-toggle "collapse" :data-target "#collapseNew3" :aria-expanded "false" :aria-controls "collapseNew3"} "new"]
              ;; delete testbed TODO
              [:button.badge.badge-pill.btn-sm.btn-danger {:data-toggle "tooltip" :data-placement "bottom"
                :title "delete selected testbed"} "delete"]]]
          [:div.col-sm-8
          (when (or (= @na-testbed "-") (nil? @na-testbed)) {:style {:display "none"}})
            [:h5 {:style {:margin-top "5px"}}
              ;; add new node TODO
              [:button.badge.badge-pill.btn-sm.btn-success {:title "add a new node to the testbed"
                :data-toggle "collapse" :data-target "#collapseNew2" :aria-expanded "false" :aria-controls "collapseNew2"} "new"]
              ;; update node TODO
              [:button.badge.badge-pill.btn-sm.btn-warning {:title "update node's resources"
                :data-toggle "collapse" :data-target "#collapseUpdate2" :aria-expanded "false" :aria-controls "collapseUpdate2"} "update"]
              ;; delete node TODO
              [:button.badge.badge-pill.btn-sm.btn-danger {:data-toggle "tooltip" :data-placement "top" :title "delete selected node"} "delete"]]]]
        [panel-new-testbed/panel]
        [panel-update/panel]
        [panel-new/panel]


        ;; NODE: cpus, gpus, memories
        [:div
        (when (or (nil? @sel-node) (= (count @sel-node) 0)) {:style {:display "none"}})
          [:div.row
            [:div.col-sm-12
              [:h5 {:style {:margin-top "15px"}}
                [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} " " (get-node-name @sel-node) ]
                " "
                [:span.badge.badge-pill.badge-primary
                  {:style {:color "#ffff99"}} "CPUs: " (str (count (@sel-node :cpus)))]
                " "
                [:span.badge.badge-pill.badge-primary
                  {:style {:color "#ffff99"}} "GPUs: " (str (count (@sel-node :gpus)))]
                " "
                [:span.badge.badge-pill.badge-primary
                  {:style {:color "#ffff99"}} "Memories: " (str (count (@sel-node :memories)))]]]]
          [:div {:id "node1-graph-parent" :style {:width "auto"}}
            [:div {:id "node1-graph"
                   :style {:width "auto" :height "600px" :border "1px solid lightgray" :background-color "#B3C2DE"
                           :margin-top "5px"}}]]]])))

;; FUNCTION:
(defn panel []
  (reagent/create-class  ;; <-- expects a map of functions
    {;; Mounting
     ;:component-will-mount    #(println "component-will-mount")
     :component-did-mount     #(do (graphs/gen-data-clusters) (graphs/gen-data-nodes))
     ;; Updating
     ;:component-will-update   #(println "component-will-update")
     ;:component-did-update    #(println "component-did-update")
     ;; Unmounting
     ;:component-will-unmount  #(println "component-will-unmount")
     ;; Class Properties
     ;:display-name            "my-component"  ;; for more helpful warnings & errors
     ;; render
     :reagent-render           #(panel-component)}))
