package ru.otus.project.coffee.order.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.FluxSink;
import ru.otus.project.coffee.order.dto.kafka.RoastResult;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    private final Map<Long, FluxSink<ServerSentEvent<?>>> subscribers;

    public NotificationServiceImpl() {
        this.subscribers = new ConcurrentHashMap<>();
    }

    @Override
    public void addSubscriber(Long customerId, FluxSink<ServerSentEvent<?>> fluxSink) {
        log.info("add subscriber {}", customerId);
        if (subscribers.containsKey(customerId)) {
            subscribers.get(customerId).complete();
            subscribers.remove(customerId);
        }
        subscribers.put(customerId, fluxSink);
    }

    @Override
    public void removeSubscriber(Long customerId) {
        log.info("remove subscriber {}", customerId);
        subscribers.remove(customerId);
    }

    @Override
    public void notify(List<RoastResult> roastResults) {
        List<RoastResult> resultsForNotify = roastResults.stream()
                .filter(rs -> subscribers.containsKey(rs.getCustomerId()))
                .toList();
        for (RoastResult rs : resultsForNotify) {
            subscribers
                    .get(rs.getCustomerId())
                    .next(ServerSentEvent.builder()
                            .event("message")
                            .data("Good news! You coffee for order %s was roasted!".formatted(rs.getOrderId()))
                            .build());
        }
    }

    @Scheduled(fixedRate = 60000, initialDelay = 60000)
    public void checkSubscribers() {
        for (var subscriber : subscribers.entrySet()) {
            subscriber.getValue().next(ServerSentEvent.builder().event("check").build());
        }
    }
}
