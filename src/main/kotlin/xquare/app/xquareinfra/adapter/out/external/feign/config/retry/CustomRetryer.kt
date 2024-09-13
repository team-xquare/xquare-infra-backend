package xquare.app.xquareinfra.adapter.out.external.feign.config.retry

import feign.RetryableException
import feign.Retryer
import org.apache.http.client.HttpResponseException

class CustomRetryer(
    retryInterval: Long = 1000,
    maxAttempts: Int = 3
) : Retryer.Default(retryInterval, retryInterval * 2, maxAttempts) {

    override fun continueOrPropagate(e: RetryableException) {
        if (shouldRetry(e)) {
            super.continueOrPropagate(e)
        } else {
            throw e
        }
    }

    private fun shouldRetry(e: RetryableException): Boolean {
        val statusCode = (e.cause as? HttpResponseException)?.statusCode ?: return false
        return statusCode == 503
    }
}