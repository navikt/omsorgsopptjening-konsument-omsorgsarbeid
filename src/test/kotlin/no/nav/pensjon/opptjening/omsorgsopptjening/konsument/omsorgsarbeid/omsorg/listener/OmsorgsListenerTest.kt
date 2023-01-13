package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.listener

import no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid.config.IntegrationtestConfig
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.App
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.listener.OmsorgsListenerTest.Companion.OMSORG_TOPIC
import org.apache.kafka.clients.producer.KafkaProducer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka

@EmbeddedKafka(partitions = 1, topics = [OMSORG_TOPIC])
@SpringBootTest(classes = [App::class])
@Import(IntegrationtestConfig::class)
internal class OmsorgsListenerTest {

    @Autowired
    lateinit var embeddedKafka: EmbeddedKafkaBroker

    @Autowired
    lateinit var omsorgsListener: OmsorgsListener

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, String>


    @Test
    fun `should call journalforing with expected json`() {
        kafkaTemplate.send(OMSORG_TOPIC, "apepay", "sfsd")

        println("")
    }

    companion object {
        const val OMSORG_TOPIC = "omsorgsarbeid"
    }
}