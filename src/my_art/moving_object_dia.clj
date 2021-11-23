(ns my-art.moving-object-dia
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def size 1000)
(def delta 0.1)
(def radius 100)
(def center [(/ size 2) (/ size 2)])

(def d 0.015708)

(defn sumv [& vecs]
  (apply mapv + vecs))


(defn derive-point
  [{:keys [angle position]}]
  [(* position (Math/cos angle))
   (* position (Math/sin angle))])

(defn move-object
  [object]
  (let [{:keys [angle direction position]} object
        possible-next-x (+ position (* direction delta))
        should-reverse? (>= (Math/abs position) radius)
        next-x (if should-reverse? (* direction radius) possible-next-x)
        next-direction (if should-reverse? (- direction) direction)]
    {:angle     angle
     :position  next-x
     :direction next-direction}
    ))



(defn draw-body [{:keys [ref position size color]}]
  (let [ref-x (first ref)
        ref-y (last ref)
        x (first position)
        y (last position)]
    (q/translate ref-x ref-y)
    (if (nil? color)
      (q/fill 255)
      (apply q/fill color))
    (q/ellipse x y size size)))


(defn draw-object
  [object]
  (q/push-matrix)
  (let [{:keys [angle position]} object
        pos (derive-point object)]
    (draw-body {:ref center :size 5 :position pos :color [0 10 200]}))
  (q/pop-matrix))

(defn draw [state]
  (q/background 0)
  (doseq
    [object (:objects state)]
    (draw-object object)))


(defn update-fn [state]
  {:planets
   (vec (for [object (:objects state)]
          (move-object object)))})

(defn setup []
  (q/frame-rate 100)
  {:objects [{:angle     0
              :position  0
              :direction 1}]})

(q/defsketch my
             :host "host"
             :size [size size]
             :draw draw
             :setup setup
             :update update-fn
             :middleware [m/fun-mode identity])

(defn -main [& args])