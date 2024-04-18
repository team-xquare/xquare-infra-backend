package xquare.app.xquareinfra.infrastructure.vault.env

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("vault")
@ConstructorBinding
data class VaultProperty(
    val address: String,
    val vaultToken: String
)