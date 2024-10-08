package com.devx.email_service.config;

import com.devx.email_service.message.MessageReceiver;
import com.devx.email_service.service.EmailService;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public DirectExchange direct() {
        return new DirectExchange("email.direct");
    }

    private static class ReceiverConfig {

        @Bean
        public Queue sendTicketResponseQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding sendTicketResponseBinding(DirectExchange direct,
                                          Queue sendTicketResponseQueue) {
            return BindingBuilder.bind(sendTicketResponseQueue)
                    .to(direct)
                    .with("sendTicketResponse");
        }

        @Bean
        public Queue sendBookingApprovedQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding sendBookingApprovedBinding(DirectExchange direct,
                                                 Queue sendBookingApprovedQueue) {
            return BindingBuilder.bind(sendBookingApprovedQueue)
                    .to(direct)
                    .with("sendBookingApproved");
        }

        @Bean
        public Queue sendBookingRejectedQueue() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding sendBookingRejectedBinding(DirectExchange direct,
                                                  Queue sendBookingRejectedQueue) {
            return BindingBuilder.bind(sendBookingRejectedQueue)
                    .to(direct)
                    .with("sendBookingRejected");
        }

        @Bean
        public MessageReceiver receiver(EmailService emailService) {
            return new MessageReceiver(emailService);
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

