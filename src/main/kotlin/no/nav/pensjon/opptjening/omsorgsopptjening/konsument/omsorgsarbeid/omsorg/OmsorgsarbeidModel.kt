package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.OmsorgsArbeidKey
import no.nav.pensjon.opptjening.omsorgsopptjening.felles.domene.kafka.messages.OmsorgsarbeidsSnapshot

private val objectMapper = jacksonObjectMapper().registerModule(JavaTimeModule()).disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

internal fun convertToOmsorgsArbeid(omsorgsarbeidsSnapshot: String) =
    objectMapper.readValue(omsorgsarbeidsSnapshot, OmsorgsarbeidsSnapshot::class.java)
internal fun convertToOmsorgsArbeidKey(omsorgsarbeidsSnapshot: String) =
    objectMapper.readValue(omsorgsarbeidsSnapshot, OmsorgsArbeidKey::class.java)