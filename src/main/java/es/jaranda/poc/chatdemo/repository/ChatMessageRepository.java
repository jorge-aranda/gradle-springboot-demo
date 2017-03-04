
package es.jaranda.poc.chatdemo.repository;

import es.jaranda.poc.chatdemo.domain.redis.ChatMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path="chat-messages")
public interface ChatMessageRepository
       extends CrudRepository<ChatMessage, String> {

}
