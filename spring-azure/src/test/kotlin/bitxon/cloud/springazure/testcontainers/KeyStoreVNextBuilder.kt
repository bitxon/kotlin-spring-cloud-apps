package bitxon.cloud.springazure.testcontainers

import org.testcontainers.containers.GenericContainer
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import kotlin.text.Charsets.UTF_8

private const val DOMAIN_CRT = "/scripts/certs/domain.crt"
private const val ROOT_CA_CRT = "/scripts/certs/rootCA.crt"

fun GenericContainer<*>.buildByExtractingCertificate(keyStorePassword: String): KeyStore {
    try {
        val rootCert = this.extractFileContent(ROOT_CA_CRT)
        println("Extracted Root Certificate:\n$rootCert\n")

        val domainCert = this.extractFileContent(DOMAIN_CRT)
        println("Extracted Domain Certificate:\n$domainCert\n")

        return buildKeyStore(rootCert.byteInputStream(), domainCert.byteInputStream(), keyStorePassword)
    } catch (ex: Exception) {
        throw IllegalStateException(ex)
    }
}

private fun GenericContainer<*>.extractFileContent(filePath: String): String {
    return this.copyFileFromContainer(filePath) { inputStream ->
        inputStream.readAllBytes().toString(UTF_8)
    }
}

private fun buildKeyStore(
    rootCertStream: InputStream,
    domainCertStream: InputStream,
    keyStorePassword: String
): KeyStore {
    val rootCert = CertificateFactory.getInstance("X.509").generateCertificate(rootCertStream)
    val domainCert = CertificateFactory.getInstance("X.509").generateCertificate(domainCertStream)

    val keystore = KeyStore.getInstance("PKCS12")
    keystore.load(null, keyStorePassword.toCharArray())
    keystore.setCertificateEntry("azure-cosmos-emulator-root", rootCert)
    keystore.setCertificateEntry("azure-cosmos-emulator-domain", domainCert)

    return keystore
}
