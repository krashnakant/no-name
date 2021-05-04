package com.vision.app.service.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.vision.app.domain.Message
import com.vision.app.service.MessageService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import java.util.concurrent.CountDownLatch

@Component
class KafkaConsumer(val messageService: MessageService, private val objectMapper: ObjectMapper) {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val latch = CountDownLatch(0)

    @KafkaListener(topics = ["\${spring.kafka.template.default-topic}"], groupId = "group_id")
    fun receive(consumerRecord: ConsumerRecord<*, *>) {
        logger.info("received payload='{}'", consumerRecord.value().toString())
        messageService.save(objectMapper.readValue(consumerRecord.value().toString(), Message::class.java))
        latch.countDown()
    }

    fun getCountDownLatch(): CountDownLatch {
        return this.latch
    }
}
