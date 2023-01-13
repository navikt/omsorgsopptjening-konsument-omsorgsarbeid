package no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid

import no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid.config.IntegrationtestConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [ IntegrationtestConfig::class])
@EmbeddedKafka(count = 1, controlledShutdown = true, brokerProperties= ["log.dir=out/embedded-kafkaomsorgsarbeid"])
@ActiveProfiles("integrationtest")
class SpringAppTest {

    @Test
    fun StartApp() {
    }
}