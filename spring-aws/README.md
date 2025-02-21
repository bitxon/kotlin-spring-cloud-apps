
# Spring Cloud AWS

## Run Application
1. Start Infrastructure
    ```shell
    docker compose up -d
    ```

2. Run App
    ```shell
    gradle bootRun
    ```

---
## Test Application

1. CosmosDb Save & Read
    ```shell
    curl -X POST 'http://localhost:8080/students' -H "Content-Type: application/json" \
      -d '{"id":"100", "name":"John Doe", "status":"A"}' | jq
    ```
    ```shell
    curl -X GET 'http://localhost:8080/students/100' | jq
    ```

2. Storage Save & Read
    ```shell
    curl -X POST 'http://localhost:8080/diplomas' -H "Content-Type: application/json" \
      -d '[{"student":"John Doe 1", "year":2001}, {"student":"John Doe 2", "year":2002}]' | jq
    ```
    ```shell
    curl -X GET 'http://localhost:8080/diplomas' | jq
    ```

---
## LocalStack
```shell
curl -s localhost:4566/_localstack/init | jq
```

```shell
aws dynamodb list-tables --endpoint-url=http://localhost:4566 --region=us-east-1
aws s3api list-buckets --endpoint-url=http://localhost:4566 --region=us-east-1
aws sqs list-queues --endpoint-url=http://localhost:4566 --region=us-east-1
```


### DynamoDB
```shell
aws dynamodb put-item \
    --endpoint-url=http://localhost:4566 --region=us-east-1 \
    --table-name=student  \
    --item='{"id":{"S":"test-id-3"}, "name":{"S":"Mike"}, "status":{"S":"A"}}'
```
```shell
aws dynamodb execute-statement \
    --endpoint-url=http://localhost:4566 --region=us-east-1 \
    --statement="INSERT INTO student VALUE {'id': 'test-id-3','name': 'Bob','status': 'A'}"
```

### S3
```shell
aws s3api create-bucket --bucket sample-bucket \
    --endpoint-url=http://localhost:4566 --region=us-east-1 
```

```shell
aws s3api put-object \
    --endpoint-url=http://localhost:4566 --region=us-east-1 \
    --bucket=student-bucket --content-type application/json --key=diplomas-archive.json \
    --body /dev/stdin <<EOF
[
  { "student": "Alice", "year": 2001},
  { "student": "Bob", "year": 2002 }
]
EOF
```

```shell
aws s3api get-object  \
    --endpoint-url=http://localhost:4566 --region=us-east-1 \
    --bucket=student-bucket --key=diplomas-archive.json /dev/stdout
```

#### SQS
```shell
aws sqs send-message \
    --endpoint-url=http://localhost:4566 --region=us-east-1 \
    --queue-url=http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/student-queue \
    --message-body='{"id": "test-id-3","name": "Bob","status": "A"}'
```

```shell
aws sqs send-message-batch \
    --endpoint-url=http://localhost:4566 --region=us-east-1 \
    --queue-url=http://sqs.us-east-1.localhost.localstack.cloud:4566/000000000000/student-queue \
    --entries='[
                {"Id":"Msg-11", "MessageBody":"{\"id\":\"11\", \"name\":\"Alice\", \"status\":\"A\"}" },
                {"Id":"Msg-12", "MessageBody":"{\"id\":\"12\", \"name\":\"Bob\", \"status\":\"A\"}" },
                {"Id":"Msg-13", "MessageBody":"{\"id\":\"13\", \"name\":\"Charlie\", \"status\":\"D\"}" },
                {"Id":"Msg-14", "MessageBody":"{\"id\":\"14\", \"name\":\"David\", \"status\":\"A\"}" },
                {"Id":"Msg-15", "MessageBody":"{\"id\":\"15\", \"name\":\"Eve\", \"status\":\"D\"}" }
              ]'
```
