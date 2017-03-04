
package es.jaranda.poc.springbootdemo;

import es.jaranda.poc.chatdemo.service.ChatMessageService;
import org.slf4j.LoggerFactory;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.amqp.Amqp;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.stream.IntStream;

@SpringBootApplication
@ComponentScan("es.jaranda.poc.chatdemo, es.jaranda.poc.springbootdemo")
@EnableRedisRepositories("es.jaranda.poc.chatdemo")
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

    @Bean
    IntegrationFlow recieveChatMessage(
            final ConnectionFactory connectionFactory,
            final ChatMessageService chatMessageService,
            @Value("${chat_example_incoming.queue_name}")
            final String chatExampleIncomingQueueName,
            @Value("${chat_example_incoming.number_of_consumers:1}")
            final int numberOfConsumers) {
        return IntegrationFlows.from(
                Amqp.inboundAdapter(
                        connectionFactory, chatExampleIncomingQueueName
                ).concurrentConsumers(numberOfConsumers)).handle(
                        p-> chatMessageService.postChatMessage(
                                null, p.getPayload().toString()
                        )
                ).get();
    }


}

@RestController
class PublisherController {

    public static final ResponseDto SUCCESS_RESPONSE_DTO = new ResponseDto(true);
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

        return SUCCESS_RESPONSE_DTO;
    }

    @RequestMapping(value = "masiveMessages", method = POST)
    public ResponseDto publishMassiveMessage(
            @RequestBody final MassiveMessageDto massiveMessageDto  ) {

        IntStream.range(0, massiveMessageDto.getTimes())
                .parallel().forEach(i ->
                chatPublisherTemplate.convertAndSend(
                        massiveMessageDto.getContent()
                )
        );

        return SUCCESS_RESPONSE_DTO;
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


@XmlRootElement(name = "response")
class MassiveMessageDto {

    private String content;
    @XmlElement(defaultValue = "1")
    private int times;

    public MassiveMessageDto() {
    }

    public MassiveMessageDto(final String content, final int times) {
        this.content = content;
        this.times = times;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }
}
