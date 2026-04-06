#!/bin/sh
export AWS_DEFAULT_REGION=us-east-1
export AWS_ACCESS_KEY_ID=test
export AWS_SECRET_ACCESS_KEY=test
export AWS_ENDPOINT_URL=http://localhost:4566

echo "Creating SQS queue..."

aws sqs create-queue --queue-name student-queue

aws sqs send-message-batch \
    --queue-url=http://localhost:4566/000000000000/student-queue \
    --entries='[
                {"Id":"Msg-6",  "MessageBody":"{\"id\":\"6\",  \"name\":\"Frank\",  \"status\":\"A\"}" },
                {"Id":"Msg-7",  "MessageBody":"{\"id\":\"7\",  \"name\":\"Grace\",  \"status\":\"G\"}" },
                {"Id":"Msg-8",  "MessageBody":"{\"id\":\"8\",  \"name\":\"Helen\",  \"status\":\"I\"}" },
                {"Id":"Msg-9",  "MessageBody":"{\"id\":\"9\",  \"name\":\"Ivan\",   \"status\":\"E\"}" },
                {"Id":"Msg-10", "MessageBody":"{\"id\":\"10\", \"name\":\"Jack\",   \"status\":\"A\"}" }
              ]'

echo "SQS queue created."
