package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.listener

import no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg.listener.OmsorgsListenerTest.Companion.OMSORG_TOPIC
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.ContainerProperties
import org.springframework.kafka.listener.KafkaMessageListenerContainer
import org.springframework.kafka.listener.MessageListener
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.kafka.test.utils.KafkaTestUtils
import org.springframework.test.annotation.DirtiesContext
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = [OMSORG_TOPIC])
internal class OmsorgsListenerTest {

    @Autowired
    lateinit var embeddedKafka: EmbeddedKafkaBroker

    @Autowired
    lateinit var omsorgsListener: OmsorgsListener

    @Test
    fun `should call journalforing with expected json`() {
        // Vent til kafka er klar
        val container = settOppUtitlityConsumer()
        container.start()
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.partitionsPerTopic)

        // Sett opp producer
        val sedSendtProducerTemplate = settOppProducerTemplate()

        // produserer sedSendt meldinger på kafka
        produserSedHendelser(sedSendtProducerTemplate)

        // Venter på at sedListener skal consumeSedSendt meldingene
     //   omsorgsListener.getLatchSendt().await(15000, TimeUnit.MILLISECONDS)


    }

    private fun settOppProducerTemplate(): KafkaTemplate<Int, String> {
        return KafkaTemplate<Int, String>(DefaultKafkaProducerFactory(KafkaTestUtils.producerProps(embeddedKafka.brokersAsString))).apply {
            defaultTopic = OMSORG_TOPIC
        }
    }

    private fun settOppUtitlityConsumer(): KafkaMessageListenerContainer<String, String> {
        val consumerProperties = KafkaTestUtils.consumerProps("eessi-pensjon-group2",
            "false",
            embeddedKafka)
        consumerProperties["auto.offset.reset"] = "earliest"

        val consumerFactory = DefaultKafkaConsumerFactory<String, String>(consumerProperties)
        val containerProperties = ContainerProperties(OMSORG_TOPIC)
        val container = KafkaMessageListenerContainer(consumerFactory, containerProperties)
        val messageListener = MessageListener<String, String> { record -> println("Konsumerer melding:  $record") }
        container.setupMessageListener(messageListener)

        return container
    }

    private fun produserSedHendelser(sedSendtProducerTemplate: KafkaTemplate<Int, String>) {
        // Sender 1 Pensjon SED til Kafka
        sedSendtProducerTemplate.sendDefault(String(Files.readAllBytes(Paths.get("src/test/resources/sed/P_BUC_01.json"))))
    }

    companion object {
        const val OMSORG_TOPIC = "omsorg-topic"
    }

}