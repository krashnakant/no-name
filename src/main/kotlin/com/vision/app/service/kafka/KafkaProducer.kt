package com.vision.app.service.kafka

import com.vision.app.service.Publisher
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture

@Service
class KafkaProducer(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    @Value("\${spring.kafka.template.default-topic}") private val kafkaTopic: String
) : Publisher {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    @Async
    override fun publish(message: String) {
        logger.info("Message {} is being sent to topic {}", message, kafkaTopic)
        val future: ListenableFuture<SendResult<String, String>> = kafkaTemplate.send(kafkaTopic, message)
        future.addCallback(FutureCallback(message = message))
    }
}
