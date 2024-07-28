package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.InvalidMessageTextException;
import com.example.exception.InvalidMessageUpdateException;
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

    // if the messageId sent doesn't exist in database return null, else return the message that is deleted
    public Message deleteMessageById(int messageId) {
        Message message = getMessageById(messageId);
        
        if(message != null) {
            messageRepository.deleteById(messageId);
            return message;
        }
            
        return null;
    }

    // return the number of row affected.
    public int updateMessageById(int messageId, String newMessageText) {
        if (newMessageText == null || newMessageText.isBlank() || newMessageText.length() > 255) {
            throw new InvalidMessageUpdateException("Invalid message text");
        }

        Message message = messageRepository.findById(messageId)
            .orElseThrow(() -> new InvalidMessageUpdateException("Message ID not found"));

        message.setMessageText(newMessageText);
        messageRepository.save(message);
        return 1; 
    }

    public List<Message> getMessagesByUser(int accountId) {
        return messageRepository.findAllByPostedBy(accountId);
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
