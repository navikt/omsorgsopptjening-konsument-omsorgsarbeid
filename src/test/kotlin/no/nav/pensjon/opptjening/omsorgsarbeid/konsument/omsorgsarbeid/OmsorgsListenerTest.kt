package no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid

import no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid.OmsorgsListenerTest.Companion.OMSORGSARBEID_TOPIC
import no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid.OmsorgsListenerTest.Companion.OMSORGSOPPTJENING_TOPIC
import no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid.common.KafkaIntegrationTestConfig
import no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid.common.OmsorgsopptjeningMockListener
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.KafkaHeaderKey
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.KafkaMessageType
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.App
import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.OmsorgsarbeidListener
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import java.nio.charset.StandardCharsets.UTF_8
import kotlin.test.assertEquals

@EmbeddedKafka(partitions = 1, topics = [OMSORGSARBEID_TOPIC, OMSORGSOPPTJENING_TOPIC])
@SpringBootTest(classes = [App::class])
@Import(KafkaIntegrationTestConfig::class, OmsorgsopptjeningMockListener::class)
internal class OmsorgsListenerTest {

    @Autowired
    lateinit var embeddedKafka: EmbeddedKafkaBroker

    @Autowired
    lateinit var omsorgsArbeidListener: OmsorgsarbeidListener

    @Autowired
    lateinit var omsorgsarbeidProducer: KafkaTemplate<String, String>

    @Autowired
    lateinit var omsorgsopptjeingListener: OmsorgsopptjeningMockListener


    @Test
    fun `given omsorgsarbeid event then produce omsorgsopptjening event`() {
        omsorgsarbeidProducer.send(OMSORGSARBEID_TOPIC, omsorgsMeldingKey(), omsorgsMeldingValue())

        val record = omsorgsopptjeingListener.getRecord(20)

        assertNotNull(record)
        assertEquals(omsorgsMeldingKey(), record!!.key())
        assertEquals(omsorgsMeldingValue(), record.value())
        assertEquals(KafkaMessageType.OMSORGSARBEID.name, record.getHeader(KafkaHeaderKey.MESSAGE_TYPE))
    }

    fun ConsumerRecord<String, String>.getHeader(key:String ) = String(headers().headers(key).first().value(), UTF_8)

    fun omsorgsMeldingKey(omsorgsyter: String = "12345678910", ar: String = "2020") =
        """
        {
            "omsorgsyterFnr": "12345678910",
            "omsorgsAr": "2005"
        }
        """.trimIndent()


    fun omsorgsMeldingValue(
        omsorgsyter: String = "12345678910",
        ar: String = "2020",
        hash: String = """2023-01-19T15:55:35.766223643"""
    ) =
        """{"omsorgsyter":{"fnr":"$omsorgsyter","utbetalingsperioder":[{"fom":"2022-01","tom":"2022-06","omsorgsmottaker":{"fnr":"11111111111"}}]},"omsorgsAr":"$ar","hash":"$hash"}""".trimIndent()


    companion object {
        const val OMSORGSARBEID_TOPIC = "omsorgsarbeid"
        const val OMSORGSOPPTJENING_TOPIC = "omsorgsopptjening"
    }
}