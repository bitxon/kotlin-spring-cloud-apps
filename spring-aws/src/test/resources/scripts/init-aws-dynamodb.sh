#!/bin/bash
echo "Creating DynamoDB tables..."

awslocal dynamodb create-table \
   --table-name student \
   --attribute-definitions AttributeName=id,AttributeType=S \
   --key-schema AttributeName=id,KeyType=HASH \
   --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5

awslocal dynamodb put-item --table-name=student --item='{"id":{"S":"1"}, "name":{"S":"Alice"}, "status":{"S":"A"}}'
awslocal dynamodb put-item --table-name=student --item='{"id":{"S":"2"}, "name":{"S":"Bob"}, "status":{"S":"G"}}'
awslocal dynamodb put-item --table-name=student --item='{"id":{"S":"3"}, "name":{"S":"Charlie"}, "status":{"S":"I"}}'
awslocal dynamodb put-item --table-name=student --item='{"id":{"S":"4"}, "name":{"S":"David"}, "status":{"S":"E"}}'
awslocal dynamodb put-item --table-name=student --item='{"id":{"S":"5"}, "name":{"S":"Eve"}, "status":{"S":"A"}}'

echo "DynamoDB tables created."

