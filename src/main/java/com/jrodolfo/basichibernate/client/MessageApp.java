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

    static final String text_01 = "text 1";
    static final String text_02 = "text 2";
    static final String text_03 = "text 3";
    static final MessageService service = new MessageService();

    public static void main(String[] args) {

        // checking whether Hibernate CRUD operations are working fine
        service.deleteAll();
        Long id_01 = service.create(text_01);
        Long id_02 = service.create(text_02);
        service.update(id_02, text_03);
        service.delete(id_01);

        getNonUniqueObjectException();
    }

    private static void getNonUniqueObjectException() {
        // trying to get NonUniqueObjectException
        final int maxNumOfCases = 4;
        for (int i = 1; i <= maxNumOfCases; i++) {
            try {
                createNonUniqueObjectException(i);
            } catch (NonUniqueObjectException e) {
                System.out.println("Option " + i);
                e.printStackTrace();
            }
        }
    }

    /**
     * We cannot have two instances referring to the same database column in one hibernate session.
     * When that happens, the exception org.hibernate.NonUniqueObjectException is thrown.
     * This method create a scenario to show this happening.
     * @throws NonUniqueObjectException
     */
    public static void createNonUniqueObjectException(int option) throws NonUniqueObjectException {

        final Message messageOne;
        final Message messageTwo;
        final List<Message> messageList;
        final Long idOne;

        switch (option) {

            case 1:  // Option 1: Artificially creating an org.hibernate.NonUniqueObjectException exception. That does throw NonUniqueObjectException.
                throw new NonUniqueObjectException(new Serializable(){}, "Artificially creating an org.hibernate.NonUniqueObjectException exception.");

            case 2:  // Option 2: we create two Message objects with the same id. That does NOT throw NonUniqueObjectException.
                final Long commonId = 100L;
                messageOne = new Message(commonId, text_01);
                messageTwo = new Message(commonId, text_02);
                messageList = new ArrayList<Message>();
                messageList.add(messageOne);
                messageList.add(messageTwo);
                service.save(messageList);
                break;

            case 3: // Option 3: we have two objects which have the same identifier (same primary key) but
                    // they are NOT the same object, and we will try to save them at the same time (i.e. same session)
                    // That does NOT throw NonUniqueObjectException.
                idOne = service.create(text_01);
                messageOne = service.get(idOne);
                messageTwo = service.get(idOne);
                messageList = new ArrayList<Message>();
                messageList.add(messageOne);
                messageList.add(messageTwo);
                service.save(messageList);
                break;

            case 4: // Option 4: we have two objects which have the same identifier (same primary key) and
                    // they are the same object, and we will try to save them at the same time (i.e. same session)
                    // That does NOT throw NonUniqueObjectException.
                idOne = service.create(text_01);
                messageOne = service.get(idOne);
                messageList = new ArrayList<Message>();
                messageList.add(messageOne);
                messageList.add(messageOne);
                service.save(messageList);
                break;
        }
    }
}
