package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.listener

import no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid.config.IntegrationtestConfig
import no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid.config.OmsorgsopptjeningListener
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.App
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.listener.OmsorgsListenerTest.Companion.OMSORGSARBEID_TOPIC
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka

@EmbeddedKafka(partitions = 1, topics = [OMSORGSARBEID_TOPIC])
@SpringBootTest(classes = [App::class])
@Import(IntegrationtestConfig::class, OmsorgsopptjeningListener::class)
internal class OmsorgsListenerTest {

    @Autowired
    lateinit var embeddedKafka: EmbeddedKafkaBroker

    @Autowired
    lateinit var omsorgsListener: OmsorgsListener

    @Autowired
    lateinit var kafkaTemplate: KafkaTemplate<String, String>

    @Autowired
    lateinit var omsorgsopptjeingListener: OmsorgsopptjeningListener


    @Test
    fun `given omsorgsarbeid event then produce omsorgsopptjening event`() {
        kafkaTemplate.send(OMSORGSARBEID_TOPIC, omsorgsMeldingKey(), omsorgsMeldingValue())





        println("")
    }

    fun omsorgsMeldingKey(omsorgsyter: String = "12345678910", ar: String = "2020") =
        """{"omsorgsyter":"$omsorgsyter","ar":"$ar"}"""


    fun omsorgsMeldingValue(
        omsorgsyter: String = "12345678910",
        ar: String = "2020",
        hash: String = """2023-01-19T15:55:35.766223643"""
    ) =
        """{ "omsorgsyter":"$omsorgsyter", "ar":"$ar", "hash":"$hash" }"""


    companion object {
        const val OMSORGSARBEID_TOPIC = "omsorgsarbeid"
    }
}

// {"omsorgsyter":"12345678910","ar":"2002"}, {"omsorgsyter":"12345678910","ar":"2002","hash":"2023-01-19T15:55:35.766223643"}