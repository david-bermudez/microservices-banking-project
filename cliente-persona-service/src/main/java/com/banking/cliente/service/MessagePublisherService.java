package com.banking.cliente.service;

import com.banking.cliente.dto.ClienteEventDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessagePublisherService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.cliente}")
    private String exchange;

    @Value("${rabbitmq.routing-key.cliente-created}")
    private String routingKeyCreated;

    @Value("${rabbitmq.routing-key.cliente-updated}")
    private String routingKeyUpdated;

    @Value("${rabbitmq.routing-key.cliente-deleted}")
    private String routingKeyDeleted;

    public void publishClienteCreated(ClienteEventDTO event) {
        log.info("Publishing cliente created event: {}", event);
        rabbitTemplate.convertAndSend(exchange, routingKeyCreated, event);
    }

    public void publishClienteUpdated(ClienteEventDTO event) {
        log.info("Publishing cliente updated event: {}", event);
        rabbitTemplate.convertAndSend(exchange, routingKeyUpdated, event);
    }

    public void publishClienteDeleted(ClienteEventDTO event) {
        log.info("Publishing cliente deleted event: {}", event);
        rabbitTemplate.convertAndSend(exchange, routingKeyDeleted, event);
    }
}
