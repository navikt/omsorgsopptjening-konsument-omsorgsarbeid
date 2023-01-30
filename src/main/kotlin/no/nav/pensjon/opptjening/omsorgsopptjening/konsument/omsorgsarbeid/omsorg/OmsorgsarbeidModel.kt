package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.time.YearMonth

private val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

data class OmsorgsArbeid(val omsorgsyter: Omsorgsyter, val omsorgsAr: String, val hash: String)
data class Omsorgsyter(val fnr: String, val utbetalingsperioder: List<UtbetalingsPeriode>)
data class UtbetalingsPeriode(val fom: YearMonth, val tom: YearMonth, val omsorgsmottaker: OmsorgsMottaker)
data class OmsorgsMottaker(val fnr: String)
data class OmsorgsArbeidKey(val omsorgsyterFnr: String, val omsorgsAr: String)

internal fun convertToOmsorgsArbeid(omsorgsArbeid: String) =
    objectMapper.readValue(omsorgsArbeid, OmsorgsArbeid::class.java)
internal fun convertToOmsorgsArbeidKey(omsorgsArbeid: String) =
    objectMapper.readValue(omsorgsArbeid, OmsorgsArbeidKey::class.java)