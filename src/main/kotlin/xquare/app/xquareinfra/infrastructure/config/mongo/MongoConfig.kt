package xquare.app.xquareinfra.infrastructure.config.mongo

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.MongoTemplate
import java.io.ByteArrayInputStream
import java.net.URL
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

@Configuration
class MongoConfig : AbstractMongoClientConfiguration() {

    @Value("\${spring.data.mongodb.uri}")
    private lateinit var mongoUri: String

    override fun getDatabaseName(): String = "tracing"

    @Bean
    override fun mongoClient(): MongoClient {
        val sslContext = createSSLContext()

        val connectionString = ConnectionString(mongoUri)
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .applyToSslSettings { builder ->
                builder.enabled(true)
                    .context(sslContext)
            }
            .build()

        return MongoClients.create(mongoClientSettings)
    }

    private fun createSSLContext(): SSLContext {
        val certUrl = "https://truststore.pki.rds.amazonaws.com/global/global-bundle.pem"
        val cert = downloadCertificate(certUrl)

        val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
        keyStore.load(null, null)
        keyStore.setCertificateEntry("docdb-root", cert)

        val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        tmf.init(keyStore)

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, tmf.trustManagers, null)

        return sslContext
    }

    private fun downloadCertificate(url: String): X509Certificate {
        val certBytes = URL(url).readBytes()
        val cf = CertificateFactory.getInstance("X.509")
        return cf.generateCertificate(ByteArrayInputStream(certBytes)) as X509Certificate
    }

    @Bean
    fun mongoTemplate(): MongoTemplate = MongoTemplate(mongoClient(), databaseName)
}