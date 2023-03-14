package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg

import org.apache.kafka.clients.consumer.ConsumerRecord
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.stereotype.Component

@Component
class OmsorgsarbeidListener(
    private val metric: OmsopptjeningKonsumentMetric,
    private val omsorgOpptjeningProducer: OmsorgopptjeningProducer
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
        metric.incrementRead()
        SECURE_LOG.info("Konsumerer omsorgsmelding: ${consumerRecord.key()}, ${consumerRecord.value()}")

        convertToOmsorgsArbeid(consumerRecord.value())
        convertToOmsorgsArbeidKey(consumerRecord.key())

        omsorgOpptjeningProducer.send(consumerRecord.key(), consumerRecord.value(), consumerRecord.headers())
        metric.incrementProduced()
        acknowledgment.acknowledge()
        metric.incrementConsumed()
    }

    companion object {
        private val SECURE_LOG = LoggerFactory.getLogger("secure")
    }
}