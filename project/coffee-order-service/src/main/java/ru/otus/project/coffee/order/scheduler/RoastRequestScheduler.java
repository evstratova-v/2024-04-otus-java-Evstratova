package ru.otus.project.coffee.order.scheduler;

import java.util.List;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.otus.project.coffee.order.dto.kafka.RoastDto;
import ru.otus.project.coffee.order.model.OrderStatus;
import ru.otus.project.coffee.order.service.OrderService;

@Slf4j
@Component
@EnableScheduling
@ConditionalOnProperty(prefix = "application.coffee-service", name = "enable-scheduling", havingValue = "true")
public class RoastRequestScheduler {

    private final String topicRequest;

    private final OrderService orderService;

    private final KafkaTemplate<String, RoastDto> template;

    public RoastRequestScheduler(
            @Value("${application.kafka.topic-request}") String topicRequest,
            OrderService orderService,
            KafkaTemplate<String, RoastDto> template) {
        this.topicRequest = topicRequest;
        this.orderService = orderService;
        this.template = template;
    }

    @Scheduled(fixedRateString = "${application.coffee-service.scheduling-fixed-rate}", initialDelay = 5000)
    public void sendRoastRequestKafka() {
        long newOrdersCount = orderService.countNewOrders();
        if (newOrdersCount > 0) {
            log.info("{} new orders found", newOrdersCount);
            for (int i = 0; i < (newOrdersCount / 10) + 1; i++) {
                List<RoastDto> roastRequestDtos = orderService.getRoastRequestForTop10NewOrders();
                send(roastRequestDtos);
            }
        } else {
            log.info("new orders not found");
        }
    }

    private void send(List<RoastDto> roastRequestDtos) {
        for (RoastDto roastRequestDto : roastRequestDtos) {
            long orderId = roastRequestDto.getOrderId();
            log.info("sending {}", roastRequestDto);
            try {
                template.send(topicRequest, roastRequestDto)
                        .whenComplete((result, ex) -> {
                            if (ex == null) {
                                log.info(
                                        "{} was sent, offset: {}",
                                        roastRequestDto,
                                        result.getRecordMetadata().offset());
                                orderService.changeOrderStatus(orderId, OrderStatus.SENT_FOR_ROAST);
                            } else {
                                log.error("{} was not sent", roastRequestDto, ex);
                            }
                        })
                        .get();
            } catch (InterruptedException e) {
                logSendingError(roastRequestDto, e);
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                logSendingError(roastRequestDto, e);
            }
        }
    }

    private void logSendingError(RoastDto roastDto, Throwable e) {
        log.error("error sending roast request {}", roastDto, e);
    }
}
