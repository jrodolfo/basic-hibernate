package com.jrodolfo.basichibernate.client;

import com.jrodolfo.basichibernate.entity.Message;
import com.jrodolfo.basichibernate.service.MessageService;
import org.hibernate.NonUniqueObjectException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.jrodolfo.basichibernate.util.MathUtil.getRandonLong;


/**
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-05
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
        // generating exception
        getNonUniqueObjectException();
    }

    private static void getNonUniqueObjectException() {
        // trying to get NonUniqueObjectException
        final int maxNumOfCases = 5;
        for (int i = 1; i <= maxNumOfCases; i++) {
            try {
                createNonUniqueObjectException(i);
            } catch (NonUniqueObjectException e) {
                System.out.println("Case #" + i);
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
    public static void createNonUniqueObjectException(int caseNumber) throws NonUniqueObjectException {

        final Message message_01;
        final Message message_02;
        final List<Message> messageList;
        final Long id_01;

        switch (caseNumber) {

            case 1:
                // Case 1: artificially create an org.hibernate.NonUniqueObjectException exception.
                // RESULT: That does throw NonUniqueObjectException.
                throw new NonUniqueObjectException(new Serializable(){}, "Artificially creating an org.hibernate.NonUniqueObjectException exception.");

            case 2:
                // Case 2: create two Message objects with the same id.
                // RESULT: That does NOT throw NonUniqueObjectException.
                final Long commonId = getRandonLong(1_000_000, 2_000_000);
                message_01 = new Message(commonId, text_01);
                message_02 = new Message(commonId, text_02);
                messageList = new ArrayList<>();
                messageList.add(message_01);
                messageList.add(message_02);
                service.save(messageList);
                break;

            case 3:
                // Case 3: we have two objects which have the same identifier (same primary key) but
                // they are NOT the same object, and we will try to save them at the same time (i.e. same session)
                // RESULT: That does NOT throw NonUniqueObjectException.
                id_01 = service.create(text_01);
                message_01 = service.get(id_01);
                message_02 = service.get(id_01);
                messageList = new ArrayList<>();
                messageList.add(message_01);
                messageList.add(message_02);
                service.save(messageList);
                break;

            case 4:
                // Case 4: we have two objects which have the same identifier (same primary key) and
                // they are the same object, and we will try to save them at the same time (i.e. same session)
                // RESULT: That does NOT throw NonUniqueObjectException.
                id_01 = service.create(text_01);
                message_01 = service.get(id_01);
                messageList = new ArrayList<>();
                messageList.add(message_01);
                messageList.add(message_01);
                service.save(messageList);
                break;

            case 5:
                // Case 5: retrieve all rows. Change the id of element 2,
                // so that it is the same as of element 1. Persist all rows.
                // RESULT: That does NOT throw NonUniqueObjectException.
                messageList = service.getAll();
                message_01 = messageList.get(1);
                id_01 = message_01.getId();
                message_02 = messageList.get(2);
                message_02.setId(id_01);
                service.save(messageList);
                break;
        }
    }
}
