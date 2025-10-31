package ru.otus.project.coffee.order.config.kafka;

import static org.springframework.kafka.support.serializer.JsonDeserializer.TYPE_MAPPINGS;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Objects;
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
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import ru.otus.project.coffee.order.dto.kafka.RoastResult;
import ru.otus.project.coffee.order.model.OrderStatus;
import ru.otus.project.coffee.order.service.NotificationService;
import ru.otus.project.coffee.order.service.OrderService;

@Slf4j
@Configuration
public class KafkaConsumerConfig {

    public final String topicResponse;

    public KafkaConsumerConfig(@Value("${application.kafka.topic-response}") String topicResponse) {
        this.topicResponse = topicResponse;
    }

    @Bean
    public ConsumerFactory<String, RoastResult> consumerFactory(KafkaProperties kafkaProperties, ObjectMapper mapper) {
        var props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(
                TYPE_MAPPINGS,
                "ru.otus.project.coffee.roast.dto.kafka.RoastResult:ru.otus.project.coffee.order.dto.kafka.RoastResult");
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 3);
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, 3_000);

        var kafkaConsumerFactory = new DefaultKafkaConsumerFactory<String, RoastResult>(props);
        kafkaConsumerFactory.setValueDeserializer(new JsonDeserializer<>(mapper));
        return kafkaConsumerFactory;
    }

    @Bean("listenerContainerFactory")
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, RoastResult>>
            listenerContainerFactory(ConsumerFactory<String, RoastResult> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, RoastResult>();
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
    public NewTopic topicRs() {
        return TopicBuilder.name(topicResponse).partitions(1).replicas(1).build();
    }

    @Bean
    public KafkaClient coffeeOrderConsumer(OrderService orderService, NotificationService notificationService) {
        return new KafkaClient(orderService, notificationService);
    }

    public static class KafkaClient {

        private final OrderService orderService;

        private final NotificationService notificationService;

        public KafkaClient(OrderService orderService, NotificationService notificationService) {
            this.orderService = orderService;
            this.notificationService = notificationService;
        }

        @KafkaListener(topics = "${application.kafka.topic-response}", containerFactory = "listenerContainerFactory")
        public void listen(@Payload List<RoastResult> roastResults) {
            log.info("roastResults.size:{}", roastResults.size());
            var nonNullResults = roastResults.stream().filter(Objects::nonNull).toList();
            orderService.changeOrdersStatus(nonNullResults, OrderStatus.READY);
            try {
                notificationService.notify(nonNullResults);
            } catch (Exception e) {
                log.error("Notification error", e);
            }
        }
    }
}
