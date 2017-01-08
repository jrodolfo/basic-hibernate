package com.jrodolfo.basichibernate.service;

import com.jrodolfo.basichibernate.dao.MessageDao;
import com.jrodolfo.basichibernate.entity.Message;

import java.util.List;

/**
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-06
 */
public class MessageService {

    MessageDao messageDao = new MessageDao();

    public Message get(Long id) {
        return messageDao.getMessage(id);
    }

    public Long create(String text) {
        return messageDao.createMessage(text);
    }

    public void save(List<Message> messageList) {
        messageDao.saveMessages(messageList);
    }

    public void update(long id, String text) {
        messageDao.updateMessage(id, text);
    }
    public void deleteAll() {
        messageDao.deleteMessages();
    }

    public void delete(long id) {
        messageDao.deleteMessage(id);
    }
}
