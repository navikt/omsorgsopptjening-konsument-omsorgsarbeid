package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg

import java.time.LocalDate

data class OmsorgsArbeidKey(val omsorgsyterFnr: String, val ar: String)
data class OmsorgsArbeid(val omsorgsyter: Omsorgsyter, val ar: String, val hash: String)
data class Omsorgsyter(val fnr: String, val utbetalingsperioder: List<UtbetalingsPeriode>)
data class UtbetalingsPeriode(val fom: LocalDate, val tom: LocalDate, val omsorgsmottaker: OmsorgsMottaker)
data class OmsorgsMottaker(val fnr: String)

