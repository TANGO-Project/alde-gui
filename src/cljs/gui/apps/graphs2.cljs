;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.apps.graphs2
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [cljsjs.vis]
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
;; Applications graph
(def GRAPH-OPTIONS-APPS (js-obj  "interaction"    (js-obj "hover" true
                                                          "navigationButtons" true)
                                  "nodes"         (js-obj "font"  (js-obj "color" "#ffffff"
                                                                          "size"  16)
                                                          "size"  20)
                                  "physics"       (js-obj "enabled"     true
                                                          "barnesHut"   (js-obj "gravitationalConstant"   -2000
                                                                                "centralGravity"          0.5))
                                  "edges"         (js-obj "width" 1 "shadow" true)
                                  "layout"        (js-obj "randomSeed" 4)))

;; Conf / Exec graph
(def GRAPH-OPTIONS-APPS-3 (js-obj  "interaction"    (js-obj "hover" true
                                                          "navigationButtons" true)
                                  "nodes"         (js-obj "font"  (js-obj "color" "#ffffff"
                                                                          "size"  13)
                                                          "size"  15)
                                  "physics"       (js-obj "enabled"     true
                                                          "barnesHut"   (js-obj "gravitationalConstant"   -2500
                                                                                "centralGravity"          0.5
                                                                                "springConstant"          0.25
                                                                                "springLength"            85))
                                  "edges"         (js-obj "width" 1 "shadow" true)
                                  "layout"        (js-obj "randomSeed" 4)))

;; Conf / Exec graph
(def GRAPH-OPTIONS-APPS-4 (js-obj  "interaction"    (js-obj "hover" true
                                                          "navigationButtons" true)
                                  "nodes"         (js-obj "font"  (js-obj "color" "#ffffff"
                                                                          "size"  16)
                                                          "size"  20)
                                  "physics"       (js-obj "enabled"     true
                                                          "barnesHut"   (js-obj "gravitationalConstant"   -8000
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


;; FUNCTION: get-config-from-exec
(defn- get-config-from-exec
  [exec v-configurations]
  (let [result1 (filter #(and (= (% :idparent) (exec :idparent)) (= (% :executable_id) (exec :id))) (v-configurations :nodes))]
    result1))


;; FUNCTION: gen-edges-sub-graph-exec
(defn- gen-edges-sub-graph-exec ""
  [v-nodes v-configurations]
  (let [result (into []
                (remove nil?
                  (for [exec (v-nodes :nodes)]
                    (let [res (get-config-from-exec exec v-configurations)]
                      (when-not (empty? res)
                        {:from (exec :id) :to ((first res) :name)})))))]
    result))


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


;; FUNCTION: show-execution-info
(defn- show-execution-info ""
  [m-info]
  (reset! modal/TXT_EXECUTION m-info)
  (re-frame/dispatch [:modal {:show? true
                              :child [modal/execution]
                              :size :large}]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; EXECUTIONS GRAPH

;; FUNCTION: gen-executions-graph-structure
;;    {
;;          "children": [],
;;          "energy_output": null,
;;          "execution_configuration": null,
;;          "execution_configuration_id": null,
;;          "execution_type": "SINGULARITY:PM",
;;          "id": 6,
;;          "nodes": [],
;;          "output": null,
;;          "parent": null,
;;          "parent_id": null,
;;          "runtime_output": null,
;;          "slurm_sbatch_id": null,
;;          "status": "SUBMITTED"
;;    }
(defn gen-executions-graph-structure "Generates the graph for the applications: ALDE_APPS_GRAPH"
  [conf-id v]
  ;(logs/info "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
  ;(logs/info v)
  (let [cfg-exec             (VARS/get-conf-by-id (js/parseInt conf-id))
        v-executables-nodes  (into []
                              (for [m v]
                                {:id (m :id) :status (m :status) :energy_output (m :energy_output) :runtime_output (m :runtime_output) :type "execution" }))
        res           {:nodes v-executables-nodes}
        ; edges for main nodes
        res           (assoc res :edges (into []
                                          (for [x (res :nodes)]
                                            {:from (str "conf_" conf-id) :to (x :id)})))
        ; root node
        res           (update-in res [:nodes] conj {:id (str "conf_" conf-id) :name (str "conf_" conf-id) :type "conf"})
        ; res-final
        res-final     {:edges (res :edges)
                       :nodes (res :nodes)}
        ]
    res-final))


;; FUNCTION: gen-nodes-4
(defn- gen-nodes-4 "generate nodes for an exec"
  [c]
  (if (> (count (c :nodes)) 0)
    (let [n (array)]
      (doseq [x (c :nodes)]
        (let [v1 (str (x :id))
              v2 (x :name)]
          (try
            (if-not (nil? (x :type))
              (case (x :type)
                "alde"  (.push n (js-obj  "id"      (str (x :id))
                                          ;"label"   (x :name)
                                          "title"   (str "<div><b>Application Lifecycle Deployment Engine</b></div>")
                                          "image"   "images/tango_mini.png"
                                          "shape"   "image"))
                "conf"  (.push n (js-obj  "id"      (str (x :id))
                                          "font"    (js-obj "multi" true "color" "black")
                                          "label"   (str "<b>" (x :name) "</b>")
                                          "title"   (str "<div>Execution configuration: <b>" (x :name) "</b></div>")
                                          "image"   "images/configuration.png"
                                          "color"   (js-obj "background" "black"
                                                            "border" "black"
                                                            "highlight" (js-obj "background" "#F3F781"
                                                                                "border" "black")
                                                            "hover" (js-obj "background" "#ffffcc"
                                                                            "border" "black"))
                                         "shape"   "image"))
                "execution"  (.push n (js-obj  "id"      (str (x :id))
                                          "font"    (if (= (x :status) "FAILED")
                                                      (js-obj "multi" true "color" "darkred")
                                                      (if (= (x :status) "COMPLETED")
                                                        (js-obj "multi" true "color" "darkgreen")
                                                        (js-obj "multi" true "color" "blue")))
                                          "label"   (str "<b>" (x :id) " - " (x :status) "</b>")
                                          "title"   (str "<div><b>" (x :id) "</b> <i>(execution)</i></div>")
                                          "image"   "images/exe.png"
                                          "color"   (js-obj ;"background" "red"
                                                            "border" "black"
                                                            "highlight" (js-obj "background" "#F3F781"
                                                                                "border" "black")
                                                            "hover" (js-obj "background" "#ffffcc"
                                                                            "border" "black"))
                                          "shape"   "circularImage"))
                ))
            (catch js/Error e
              ;(.push n (js-obj  "id"      (str (x :id))
              ;                  "label"   (x :name)
              ;                  "title"   (str "<div><h6>ID: " (str (x :id)) "</h6><h6>NAME: " (x :name) "</h6></div>")
              ;                  "image"   (if (= (x :name) "provider") "images/provider.png" "images/apps_mini.png")
              ;                  "shape"   "image"))
              ))
            ))
      n)
      (array)))


;; FUNCTION: gen-edges-4
(defn- gen-edges-4 "generate edges for execs graph"
  [c]
  (let [n (array)]
    (doseq [x (c :edges)]
      (if (nil? (x :connected))
        (.push n (js-obj  "from"    (str (x :from))
                          "to"      (x :to)
                          "dashes"  false
                          "color"   "lightgray"))
        (.push n (js-obj  "from"    (str (x :from))
                          "to"      (x :to)
                          "dashes"  true
                          "title"   "no executable found!"
                          "color"   "#FF5733"))))
    n))


;; FUNCTION: on-double-click-node-4
(defn- on-double-click-node-4 "on-double-click event for execs"
  [params]
  (try
    (let [node-sel  (first (goog.object/getValueByKeys params #js ["nodes"]))]
      (when-not (and (nil? node-sel) (> node-sel 0))
          (show-execution-info (VARS/get-execution-by-id (js/parseInt node-sel)))))
    (catch js/Error e
      (logs/error e))))


;; FUNCTION: on-click-4
(defn- on-click-4 "on-click event for execs"
  [params]
  (let [id-node  (first (goog.object/getValueByKeys params #js ["nodes"]))]
    (if-not (nil? id-node)
      (if (str/starts-with? id-node "conf_")
        (do
          (re-frame/dispatch [::events/set-selected-execution-id "-"])
          (re-frame/dispatch [::events/set-selected-execution {}]))
        (do
          (re-frame/dispatch [::events/set-selected-execution-id id-node])
          (re-frame/dispatch [::events/set-selected-execution {}])))
          ;(re-frame/dispatch [::events/set-selected-execution (VARS/get-execution-by-id (js/parseInt id-node))])))
      (do
        (re-frame/dispatch [::events/set-selected-execution-id "-"])
        (re-frame/dispatch [::events/set-selected-execution {}])))))


;; FUNCTION: gen-executions-nodes
(defn gen-executions-nodes [conf-id]
  ;(logs/info "Loading executions for config: " conf-id)
  (reset! VARS/TAB_EXECUTIONS_LOADING true)
  ;; EXECUTIONS GRAPH
  (do
    (logs/info "Call to: " (str @VARS/REST_API_URL "execution_configurations/" conf-id))
    ;(go (let [response (<! (http/get "http://localhost:8081/mock-data/execution_configurations/x" {:with-credentials? false}))]
    (go (let [response (<! (http/get (str @VARS/REST_API_URL "execution_configurations/" conf-id) {:with-credentials? false}))]
          ;(logs/info "response: " (str response))
          (if (or (nil? response) (clojure.string/blank? response))
            (do
              (logs/error "Executions (from execs_config) content is nil!!!")
              (reset! VARS/TAB_EXECUTIONS_LOADING false))
            (do
              (reset! VARS/ALDE_CFG_EXECS (get-in response [:body :executions]))

              (re-frame/dispatch [::events/set-exec_cfg_execs_total (count @VARS/ALDE_CFG_EXECS)])

              (let [execs-completed (filter #(= (% :status) "COMPLETED") @VARS/ALDE_CFG_EXECS)]
                (re-frame/dispatch [::events/set-exec_cfg_execs_completed (count execs-completed)]))

              (let [execs-failed (filter #(= (% :status) "FAILED") @VARS/ALDE_CFG_EXECS)]
                (re-frame/dispatch [::events/set-exec_cfg_execs_failed (count execs-failed)]))

              (let [execs-running (filter #(= (% :status) "RUNNING") @VARS/ALDE_CFG_EXECS)]
                (re-frame/dispatch [::events/set-exec_cfg_execs_running (count execs-running)]))

              (let [execs-cancelled (filter #(= (% :status) "CANCELLED") @VARS/ALDE_CFG_EXECS)]
                (re-frame/dispatch [::events/set-exec_cfg_execs_cancelled (count execs-cancelled)]))

              (let [execs-restart (filter #(= (% :status) "RESTART") @VARS/ALDE_CFG_EXECS)]
                (re-frame/dispatch [::events/set-exec_cfg_execs_restart (count execs-restart)]))

              (let [execs-1 (filter #(= (% :status) "TIMEOUT") @VARS/ALDE_CFG_EXECS)]
                (re-frame/dispatch [::events/set-exec_cfg_execs_timeout (count execs-1)]))

              (let [execs-2 (filter #(= (% :status) "UNKNOWN") @VARS/ALDE_CFG_EXECS)]
                (re-frame/dispatch [::events/set-exec_cfg_execs_unknown (count execs-2)]))

              (let [grph-str    (gen-executions-graph-structure conf-id @VARS/ALDE_CFG_EXECS)
                    nodes       (gen-nodes-4 grph-str)    ;; nodes
                    edges       (gen-edges-4 grph-str)    ;; edges
                    grph        (js/vis.Network. (.getElementById js/document "apps-graph4")
                                                 (js-obj "nodes" nodes "edges" edges)
                                                 GRAPH-OPTIONS-APPS-4)]
                ;; on-click event:
                (.on grph "click" #(on-click-4 %))
                (.on grph "doubleClick" #(on-double-click-node-4 %)))
              (reset! VARS/TAB_EXECUTIONS_LOADING false)))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; APP / CONF / EXECS GRAPH

;; FUNCTION: gen-app-conf-execs-graph-structure
;;    {
;;       "executables": [],
;;       "execution_configurations": [],
;;       "id": 1,
;;       "name": "test_add_resource"
;;    }
(defn gen-app-conf-execs-graph-structure "Generates the graph for the applications: ALDE_APPS_GRAPH"
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


;; FUNCTION: gen-nodes-3
(defn- gen-nodes-3 "generate nodes for an application"
  [c]
  (if (> (count (c :nodes)) 0)
    (let [n (array)]
      (doseq [x (c :nodes)]
        (let [v1 (str (x :id))
              v2 (x :name)]
          (try
            (if-not (nil? (x :type))
              (case (x :type)
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
                                          "color"   (js-obj "background" "white"
                                                            "border" "black"
                                                            "highlight" (js-obj "background" "#F3F781"
                                                                                "border" "black")
                                                            "hover" (js-obj "background" "#ffffcc"
                                                                            "border" "black"))
                                          "shape"   "image"))
                "exec"  (.push n (js-obj  "id"      (str (x :id))
                                          "font"    (js-obj "multi" true)
                                          "label"   (str "<b>" (x :name) "</b>")
                                          "title"   (str "<div>Executable: <b>" (x :name) "</b></div>")
                                          "image"   "images/executable.png"
                                          "color"   (js-obj "background" "white"
                                                            "border" "black"
                                                            "highlight" (js-obj "background" "#F3F781"
                                                                                "border" "black")
                                                            "hover" (js-obj "background" "#ffffcc"
                                                                            "border" "black"))
                                          "shape"   "image"))
                "conf"  (.push n (js-obj  "id"      (str (x :id))
                                          "font"    (js-obj "multi" true)
                                          "label"   (str "<b>" (x :name) "</b>")
                                          "title"   (str "<div>Execution configuration: <b>" (x :name) "</b></div>")
                                          "image"   "images/configuration.png"
                                          "color"   (js-obj "background" "white"
                                                            "border" "black"
                                                            "highlight" (js-obj "background" "#F3F781"
                                                                                "border" "black")
                                                            "hover" (js-obj "background" "#ffffcc"
                                                                            "border" "black"))
                                         "shape"   "image"))))
            (catch js/Error e
              ;(.push n (js-obj  "id"      (str (x :id))
              ;                  "label"   (x :name)
              ;                  "title"   (str "<div><h6>ID: " (str (x :id)) "</h6><h6>NAME: " (x :name) "</h6></div>")
              ;                  "image"   (if (= (x :name) "provider") "images/provider.png" "images/apps_mini.png")
              ;                  "shape"   "image"))
              ))
            ))
      n)
      (array)))


;; FUNCTION: gen-edges-3
(defn- gen-edges-3 "generate edges for apps graph"
  [c]
  (let [n (array)]
    (doseq [x (c :edges)]
      (if (nil? (x :connected))
        (.push n (js-obj  "from"    (str (x :from))
                          "to"      (x :to)
                          "dashes"  false
                          "color"   "lightgray"))
        (.push n (js-obj  "from"    (str (x :from))
                          "to"      (x :to)
                          "dashes"  true
                          "title"   "no executable found!"
                          "color"   "#FF5733"))))
    n))


;; FUNCTION: on-double-click-node-3
(defn- on-double-click-node-3 "on-double-click event for apps"
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


;; FUNCTION: on-click-3
(defn- on-click-3 "on-click event for apps"
  [params]
  (let [id-node  (first (goog.object/getValueByKeys params #js ["nodes"]))]
    (re-frame/dispatch [::events/set-exec_cfg_execs_completed 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_failed 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_running 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_total 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_cancelled 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_restart 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_timeout 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_unknown 0])

    (if-not (nil? id-node)
      (if (str/starts-with? id-node "conf_")
        (do
          (re-frame/dispatch [::events/set-selected-exec-conf-id id-node])
          (re-frame/dispatch [::events/set-selected-exec-exec-id "-"])
          (re-frame/dispatch [::events/set-selected-execution-id "-"])
          (re-frame/dispatch [::events/set-selected-execution {}])
          (re-frame/dispatch [::events/set-selected-exec-app-type "conf"])
          (gen-executions-nodes (js/parseInt (subs id-node 5))))
        (if (str/starts-with? id-node "exec_")
          (do
            (re-frame/dispatch [::events/set-selected-exec-conf-id "-"])
            (re-frame/dispatch [::events/set-selected-execution-id "-"])
            (re-frame/dispatch [::events/set-selected-execution {}])
            (re-frame/dispatch [::events/set-selected-exec-exec-id id-node])
            (re-frame/dispatch [::events/set-selected-app-id id-node])
            (re-frame/dispatch [::events/set-selected-exec-app-type "exec"]))
          (do
            (re-frame/dispatch [::events/set-selected-exec-conf-id "-"])
            (re-frame/dispatch [::events/set-selected-execution-id "-"])
            (re-frame/dispatch [::events/set-selected-execution {}])
            (re-frame/dispatch [::events/set-selected-exec-exec-id "-"])
            (re-frame/dispatch [::events/set-selected-exec-app-type ""]))))
      (do
        (re-frame/dispatch [::events/set-selected-exec-conf-id "-"])
        (re-frame/dispatch [::events/set-selected-execution-id "-"])
        (re-frame/dispatch [::events/set-selected-execution {}])
        (re-frame/dispatch [::events/set-selected-exec-exec-id "-"])
        (re-frame/dispatch [::events/set-selected-exec-app-type ""])))))


;; FUNCTION: reload-executions
(defn reload-executions ""
  [id-conf-exe]
  (do
    (re-frame/dispatch [::events/set-exec_cfg_execs_completed 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_failed 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_running 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_total 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_cancelled 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_restart 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_timeout 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_unknown 0])

    (re-frame/dispatch [::events/set-selected-exec-conf-id id-conf-exe])
    (re-frame/dispatch [::events/set-selected-exec-exec-id "-"])
    (re-frame/dispatch [::events/set-selected-execution-id "-"])
    (re-frame/dispatch [::events/set-selected-execution {}])
    (re-frame/dispatch [::events/set-selected-exec-app-type "conf"])
    (gen-executions-nodes (js/parseInt (subs id-conf-exe 5)))))


;; FUNCTION: gen-app-conf-execs-nodes
(defn gen-app-conf-execs-nodes [app-id]
  (let [res         (filter #(= (% :id) app-id) @VARS/ALDE_APPS)
        grph-str    (gen-app-conf-execs-graph-structure res)
        nodes       (gen-nodes-3 grph-str)    ;; nodes
        edges       (gen-edges-3 grph-str)    ;; edges
        grph        (js/vis.Network. (.getElementById js/document "apps-graph3")
                                     (js-obj "nodes" nodes "edges" edges)
                                     GRAPH-OPTIONS-APPS-3)]
    ;; on-click event:
    (.on grph "click" #(on-click-3 %))
    (.on grph "doubleClick" #(on-double-click-node-3 %))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; APPS:

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

        ; configurations
        res-nodes-tmp2 {:nodes (gen-nodes-sub-graph-conf v)}
        res-edges-tmp2 {:edges (into []
                                (remove nil?
                                  (into []
                                    (for [x (res-nodes-tmp2 :nodes)]
                                      (when-not (check-exec-id ((get-app-by-id (x :idparent)) :executables) (x :executable_id))
                                        {:from (x :id) :to (x :idparent) :connected false})))))}      ; edges: conf to application
        res-edges-tmp3 {:edges (into []
                                (remove nil?
                                (into []
                                (for [x (res-nodes-tmp2 :nodes)]
                                  (when (check-exec-id ((get-app-by-id (x :idparent)) :executables) (x :executable_id))
                                    {:from (x :id) :to (x :idparent)})))))} ; :executable_id)}))} ; edges: conf to exec

        ; executables v2
        res-nodes-tmp {:nodes (gen-nodes-sub-graph-exec v)}
        res-edges-tmp {:edges (gen-edges-sub-graph-exec res-nodes-tmp res-nodes-tmp2)}

        res-final     {:edges (concat (res :edges) (res-edges-tmp :edges) (res-edges-tmp2 :edges) (res-edges-tmp3 :edges))
                       :nodes (concat (res :nodes) (res-nodes-tmp :nodes) (res-nodes-tmp2 :nodes))}
        ]
    res-final))


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
                                          "color"   (js-obj "background" "white"
                                                            "border" "black"
                                                            "highlight" (js-obj "background" "#F3F781"
                                                                                "border" "black")
                                                            "hover" (js-obj "background" "#ffffcc"
                                                                            "border" "black"))
                                          "shape"   "image"))))
            (catch js/Error e ))))
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
                          "color"   "lightgray"))
        (.push n (js-obj  "from"    (str (x :from))
                          "to"      (x :to)
                          "dashes"  true
                          "title"   "no executable found!"
                          "color"   "#FF5733"))))
    n))


;; FUNCTION: on-click
(defn- on-click "on-click event"
  [params]
  (let [id-node  (first (goog.object/getValueByKeys params #js ["nodes"]))]
    (re-frame/dispatch [::events/set-exec_cfg_execs_completed 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_failed 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_running 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_total 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_cancelled 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_restart 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_timeout 0])
    (re-frame/dispatch [::events/set-exec_cfg_execs_unknown 0])

    (re-frame/dispatch [::events/set-selected-exec-conf-id "-"])
    (re-frame/dispatch [::events/set-selected-exec-exec-id "-"])
    (re-frame/dispatch [::events/set-selected-execution {}])
    (re-frame/dispatch [::events/set-selected-execution-id "-"])

    (if-not (nil? id-node)
      (do
        (re-frame/dispatch [::events/set-selected-exec-app-id id-node])
        (re-frame/dispatch [::events/set-selected-exec-app (VARS/get-app-by-id (js/parseInt id-node))])
        (gen-app-conf-execs-nodes (js/parseInt id-node)))
      (do
        (re-frame/dispatch [::events/set-selected-exec-app-id ""])
        (re-frame/dispatch [::events/set-selected-exec-app {}])
        (gen-app-conf-execs-nodes (js/parseInt id-node))))))



;; FUNCTION: reload-app
(defn reload-app ""
  [id-app]
  ;(go (let [response (<! (http/get "http://localhost:8081/mock-data/applications" {:with-credentials? false}))]
  (go (let [response (<! (http/get (str @VARS/REST_API_URL "applications") {:with-credentials? false}))]
    (if (or (nil? response) (clojure.string/blank? response))
      (do
        (logs/error "Applications content is nil!!!")
        (reset! VARS/TAB_EXECUTIONS_LOADING false))
      (do
        (logs/info "Reloading application graph...")
        (VARS/update-apps (:body response))

        (re-frame/dispatch [::events/set-exec_cfg_execs_completed 0])
        (re-frame/dispatch [::events/set-exec_cfg_execs_failed 0])
        (re-frame/dispatch [::events/set-exec_cfg_execs_running 0])
        (re-frame/dispatch [::events/set-exec_cfg_execs_total 0])
        (re-frame/dispatch [::events/set-exec_cfg_execs_cancelled 0])
        (re-frame/dispatch [::events/set-exec_cfg_execs_restart 0])
        (re-frame/dispatch [::events/set-exec_cfg_execs_timeout 0])
        (re-frame/dispatch [::events/set-exec_cfg_execs_unknown 0])

        (re-frame/dispatch [::events/set-selected-exec-conf-id "-"])
        (re-frame/dispatch [::events/set-selected-exec-exec-id "-"])
        (re-frame/dispatch [::events/set-selected-execution {}])
        (re-frame/dispatch [::events/set-selected-execution-id "-"])

        (re-frame/dispatch [::events/set-selected-exec-app-id id-app])
        (re-frame/dispatch [::events/set-selected-exec-app (VARS/get-app-by-id (js/parseInt id-app))])
        (gen-app-conf-execs-nodes (js/parseInt id-app)))))))





;; FUNCTION: gen-data-nodes
(defn gen-data-nodes []
  (let [grph-str    (gen-apps-graph-structure @VARS/ALDE_APPS)
        nodes       (gen-nodes grph-str)    ;; nodes
        edges       (gen-edges grph-str)    ;; edges
        grph    (js/vis.Network. (.getElementById js/document "apps-graph2")
                                 (js-obj "nodes" nodes "edges" edges)
                                 GRAPH-OPTIONS-APPS)]
    ;; on-click event:
    (.on grph "click" #(on-click %))
    (.on grph "doubleClick" #(on-double-click-node %))))
