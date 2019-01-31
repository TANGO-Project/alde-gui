;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; SERVER.clj
;;  Main 'class'. It launches the jetty server -default port 3001-. It also
;;  initializes the REST API handler.
;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.server
  (:require [gui.handler :refer [handler]]
            [config.core :refer [env]]
            [ring.adapter.jetty :refer [run-jetty]])
  (:gen-class))


;; RUN JETTY
(defn- run-server ""
  [port]
  (run-jetty handler
    {:port        port
     ;:http?       false       ; listen on :port for HTTP traffic -defaults to true-
     ;:ssl?        true        ; allow connections over HTTPS
     ;:ssl-port    3002        ; the SSL port to listen on -defaults to 443, implies :ssl? is true-
    :join?       false}))


;; START SERVER:
;; HTTPS configuration:
;;    - nginx working as a reverse proxy in front of the ring based webapp
;;    - or use the Jetty adapter
;;          - http://ring-clojure.github.io/ring/ring.adapter.jetty.html
;;          - http://practice.kokonino.net/posts/ring-with-ssl-only
(defn -main [& args]
  (if (> (count args) 0)
    (let [port (Integer/parseInt (first args))]
      (run-server port))
    (let [port (Integer/parseInt (or (env :port) "3001"))]
      (run-server port))))
