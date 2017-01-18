package com.jrodolfo.basichibernate.client;

import com.jrodolfo.basichibernate.entity.Message;
import com.jrodolfo.basichibernate.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static com.jrodolfo.basichibernate.util.ExceptionUtil.getNonUniqueObjectException;

/**
 * Main class of this app
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-05
 */
public class MessageApp {

    private final static MessageService service = new MessageService();
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static String textOne = "text 1";
    private final static String textTwo = "text 2";
    private final static String textThree = "text 3";

    public static void main(String[] args) {

        // Basic CRUD (create, retrieve, update, delete) operations:

        // create
        Message message_01 = service.create(textOne);
        Long id_01 = message_01.getId();
        Message message_02 = service.create(textTwo);
        Long id_02 = message_02.getId();

        // retrieve
        Message message_03 = service.get(id_01);
        logger.debug("Message retrieved: " + message_03);

        // update
        service.update(id_02, textThree);

        // delete
        service.delete(id_01);
        service.deleteAll();

        // Get exception:
        getNonUniqueObjectException();
    }
}
