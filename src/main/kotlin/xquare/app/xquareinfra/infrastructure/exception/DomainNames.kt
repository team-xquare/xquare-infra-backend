package xquare.app.xquareinfra.infrastructure.exception

enum class DomainNames(
    val value: String,
    val lowCaseValue: String
) {
    USER("User", "user"),
    TEAM("Team", "team"),
    USER_TEAM("User_Team", "user_team"),
    DEPLOY("Deploy", "deploy")
}
