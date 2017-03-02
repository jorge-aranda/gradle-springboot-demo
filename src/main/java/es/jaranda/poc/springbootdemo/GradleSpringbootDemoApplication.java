
package es.jaranda.poc.springbootdemo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;

@SpringBootApplication
public class GradleSpringbootDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(GradleSpringbootDemoApplication.class, args);
    }

    @Bean(name="chatPublisherTemplate")
    RabbitTemplate chatPublisherTemplate(
            final ConnectionFactory connectionFactory,
           @Value("${chat_example_publisher.exchange_name}")
           final String exchangeName) {

        final RabbitTemplate rabbitTemplate =
                new RabbitTemplate(connectionFactory);

        rabbitTemplate.setExchange(exchangeName);

        return rabbitTemplate;
    }

    @Bean
    RabbitAdmin rabbitAdmin(final ConnectionFactory connectionFactory,
                            @Value("${chat_example_publisher.exchange_name}")
                            final String chatExchangePublisherExchangeName,
                            @Value("${chat_example_incoming.queue_name}")
                            final String chatExampleIncomingQueueName) {

        final RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);

        rabbitAdmin.declareExchange(
                new DirectExchange(
                        chatExchangePublisherExchangeName,
                        true,
                        false
                )
        );
        rabbitAdmin.declareQueue(
                new Queue(chatExampleIncomingQueueName)
        );

        rabbitAdmin.declareBinding(
                new Binding(
                        chatExampleIncomingQueueName,
                        Binding.DestinationType.QUEUE,
                        chatExchangePublisherExchangeName,
                        "",
                        new HashMap<>()
                )
        );

        return new RabbitAdmin(connectionFactory);
    }



}

@RestController
class PublisherController {

    private final RabbitTemplate chatPublisherTemplate;

    @Autowired
    public PublisherController(
            @Qualifier("chatPublisherTemplate")
            final RabbitTemplate chatPublisherTemplate) {
        this.chatPublisherTemplate = chatPublisherTemplate;
    }

    @RequestMapping(value = "messages", method = POST)
    public ResponseDto publishMessage(@RequestBody final String message) {

        chatPublisherTemplate.convertAndSend(message);

        return new ResponseDto(true);
    }


}

@XmlRootElement(name = "response")
class ResponseDto {

    private boolean success;

    public ResponseDto() {

    }

    public ResponseDto(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
