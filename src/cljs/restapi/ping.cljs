;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns restapi.ping
  (:require [utils.http_cljs :as http]
            [gui.globals :as VARS]))


;; FUNCTION: execute-periodic-task
(defn execute-periodic-task "Excecute a periodic task 'f' every 't' seconds. Returns the task's id"
  [f t]
  (js/setInterval f (* t 1000)))


;; FUNCTION: ping-alde
(defn ping-alde "Ping to ALDE REST API"
  [f-ping]
  (http/GETf (str @VARS/REST_API_URL "testbeds?") f-ping))
