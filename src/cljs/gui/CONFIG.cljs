;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under an Apache 2.0 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.CONFIG)


;; INITIAL / DEFAULT CONFIGURATIONS:
(def CONFIGURATIONS {
    :app {
      ;; GUI properties
      :name "ALDE-GUI"
      :version "0.0.1"
      ;; REST API - ALDE - default url:
      ;;    "https://private-d69ab-applicationlifecycledeploymentengine.apiary-mock.com/api/v1/"
      :alde-rest-api-url "http://192.168.252.2:5000/api/v1/"
      :user "user"
      :pwd "user"
    }
  })
