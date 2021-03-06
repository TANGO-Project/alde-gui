;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.testbeds.graphs
  (:require [cljsjs.vis]
            [re-frame.core :as re-frame]
            [gui.events :as events]
            [gui.subs :as subs]
            [gui.globals :as VARS]
            [utils.common :as utils.common]
            [utils.logs :as logs]
            [gui.testbeds.modal :as modal]))


;; id (int) of the selected node
(def SELECTED-NODE (atom nil))

;; GRAPH OPTIONS:
;; Testbeds graph
(def GRAPH-OPTIONS (js-obj  "interaction"   (js-obj "hover" true)
                            "nodes"         (js-obj "font"  (js-obj "bold"          (js-obj   "color" "#ffffff")
                                                                    "size" 14
                                                                    "color"          "white" ;(js-obj  "border" "#222222"
                                                                                              ;"background" "#666666")
                                                                    "strokeWidth"   0
                                                                    "strokeColor"   "white"))
                            "physics"       (js-obj "enabled"     true
                                                    "barnesHut"   (js-obj "gravitationalConstant"   -12000
                                                                          "centralGravity"          0.7))
                            "edges"         (js-obj "width"   1
                                                    "shadow"  true)
                            "layout"        (js-obj "randomSeed"    2
                                                    )))
;"hierarchical"  (js-obj "direction"     "LR" ;"UD"
;                        "sortMethod"    "directed")

;; Testbed's nodes graph
(def GRAPH-OPTIONS-NODES (js-obj  "interaction"   (js-obj "hover" true
                                                          "navigationButtons" true)
                                  "nodes"         (js-obj "font"  (js-obj "bold"          (js-obj "color" "#ffff99")
                                                                          "size"      13
                                                                          "strokeWidth"   0
                                                                          "color"         "white"
                                                                          "strokeColor"   "#FFFFCC"))
                                  "physics"       (js-obj "enabled"     true
                                                          "barnesHut"   (js-obj "gravitationalConstant"   -1600
                                                                                "centralGravity"          0.5))
                                  "edges"         (js-obj "width"   1
                                                          "shadow"  true)
                                  "layout"        (js-obj "randomSeed" 4)))

;; Node graph
(def GRAPH-OPTIONS-1NODE (js-obj  "interaction"   (js-obj "hover" true
                                                          "navigationButtons" true)
                                  "nodes"         (js-obj "font"  (js-obj "bold"      (js-obj "color" "#ffffbb")
                                                                          "size"      12
                                                                          "color"     "#bbbbbb"))
                                  "physics"       (js-obj "enabled"     true
                                                          "barnesHut"   (js-obj "gravitationalConstant"   -1600
                                                                                "centralGravity"          0.5
                                                                                "springLength"            250))
                                  "edges"         (js-obj "width"   1
                                                          "shadow"  true)
                                  "layout"        (js-obj "randomSeed" 4)))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; 1NODE:
;; FUNCTION: show-1node-element-info
(defn- show-1node-element-info ""
  [n]
  (reset! modal/TXT_NODE_ELEM n)
  (when-not (nil? n)
    (case (n :ntype)
      "cpu" (re-frame/dispatch [:modal {:show? true
                                  :child [modal/node_elem_cpu]
                                  :size :large}])
      "gpu" (re-frame/dispatch [:modal {:show? true
                                  :child [modal/node_elem_gpu]
                                  :size :large}])
      "mem" (re-frame/dispatch [:modal {:show? true
                                  :child [modal/node_elem_mem]
                                  :size :large}])
      "default" (logs/debug (n :ntype)))))


;; FUNCTION: gen-nodes-1node
(defn- gen-nodes-1node "generate nodes"
  [c]
  (if (> (count (c :nodes)) 0)
    (let [n (array)]
      (doseq [x (c :nodes)]
        (try
          (if-not (nil? (x :type))
            (case (x :type)
              "node"    (.push n (js-obj  "id"      (str (x :id))
                                "font"    (js-obj "multi" true)
                                "label"   (str "<b>" (x :name) "</b>")
                                "title"   (str "<div><b>NODE:</b> " (x :name) "</div>")
                                "image"   "images/node.png"
                                "color"   (js-obj "background" "white"
                                                  "border" "black"
                                                  "highlight" (js-obj "background" "#F3F781"
                                                                      "border" "black")
                                                  "hover" (js-obj "background" "#ffffcc"
                                                                  "border" "black"))
                                "shape"   "circularImage")) ; image
              "cpu"     (.push n (js-obj  "id"      (str (x :id))
                                "font"    (js-obj "multi" true)
                                "label"   (str "<b>" (x :name) "</b>\n"
                                            (x :arch) "\n"
                                            (x :cores) " (cores)\n"
                                            (x :vendor_id))
                                "title"   (str "<div><b>CPU:</b> " (x :name) "</div>")
                                "image"   "images/cpu.png"
                                "color"   (js-obj "background" "white"
                                                  "border" "black"
                                                  "highlight" (js-obj "background" "#F3F781"
                                                                      "border" "black")
                                                  "hover" (js-obj "background" "#ffffcc"
                                                                  "border" "black"))
                                "shape"   "circularImage"))
              "gpu"     (.push n (js-obj  "id"      (str (x :id))
                                "font"    (js-obj "multi" true)
                                "label"   (str "<b>" (x :name) "</b>\n"
                                            (x :model_name) "\n"
                                            (x :vendor_id))
                                "title"   (str "<div><b>GPU:</b> " (x :name) "</div>")
                                "image"   "images/gpu.png"
                                "color"   (js-obj "background" "white"
                                                  "border" "black"
                                                  "highlight" (js-obj "background" "#F3F781"
                                                                      "border" "black")
                                                  "hover" (js-obj "background" "#ffffcc"
                                                                  "border" "black"))
                                "shape"   "circularImage"))
              "mem"     (.push n (js-obj  "id"      (str (x :id))
                                "font"    (js-obj "multi" true)
                                "label"   (str "<b>" (x :name) "</b>\n"
                                            (x :size) "\n"
                                            (x :units))
                                "title"   (str "<div><b>MEMORY:</b> " (x :name) "</div>")
                                "image"   "images/mem.png"
                                "color"   (js-obj "background" "white"
                                                  "border" "black"
                                                  "highlight" (js-obj "background" "#F3F781"
                                                                      "border" "black")
                                                  "hover" (js-obj "background" "#ffffcc"
                                                                  "border" "black"))
                                "shape"   "circularImage"))
              (.push n (js-obj  "id"      (str (x :id))
                                "label"   (str (x :name) " (default)")
                                "title"   (str "<div><h6>ID: " (str (x :id)) "</h6><h6>DEFAULT: " (x :name) "</h6></div>")
                                "image"   "images/cluster.png"
                                "shape"   "circularImage")))
            ; if nil
            (.push n (js-obj  "id"      (str (x :id))
                              "label"   (str (x :name) " (default)")
                              "title"   (str "<div><h6>ID: " (str (x :id)) "</h6><h6>DEFAULT: " (x :name) "</h6></div>")
                              "image"   "images/cluster.png"
                              "shape"   "circularImage")))
          (catch js/Error e
            (.push n (js-obj  "id"      (str (x :id))
                              "label"   (x :name)
                              "title"   (str "<div><h6>ID: " (str (x :id)) "</h6><h6>NAME: " (x :name) "</h6></div>")
                              "image"   "images/node.png"
                              "shape"   "image")))))
      n)
      (array)))


;; FUNCTION: gen-edges-1node
(defn- gen-edges-1node "generate edges"
  [c]
  (let [n (array)]
    (doseq [x (c :edges)]
      (.push n (js-obj  "from"    (str (x :from))
                        "to"      (x :to)
                        "dashes"  "true"
                        "color"   "#eeeeee")))
    n))


;; FUNCTION: on-click-cluster
(defn- on-2click-testbed-1node "on-click event for node"
  [params]
  (let [id-nodeelem (first (aget params "nodes"))
        id-selnode  @SELECTED-NODE]
    (when-not (nil? id-nodeelem)
      (show-1node-element-info (VARS/get-node-elem id-selnode id-nodeelem)))))


;; FUNCTION: gen-data-1node
(defn gen-data-1node [node-sel]
  (let [node1   (VARS/get-node-by-id node-sel)
        nodes   (gen-nodes-1node node1)  ;; nodes
        edges   (gen-edges-1node node1)  ;; edges
        grph    (js/vis.Network. (.getElementById js/document "node1-graph")
                                 (js-obj "nodes" nodes "edges" edges)
                                 GRAPH-OPTIONS-1NODE)]
    (.on grph "doubleClick" #(on-2click-testbed-1node %)))) ;; on-click event:


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; NODES:

;; FUNCTION: get-node-info
(defn- get-node-info ""
  [id-node]
  (when-not (nil? id-node)
    (VARS/get-node-by-id id-node)))

;; FUNCTION: show-testbed-info
(defn- show-testbed-info ""
  []
  (let [id-testbed  (re-frame/subscribe [::subs/selected-cluster])]
    (when-not (nil? @id-testbed)
      (let [res (filter #(= (% :id) @id-testbed) @VARS/ALDE_TESTBEDS)]
        (when-not (and (nil? res) (> (count res) 0))
          (let [info-testbed (apply dissoc (first res) [:on_line :edges])]
            (logs/info "show-testbed-info-> " (type info-testbed))
            (reset! modal/TXT info-testbed))
          (re-frame/dispatch [:modal {:show? true
                                      :child [modal/testbed]
                                      :size :large}]))))))

;; FUNCTION: show-node-info
(defn- show-node-info ""
  [n]
  (reset! modal/TXT_NODE n)
  (re-frame/dispatch [:modal {:show? true
                              :child [modal/node]
                              :size :large}]))


;; FUNCTION: show-selected-node-info
(defn show-selected-node-info ""
  []
  (when-not (nil? @SELECTED-NODE)
    (let [n (get-node-info @SELECTED-NODE)]
      (reset! modal/TXT_NODE n)
      (re-frame/dispatch [:modal {:show? true
                                  :child [modal/node]
                                  :size :large}]))))


;; FUNCTION: on-click-node
(defn- on-click-node "click event for nodes"
  [params]
  (try
    (let [node-sel  (first (goog.object/getValueByKeys params #js ["nodes"]))
          node-info (get-node-info node-sel)]
      (if-not (or (nil? node-info) (= node-sel "0"))
        (do
          (re-frame/dispatch [::events/set-selected-cluster-node node-info])
          (re-frame/dispatch [::events/set-selected-node-name (node-info :name)])
          (reset! SELECTED-NODE node-sel)
          ; refresh / load cluster
          (gen-data-1node node-sel))
        (do
          (reset! SELECTED-NODE nil)
          (re-frame/dispatch [::events/set-selected-node-name nil]))))
    (catch js/Error e
      (logs/error e))))


;; FUNCTION: on-double-click-node
(defn- on-double-click-node "double click event for nodes"
  [params]
  (try
    (let [node-sel  (first (goog.object/getValueByKeys params #js ["nodes"]))
          node-info (get-node-info node-sel)]
      (when (= node-sel "0")
        (show-testbed-info)))
    (catch js/Error e
      (logs/error e))))


;; FUNCTION: gen-nodes
(defn- gen-nodes "generate nodes"
  [c]
  (if (and (not (nil? c)) (> (count (c :nodes)) 0))
    (let [n (array)]
      (doseq [x (c :nodes)]
        (try
          (if-not (nil? (x :type))
            ;; TESTBED
            (.push n (js-obj  "id"      (str (x :id))
                              "label"   (str (x :name) " (testbed)")
                              "title"   (str "<div><b>TESTBED:</b> " (x :name) "</div>")
                              "image"   "images/cluster.png"
                              "color"  (js-obj  "background" "white"
                                                "border" "black"
                                                "highlight" (js-obj "background" "#F3F781"
                                                                    "border" "black")
                                                "hover" (js-obj "background" "#ffffcc"
                                                                "border" "black"))
                              "shape"   "circularImage"))
            ;; NODE
            (let [node-info (get-node-info  (x :id))]
              (.push n (js-obj  "id"      (str (x :id))
                                "font"    (js-obj "multi" true)
                                "label"   (str "<b>" (x :name) "</b>\n"
                                            "cpus:" (count (node-info :cpus)) "\n"
                                            "gpus:" (count (node-info :gpus)))
                                "title"   (str "<div><b>NODE:</b> " (x :name)
                                               "<br/> <b>ID:</b> " (str (x :id))
                                               "<br/>" (x :state) "</div>")
                                "image"   "images/node.png"

                                "color"   (js-obj  "background"
                                                      (if (or (nil? (x :state)) (clojure.string/blank? (x :state)) (= (x :state) "IDLE"))
                                                        "#FFFFFF"
                                                        (if (= (x :state) "ALLOCATED")
                                                          "#A9BCF5"
                                                          "#FAAC58"))
                                                   "border" "black"
                                                   "highlight" (js-obj "background" "#F3F781"
                                                                      "border" "black")
                                                   "hover" (js-obj "background" "#ffffcc"
                                                                  "border" "black"))

                                "shape"   "circularImage"))))
          (catch js/Error e
            (.push n (js-obj  "id"      (str (x :id))
                              "label"   (x :name)
                              "title"   (str "<div><h6>ID: " (str (x :id)) "</h6><h6>NAME: " (x :name) "</h6></div>")
                              "image"   "images/node.png"
                              "shape"   "circularImage")))))
      n)
      (array)))


;; FUNCTION: gen-edges
(defn- gen-edges "generate edges"
  [c]
  (if (and (not (nil? c)) (> (count (c :edges)) 0))
    (let [n (array)]
      (doseq [x (c :edges)]
        (.push n (js-obj  "from"    (str (x :from))
                          "to"      (x :to)
                          "dashes"  true
                          "color"   "#eeeeee")))
      n)
    (array)))


;; FUNCTION: gen-data-nodes-cluster
(defn- gen-data-nodes-cluster "generate graph"
  [c]
  (let [nodes   (gen-nodes c)   ;; nodes
        edges   (gen-edges c)         ;; edges   (array)
        grph    (js/vis.Network. (.getElementById js/document "nodes-graph")
                                 (js-obj "nodes" nodes "edges" edges)
                                 GRAPH-OPTIONS-NODES)]
    ;; on-click event:
    (.on grph "click" #(on-click-node %))
    (.on grph "doubleClick" #(on-double-click-node %))))


;; FUNCTION: fnodes-ini
(defn- fnodes-ini "Create nodes elements"
  []
  (let [cluster-sel (re-frame/subscribe [::subs/selected-cluster])]
    (if (nil? @cluster-sel)
      ;; no cluster selected
      (if (> (count @VARS/ALDE_TESTBEDS) 0)
        (let [v (first @VARS/ALDE_TESTBEDS)]
          (gen-nodes v))
        (array))
      ;; 1 cluster selected:
      (do
        (let [v (first (filter #(= (% :id) @cluster-sel) @VARS/ALDE_TESTBEDS))]
          (gen-nodes v))))))


;; FUNCTION: fedges-ini
(defn- fedges-ini "Create edges elements"
  []
  (let [cluster-sel (re-frame/subscribe [::subs/selected-cluster])]
    (if (nil? @cluster-sel)
      ;; no cluster selected
      (if (> (count @VARS/ALDE_TESTBEDS) 0)
        (let [v (first @VARS/ALDE_TESTBEDS)]
          (gen-edges v))
        (array))
      ;; 1 cluster selected
      (let [v (first (filter #(= (% :id) @cluster-sel) @VARS/ALDE_TESTBEDS))]
        (gen-edges v)))))


;; FUNCTION: gen-data-nodes
(defn gen-data-nodes []
  (let [nodes   (fnodes-ini)    ;; nodes
        edges   (fedges-ini)         ;; edges (array)
        grph    (js/vis.Network. (.getElementById js/document "nodes-graph")
                                 (js-obj "nodes" nodes "edges" edges)
                                 GRAPH-OPTIONS-NODES)]
    ;; on-click event:
    (.on grph "click" #(on-click-node %))
    (.on grph "doubleClick" #(on-double-click-node %))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; TEST:
;; FUNCTION: fnodes-clusters
(defn- fnodes-clusters "Create nodes elements"
  []
  (let [n (array)]
    (doseq [v @VARS/ALDE_TESTBEDS]
      (.push n (js-obj  "id"      (v :id)
                        "label"   (v :name)
                        "font"    (js-obj "multi" true)
                        "title"   (str "<div><b>TESTBED:</b> " (v :name) "</div>")
                        "image"   "images/cluster.png"
                        "color"  (js-obj  "background" "white"
                                          "border" "black"
                                          "highlight" (js-obj "background" "#F3F781"
                                                              "border" "black")
                                          "hover" (js-obj "background" "#ffffcc"
                                                          "border" "black"))
                        "shape"   "circularImage")))
    n))


;; FUNCTION: on-click-cluster
(defn- on-click-testbed "on-click event for clusters"
  [params]
  (let [id-cluster (first (aget params "nodes"))]
    (when-not (nil? id-cluster)
      (do
        ; add an Event to the queue / dispatch event
        (re-frame/dispatch [::events/set-selected-cluster id-cluster])
        (re-frame/dispatch [::events/set-selected-cluster-node {}])
        ; refresh / load cluster
        (gen-data-nodes-cluster (first (filter #(= (% :id) id-cluster) @VARS/ALDE_TESTBEDS)))))))


;; FUNCTION: gen-data-clusters
(defn gen-data-clusters []
  (let [nodes   (fnodes-clusters) ;; nodes
        edges   (array)           ;; edges
        grph    (js/vis.Network. (.getElementById js/document "clusters-graph")
                                 (js-obj "nodes" nodes "edges" edges)
                                 GRAPH-OPTIONS)]
    (.on grph "click" #(on-click-testbed %)))) ;; on-click event:
