;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns utils.tasks
  (:require-macros [cljs.core.async.macros :refer [go go-loop]])
  (:require [utils.logs :as logs]
            [utils.http_cljs :as http]
            [cljs.core.async :as async]))

;; FUNCTION: execute-periodic-task
(defn execute-periodic-task "Excecute a periodic task 'f' every 't' seconds. Returns the task's id"
  [f t]
  (js/setInterval f (* t 1000)))

;; FUNCTION: finalize-periodic-task
(defn finalize-periodic-task "Finalizes periodic task"
  [id-task]
  (js/clearInterval id-task))

;; FUNCTION: execute-task
(defn execute-task "Excecute a task 'f' after 't' milliseconds"
  [f t]
  (js/setTimeout f t))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; core.async
;;    https://purelyfunctional.tv/guide/clojure-concurrency/

(def THR_TICK_CLUSTERS (atom true))

;; create a channel with a buffer of up to 1 value
(def CLUSTERS_CHANNEL (async/chan 1))

;; FUNCTION: startThrd
;;    https://github.com/clojure/core.async/blob/master/examples/walkthrough.clj
(defn startThrdTICK_CLUSTERS ""
  [func]
  (go
    (while (deref THR_TICK_CLUSTERS)
      (try
        (logs/info "CALL TO http://localhost:8082/api-db/docs/clusters")
        (async/>! CLUSTERS_CHANNEL (http/GET "http://localhost:8082/api-db/docs/clusters" func {:basic-auth {:username "user" :password "user"}}))
        (async/<! (async/timeout 15000))
        (catch js/Error e (logs/error e))))))

;; FUNCTION: stopThrd
(defn stopThrdTICK_CLUSTERS ""
  []
  (reset! THR_TICK_CLUSTERS false))
