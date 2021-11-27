(ns my-art.moving-object-dia
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]
            [my-art.utils :as u]))

(def size 1000)
(def delta 0.5)
(def radius 100)
(def center [(/ size 2) (/ size 2)])

(def d 0.015708)

(defn sumv [& vecs]
  (apply mapv + vecs))


(defn derive-point
  [{:keys [angle position]}]
  [(* position (Math/cos (u/deg->radian angle)))
   (* position (Math/sin (u/deg->radian angle)))])

(defn move-object
  [object]
  (println object)
  (let [{:keys [angle direction position]} object
        possible-next-x (+ position (* direction delta))
        should-reverse? (>= (Math/abs possible-next-x) radius)
        next-x possible-next-x
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
    (println [[ref-x ref-y] [x y]])
    (q/translate ref-x ref-y)
    (if (nil? color)
      (q/fill 255)
      (apply q/fill color))
    (q/ellipse x y size size)))


(defn draw-object
  [object]
  (q/push-matrix)
  (let [pos (derive-point object)]
    (draw-body {:ref center :size 10 :position pos :color [0 10 200]}))
  (q/pop-matrix))



(defn draw [state]
  (q/background 0)
  (doseq
    [object (:objects state)]
    (draw-object object)))


(defn update-fn [state]
  {:objects
   (vec (for [object (:objects state)]
          (move-object object)))})

(defn generate-points
  "Returns the given equidistant points on a circle with the given radius.
  It is assumed that the center of the circle is at origin. If not, the caller is expected to do the
  translation."
  [radius number-of-objects]
  (let [step (/ 360 number-of-objects)
        angles (take number-of-objects (map * (range) (step)))]
    (mapv (partial u/polar->cartesian radius) angles)))

(defn to-parent-circle
  [])

(defn generate-objects-in-circle
  [[x y] [x1 y1] n]
  (let [inner-radius (u/distance [x y] [x1 y1])]
    ))

(defn setup []
  (q/frame-rate 100)
  {:objects [{:angle     45
              :position  0
              :direction 1}
             {:angle     90
              :position  0
              :direction 1}
             {:angle     0
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