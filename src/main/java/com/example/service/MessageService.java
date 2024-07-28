package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.InvalidMessageTextException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private AccountRepository accountRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository, AccountRepository accountRepository) {
        this.messageRepository = messageRepository;
        this.accountRepository = accountRepository;
    }

    public Message createMessage(Message message) {
        if (!massageTextValid(message)) throw new InvalidMessageTextException("message text is invalid");
            
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Message getMessageById(int messageId) {
        return messageRepository.findById(messageId).orElse(null);
    }

    private boolean massageTextValid(Message message) {
        if(!message.getMessageText().isEmpty() && 
            message.getMessageText().length() <= 255 &&
            message.getPostedBy() >= 0 &&
            accountRepository.findById(message.getPostedBy()).isPresent()
        ) return true;

        return false;
    }
}
