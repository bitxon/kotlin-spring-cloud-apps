# Spring Boot Cloud - AWS | Azure | GCP

## Local Development Comparison
Comparison done as of 2025-03-01
### AWS
| Service  | Docker Image | Init Resource | Init Data | Note |
|----------|--------------|---------------|-----------|------|
| DynamoDB | ✅ LocalStack | ✅ Script      | ✅ Script  | ---  |
| S3       | ✅ LocalStack | ✅ Script      | ✅ Script  | ---  |
| SQS      | ✅ LocalStack | ✅ Script      | ✅ Script  | ---  |

### Azure
| Service  | Docker Image | Init Resource | Init Data   | Note |
|----------|--------------|---------------|-------------|------|
| CosmosDB | ✅ Official   | ❌No support   | ❌No support | ---  |
| Storage  | ✅ Official   | ❌No support   | ❌No support | ---  |
| EventHub | ✅ Official   | ✅ Config      | ❌No support | ---  |


### GCP
| Service   | Docker Image | Init Resource | Init Data   | Note |
|-----------|--------------|---------------|-------------|------|
| Firestore | ✅ Official   | ❌No support   | ❌No support | ---  |
| Storage   | ⚠️ Custom    | ✅ Mount       | ✅ Mount     | ---  |
| Pub/Sub   | ✅ Official   | ✅ Config      | ❌No support | ---  |