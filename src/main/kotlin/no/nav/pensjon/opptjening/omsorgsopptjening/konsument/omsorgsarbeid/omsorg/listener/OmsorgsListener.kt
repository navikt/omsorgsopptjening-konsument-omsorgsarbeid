package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.listener

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.OmsorgsArbeid
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.OmsorgsArbeidKey
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class OmsorgsListener(
    private val kafkaProducer: KafkaTemplate<String, String>,
    @Value("\${OMSORGSOPPTJENING_TOPIC}") private val omsorgsOpptjeningTopic: String
) {
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
        logger.info("Konsumerer omsorgsmelding: ${consumerRecord.key()}, ${consumerRecord.value()}")

        jacksonObjectMapper().readValue(consumerRecord.value(), OmsorgsArbeid::class.java)
        jacksonObjectMapper().readValue(consumerRecord.key(), OmsorgsArbeidKey::class.java)

        kafkaProducer.send(omsorgsOpptjeningTopic, "test", "test")
        acknowledgment.acknowledge()
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OmsorgsListener::class.java)
    }
}