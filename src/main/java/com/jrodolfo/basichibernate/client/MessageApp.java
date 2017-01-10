package com.jrodolfo.basichibernate.client;

import com.jrodolfo.basichibernate.entity.Message;
import com.jrodolfo.basichibernate.service.MessageService;
import static com.jrodolfo.basichibernate.util.ExceptionUtil.getNonUniqueObjectException;

/**
 * Main class of this app
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-05
 */
public class MessageApp {

    static final String text_01 = "text 1";
    static final String text_02 = "text 2";
    static final String text_03 = "text 3";
    static final MessageService service = new MessageService();

    public static void main(String[] args) {
        // run some basic Hibernate CRUD operations
        service.deleteAll();
        Message message_01 = service.create(text_01);
        Message message_02 = service.create(text_02);
        service.update(message_02.getId(), text_03);
        service.delete(message_01.getId());
        // generate exception
        getNonUniqueObjectException();
    }
}
