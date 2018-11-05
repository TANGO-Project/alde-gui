;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license.
;; Please, refer to the LICENSE.TXT file for more information
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
        (let [response (<! (http/get endpoint {:with-credentials? false}))]
          ;(logs/debug "GETf [1] response: " response);
          ;(logs/debug "GETf [1] status: " (:status response) ", body: " (:body response));
          (f (:status response) (:body response))))
      (catch js/Error e
        (logs/log-exception e))))
  ([endpoint f m]
    (try
      (go
        (let [response (<! (http/get endpoint m))]
          ;(logs/debug "GETf [2] response: " response);
          ;(logs/debug "GETf [2] status: " (:status response) ", body: " (:body response));
          (f (:status response) (:body response))))
      (catch js/Error e
        (logs/log-exception e)))))


(defn GET ""
  ([endpoint]
    (go (let [response (<! (http/get endpoint {:with-credentials? false}))]
          ;(logs/debug "GET [1] response: " response);
          {:status (response :status) :body (response :body)})))
  ;; map example:
  ;;  {:with-credentials? false, :query-params {"since" 135}}
  ([endpoint m]
    (go (let [response (<! (http/get endpoint m))]
          ;(logs/debug "GET [2] response: " response);
          {:status (response :status) :body (response :body)})))
  ([endpoint m func]
    (go (let [response (<! (http/get endpoint m))]
          ;(logs/debug "GET [3] response: " response);
          (func (:body response)))))
  ([endpoint m func res-elem]
    (go (let [response (<! (http/get endpoint m))]
          ;(logs/debug "GET [4] response: " response);
          ;(logs/debug "endpoint: " endpoint ", response: " response);
          (func (res-elem response))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; POST
(defn POST ""
  ([endpoint json-params]
    (go (let [response (<! (http/post endpoint {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response)))))
  ([endpoint func json-params]
    (go (let [response (<! (http/post endpoint {:with-credentials? false, :json-params (.parse js/JSON json-params)}))]
          (logs/debug "POST [2] response: " response)
          (logs/debug "POST [2] status: " (:status response) ", body: " (:body response))
          (func)))))
          ;(func (:body response))))))


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; PATCH
(defn PATCH ""
  ([endpoint json-params]
    (go (let [response (<! (http/patch endpoint {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response)))))
  ([endpoint func json-params]
    (go (let [response (<! (http/patch endpoint {:with-credentials? false, :json-params (.parse js/JSON json-params)}))]
          (logs/debug "PATCH [2] response: " response)
          (logs/debug "PATCH [2] status: " (:status response) ", body: " (:body response))
          (func)))))


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
;; DELETE
(defn DELETE ""
  ([endpoint func]
    (go (let [response (<! (http/delete endpoint {:with-credentials? false}))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (func))))
  ([endpoint func json-params]
    (go (let [response (<! (http/delete endpoint {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (func (:body response)))))
  ([endpoint func m json-params]
    (go (let [response (<! (http/delete endpoint m {:json-params json-params}))]
          (logs/debug "status: " (:status response) ", body: " (:body response))
          (func (:body response))))))
