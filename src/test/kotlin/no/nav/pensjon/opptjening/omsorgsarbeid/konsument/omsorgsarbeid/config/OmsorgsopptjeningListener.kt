package no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid.config

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.OmsorgsArbeid
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.OmsorgsArbeidKey
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class OmsorgsopptjeningListener {
    @KafkaListener(
        containerFactory = "omsorgsArbeidKafkaListenerContainerFactory",
        idIsGroup = false,
        topics = ["\${OMSORGSOPPTJENING_TOPIC}"],
        groupId = "TEST"
    )
    fun consumeOmsorgPGodskriving(hendelse: String, consumerRecord: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        logger.info("Konsumerer omsorgsmelding: ${consumerRecord.key()}, ${consumerRecord.value()}")

        jacksonObjectMapper().readValue(consumerRecord.value(), OmsorgsArbeid::class.java)
        jacksonObjectMapper().readValue(consumerRecord.key(), OmsorgsArbeidKey::class.java)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OmsorgsopptjeningListener::class.java)
    }
}