;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; This code is being developed for the TANGO Project: http://tango-project.eu
;;
;; Copyright: Roi Sucasas Font, Atos Research and Innovation, 2018.
;;
;; This code is licensed under a GNU General Public License, version 3 license.
;; Please, refer to the LICENSE.TXT file for more information
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(ns utils.common)

(defn length
  [nodes]
  (. nodes -length))

(defn item
  [nodes n]
  (.item nodes n))

(defn as-seq
  [nodes]
  (for [i (range (length nodes))] (item nodes i)))

(defn by-id
  [id]
  (.getElementById js/document (name id)))

(defn by-tag
  [tag]
  (as-seq
    (.getElementsByTagName js/document (name tag))))

(defn html
  [dom]
  (. dom -innerHTML))

(defn set-html!
  [dom content]
  (set! (. dom -innerHTML) content))
