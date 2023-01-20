package no.nav.pensjon.opptjening.omsorgsopptjening.konsument.omsorgsarbeid.config


import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.config.SslConfigs
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.listener.ContainerProperties
import java.time.Duration
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory


@EnableKafka
@Profile("dev-gcp")
@Configuration
class KafkaConfig(
    @Value("\${kafka.keystore.path}") private val keystorePath: String,
    @Value("\${kafka.credstore.password}") private val credstorePassword: String,
    @Value("\${kafka.truststore.path}") private val truststorePath: String,
    @Value("\${kafka.brokers}") private val aivenBootstrapServers: String,
) {

    @Bean
    fun omsorgsArbeidKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String>? =
        ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            consumerFactory = kafkaConsumerFactory()
            containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
            containerProperties.setAuthExceptionRetryInterval(Duration.ofSeconds(4L))
        }

    @Bean
    fun omsorgsopptjeningProducerKafkaTemplate(): KafkaTemplate<String, String> =
        KafkaTemplate(DefaultKafkaProducerFactory(omsorgsopptjeningProducerConfig() + securityConfig()))

    fun kafkaConsumerFactory() =
        DefaultKafkaConsumerFactory(consumerConfig() + securityConfig(), StringDeserializer(), StringDeserializer())

    private fun consumerConfig() = mapOf(
        ConsumerConfig.CLIENT_ID_CONFIG to "omsorgsopptjening-konsument-omsorgsarbeid",
        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to aivenBootstrapServers,
        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ConsumerConfig.MAX_POLL_RECORDS_CONFIG to 1,
    )

    private fun omsorgsopptjeningProducerConfig() = mapOf(
        ProducerConfig.CLIENT_ID_CONFIG to "omsorgsopptjening-producer-omsorgsarbeid",
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to aivenBootstrapServers,
    )

    private fun securityConfig() = mapOf(
        SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG to keystorePath,
        SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG to credstorePassword,
        SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG to credstorePassword,
        SslConfigs.SSL_KEY_PASSWORD_CONFIG to credstorePassword,
        SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG to "JKS",
        SslConfigs.SSL_KEYSTORE_TYPE_CONFIG to "PKCS12",
        SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG to truststorePath,
        CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to "SSL",
    )
}