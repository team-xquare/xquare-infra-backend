package xquare.app.xquareinfra.infrastructure.error.filter

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import xquare.app.xquareinfra.infrastructure.error.log.ErrorLog
import xquare.app.xquareinfra.infrastructure.exception.*
import java.io.IOException
import java.lang.SecurityException
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ErrorLogResponseFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: XquareException) {
            val errorLog = printErrorLogAndReturn(e)

            if (e !is BusinessLogicException &&
                e !is SecurityException &&
                e !is FilterException &&
                e !is InterceptorException &&
                e !is AuthenticationException &&
                e !is PresentationValidationException
            ) {
                e.printStackTrace()
            }

            if (errorLog.exceptionClassName == PresentationValidationException::javaClass.name) {
                writeFieldErrorResponse(response, errorLog, e as PresentationValidationException)
            } else {
                writeCommonErrorResponse(response, errorLog)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            val errorLog = printErrorLogAndReturn(e)
            writeCommonErrorResponse(response, errorLog)
        }
    }

    private fun printErrorLogAndReturn(e: Exception): ErrorLog {
        when (e) {
            is PresentationValidationException -> e.run {
                ErrorLog(
                    exceptionClassName = javaClass.name,
                    errorOccurredClassName = stackTrace[2].className + " or " + stackTrace[1].className,
                    status = status,
                    message = message + fields
                )
            }

            is XquareException -> e.run {
                ErrorLog(
                    exceptionClassName = javaClass.name,
                    errorOccurredClassName = stackTrace[2].className + " or " + stackTrace[1].className,
                    status = status,
                    message = message
                )
            }

            else -> e.run {
                ErrorLog(
                    exceptionClassName = javaClass.name,
                    errorOccurredClassName = stackTrace[2].className + " or " + stackTrace[1].className,
                    message = message
                )
            }
        }.run {
            logger.info(
                "[$id] $status : \"$message\" in $errorOccurredClassName cause $exceptionClassName"
            )
            return this
        }
    }

    @Throws(IOException::class)
    private fun writeCommonErrorResponse(response: HttpServletResponse, errorLog: ErrorLog) {
        response.errorResponseDefaultSetting(errorLog)
        response.writeErrorLogResponse(
            errorLog.run {
                ErrorLogResponse(status, message, id, timestamp)
            }
        )
    }

    @Throws(IOException::class)
    private fun writeFieldErrorResponse(
        response: HttpServletResponse,
        errorLog: ErrorLog,
        e: PresentationValidationException
    ) {
        response.errorResponseDefaultSetting(errorLog)
        response.writeErrorLogResponse(
            errorLog.run {
                ErrorLogResponse(status, message, id, timestamp, e.fields)
            }
        )
    }

    private fun HttpServletResponse.errorResponseDefaultSetting(errorLog: ErrorLog) {
        status = errorLog.status
        characterEncoding = StandardCharsets.UTF_8.name()
        contentType = MediaType.APPLICATION_JSON_VALUE
    }

    private fun HttpServletResponse.writeErrorLogResponse(errorLogResponse: ErrorLogResponse) {
        writer.write(
            objectMapper.writeValueAsString(errorLogResponse)
        )
        writer.flush()
    }
}

data class ErrorLogResponse(
    val status: Int,
    val message: String?,
    val errorLogId: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val map: Map<String, String> = mapOf()
)
