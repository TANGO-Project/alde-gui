;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license. Please, refer to the
;; LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns gui.CONFIG)


;; INITIAL / DEFAULT CONFIGURATIONS:
(def CONFIGURATIONS {
    :app {
      ;; GUI properties
      :name "ALDE-GUI"
      :version "0.1.4.1"
      ;; REST API - ALDE - default url:
      :alde-rest-api-url "http://192.168.252.7:5000/api/v1/"
      :user "user"
      :pwd "user"
    }
  })
