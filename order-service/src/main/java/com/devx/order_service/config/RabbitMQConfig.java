package com.devx.order_service.config;

import com.devx.order_service.message.MessageReceiver;
import com.devx.order_service.message.MessageSender;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public DirectExchange orderDirect() {
        return new DirectExchange("order.direct");
    }

    @Bean
    public DirectExchange wsDirect() {
        return new DirectExchange("ws.direct", false, false);
    }


    private static class ReceiverConfig {

        @Bean
        public Queue addMenuItemQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding addMenuItemBinding(@Qualifier("orderDirect") DirectExchange direct,
                                          Queue addMenuItemQueue) {
            return BindingBuilder.bind(addMenuItemQueue)
                    .to(direct)
                    .with("addMenuItem");
        }

        @Bean
        public Queue addTableQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding addTableBinding(@Qualifier("orderDirect") DirectExchange direct,
                                                 Queue addTableQueue) {
            return BindingBuilder.bind(addTableQueue)
                    .to(direct)
                    .with("addTable");
        }

        @Bean
        public Queue updateMenuItemStatusQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding updateMenuItemStatusBinding(@Qualifier("orderDirect") DirectExchange direct,
                                       Queue updateMenuItemStatusQueue) {
            return BindingBuilder.bind(updateMenuItemStatusQueue)
                    .to(direct)
                    .with("updateMenuItemStatus");
        }

        @Bean
        public MessageReceiver receiver() {
            return new MessageReceiver();
        }
    }

    private static class SenderConfig {
        @Bean
        public MessageSender messageSender(RabbitTemplate template, @Qualifier("wsDirect") DirectExchange direct) {
            return new MessageSender(template, direct);
        }
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory)
    {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
    }

    @Bean
    public MessageConverter jsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

}

