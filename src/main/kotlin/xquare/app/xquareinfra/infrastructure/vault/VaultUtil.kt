package xquare.app.xquareinfra.infrastructure.vault

interface VaultUtil {
    fun addSecret(secrets: Map<String, String>, path: String)
//    fun revoke(path: String)
}