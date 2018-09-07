;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.graphs
  (:require [cljsjs.vis]
            [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.subs :as subs]
            [gui.globals :as VARS]
            [utils.common :as utils.common]
            [utils.logs :as logs]
            [gui.apps.modal :as modal]
            [clojure.string :as str]))


;; APPLICATIONS GRAPH
(def ALDE_APPS_GRAPH (atom []))

;; GRAPH OPTIONS:
;; Cluster's nodes graph
(def GRAPH-OPTIONS-APPS (js-obj  "interaction"    (js-obj "hover" true
                                                          "navigationButtons" true)
                                  "nodes"         (js-obj "font"  (js-obj "bold"          (js-obj "color" "#0077aa")
                                                                          "strokeWidth"   2
                                                                          "strokeColor"   "#FFFFCC"))
                                  "physics"       (js-obj "enabled"     true
                                                          "barnesHut"   (js-obj "gravitationalConstant"   -2000
                                                                                "centralGravity"          0.5))
                                  "edges"         (js-obj "width" 1 "shadow" true)
                                  "layout"        (js-obj "randomSeed" 4)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; FUNCTION:
(defn- get-app-by-id ""
  [id-app]
  (first (filter #(= (% :id) id-app) @VARS/ALDE_APPS)))


;; FUNCTION:
(defn- check-exec-id ""
  [v-execs id-exec]
  (let [res (filter #(= (str "exec_" (% :id)) id-exec) v-execs)]
    (if (> (count res) 0)
      true
      false)))


;; FUNCTION: gen-nodes-testbeds
(defn- gen-nodes-testbeds "Create testbeds elements"
  []
  (into []
    (for [v @VARS/ALDE_TESTBEDS]
      {:id    (str "testbed_" (v :id))
       :name  (v :name)
       :type  "testbed"})))


;; FUNCTION: gen-nodes-sub-graph-exec
(defn- gen-nodes-sub-graph-exec ""
  [v]
  (into []
    (apply concat
      (for [m-app v]
        (for [m (m-app :executables)]
          {:id (str "exec_" (m :id)) :idalde (m :id) :idparent (m :application_id)
           :name (str "exec_" (m :id)) :compilation_script (m :compilation_script)
           :type "exec"})))))


;; FUNCTION: gen-nodes-sub-graph-conf
(defn- gen-nodes-sub-graph-conf ""
  [v]
  (into []
    (apply concat
      (for [m-app v]
        (for [m (m-app :execution_configurations)]
          {:id (str "conf_" (m :id)) :idalde (m :id) :idparent (m :application_id)
           :executable_id (str "exec_" (m :executable_id)) ;(m :executable_id)
           :name (str "conf_" (m :id)) :execution_type (m :execution_type)
           :type "conf" :testbed_id (str "testbed_" (m :testbed_id))})))))


;; FUNCTION: gen-apps-graph-structure
;;    {
;;       "executables": [],
;;       "execution_configurations": [],
;;       "id": 1,
;;       "name": "test_add_resource"
;;    }
(defn gen-apps-graph-structure "Generates the graph for the applications: ALDE_APPS_GRAPH"
  [v]
  ;; 1. create main ':nodes' element in each node for the graph
  (let [v-main-nodes  (into []
                        (for [m v]
                          {:id (m :id) :idalde (m :id) :name (m :name) :type "main"
                           :executables (count (m :executables))
                           :execution_configurations (count (m :execution_configurations))}))
        res           {:nodes v-main-nodes}
        ; edges for main nodes
        res           (assoc res :edges (into []
                                          (for [x (res :nodes)]
                                            {:from 0 :to (x :id)})))
        ;alde node
        res           (update-in res [:nodes] conj {:id 0 :name "ALDE" :type "alde"})
        ; executables
        res-nodes-tmp {:nodes (gen-nodes-sub-graph-exec v)}
        res-edges-tmp {:edges (into []
                                (for [x (res-nodes-tmp :nodes)]
                                  {:from (x :id) :to (x :idparent)}))}
        ; configurations
        res-nodes-tmp2 {:nodes (gen-nodes-sub-graph-conf v)}
        res-edges-tmp2 {:edges (into []
                                (remove nil?
                                  (into []
                                    (for [x (res-nodes-tmp2 :nodes)]
                                      (when-not (check-exec-id ((get-app-by-id (x :idparent)) :executables) (x :executable_id))
                                        {:from (x :id) :to (x :idparent) :connected false})))))}      ; edges: conf to application
        res-edges-tmp3 {:edges (into []
                                (for [x (res-nodes-tmp2 :nodes)]
                                  {:from (x :id) :to (x :executable_id)}))} ; edges: conf to exec

        res-final     {:edges (concat (res :edges) (res-edges-tmp :edges) (res-edges-tmp2 :edges) (res-edges-tmp3 :edges))
                       :nodes (concat (res :nodes) (res-nodes-tmp :nodes) (res-nodes-tmp2 :nodes))}
        ]
    res-final))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; APPS:

;; FUNCTION: get-node-info
(defn- get-node-info ""
  [id]
  (let [id-testbed  (re-frame/subscribe [::subs/selected-cluster])
        res         (filter #(= (% :id) @id-testbed) @VARS/ALDE_TESTBEDS)]
      (if-not (and (nil? res) (> (count res) 0))
          (let [testbed-info  (first res)
                node-res      (filter #(= (str (% :id)) id) (testbed-info :nodes))]
            (first node-res))
          nil)))


;; FUNCTION: show-app-info
(defn- show-app-info ""
  [id]
  (let [app (VARS/get-app-by-id id)]
    (reset! modal/TXT_APP app)
    (re-frame/dispatch [:modal {:show? true
                                :child [modal/application]
                                :size :large}])))

;; FUNCTION: show-exec-info
(defn- show-exec-info ""
  [m-info]
  (reset! modal/TXT_EXEC m-info)
  (re-frame/dispatch [:modal {:show? true
                              :child [modal/executable]
                              :size :large}]))

;; FUNCTION: show-conf-info
(defn- show-conf-info ""
  [m-info]
  (reset! modal/TXT_CONF m-info)
  (re-frame/dispatch [:modal {:show? true
                              :child [modal/conf]
                              :size :large}]))

;; FUNCTION: on-double-click-node
(defn- on-double-click-node "click event for graph nodes"
  [params]
  (try
    (let [node-sel  (first (goog.object/getValueByKeys params #js ["nodes"]))]
      (when-not (nil? node-sel)
        ; node-sel = conf_*, exec_*, *
        (if (str/starts-with? node-sel "conf_")
          (show-conf-info (VARS/get-conf-by-id (js/parseInt (subs node-sel 5))))
          (if (str/starts-with? node-sel "exec_")
            (show-exec-info (VARS/get-exec-by-id (js/parseInt (subs node-sel 5))))
            (show-app-info (js/parseInt node-sel))))))
    (catch js/Error e
      (logs/error e))))


;; FUNCTION: gen-nodes
(defn- gen-nodes "generate nodes"
  [c]
  (if (> (count (c :nodes)) 0)
    (let [n (array)]
      (doseq [x (c :nodes)]
        (let [v1 (str (x :id))
              v2 (x :name)]
          (try
            (if-not (nil? (x :type))
              (case (x :type)
                "testbed" (.push n (js-obj  "id"      (x :id)
                                            "label"   (x :name)
                                            "font"    (js-obj "multi" true)
                                            "title"   (str "<div><b>TESTBED:</b> " (x :name) "</div>")
                                            "image"   "images/cluster.png"
                                            "shape"   "image"))
                "alde"  (.push n (js-obj  "id"      (str (x :id))
                                          ;"label"   (x :name)
                                          "title"   (str "<div><b>Application Lifecycle Deployment Engine</b></div>")
                                          "image"   "images/tango_mini.png"
                                          "shape"   "image"))
                "main"  (.push n (js-obj  "id"      (str (x :id))
                                          "font"    (js-obj "multi" true)
                                          "label"   (str "<b>" (x :name) "</b>")
                                          "title"   (str "<div><b>" (x :name) "</b> <i>(application)</i></div>")
                                          "image"   "images/apps_mini.png"
                                          "shape"   "image"))
                "exec"  (.push n (js-obj  "id"      (str (x :id))
                                          "font"    (js-obj "multi" true)
                                          "label"   (str "<b>" (x :name) "</b>")
                                          "title"   (str "<div>Executable: <b>" (x :name) "</b></div>")
                                          "image"   "images/executable.png"
                                          "shape"   "image"))
                "conf"  (.push n (js-obj  "id"      (str (x :id))
                                          "font"    (js-obj "multi" true)
                                          "label"   (str "<b>" (x :name) "</b>")
                                          "title"   (str "<div>Execution configuration: <b>" (x :name) "</b></div>")
                                          "image"   "images/configuration.png"
                                          "shape"   "image")))
                (.push n (js-obj  "id"      (str (x :id))
                                  "label"   (str (x :name) " (default)")
                                  "title"   (str "<div><h6>ID: " (str (x :id)) "</h6><h6>DEFAULT: " (x :name) "</h6></div>")
                                  "image"   "images/apps_mini.png"
                                  "shape"   "image")))
            (catch js/Error e
              (.push n (js-obj  "id"      (str (x :id))
                                "label"   (x :name)
                                "title"   (str "<div><h6>ID: " (str (x :id)) "</h6><h6>NAME: " (x :name) "</h6></div>")
                                "image"   (if (= (x :name) "provider") "images/provider.png" "images/apps_mini.png")
                                "shape"   "image"))))
            ))
      n)
      (array)))


;; FUNCTION: gen-edges
(defn- gen-edges "generate edges"
  [c]
  (let [n (array)]
    (doseq [x (c :edges)]
      (if (nil? (x :connected))
        (.push n (js-obj  "from"    (str (x :from))
                          "to"      (x :to)
                          "dashes"  false
                          "color"   "#000000"))
        (.push n (js-obj  "from"    (str (x :from))
                          "to"      (x :to)
                          "dashes"  true
                          "title"   "no executable found!"
                          "color"   "#9CA8BF"))))
    n))


;; FUNCTION: on-click
(defn- on-click "on-click event"
  [params]
  (let [id-node  (first (goog.object/getValueByKeys params #js ["nodes"]))]
    (when-not (nil? id-node)
      (re-frame/dispatch [::events/set-selected-app-id id-node])
      ; id-node = conf_*, exec_*, *
      (if (str/starts-with? id-node "conf_")
        (do
          (re-frame/dispatch [::events/set-selected-app-type "conf"])
          (re-frame/dispatch [::events/set-selected-app (VARS/get-conf-by-id (js/parseInt (subs id-node 5)))]))
        (if (str/starts-with? id-node "exec_")
          (do
            (re-frame/dispatch [::events/set-selected-app-type "exec"])
            (re-frame/dispatch [::events/set-selected-app (VARS/get-exec-by-id (js/parseInt (subs id-node 5)))]))
          (do
            (re-frame/dispatch [::events/set-selected-app (VARS/get-app-by-id (js/parseInt id-node))])
            (re-frame/dispatch [::events/set-selected-app-type "app"])))))))


;; FUNCTION: gen-data-nodes
(defn gen-data-nodes []
  (let [grph-str    (gen-apps-graph-structure @VARS/ALDE_APPS)
        nodes       (gen-nodes grph-str)    ;; nodes
        edges       (gen-edges grph-str)    ;; edges
        grph    (js/vis.Network. (.getElementById js/document "apps-graph")
                                 (js-obj "nodes" nodes "edges" edges)
                                 GRAPH-OPTIONS-APPS)]
    ;; on-click event:
    (.on grph "click" #(on-click %))
    (.on grph "doubleClick" #(on-double-click-node %))))
