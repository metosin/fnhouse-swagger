# fnhouse-swagger

[![Build Status](https://travis-ci.org/metosin/fnhouse-swagger.svg?branch=master)](https://travis-ci.org/metosin/fnhouse-swagger)
[![Dependencies Status](http://jarkeeper.com/metosin/fnhouse-swagger/status.png)](http://jarkeeper.com/metosin/fnhouse-swagger)

[Swagger](https://helloreverb.com/developers/swagger) implementation
for [fnhouse](https://github.com/Prismatic/fnhouse) using the
[Ring-swagger](https://github.com/metosin/ring-swagger). Supports both 1.2 and 2.0 versions of the spec.

This is an alpha release, like fnhouse itself.

## Latest version

[![Clojars Project](http://clojars.org/metosin/fnhouse-swagger/latest-version.svg)](http://clojars.org/metosin/fnhouse-swagger)

[Release notes](https://github.com/metosin/fnhouse-swagger/releases)

## Usage

- create proto-handlers also from `fnhouse.swagger` namespace
- call `collect-routes` to get ring-swagger map of handlers
- assoc the map to key `:swagger` into the plumbing resource map

to use the embedded [swagger-ui](https://github.com/wordnik/swagger-ui),
add a dependency to latest [ring-swagger-ui](https://github.com/metosin/ring-swagger-ui)
and add add a ring-route `swagger-ui` to your app.

## Examples

Guesthouse with fnhouse-swagger is found [here](https://github.com/metosin/fnhouse-swagger/tree/master/examples/guesthouse).

Running the example:

```bash
git clone https://github.com/metosin/fnhouse-swagger.git
cd fnhouse-swagger/examples/guesthouse/src
lein repl
> (use 'guesthouse.core)
> (start)
```


## License

Copyright © 2014-2015 [Metosin Oy](http://www.metosin.fi)

Distributed under the Eclipse Public License, the same as Clojure.
