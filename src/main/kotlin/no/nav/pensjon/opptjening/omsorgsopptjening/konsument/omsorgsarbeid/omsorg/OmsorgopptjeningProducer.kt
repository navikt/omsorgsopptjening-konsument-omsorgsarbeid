package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg

import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.KafkaHeaderKey.Companion.MESSAGE_TYPE
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.KafkaMessageType
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.header.Header
import org.apache.kafka.common.header.Headers
import org.apache.kafka.common.header.internals.RecordHeader
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

@Component
class OmsorgopptjeningProducer(
    @Value("\${OMSORGSOPPTJENING_TOPIC}") private val omsorgsOpptjeningTopic: String,
    private val kafkaProducer: KafkaTemplate<String, String>,
) {

    fun send(key: String, value: String, headers: Headers) {
        val pr = ProducerRecord(omsorgsOpptjeningTopic, null, null, key, value, populerEllerVideresendHeader(headers))

        kafkaProducer.send(pr).get(1, TimeUnit.SECONDS)
    }

    private fun populerEllerVideresendHeader(headers: Headers): Iterable<Header>  {
        if (headers.getMessageTypeHeaders().isNotEmpty()) {
            return headers
        }
        return mutableListOf(RecordHeader(MESSAGE_TYPE,KafkaMessageType.OMSORGSARBEID.name.encodeToByteArray()))
    }

    private fun Headers.getMessageTypeHeaders() =
        headers(MESSAGE_TYPE)
            .map { String(it.value(), StandardCharsets.UTF_8) }
}