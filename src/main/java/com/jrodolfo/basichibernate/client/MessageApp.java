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

        // Basic CRUD (create, retrieve, update, delete) operations:

        // create
        Message message_01 = service.create(text_01);
        Long id_01 = message_01.getId();
        Message message_02 = service.create(text_02);
        Long id_02 = message_02.getId();

        // retrieve
        Message message_03 = service.get(id_01);
        System.out.println("Message retrieved: " + message_03);

        // update
        service.update(id_02, text_03);

        // delete
        service.delete(id_01);
        service.deleteAll();

        // Get exception:
        getNonUniqueObjectException();
    }
}
