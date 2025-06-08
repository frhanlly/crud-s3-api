package br.com.hanlly.s3api.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    public String kafkaServer;
    @Value("${spring.kafka.topics.bucket-topic}")
    public String bucketTopic;
    @Value("${spring.kafka.topics.object-topic}")
    public String objectTopic;


    @Bean
    public ProducerFactory<String, String> producerFactory(){

        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);


        return new DefaultKafkaProducerFactory<String, String>(props);

    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(){
        return new KafkaTemplate<String, String>(producerFactory());
    }

    @Bean
    public NewTopic createBucketTopic(){
        return TopicBuilder
                .name(bucketTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }


    @Bean
    public NewTopic createObjectTopic(){
        return TopicBuilder
                .name(objectTopic)
                .replicas(1)
                .partitions(1)
                .build();

    }


}
