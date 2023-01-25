package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg

import io.micrometer.core.instrument.MeterRegistry
import org.springframework.stereotype.Component

@Component
class OmsopptjeningKonsumentMetric(registry: MeterRegistry) {
    private val antallLesteMeldinger = registry.counter("omsorgsArbeidListener", "antall", "lest")
    private val antallProduserteMeldinger = registry.counter("omsorgsArbeidListener", "antall", "produsert")
    private val antallKonsumerteMeldinger = registry.counter("omsorgsArbeidListener", "antall", "konsumert")

    fun incrementRead() = antallLesteMeldinger.increment()
    fun incrementProduced() = antallProduserteMeldinger.increment()
    fun incrementConsumed() = antallKonsumerteMeldinger.increment()
}