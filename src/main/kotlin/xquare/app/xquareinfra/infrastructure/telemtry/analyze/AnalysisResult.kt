package xquare.app.xquareinfra.infrastructure.telemtry.analyze

data class AnalysisResult(val level: Level, val message: String) {
    enum class Level {
        INFO, WARNING, ERROR
    }
}