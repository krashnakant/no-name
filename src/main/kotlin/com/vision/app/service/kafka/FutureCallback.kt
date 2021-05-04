package com.vision.app.service.kafka

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.support.SendResult
import org.springframework.util.concurrent.ListenableFutureCallback

class FutureCallback(private val message: String) : ListenableFutureCallback<SendResult<String, String>> {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun onSuccess(result: SendResult<String, String>?) {
        logger.info("Success Callback: [{}] delivered with offset -{}", message, result?.recordMetadata?.offset())
    }

    override fun onFailure(ex: Throwable) {
        logger.warn("Failure Callback: Unable to deliver message [{}]. {}", message, ex.message)
    }
}
