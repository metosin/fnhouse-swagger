# Guesthouse

A simple example project that shows how to use fnhouse with ring.  See
`guesthouse.core` for the server, `guesthouse.guestbook` for the handlers,
`guesthouse.schemas` for the coercion middleware, `guesthouse.core-test` for
test usage.

The application is a guestbook where users can add, search for, modify, and
delete guestbook entries.  Each guestbook `Entry` is represented as a map with
keyword keys containing a name, age, and programming language (clj, cljs).  The
`Entry` has a different server and client representation.  On the server, the
`Entry` just has a single field for the name, whereas the `ClientEntry` has a
first and last name.  Serverside `Entry`s are coerced into `ClientEntry`s via a
custom Schema cooercion middleware (see `guesthouse.schemas` for implementation
details).

# Swagger Docs

Demo of fnHouse with
[fnhouse-swagger](https://github.com/metosin/fnhouse-swagger).

```clojure
lein repl
;; nREPL server started on port 56496 on host 127.0.0.1
;; REPL-y 0.3.0
;; Clojure 1.5.1
;;     Docs: (doc function-name-here)
;;           (find-doc "part-of-name-here")
;;   Source: (source function-name-here)
;;  Javadoc: (javadoc java-object-or-class-here)
;;     Exit: Control+D or (exit) or (quit)
;;  Results: Stored in vars *1, *2, *3, an exception in *e

user=> (use 'guesthouse.core)
;; nil
user=> (start)
;; 2014-03-26 09:08:59.560:INFO:oejs.Server:jetty-7.6.8.v20121106
;; 2014-03-26 09:08:59.588:INFO:oejs.AbstractConnector:Started SelectChannelConnector@0.0.0.0:8080
;; #<Server org.eclipse.jetty.server.Server@2c6d8073>
user=>
```

and browse to http://localhost:8080/index.html

## Api listing

![](https://raw.githubusercontent.com/metosin/fnhouse-swagger/master/examples/guesthouse/pics/api-listing.png)

## Api usage

![](https://raw.githubusercontent.com/metosin/fnhouse-swagger/master/examples/guesthouse/pics/api-usage.png)
