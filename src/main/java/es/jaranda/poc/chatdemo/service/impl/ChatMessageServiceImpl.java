
package es.jaranda.poc.chatdemo.service.impl;

import es.jaranda.poc.chatdemo.domain.redis.ChatMessage;
import es.jaranda.poc.chatdemo.repository.ChatMessageRepository;
import es.jaranda.poc.chatdemo.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    @Autowired
    public ChatMessageServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public void postChatMessage(final String author, final String content) {

        final ChatMessage chatMessage = new ChatMessage(
                null, author, null, content,
                Instant.now().toEpochMilli()
        );

        chatMessageRepository.save(chatMessage);

    }
}
