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
            [reagent.core :as r]
            [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.subs :as subs]
            [reagent.core :as reagent]
            [gui.globals :as VARS]
            [utils.logs :as logs]
            [restapi.testbeds :as restapi]
            [gui.testbeds.modal :as modal]
            [gui.testbeds.newtestbed :as panel-new-testbed]))


;; FUNCTION: get-sel-testbed-info
(defn- get-sel-testbed-info ""
  [id-testbed]
  (let [res (VARS/get-testbed-by-id id-testbed)]
    (when-not (nil? res)
      (do
        (re-frame/dispatch [::events/set-selected-cluster-name (res :name)])
        ;(str " / " (res :id))
        ""))))


;; FUNCTION: get-total-nodes
(defn- get-total-nodes ""
  [id-testbed]
  (let [res (VARS/get-testbed-by-id id-testbed)]
    (if-not (nil? res)
      (- (count (res :nodes)) 1)
      "-")))


;; FUNCTION: get-node-name
(defn- get-node-name ""
  [n]
  (if (nil? n)
    "-"
    (str (n :name))))


;; FUCNCTION: reload-info
(defn- reload-info ""
  []
  (do

    (graphs/gen-data-clusters)
    (graphs/gen-data-nodes)
    (re-frame/dispatch [::events/set-selected-cluster nil])
    (re-frame/dispatch [::events/set-selected-cluster-name "-"])
    (re-frame/dispatch [::events/set-selected-node-name nil])))


;; FUCNCTION: after-add-testbed
(defn- after-add-testbed ""
  []
  (re-frame/dispatch [::events/set-total-testbeds 0]) ; forces panel to reload
  (restapi/get-testbeds VARS/update-testbeds)
  (reload-info))


;; FUCNCTION: after-del-testbed
(defn- after-del-testbed ""
  []
  (re-frame/dispatch [::events/set-total-testbeds 0]) ; forces panel to reload
  (restapi/get-testbeds VARS/update-testbeds)
  (reload-info))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; WEB CONTENT
;; FUNCTION:
(defn- panel-component []
  (let [id-testbed    (re-frame/subscribe [::subs/selected-cluster])
        na-testbed    (re-frame/subscribe [::subs/selected-cluster-name])
        sel-node      (re-frame/subscribe [::subs/selected-cluster-node])
        sel-node-name (re-frame/subscribe [::subs/selected-node-name])
        totaltestbeds (re-frame/subscribe [::subs/total-testbeds])]
    (fn []
      [:div {:style {:width "auto"}}
        [modal/modal]

        ;; 'HEADER'
        [:div.row
          [:div.col-sm-4
            [:h5 {:style {:margin-top "-5px"}}
              [:span.badge.badge-pill.badge-primary
                {:style {:color "#ffff99"}} "Testbeds: " (str @totaltestbeds)]
              " "
              [:span.badge.badge-pill.badge-secondary
                {:style {:color "#ffff99"}} (str @na-testbed " " (get-sel-testbed-info @id-testbed))]]]
          [:div.col-sm-8 (when (or (= @na-testbed "-") (nil? @na-testbed)) {:style {:display "none"}})
            [:h5 {:style {:margin-top "-5px"}}
              [:span.badge.badge-pill.badge-primary {:style {:color "#ffff99"}} "Total nodes: " (str (get-total-nodes @id-testbed))]
              " "
              [:span.badge.badge-pill.badge-secondary {:style {:color "#ffff99"}} " Selected node: " (get-node-name @sel-node) ]]]]

        ;; TESTBEDS
        [:div.row
          [:div.col-sm-4 {:id "clusters-graph-parent" :style {:width "auto"}}
            [:div {:id "clusters-graph"
                   :style {:width "auto" :height "300px" :border "1px solid lightgray" :background-color "#555555" :color "white"}}]]
          ;; NODES
          [:div.col-sm-8
          (when (or (= @na-testbed "-") (nil? @na-testbed)) {:style {:display "none"}})
            [:div {:id "nodes-graph-parent" :style {:width "auto"}}
              [:div {:id "nodes-graph"
                     :style {:width "auto" :height "300px" :border "1px solid lightgray" :background-color "#666666"}}]]]]

        ;; 'FOOTER' (TESTBEDS and NODEs)
        [:div.row
          [:div.col-sm-4
            [:h5 {:style {:margin-top "5px"}}
              ;; add new testbed TODO
              [:button.badge.badge-pill.btn-sm.btn-success {:title "add new testbed"
                ;:data-toggle "collapse" :data-target "#collapseNew3" :aria-expanded "false" :aria-controls "collapseNew3"
                :on-click #(re-frame/dispatch [:modal {:show? true
                                               :child [panel-new-testbed/panel]
                                               :size :large}])} "new testbed"]
              ;; delete testbed TODO
              (when-not (or (= @na-testbed "-") (nil? @na-testbed))
                [:button.badge.badge-pill.btn-sm.btn-danger {:data-toggle "tooltip" :data-placement "bottom"
                  :title "delete selected testbed"
                  :on-click #(when (.confirm js/window (str "Are you sure you want to delete the testbed [" @na-testbed "]?"))
                                (restapi/rem-testbed @id-testbed after-del-testbed))} (str "remove '" @na-testbed "'")])]]
          [:div.col-sm-8
            (when (or (= @na-testbed "-") (nil? @na-testbed)) {:style {:display "none"}})
              [:h5 {:style {:margin-top "5px"}}
                (when-not (nil? @sel-node-name)
                  ;; view selected node TODO
                  [:button.badge.badge-pill.btn-sm.btn-primary {:title "view selected node" :on-click #(graphs/show-selected-node-info)}
                    (str "view " @sel-node-name)])
              ]]]


        ;; NODE: cpus, gpus, memories
        [:div
          (if (nil? @sel-node-name) {:style {:display "none"}} {})
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
                   :style {:width "auto" :height "600px" :border "1px solid lightgray" :background-color "#777777"
                           :margin-top "5px"}}]]]])))


;; FUNCTION:
(defn panel [l-testbeds]
  (reagent/create-class  ;; <-- expects a map of functions
    {;; Mounting
     :component-will-mount      #(logs/info "GRAPHS: component-will-mount")
     :component-did-mount       #(do
                                  (graphs/gen-data-clusters)
                                  (graphs/gen-data-nodes)
                                  (re-frame/dispatch [::events/set-selected-cluster nil])
                                  (re-frame/dispatch [::events/set-selected-cluster-name "-"])
                                  (re-frame/dispatch [::events/set-selected-node-name nil])
                                  (logs/info "GRAPHS: component-did-mount"))
     ;; Updating
     ;;:component-will-update   #(logs/info "GRAPHS: component-will-update")
     ;;:component-did-update    #(logs/info "GRAPHS: component-did-update")
     ;; Unmounting
     ;:component-will-unmount  #(logs/info "GRAPHS: component-will-unmount")
     ;; Class Properties
     ;:display-name            "my-component"  ;; for more helpful warnings & errors
     ;; render
     :reagent-render           #(panel-component)}))
