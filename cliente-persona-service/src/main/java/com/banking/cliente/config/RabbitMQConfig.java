package com.banking.cliente.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.exchange.cliente}")
    private String exchange;

    @Value("${rabbitmq.queue.cliente-created}")
    private String queueCreated;

    @Value("${rabbitmq.queue.cliente-updated}")
    private String queueUpdated;

    @Value("${rabbitmq.queue.cliente-deleted}")
    private String queueDeleted;

    @Value("${rabbitmq.routing-key.cliente-created}")
    private String routingKeyCreated;

    @Value("${rabbitmq.routing-key.cliente-updated}")
    private String routingKeyUpdated;

    @Value("${rabbitmq.routing-key.cliente-deleted}")
    private String routingKeyDeleted;

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(exchange);
    }

    @Bean
    public Queue queueClienteCreated() {
        return new Queue(queueCreated, true);
    }

    @Bean
    public Queue queueClienteUpdated() {
        return new Queue(queueUpdated, true);
    }

    @Bean
    public Queue queueClienteDeleted() {
        return new Queue(queueDeleted, true);
    }

    @Bean
    public Binding bindingClienteCreated() {
        return BindingBuilder
                .bind(queueClienteCreated())
                .to(exchange())
                .with(routingKeyCreated);
    }

    @Bean
    public Binding bindingClienteUpdated() {
        return BindingBuilder
                .bind(queueClienteUpdated())
                .to(exchange())
                .with(routingKeyUpdated);
    }

    @Bean
    public Binding bindingClienteDeleted() {
        return BindingBuilder
                .bind(queueClienteDeleted())
                .to(exchange())
                .with(routingKeyDeleted);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
