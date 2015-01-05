## 0.5.0 (xx.xx.xxxx)

- ships with both Swagger 1.2 & 2.0 Support:
  - **1.2**: `fnhouse.swagger`
  - **2.0**: `fnhouse.swagger2`
- updated guesthouse to use Swagger 2.0
- added tests for both 1.2 & 2.0 generation
- `collect-routes` takes extra parameters allowing the client to better to extend the ring-swagger functionality, thanks to [Dmitry Groshev](https://github.com/si14)!
- adding `no-doc: true` metadata to fnhouse defnk namespaces causes the handlers
in that namespace not to be collected into swagger docs
- **breaking change**: `fnhouse.swagger/swagger-ui` is now called
`fnhouse.swagger/wrap-swagger.ui`.
- new coolness from Ring-Swagger 0.16.0
- updated deps:
```clojure
[metosin/ring-swagger "0.16.0-SNAPSHOT"] is available but we use "0.14.0"
[metosin/ring-swagger-ui "2.0.24"] is available but we use "2.0.17"
[ring/ring-core "1.3.2"] is available but we use "1.3.1"
```

## 0.4.0 (7.11.2014)

- allow prefixed resources (https://github.com/metosin/fnhouse-swagger/pull/1)
- enable setting response model meta-data
- do not collect http-status code 200 to the responseMessages
- updated deps:

```
[prismatic/plumbing "0.3.5"] is available but we use "0.3.3"
[prismatic/fnhouse "0.1.1"] is available but we use "0.1.0"
[metosin/ring-swagger "0.14.0"] is available but we use "0.10.5"
[ring/ring-core "1.3.1"] is available but we use "1.3.0"
```

## 0.3.0 (18.6.2014)

- use `[metosin/ring-swagger "0.10.0"]`
- support for ResponseModels

# 0.2.0 (17.6.2014)

- use `[metosin/ring-swagger "0.9.1"]`
