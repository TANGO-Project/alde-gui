;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns utils.http_cljs
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [utils.logs :as logs]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; GET
(defn GETf "Executes f with GET request response. f should have 2 parameters: response status and response content"
  ([endpoint f]
    (try
      (go
        (let [response (<! (http/get endpoint))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (f (:status response) (:body response))))
      (catch js/Error e
        (logs/log-exception e))))
  ([endpoint f m]
    (try
      (go
        (let [response (<! (http/get endpoint))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (f (:status response) (:body response))))
      (catch js/Error e
        (logs/log-exception e)))))


(defn GET ""
  ([endpoint]
    (go (let [response (<! (http/get endpoint))]
          {:status (response :status) :body (response :body)})))
  ;; map example:
  ;;  {:with-credentials? false, :query-params {"since" 135}}
  ([endpoint m]
    (go (let [response (<! (http/get endpoint))]
          {:status (response :status) :body (response :body)})))
  ([endpoint m func]
    (go (let [response (<! (http/get endpoint))]
          (func (:body response)))))
  ([endpoint m func res-elem]
    (go (let [response (<! (http/get endpoint))]
          (func (res-elem response))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; POST
(defn POST ""
  ([endpoint func json-params]
    (go (let [response (<! (http/post endpoint {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (func (:body response)))))
  ([endpoint func m json-params]
    ;; map example:
    ;;  {:with-credentials? false, :query-params {"since" 135}}
    (go (let [response (<! (http/post endpoint m {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (func (:body response))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PUT
(defn PUT ""
  ([endpoint func json-params]
    (go (let [response (<! (http/put endpoint {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (func (:body response)))))
  ([endpoint func m json-params]
    (go (let [response (<! (http/put endpoint m {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (func (:body response))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PATCH
(defn PATCH ""
  ([endpoint func json-params]
    (go (let [response (<! (http/patch endpoint {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (func (:body response)))))
  ([endpoint func m json-params]
    (go (let [response (<! (http/patch endpoint m {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (func (:body response))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; DELETE
(defn DELETE ""
  ([endpoint func json-params]
    (go (let [response (<! (http/delete endpoint {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (func (:body response)))))
  ([endpoint func m json-params]
    (go (let [response (<! (http/delete endpoint m {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (func (:body response))))))
