# Spring Boot Cloud - AWS | Azure | GCP

## Local Development Comparison
Comparison done as of 2026-04-01
### AWS
| Service  | Docker Image | Init Resource | Init Data |
|----------|--------------|---------------|-----------|
| DynamoDB | ✅ Floci      | ✅ Script      | ✅ Script  |
| S3       | ✅ Floci      | ✅ Script      | ✅ Script  |
| SQS      | ✅ Floci      | ✅ Script      | ✅ Script  |

### Azure
| Service  | Docker Image | Init Resource | Init Data   |
|----------|--------------|---------------|-------------|
| CosmosDB | ✅ Official   | ❌No support   | ❌No support |
| Storage  | ✅ Official   | ❌No support   | ❌No support |
| EventHub | ✅ Official   | ✅ Config      | ❌No support |


### GCP
| Service   | Docker Image | Init Resource | Init Data   |
|-----------|--------------|---------------|-------------|
| Firestore | ✅ Official   | ❌No support   | ❌No support |
| Storage   | ⚠️ Custom    | ✅ Mount       | ✅ Mount     |
| Pub/Sub   | ✅ Official   | ✅ REST        | ✅ REST      |
