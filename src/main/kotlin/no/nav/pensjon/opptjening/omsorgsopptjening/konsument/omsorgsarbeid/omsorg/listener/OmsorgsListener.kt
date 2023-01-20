package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.listener

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.OmsorgsMelding
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.OmsorgsMeldingKey
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class OmsorgsListener {
    @KafkaListener(
        containerFactory = "omsorgsArbeidKafkaListenerContainerFactory",
        idIsGroup = false,
        topics = ["\${OMSORGSARBEID_TOPIC}"],
        groupId = "\${OMSORGP_GODSKRIVING_GROUP_ID}"
    )
    fun consumeOmsorgPGodskriving(hendelse: String, consumerRecord: ConsumerRecord<String, String>, acknowledgment: Acknowledgment) {
        logger.info("Konsumerer omsorgsmelding: ${consumerRecord.key()}, ${consumerRecord.value()}")

        jacksonObjectMapper().readValue(consumerRecord.value(), OmsorgsMelding::class.java)
        jacksonObjectMapper().readValue(consumerRecord.key(), OmsorgsMeldingKey::class.java)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OmsorgsListener::class.java)
    }
}