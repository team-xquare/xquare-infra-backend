package xquare.app.xquareinfra.infrastructure.kubernetes.config

import io.kubernetes.client.openapi.Configuration
import io.kubernetes.client.util.ClientBuilder
import io.kubernetes.client.util.KubeConfig
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import software.amazon.awssdk.regions.Region
import xquare.app.xquareinfra.infrastructure.kubernetes.env.KubernetesProperty
import xquare.app.xquareinfra.infrastructure.kubernetes.env.XquareProperties
import java.io.StringReader
import java.nio.charset.Charset
import java.util.*

@org.springframework.context.annotation.Configuration
@EnableScheduling
class KubernetesTokenRefreshScheduler(
    private val xquareProperties: XquareProperties,
    private val kubernetesProperty: KubernetesProperty
) {
    @Scheduled(fixedRate = 14 * 60 * 1000) // 14 minutes
    fun refreshKubernetesToken() {
        configureAWS("default", xquareProperties.accessKey, xquareProperties.secretKey, Region.AP_NORTHEAST_2.toString())
        val decodedBytes = Base64.getDecoder().decode(kubernetesProperty.kubeConfig)
        val kubeconfig = String(decodedBytes, Charset.defaultCharset())
        val client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(StringReader(kubeconfig))).build()
        Configuration.setDefaultApiClient(client)
    }

    private fun configureAWS(profileName: String, accessKeyId: String, secretAccessKey: String, region: String) {
        try {
            val processBuilder = ProcessBuilder()
            processBuilder.command("aws", "configure", "set", "aws_access_key_id", accessKeyId, "--profile", profileName)
            var process = processBuilder.start()
            process.waitFor()

            processBuilder.command("aws", "configure", "set", "aws_secret_access_key", secretAccessKey, "--profile", profileName)
            process = processBuilder.start()
            process.waitFor()

            processBuilder.command("aws", "configure", "set", "region", region, "--profile", profileName)
            process = processBuilder.start()
            process.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}