package xquare.app.xquareinfra.infrastructure.vault

import com.bettercloud.vault.Vault
import org.springframework.stereotype.Service
import xquare.app.xquareinfra.infrastructure.exception.CriticalException

@Service
private class VaultUtilImpl(
    private val vault: Vault
): VaultUtil {

    companion object {
        const val XQUARE_PATH = "xquare-kv"
    }
    override fun addSecret(secrets: Map<String, String>, path: String) {
        val response = vault.logical().write("$XQUARE_PATH/$path", secrets)
        if(response.restResponse.status == 500) {
            throw CriticalException(500, "Vault Exception")
        }
    }
}