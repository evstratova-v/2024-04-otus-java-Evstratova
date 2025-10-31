package ru.otus.project.coffee.order.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.project.coffee.order.service.CustomerService;
import ru.otus.project.coffee.order.service.NotificationService;

@SuppressWarnings("java:S1452")
@Slf4j
@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    private final CustomerService customerService;

    @GetMapping(value = "/api/v1/order-notification", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<?>> orderNotification(@AuthenticationPrincipal Jwt principal) {
        long customerId =
                customerService.findCustomerIdByLogin(principal.getClaim("sub").toString());

        return Flux.create(sink -> {
            sink.onRequest(r -> log.info("onRequest {}", r));
            sink.onCancel(() -> {
                log.info("onCancel");
                notificationService.removeSubscriber(customerId);
            });
            sink.onDispose(() -> {
                log.info("onDispose");
                notificationService.removeSubscriber(customerId);
            });
            notificationService.addSubscriber(customerId, sink);
            sink.next(ServerSentEvent.builder().event("open").build());
        });
    }
}
