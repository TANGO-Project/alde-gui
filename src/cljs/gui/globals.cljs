;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.globals
  (:require [reagent.core :as r]
            [utils.logs :as logs]
            [gui.CONFIG :as CONFIG]
            [re-frame.core :as re-frame]
            [gui.events :as events]))


;;
(defn set-item! "Set `key' in browser's localStorage to `val`."
  [key val]
  ;(logs/debug "KEY: " key " SET --> " val)
  (.setItem (.-localStorage js/window) key (.stringify js/JSON (clj->js val))))


;;
(defn get-item "Returns value of `key' from browser's localStorage."
  [key]
  ;(logs/debug "KEY: " key " GET --> " (js->clj (.parse js/JSON (.getItem (.-localStorage js/window) key))))
  (js->clj (.parse js/JSON (.getItem (.-localStorage js/window) key))))


;;
(defn remove-item! "Remove the browser's localStorage value for the given `key`"
  [key]
  (.removeItem (.-localStorage js/window) key))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; REST API
(def REST_API_URL
        (if (nil? (get-item "REST_API_URL"))
          (r/atom (get-in CONFIG/CONFIGURATIONS [:app :alde-rest-api-url]))
          (r/atom (get-item "REST_API_URL"))))

;; BASIC AUTH FOR REST API
(def M_BASIC_AUTH {:basic-auth {:username (get-in CONFIG/CONFIGURATIONS [:app :user])
                                :password (get-in CONFIG/CONFIGURATIONS [:app :pwd])}})

;; FUNCTION: check-configuration
(defn check-configuration "Check app configuration"
  []
  (logs/debug "Checking configuration ...")
  (logs/debug @REST_API_URL)
  (logs/debug M_BASIC_AUTH)
  (logs/debug "Configuration ... ok"))

;; DATA FROM ALDE
;; Current testbeds
(def ALDE_TESTBEDS (atom []))

;; Current nodes
(def ALDE_NODES (atom []))
;; fields: cpus [], gpus [], disabled, information_retrieved, id, memories [], name, state, testbed {}, testbed_id

;; Current apps
(def ALDE_APPS (atom []))

;; Current executions
(def ALDE_EXECS (atom []))
(def ALDE_EXECS_COMPLETED (atom []))
(def ALDE_EXECS_RUNNING (atom []))
(def ALDE_EXECS_FAILED (atom []))

;; Current config_execution executions
(def ALDE_CFG_EXECS (atom []))


(def TAB_LOADING (r/atom false))

(def TAB_EXECUTIONS_LOADING (r/atom false))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; TESTBEDS:
;; FUNCTION: update-testbeds-vector
(defn- update-testbeds-vector "Updates the testbeds vector by adding new elements: edges (for graphs)"
  [v]
  (let [v-1   (into []
                (for [m v]
                  (let [edges (into []
                                (let [nodes (get-in m [:nodes])]
                                  (for [x nodes]
                                    {:from 0 :to (x :id)})))]
                    (assoc m :edges edges))))]
    (into []
      (for [m v-1]
        (let [nodes (conj (get-in m [:nodes]) {:id 0 :name (m :name) :type "testbed"})]
          (assoc m :nodes nodes))))))

;; FUNCTION: update-testbeds
(defn update-testbeds "Updates the testbeds vector"
  [res]
  (if (or (nil? res) (clojure.string/blank? res))
    (logs/error "Testbeds content is nil!!!")
    (do
      ;(logs/info "Updating Testbeds content ...")
      (reset! ALDE_TESTBEDS (update-testbeds-vector (res :objects)))
      (re-frame/dispatch [::events/set-total-testbeds (count @ALDE_TESTBEDS)])
      ;(logs/info "Total Testbeds: " (count @ALDE_TESTBEDS))
      )))

;; FUNCTION: get-total-testbeds
(defn get-total-testbeds "returns the the total testbeds"
  []
  (count @ALDE_TESTBEDS))

;; FUNCTION: get-testbed-by-id
(defn get-testbed-by-id ""
  [id-testbed]
  (when-not (nil? id-testbed)
    (let [res (filter #(= (% :id) id-testbed) @ALDE_TESTBEDS)]
      (when-not (or (nil? res) (<= (count res) 0))
        (first res)))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; NODES:

;; FUNCTION: update-nodes-vector
;; fields: cpus [], gpus [], disabled, information_retrieved, id, memories [], name, state, testbed {}, testbed_id
(defn- update-nodes-vector "Updates the nodes vector by adding new elements: edges and nodes (for graphs)"
  [v-nodes]
  ;; 1. create ':nodes' element in each node for the graph
  (let [v-1   (into []
                (for [m v-nodes]
                  (let [nodes1  (into []
                                  (let [cpus (get-in m [:cpus])]
                                    (for [x cpus]
                                      {:id (x :id) :name (str "cpu_" (x :id)) :type "cpu" :arch (x :arch) :cores (x :cores) :vendor_id (x :vendor_id)})))
                        nodes2  (into []
                                  (let [gpus (get-in m [:gpus])]
                                    (for [x gpus]
                                      {:id (x :id) :name (str "gpu_" (x :id)) :type "gpu" :model_name (x :model_name) :vendor_id (x :vendor_id)})))
                        nodes3  (into []
                                  (let [mem (get-in m [:memories])]
                                    (for [x mem]
                                      {:id (x :id) :name (str "mem_" (x :id)) :type "mem" :size (x :size) :units (x :units)})))
                        res-nodes (into [] (concat nodes1 nodes2 nodes3))]
                    ;; add ':nodes' to map
                    (assoc m :nodes res-nodes))))]
    ;; 2. create ':edges' element in each node for the graph
    (let [v-2   (into []
                  (for [m v-1]
                    (let [edges (into []
                                  (let [nodes (get-in m [:nodes])]
                                    (for [x nodes]
                                      {:from 0 :to (x :id)})))]
                      ;; add ':edges' to map
                      (assoc m :edges edges))))]
      (into []
        (for [m v-2]
          (let [nodes (conj (get-in m [:nodes]) {:id 0 :name (m :name) :type "node"})]
            ;; add main node to map
            (assoc m :nodes nodes)))))))


;; FUNCTION: get-total-nodes
(defn get-total-nodes "returns the the total nodes in all testbeds"
  []
  (apply + (for [x @ALDE_TESTBEDS] (- (count (x :nodes)) 1))))


;; FUNCTION: update-nodes
(defn update-nodes "Updates the nodes vector"
  [res]
  (if (or (nil? res) (clojure.string/blank? res))
    (logs/error "Nodes content is nil!!!")
    (do
      ;(logs/info "Updating Nodes content ...")
      (reset! ALDE_NODES (update-nodes-vector (res :objects)))
      (re-frame/dispatch [::events/set-total-nodes (get-total-nodes)])
      ;(logs/info "Total Nodes: " (count @ALDE_NODES))
      )))


;; FUNCTION: get-node-by-id
(defn get-node-by-id ""
  [id-node]
  (when-not (and (nil? id-node) (not (int? id-node)))
    (let [res (filter #(= (% :id) (js/parseInt id-node)) @ALDE_NODES)]
      ;(logs/info "Total nodes retrieved: " (count res))
      (when-not (or (nil? res) (<= (count res) 0))
        (first res)))))


;; FUNCTION: get-elem
;; fields: cpus [], gpus [], disabled, information_retrieved, id, memories [], name, state, testbed {}, testbed_id
(defn- get-elem ""
  [node id-elem]
  (let [res-cpus (filter #(= (% :id) id-elem) (node :cpus))]
    (if (> (count res-cpus) 0)
      (assoc (first res-cpus) :ntype "cpu")
      (let [res-gpus (filter #(= (% :id) id-elem) (node :gpus))]
        (if (> (count res-gpus) 0)
          (assoc (first res-gpus) :ntype "gpu")
          (let [res-mem (filter #(= (% :id) id-elem) (node :memories))]
            (if (> (count res-mem) 0)
              (assoc (first res-mem) :ntype "mem")
              nil)))))))


;; FUNCTION: get-node-elem
(defn get-node-elem ""
  [id-node id-elem]
  (when-not (and (nil? id-node) (not (int? id-node)))
    (let [res (filter #(= (% :id) (js/parseInt id-node)) @ALDE_NODES)]
      (when-not (or (nil? res) (<= (count res) 0))
        (get-elem (first res) (js/parseInt id-elem))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; APPS:

;; FUNCTION: update-apps-vector
(defn- update-apps-vector "Updates the apps vector by adding new elements: edges and nodes (for graphs)"
  [v-nodes]
  nil)

;; FUNCTION: update-apps
(defn update-apps "Updates the apps vector"
  [res]
  (if (or (nil? res) (clojure.string/blank? res))
    (logs/error "Applications content is nil!!!")
    (do
      ;(logs/info "Updating Applications content ...")
      (reset! ALDE_APPS (res :objects)) ;(update-apps-vector (res :objects)))
      (re-frame/dispatch [::events/set-total-apps (count @ALDE_APPS)])
      ;(logs/info "Total Applications: " (count @ALDE_APPS))
      )))


;; FUNCTION: get-total-apps
(defn get-total-apps "returns the the total applications in all nodes"
  []
  (count @ALDE_APPS))

;; FUNCTION: get-app-by-id
(defn get-app-by-id ""
  [id-app]
  (when-not (nil? id-app)
    (if-let [info (filter #(= (% :id) id-app) @ALDE_APPS)]
      (when (> (count info) 0)
        (first info)))))


;; FUNCTION: get-exec-by-id
(defn get-exec-by-id ""
  [id-exec]
  (when-not (nil? id-exec)
    (let [res (remove empty?
          (for [app @ALDE_APPS]
            (remove nil?
              (for [exec (app :executables)]
                (when (= id-exec (exec :id))
                  exec)))))]
      (if (> (count res) 0)
        (ffirst res)
        nil))))


;; FUNCTION: get-conf-by-id
(defn get-conf-by-id ""
  [id-exec]
  (when-not (nil? id-exec)
    (let [res (remove empty?
          (for [app @ALDE_APPS]
            (remove nil?
              (for [exec (app :execution_configurations)]
                (when (= id-exec (exec :id))
                  exec)))))]
      (if (> (count res) 0)
        (ffirst res)
        nil))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; EXECUTIONS:

;; FUNCTION: update-execs
(defn update-execs "Updates the execs vector"
  [res]
  (if (or (nil? res) (clojure.string/blank? res))
    (logs/error "Executions content is nil!!!")
    (do
      (reset! ALDE_EXECS (res :objects))
      (re-frame/dispatch [::events/set-total-execs (count @ALDE_EXECS)]))))


(defn update-execs-completed ""
  [res]
  (if (or (nil? res) (clojure.string/blank? res))
    (logs/error "Executions (completed) content is nil!!!")
    (do
      (reset! ALDE_EXECS_COMPLETED (res :objects))
      (re-frame/dispatch [::events/set-total-execs-completed (count @ALDE_EXECS_COMPLETED)]))))


(defn update-execs-running ""
  [res]
  (if (or (nil? res) (clojure.string/blank? res))
    (logs/error "Executions (running) content is nil!!!")
    (do
      (reset! ALDE_EXECS_RUNNING (res :objects))
      (re-frame/dispatch [::events/set-total-execs-running (count @ALDE_EXECS_RUNNING)]))))


(defn update-execs-failed ""
  [res]
  (if (or (nil? res) (clojure.string/blank? res))
    (logs/error "Executions (failed) content is nil!!!")
    (do
      (reset! ALDE_EXECS_FAILED (res :objects))
      (re-frame/dispatch [::events/set-total-execs-failed (count @ALDE_EXECS_FAILED)]))))


; FUNCTION: update-cfg-execs ALDE_CFG_EXECS
(defn update-cfg-execs "Updates the execs from exec_config"
  [res]
  (if (or (nil? res) (clojure.string/blank? res))
    (logs/error "Executions (from execs_config) content is nil!!!")
    (do
      (reset! ALDE_CFG_EXECS (res :executions))

      (re-frame/dispatch [::events/set-exec_cfg_execs_total (count @ALDE_CFG_EXECS)])

      (let [execs-completed (filter #(= (% :status) "COMPLETED") @ALDE_CFG_EXECS)]
        (re-frame/dispatch [::events/set-exec_cfg_execs_completed (count execs-completed)]))

      (let [execs-failed (filter #(= (% :status) "FAILED") @ALDE_CFG_EXECS)]
        (re-frame/dispatch [::events/set-exec_cfg_execs_failed (count execs-failed)]))

      (let [execs-running (filter #(= (% :status) "RUNNING") @ALDE_CFG_EXECS)]
        (re-frame/dispatch [::events/set-exec_cfg_execs_running (count execs-running)]))

      (res :executions))))


; FUNCTION: update-cfg-execs ALDE_CFG_EXECS
(defn get-executions-from-cfg ""
  [id-config]
  (do
    (let [res (filter #(= (get-in [:execution_configuration :id] %) id-config) @ALDE_EXECS)]
      (reset! ALDE_CFG_EXECS (into [] res))
      (logs/info "@ALDE_CFG_EXECS=" @ALDE_CFG_EXECS))

    (re-frame/dispatch [::events/set-exec_cfg_execs_total (count @ALDE_CFG_EXECS)])

    (let [execs-completed (filter #(= (% :status) "COMPLETED") @ALDE_CFG_EXECS)]
      (re-frame/dispatch [::events/set-exec_cfg_execs_completed (count execs-completed)]))

    (let [execs-failed (filter #(= (% :status) "FAILED") @ALDE_CFG_EXECS)]
      (re-frame/dispatch [::events/set-exec_cfg_execs_failed (count execs-failed)]))

    (let [execs-running (filter #(= (% :status) "RUNNING") @ALDE_CFG_EXECS)]
      (re-frame/dispatch [::events/set-exec_cfg_execs_running (count execs-running)]))

    @ALDE_CFG_EXECS))


;; FUNCTION: get-execution-by-id
(defn get-execution-by-id ""
  [id-exec]
  ;(logs/info "id-exec: " id-exec)
  ;(logs/info "@ALDE_EXECS: " @ALDE_EXECS)
  (let [exec (filter #(= (% :id) (js/parseInt id-exec)) @ALDE_EXECS)]
    ;(logs/info "exec: " exec)
    (if (= 0 (count exec))
      {}
      (first exec))))



;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; DELETE
(check-configuration)
