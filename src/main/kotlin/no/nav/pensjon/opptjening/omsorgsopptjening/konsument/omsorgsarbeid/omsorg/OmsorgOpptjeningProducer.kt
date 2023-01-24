package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class OmsorgOpptjeningProducer(
    @Value("\${OMSORGSOPPTJENING_TOPIC}") private val omsorgsOpptjeningTopic: String,
    private val kafkaProducer: KafkaTemplate<String, String>,
) {

    fun send(key: String, value: String) {
        kafkaProducer.send(omsorgsOpptjeningTopic, key, value)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(OmsorgsArbeidListener::class.java)
    }
}