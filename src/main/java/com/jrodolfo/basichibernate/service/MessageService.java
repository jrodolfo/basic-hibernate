package com.jrodolfo.basichibernate.service;

import com.jrodolfo.basichibernate.dao.MessageDao;
import com.jrodolfo.basichibernate.entity.Message;

import java.util.List;

/**
 * Class that provides service for Message and uses MessageDAO
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-06
 */
public class MessageService {

    MessageDao messageDao = new MessageDao();

    public Message get(Long id) {
        return messageDao.get(id);
    }

    public List<Message> getAll() {
        return messageDao.getAll();
    }

    public Message create(String text) {
        return messageDao.create(text);
    }

    public void save(List<Message> messageList) {
        messageDao.save(messageList);
    }

    public void update(long id, String text) {
        messageDao.update(id, text);
    }

    public void update(Message message) {
        messageDao.update(message);
    }

    public void deleteAll() {
        messageDao.deleteAll();
    }

    public void delete(long id) {
        messageDao.delete(id);
    }
}
