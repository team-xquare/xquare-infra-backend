package xquare.app.xquareinfra.infrastructure.telemetry.analyze

data class AnalysisResult(val level: Level, val message: String) {
    enum class Level {
        INFO, WARNING, ERROR
    }
}