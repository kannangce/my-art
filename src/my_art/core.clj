(ns my-art.core
  (:require [quil.core :as q :include-macros true]
            [quil.middleware :as m]))

(def size 1000)

(def center [(/ size 2) (/ size 2)])

(def d 0.015708)

(def offset (/ size 2))

(defn sum [& vecs]
  (apply mapv + vecs))

(defn next-point [{:keys [radius step]}]
  (let [angle (* step d)]
    [(* radius (Math/cos angle))
     (* radius (Math/sin angle))]))

(defn draw-body [{:keys [ref size]}]
  (let [x (first ref)
        y (last ref)]
    (q/translate x y)
    (q/ellipse 0 0 size size)))

(defn draw-satellite [[x y] step]
  (q/stroke 255)
  (q/fill 220 200 255)
  (q/ellipse (+ offset x) (+ offset y) 15 15))

(defn draw-planet [[x y]]
  (let [c (- 255 (* x y))]
    (q/stroke 255)
    (q/shininess 2)
    (q/fill 220 200 255)
    (q/ellipse (+ offset x) (+ offset y) 15 15)))


(defn draw-planet1
  [planet]
  (let [step (:step planet)
        position (next-point planet)
        abs-position (sum position center)]
    (draw-body {:ref abs-position :size (:size planet)})
    (println {:ref abs-position :radius (:size planet)})
    (doseq [satellite (:satellites planet)]
      (let [sat-position (next-point {:step step :radius (:radius satellite)})
            sat-radius (:size satellite)]
        (println {:ref sat-position :radius sat-radius})
        (draw-body {:ref sat-position :size sat-radius})))))


(defn draw [state]
  (q/background 0)
  (doseq [planet (:planets state)] (draw-planet1 planet)))

(defn move-planet [planet]
  (update-in planet [:step] inc))

(defn update-fn [state]
  {:planets
   (vec (for [planet (:planets state)]
          (move-planet planet)))})

(defn setup []
  ;(q/frame-rate 100)
  {:planets [{:step         1
              :radius       50
              :size         10
              :satellites   [{:radius       20
                              :orbit-radius 3
                              :size         20
                              }]
              }]})

(q/defsketch my
             :host "host"
             :size [size size]
             :draw draw
             :setup setup
             :update update-fn
             ;:renderer :p3d
             :middleware [m/fun-mode identity])

(defn -main [& args])