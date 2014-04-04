# fnhouse-swagger

[Swagger](https://helloreverb.com/developers/swagger) implementation
for [fnhouse](https://github.com/Prismatic/fnhouse) using the
[Ring-swagger](https://github.com/metosin/ring-swagger).

## Latest version

```clojure
[metosin/fnhouse-swagger "0.1.0-SNAPSHOT"]
```

## Usage

- create proto-handlers also from `fnhouse.swagger` namespace
- call `collect-routes` to get ring-swagger map of handlers
- assoc the map to key `:swagger` into the plumbing resource map

to use the embedded [Swagger-ui](https://github.com/wordnik/swagger-ui),
add a dependency to `metosin/swagger-ui` and add add a ring-route to
`swagger-ui`.

## License

Copyright © 2014 Metosin Oy

Distributed under the Eclipse Public License, the same as Clojure.
