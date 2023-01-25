package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg

import io.micrometer.core.instrument.MeterRegistry
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class OmsorgsarbeidListener(
    registry: MeterRegistry, private val
    omsorgOpptjeningProducer: OmsorgopptjeningProducer
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
        SECURE_LOG.info("Konsumerer omsorgsmelding: ${consumerRecord.key()}, ${consumerRecord.value()}")

        convertToOmsorgsArbeid(consumerRecord.value())
        convertToOmsorgsArbeidKey(consumerRecord.key())

        omsorgOpptjeningProducer.send(consumerRecord.key(), consumerRecord.value())
        antallProduserteMeldinger.increment()
        acknowledgment.acknowledge()
        antallKonsumerteMeldinger.increment()
    }

    companion object {
        private val SECURE_LOG = LoggerFactory.getLogger("secureLog")
    }
}