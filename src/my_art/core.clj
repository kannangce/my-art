(ns my-art.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def size 1000)

(def center [(/ size 2) (/ size 2)])

(def d 0.015708)

(defn sumv [& vecs]
  (apply mapv + vecs))

(defn next-point [{:keys [radius step velocity]}]
  (let [angle (* step d velocity)]
    [(* radius (Math/cos angle))
     (* radius (Math/sin angle))]))

(defn draw-body [{:keys [ref size color]}]
  (let [x (first ref)
        y (last ref)]
    (q/translate x y)
    (if (nil? color)
      (q/fill 255)
      (apply q/fill color))
    (q/ellipse 0 0 size size)))


(defn draw-planet
  [planet]
  (q/push-matrix)
  (let [step (:step planet)
        position (next-point planet)
        abs-position (sumv position center)]
    (draw-body (merge planet {:ref abs-position}))
    (doseq [satellite (:satellites planet)]
      (let [sat-position (next-point {:step     step
                                      :radius   (:radius satellite)
                                      :velocity (:velocity satellite)})
            sat-radius (:size satellite)]
        (draw-body {:ref sat-position :size sat-radius})))
    ; Reset any transformations that has happened
    (q/pop-matrix)))

(defn draw [state]
  (q/background 0)
  (doseq
    [planet (:planets state)]
    (draw-planet planet)))

(defn move-planet [planet]
  (update-in planet [:step] inc))

(defn update-fn [state]
  {:planets
   (vec (for [planet (:planets state)]
          (move-planet planet)))})

(defn setup []
  ;(q/frame-rate 100)
  {:planets [{:step     1
              :radius   0
              :size     50
              :velocity 0
              :color    [200 200 200]
              }
             {:step       1
              :radius     250
              :size       20
              :velocity   0.1
              :color      [0 10 200]
              :satellites [{:radius       30
                            :orbit-radius 3
                            :size         10
                            :velocity     3
                            }]
              }]})

(q/defsketch my
             :host "host"
             :size [size size]
             :draw draw
             :setup setup
             :update update-fn
             :middleware [m/fun-mode identity])

(defn -main [& args])