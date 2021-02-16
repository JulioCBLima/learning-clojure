(ns recprotmm.model)

;; https://www.youtube.com/watch?v=kQhOlWXXl2I
(defprotocol Dateable
  (to-ms [this]))

(extend-type java.lang.Number
  Dateable
  (to-ms
    [this]
    this))

(extend-type java.util.Date
  Dateable
  (to-ms
    [this]
    (.getTime this)))

(extend-type java.util.Calendar
  Dateable
  (to-ms
    [this]
    (-> (.getTime this) ; returns a Date
        to-ms)))
