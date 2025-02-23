package bitxon.cloud.springgcp

import com.google.api.gax.core.CredentialsProvider
import com.google.api.gax.core.NoCredentialsProvider
import com.google.auth.Credentials
import com.google.cloud.NoCredentials
import com.google.cloud.spring.autoconfigure.core.GcpContextAutoConfiguration
import com.google.cloud.spring.autoconfigure.storage.GcpStorageAutoConfiguration
import com.google.cloud.spring.autoconfigure.storage.GcpStorageProperties
import com.google.cloud.spring.core.GcpProjectIdProvider
import com.google.cloud.spring.core.UserAgentHeaderProvider
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(name = ["spring.cloud.gcp.development-mode"], havingValue = "true")
class LocalConfig {

    /**
     * Overrides the default [CredentialsProvider] bean supplied by [GcpContextAutoConfiguration] to use a custom
     */
    @Bean
    fun googleCredentials(): CredentialsProvider = CustomNoCredentialsProvider()

    class CustomNoCredentialsProvider : CredentialsProvider {
        override fun getCredentials(): Credentials = NoCredentials.getInstance()
        override fun toString(): String = "CustomNoCredentialsProvider"
    }


    /**
     * Overrides the default [Storage] bean supplied by [GcpStorageAutoConfiguration] to use a custom
     */
    @Bean
    fun storage(
        gcpProjectIdProvider: GcpProjectIdProvider,
        credentialsProvider: CredentialsProvider,
        gcpStorageProperties: GcpStorageProperties
    ): Storage = StorageOptions.newBuilder()
        .setHeaderProvider(UserAgentHeaderProvider(LocalConfig::class.java))
        .setProjectId(gcpProjectIdProvider.projectId)
        .setCredentials(credentialsProvider.credentials)
        .setHost(gcpStorageProperties.host)
        .build().service
}
