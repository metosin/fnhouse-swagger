# fnhouse-swagger

[Swagger](https://helloreverb.com/developers/swagger) implementation
for [fnhouse](https://github.com/Prismatic/fnhouse) using the
[Ring-swagger](https://github.com/metosin/ring-swagger).

## Latest version

```clojure
[metosin/fnhouse-swagger "0.1.0"]
```

## Usage

- change the `schema.core/defschema`s to `ring.swagger.schema/defmodel`
- create proto-handlers also from `fnhouse.swagger` namespace
- call `collect-routes` to get ring-swagger map of handlers
- assoc the map to key `:swagger` into the plumbing resource map

to use the embedded [swagger-ui](https://github.com/wordnik/swagger-ui),
add a dependency to latest [ring-swagger-ui](https://github.com/metosin/ring-swagger-ui)
and add add a ring-route `swagger-ui` to your app.

needed changes are isolated in this [commit](https://github.com/metosin/fnhouse-swagger/commit/09fbbff348d1240f24b82962fbcb5ec4dcbca4d2).

## Examples

Guesthouse with fnhouse-swagger is found [here](https://github.com/metosin/fnhouse-swagger/tree/master/examples/guesthouse).

## License

Copyright © 2014 Metosin Oy

Distributed under the Eclipse Public License, the same as Clojure.
