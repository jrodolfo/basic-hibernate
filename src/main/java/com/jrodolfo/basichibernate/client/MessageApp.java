package com.jrodolfo.basichibernate.client;

import com.jrodolfo.basichibernate.entity.Message;
import com.jrodolfo.basichibernate.service.MessageService;
import org.hibernate.NonUniqueObjectException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-05.
 */
public class MessageApp {

    static final String textOne = "text 1";
    static final String textTwo = "text 2";
    static final String textThree = "text 3";
    static final MessageService service = new MessageService();

    public static void main(String[] args) {

        // Just checking whether Hibernate CRUD operations are working fine:
        service.deleteAll();
        Long idOne = service.create(textOne);
        Long idTwo = service.create(textTwo);
        service.update(idTwo, textThree);
        service.delete(idOne);

        // We want to create a situation where we get NonUniqueObjectException
        try {
            final int option = 2;
            generateNonUniqueObjectException(option);
        } catch (NonUniqueObjectException e) {
            System.out.println("Catching a NonUniqueObjectException.");
            e.printStackTrace();
        }
    }

    /**
     * We cannot have two instances referring to the same database column in one hibernate session.
     * When that happens, the exception org.hibernate.NonUniqueObjectException is thrown.
     * This method create a scenario to show this happening.
     * @throws NonUniqueObjectException
     */
    public static void generateNonUniqueObjectException(int option) throws NonUniqueObjectException {

        switch (option) {

            case 1 :  // Option 1: Artificially creating an org.hibernate.NonUniqueObjectException exception - it works
                throw new NonUniqueObjectException(new Serializable(){}, "Artificially creating an org.hibernate.NonUniqueObjectException exception.");

            case 2 :  // Option 2: create two Message objects with the same id - it does NOT work
                Long commonId = 100L;
                Message messageOne = new Message(commonId, textOne);
                Message messageTwo = new Message(commonId, textTwo);
                List<Message> messageList = new ArrayList<Message>();
                messageList.add(messageOne);
                messageList.add(messageTwo);
                service.saveMessages(messageList);
        }
    }
}
