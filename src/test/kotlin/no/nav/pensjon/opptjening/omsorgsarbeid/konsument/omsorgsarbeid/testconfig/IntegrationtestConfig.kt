package no.nav.pensjon.opptjening.omsorgsarbeid.konsument.omsorgsarbeid.testconfig


import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*
import org.springframework.kafka.listener.ContainerProperties
import java.time.Duration


@EnableKafka
@Configuration
class IntegrationtestConfig(
    @Value("\${spring.embedded.kafka.brokers}") private val bootstrapServers: String
) {
    @Bean
    fun producerFactory(): ProducerFactory<String, String> = DefaultKafkaProducerFactory(
        mapOf(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG to StringSerializer::class.java,
        )
    )

    @Bean
    fun kafkaTemplate(producer: ProducerFactory<String, String>): KafkaTemplate<String, String> {
        return KafkaTemplate(producer)
    }

    @Bean
    fun omsorgsArbeidKafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, String>? {
        return ConcurrentKafkaListenerContainerFactory<String, String>().apply {
            consumerFactory = kafkaConsumerFactory()
            containerProperties.ackMode = ContainerProperties.AckMode.MANUAL
            containerProperties.setAuthExceptionRetryInterval(Duration.ofSeconds(4L))
        }
    }

    fun kafkaConsumerFactory(): ConsumerFactory<String, String> {
        return DefaultKafkaConsumerFactory(mapOf(
                    CommonClientConfigs.SECURITY_PROTOCOL_CONFIG to "PLAINTEXT",
                    ConsumerConfig.CLIENT_ID_CONFIG to "eessi-pensjon-begrens-innsyn",
                    ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                    ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG to StringDeserializer::class.java,
                    ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG to bootstrapServers,
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
                    ConsumerConfig.MAX_POLL_RECORDS_CONFIG to 1,
                    ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG to false,
                    ConsumerConfig.AUTO_OFFSET_RESET_CONFIG to "earliest",
        ))
    }
}