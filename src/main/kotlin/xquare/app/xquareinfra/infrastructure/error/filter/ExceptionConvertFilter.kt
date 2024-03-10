package xquare.app.xquareinfra.infrastructure.error.filter

import xquare.app.xquareinfra.infrastructure.exception.XquareException
import xquare.app.xquareinfra.infrastructure.exception.PresentationValidationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.filter.GenericFilterBean
import org.springframework.web.util.NestedServletException
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.validation.ConstraintViolationException

class ExceptionConvertFilter : GenericFilterBean() {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        try {
            chain.doFilter(request, response)
        } catch (e: NestedServletException) {
            throw when (e.cause) {
                is XquareException -> e.cause as XquareException

                is MethodArgumentNotValidException ->
                    (e.cause as MethodArgumentNotValidException).run {
                        PresentationValidationException(
                            400,
                            "field : ${fieldError?.field}\nparameter : $parameter",
                            mapOf()
                        )
                    }

                is ConstraintViolationException ->
                    (e.cause as ConstraintViolationException).run {
                        PresentationValidationException(
                            status = 400,
                            message = message ?: "",
                            fields = constraintViolations.associate {
                                Pair<String, String>(it.propertyPath.toString(), it.message)
                            }
                        )
                    }

                is MissingServletRequestParameterException ->
                    (e.cause as MissingServletRequestParameterException).run {
                        PresentationValidationException(
                            status = 400,
                            message = message,
                            fields = mapOf(Pair(parameterName, localizedMessage))
                        )
                    }

                else -> e.cause ?: e
            }
        }
    }
}
