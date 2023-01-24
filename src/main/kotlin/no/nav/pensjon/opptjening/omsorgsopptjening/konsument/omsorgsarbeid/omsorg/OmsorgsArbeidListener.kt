package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.micrometer.core.instrument.MeterRegistry
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class OmsorgsArbeidListener(
    private val registry: MeterRegistry,
    private val kafkaProducer: KafkaTemplate<String, String>,
    @Value("\${OMSORGSOPPTJENING_TOPIC}") private val omsorgsOpptjeningTopic: String
) {
    private val antallLesteMeldinger = registry.counter("omsorgsArbeidListener", "antall", "lest")
    private val antallProduserteMeldinger = registry.counter("omsorgsArbeidListener", "antall", "produsert")
    private val antallKonsumerteMeldinger = registry.counter("omsorgsArbeidListener", "antall", "konsumert")

    @KafkaListener(
        containerFactory = "omsorgsArbeidKafkaListenerContainerFactory",
        idIsGroup = false,
        topics = ["\${OMSORGSARBEID_TOPIC}"],
        groupId = "\${OMSORGP_GODSKRIVING_GROUP_ID}"
    )
    fun consumeOmsorgPGodskriving(
        hendelse: String,
        consumerRecord: ConsumerRecord<String, String>,
        acknowledgment: Acknowledgment
    ) {
        antallLesteMeldinger.increment()
        logger.info("Konsumerer omsorgsmelding: ${consumerRecord.key()}, ${consumerRecord.value()}")

        jacksonObjectMapper().readValue(consumerRecord.value(), OmsorgsArbeid::class.java)
        jacksonObjectMapper().readValue(consumerRecord.key(), OmsorgsArbeidKey::class.java)

        kafkaProducer.send(omsorgsOpptjeningTopic, consumerRecord.key(), consumerRecord.value())
        antallProduserteMeldinger.increment()
        acknowledgment.acknowledge()
        antallKonsumerteMeldinger.increment()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OmsorgsArbeidListener::class.java)
    }
}