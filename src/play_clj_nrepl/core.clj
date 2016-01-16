(ns play-clj-nrepl.core
  (:require [clojure.tools.nrepl.transport :as t]
            [clojure.tools.nrepl.server :as s]
            [play-clj.core :refer :all])
  (:import [clojure.tools.nrepl.transport Transport]))

(defn make-screen-loader-middleware [game]
    (fn screen-loader-middleware [h]
      (fn [msg]
        (if (= "load-file" (:op msg))
          (h (update-in msg [:transport]
                        (fn [transport]
                          (reify
                            Transport
                            (recv [this timeout]
                              (t/recv transport timeout))
                            (send [this msg]
                              (let [r (t/send transport msg)]
                                (on-gl (-> game .getScreen .show))
                                r))))))
          (h msg)))))

(defn start-nrepl [game port]
    (try
      (when port (let [{port :port} (s/start-server :port (if (string? port)
                                                            (Integer/parseInt port)
                                                            port)
                                                    :handler (s/default-handler (make-screen-loader-middleware game)))]
                    (doseq [port-file ["target/repl-port" ".nrepl-port"]]
                      (-> port-file
                          java.io.File.
                          (doto .deleteOnExit)
                          (spit port)))
                    (println "Started nREPL server on port" port)))
      (catch Exception e
        (println e))))
