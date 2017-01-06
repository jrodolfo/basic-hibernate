package com.jrodolfo.basichibernate.service;

import com.jrodolfo.basichibernate.dao.MessageDao;

/**
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-06.
 */
public class MessageService {

    MessageDao messageDao = new MessageDao();

    public Long create(String text) {
        return messageDao.createMessage(text);
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
