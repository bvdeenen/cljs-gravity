(ns gravity.view.tools
  "Contain tools like selection animation, lights, background, etc…"

  (:require [gravity.tools :refer [log]])
  (:require-macros [gravity.macros :refer [λ]]))



(defn fill-window!
  "Resize a canvas to make it fill the window"
  [canvas]
  (let [width (.-innerWidth js/window)
        height (.-innerHeight js/window)]
    (set! (.-width canvas) width)
    (set! (.-height canvas) height))
  nil)



(defn make-stats
  "Create a stat view to monitor performances"
  []
  (let [stats (new js/Stats)
        style (-> stats
                  (.-domElement)
                  (.-style))]
    (set! (.-position style) "absolute")
    (set! (.-left style) "0px")
    (set! (.-top style) "0px")
    stats))




(defn- get-background
  "Generate a gray sphere as a background"
  []
  (let [material (new js/THREE.MeshLambertMaterial #js {"color" 0xa0a0a0
                                                        ;"ambiant"  0xffffff
                                                        "side" 1})
        geometry (new js/THREE.SphereGeometry 20 20 20)
        background (new js/THREE.Mesh geometry material)]
    (.set (.-scale background) 100 100 100)
    (set! (.-receiveShadow background) true)
    background))


(defn- get-lights
  "Add lights into the scene"
  []
  (let [color (new js/THREE.Color 0xffffff)
        strength   0.8
        shadow-map 2048
        positions [[-1000 1000 1000]
                   [1000 1000 1000]
                   [-1000 -1000 1000]
                   [1000 -1000 1000]

                   [1000 1000 -1000]]
        lights (map (λ [pos]
                       (let [light (new js/THREE.SpotLight color strength)
                             [x y z] pos]
                         (.set (.-position light) x y z)
                         (set! (.-shadowCameraFar light) 4000)

                         light))
                    positions)]
    (let [main-light (first lights)]
      (set! (.-castShadow main-light) true)
      (set! (.-shadowCameraVisible main-light) true))

    lights))
