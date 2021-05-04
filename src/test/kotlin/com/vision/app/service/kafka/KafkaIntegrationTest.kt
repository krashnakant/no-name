package com.vision.app.service.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.vision.app.service.MessageService
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.annotation.DirtiesContext
import java.util.concurrent.TimeUnit

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = ["listeners=PLAINTEXT://localhost:9092", "port=9092"])
internal class KafkaIntegrationTest constructor(
    @Autowired final val messageService: MessageService,
    @Autowired objectMapper: ObjectMapper,
    @Autowired val kafkaTemplate: KafkaTemplate<String, String>
) {

    @Value("\${test.topic}")
    private val topic: String? = null

    private val consumer = KafkaConsumer(messageService, objectMapper)

    private val producer = topic?.let { KafkaProducer(kafkaTemplate, it) }

    @Test
    @Throws(Exception::class)
    fun givenEmbeddedKafkaBroker_whenSendingToSimpleProducer_thenMessageReceived() {
        producer?.publish("Sending with own simple KafkaProducer")
        consumer.getCountDownLatch().await(10000, TimeUnit.MILLISECONDS)
        assertThat(consumer.getCountDownLatch().count, equalTo(0L))
    }

    @Test
    @Throws(Exception::class)
    fun givenEmbeddedKafkaBroker_whenSendingToSimpleTemplate_thenMessageReceived() {
        if (topic != null) {
            kafkaTemplate.send(topic, "Sending with our own simple KafkaProducer")
        }
        consumer.getCountDownLatch().await(10000, TimeUnit.MILLISECONDS)
        assertThat(consumer.getCountDownLatch().count, equalTo(0L))
    }
}
