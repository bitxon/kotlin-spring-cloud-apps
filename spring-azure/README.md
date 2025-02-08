
# Spring Cloud Azure

---
## Application

```shell
curl -X POST 'http://localhost:8080/students' -H "Content-Type: application/json" \
  -d '{"id":"100", "name":"John Doe", "status":"A"}' | jq
```
```shell
curl -X GET 'http://localhost:8080/students/100' | jq
```

```shell
curl -X POST 'http://localhost:8080/diplomas' -H "Content-Type: application/json" \
  -d '[{"student":"John Doe 1", "year":2001}, {"student":"John Doe 2", "year":2002}]' | jq
```
```shell
curl -X GET 'http://localhost:8080/diplomas' | jq
```

---
## Azure


### CosmosDb

### Storage

#### EventHub
