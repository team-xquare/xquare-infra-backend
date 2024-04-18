package xquare.app.xquareinfra.infrastructure.kubernetes

import io.kubernetes.client.openapi.ApiException
import io.kubernetes.client.openapi.apis.CoreV1Api
import io.kubernetes.client.openapi.apis.CustomObjectsApi
import io.kubernetes.client.openapi.models.V1DeleteOptions
import org.springframework.stereotype.Service
import xquare.app.xquareinfra.infrastructure.exception.BusinessLogicException
import xquare.app.xquareinfra.infrastructure.exception.CriticalException

@Service
class KubernetesClientUtilImpl(
    private val customObjectsApi: CustomObjectsApi,
    private val coreV1Api: CoreV1Api
): KubernetesClientUtil {
    override fun deleteSecret(namespace: String, crName: String) {
        val v1DeleteOption = V1DeleteOptions()
        try {
            customObjectsApi.deleteNamespacedCustomObject(
                "secrets.hashicorp.com",
                "v1beta1",
                namespace,
                "vaultdynamicsecrets",
                "$crName-secret",
                null,
                null,
                null,
                null,
                v1DeleteOption
            )
        } catch (e: Exception) {
            println(e.printStackTrace())
            throw CriticalException(500, "Kubernetes Exception")
        }
    }

    override fun checkContainerStatus(deploymentName: String, namespace: String): String {
        try {
            val podList = coreV1Api.listNamespacedPod(
                namespace,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            )
            podList.items.forEach { pod ->
                if (pod.metadata?.name?.startsWith(deploymentName) == true) {
                    return pod.status?.phase ?: "배포되지 않았습니다."
                }
            }
        } catch (e: ApiException) {
            e.printStackTrace()
        }
        return "배포되지 않았습니다."
    }
}