package bitxon.cloud.springaws.database

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey

@DynamoDbBean
data class Student(
    @get:DynamoDbPartitionKey
    var id: String? = "",
    var name: String = "",
    var status: String = ""
)