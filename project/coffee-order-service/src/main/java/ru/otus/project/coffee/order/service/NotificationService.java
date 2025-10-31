package ru.otus.project.coffee.order.service;

import java.util.List;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.FluxSink;
import ru.otus.project.coffee.order.dto.kafka.RoastResult;

public interface NotificationService {

    void addSubscriber(Long customerId, FluxSink<ServerSentEvent<?>> fluxSink);

    void removeSubscriber(Long customerId);

    void notify(List<RoastResult> roastResults);
}
