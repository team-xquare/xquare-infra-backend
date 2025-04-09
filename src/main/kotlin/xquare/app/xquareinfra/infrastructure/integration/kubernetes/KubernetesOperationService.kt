package xquare.app.xquareinfra.infrastructure.integration.kubernetes

interface KubernetesOperationService {
    fun deleteSecret(namespace: String, crName: String)
    fun checkContainerStatus(deploymentName: String, namespace: String): String
}