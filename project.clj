(defproject org.clojure/data.priority-map "0.2.2-SNAPSHOT"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :source-paths ["src/main/clojure"]
  :test-paths ["src/test/clojure"]
  ;:dependencies []
  #_#_
  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0-alpha13"]
                                  [org.clojure/core.typed "0.3.29-SNAPSHOT"]]
                   :repl-options {:timeout 3000000}
                   }}
  :dependencies [[org.clojure/clojure "1.9.0-alpha13"]
                 [org.clojure/core.typed "0.3.29-SNAPSHOT"]]
  ;:repl-options {:timeout 3000000}
  :injections [(require 'clojure.core.typed)
               (clojure.core.typed/install
                 #{:load})]
  :java-source-paths ["src/main/java"]
  ;:jvm-opts ^:replace ["-server" "-Xmx10g" "-XX:-OmitStackTraceInFastThrow" "-Xss750k"]
  :global-vars {*warn-on-reflection* true})
