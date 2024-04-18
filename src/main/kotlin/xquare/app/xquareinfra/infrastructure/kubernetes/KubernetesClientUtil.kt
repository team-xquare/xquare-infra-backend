package xquare.app.xquareinfra.infrastructure.kubernetes

interface KubernetesClientUtil {
    fun deleteSecret(namespace: String, crName: String)
    fun checkContainerStatus(deploymentName: String, namespace: String): String
}