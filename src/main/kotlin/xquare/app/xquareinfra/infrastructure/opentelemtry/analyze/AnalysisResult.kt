package xquare.app.xquareinfra.infrastructure.opentelemtry.analyze

data class AnalysisResult(val level: Level, val message: String) {
    enum class Level {
        INFO, WARNING, ERROR
    }
}