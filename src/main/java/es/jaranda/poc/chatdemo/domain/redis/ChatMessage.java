
package es.jaranda.poc.chatdemo.domain.redis;

import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@RedisHash("chat_message")
public class ChatMessage {

    @Id private String id;
    private String from;
    private String destination;
    private String content;
    private Long timestamp;

    public ChatMessage() {

    }

    public ChatMessage(String id, String from, String destination,
                       String content, Long timestamp) {
        this.id = id;
        this.from = from;
        this.destination = destination;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
