package bitxon.cloud.springaws.testutil

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.sqs.SqsClient
import java.net.URI

class SqsWriter(endpointUrl: String, private val queueUrl: String) {
    private val sqsClient: SqsClient = SqsClient.builder()
        .endpointOverride(URI.create(endpointUrl))
        .region(Region.US_EAST_1)
        .credentialsProvider(StaticCredentialsProvider.create(
            AwsBasicCredentials.create("test", "test")
        ))
        .build()

    fun send(message: String) {
        sqsClient.sendMessage { it.queueUrl(queueUrl).messageBody(message) }
    }
}
