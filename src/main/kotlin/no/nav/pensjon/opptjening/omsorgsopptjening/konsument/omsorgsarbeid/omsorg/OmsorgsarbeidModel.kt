package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.omsorg

data class OmsorgsMelding(val omsorgsyter: String, val ar: String, val hash: String)
data class OmsorgsMeldingKey(val omsorgsyter: String, val ar: String)