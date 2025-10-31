package ru.otus.project.coffee.roast.config.kafka;

import static org.springframework.kafka.support.serializer.JsonDeserializer.TYPE_MAPPINGS;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.JacksonUtils;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import ru.otus.project.coffee.roast.dto.kafka.RoastDto;
import ru.otus.project.coffee.roast.service.RoastService;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

    public final String topicRequest;

    public KafkaConsumerConfig(@Value("${application.kafka.topic-request}") String topicRequest) {
        this.topicRequest = topicRequest;
    }

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtils.enhancedObjectMapper();
    }

    @Bean
    public ConsumerFactory<String, RoastDto> consumerFactory(KafkaProperties kafkaProperties, ObjectMapper mapper) {
        var props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(
                TYPE_MAPPINGS,
                "ru.otus.project.coffee.order.dto.kafka.RoastDto:ru.otus.project.coffee.roast.dto.kafka.RoastDto");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 3_000);

        var kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, RoastDto>(props);
        kafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(mapper));
        return kafkaConsumerFactory;
    }

    @Bean("listenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RoastDto>> listenerContainerFactory(
            ConsumerFactory<String, RoastDto> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, RoastDto>();
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setIdleBetweenPolls(1_000);
        factory.getContainerProperties().setPollTimeout(1_000);

        var executor = new SimpleAsyncTaskExecutor("k-consumer-");
        executor.setConcurrencyLimit(10);
        var listenerTaskExecutor = new ConcurrentTaskExecutor(executor);
        factory.getContainerProperties().setListenerTaskExecutor(listenerTaskExecutor);
        return factory;
    }

    @Bean
    public NewTopic topicRq() {
        return TopicBuilder.name(topicRequest).partitions(1).replicas(1).build();
    }

    @Bean
    public KafkaClient coffeeRoastConsumer(RoastService roastService) {
        return new KafkaClient(roastService);
    }

    public static class KafkaClient {
        private final RoastService roastService;

        public KafkaClient(RoastService roastService) {
            this.roastService = roastService;
        }

        @KafkaListener(topics = "${application.kafka.topic-request}", containerFactory = "listenerContainerFactory")
        public void listen(@Payload List<RoastDto> roastDtos) {
            log.info("roastDtos.size:{}", roastDtos.size());
            roastService.saveRoastTasks(roastDtos);
        }
    }
}
