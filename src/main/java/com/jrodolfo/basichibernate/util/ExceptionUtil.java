package com.jrodolfo.basichibernate.util;

import com.jrodolfo.basichibernate.entity.Message;
import com.jrodolfo.basichibernate.service.MessageService;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.jrodolfo.basichibernate.util.MathUtil.getRandonLong;

/**
 * Class to create some Hibernate common exceptions
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-10
 */
public class ExceptionUtil {

    static final String text_01 = "text 1";
    static final String text_02 = "text 2";
    static final String text_03 = "text 3";
    static final MessageService service = new MessageService();

    public static void getNonUniqueObjectException() {
        // trying to get NonUniqueObjectException
        final int maxNumOfCases = 8;
        for (int i = 1; i <= maxNumOfCases; i++) {
            try {
                createNonUniqueObjectException(i);
            } catch (NonUniqueObjectException e) {
                System.out.println("\t****** Failed on Case " + i + " ******");
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
    private static void createNonUniqueObjectException(int caseNumber) throws NonUniqueObjectException {

        final Message message_01;
        final Message message_02;
        final List<Message> messageList;
        final Long id_01;

        System.out.println("\n\tRunning Case " + caseNumber + "\n");

        switch (caseNumber) {

            case 1:
                // Case 1: artificially create an org.hibernate.NonUniqueObjectException exception.
                // RESULT: Case 1 throws NonUniqueObjectException.
                throw new NonUniqueObjectException(new Serializable(){}, "Artificially creating an " +
                        "org.hibernate.NonUniqueObjectException exception.");

            case 2:
                // Case 2: create two Message objects with the same id.
                // RESULT: Case 2 does NOT throw NonUniqueObjectException
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
                // RESULT:  Case 3 does NOT throw NonUniqueObjectException.
                message_01 = service.create(text_01);
                message_02 = service.get(message_01.getId());
                messageList = new ArrayList<>();
                messageList.add(message_01);
                messageList.add(message_02);
                service.save(messageList);
                break;

            case 4:
                // Case 4: we have two objects which have the same identifier (same primary key) and
                // they are the same object, and we will try to save them at the same time (i.e. same session)
                // RESULT: Case 4 does NOT throw NonUniqueObjectException.
                message_01 = service.create(text_01);
                messageList = new ArrayList<>();
                messageList.add(message_01);
                messageList.add(message_01);
                service.save(messageList);
                break;

            case 5:
                // Case 5: retrieve all rows. Change the id of element 2,
                // so that it is the same as of element 1. Persist all rows.
                // RESULT: Case 5 does NOT throw NonUniqueObjectException.
                messageList = service.getAll();
                message_01 = messageList.get(1);
                id_01 = message_01.getId();
                message_02 = messageList.get(2);
                message_02.setId(id_01);
                service.save(messageList);
                break;

            case 6:
                // Case 6: Based on a case described on book
                //     "Java Persistence with Hibernate"
                //     by Christian Bauer and Gavin King;
                //     Manning Publications
                // Merging of a detached object is an alternative approach.
                // It can be complementary to or can replace reattachment.
                // Merging was first introduced in Hibernate to deal with a
                // particular case where reattachment was no longer
                // sufficient (the old name for the merge() method in
                // Hibernate 2.x was saveOrUpdateCopy()). Look at the
                // following code, which tries to reattach a detached object:
                /*
                item.getId(); // The database identity is "1234"
                item.setDescription();
                Session session = sessionFactory.openSession();
                Transaction tx = session.beginTransaction();
                Item item2 = (Item) session.get(Item.class, new Long(1234));
                session.update(item); // Throws exception!
                tx.commit();
                session.close();
                */
                // RESULT: Case 6 throws NonUniqueObjectException.
                message_01 = service.create(text_01);
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                message_02 = (Message) session.get(Message.class, message_01.getId());
                message_02.setText(text_02);
                session.update(message_01); // Throws exception!
                session.getTransaction().commit();
                session.close();
                break;

            case 7:
                // Case 7: similar to Case 6
                // RESULT: Case 7 does NOT throw NonUniqueObjectException.
                message_01 = service.create(text_01);
                message_02 = service.get(message_01.getId());
                System.out.println("\tmessage_01: " + message_01);
                System.out.println("\tmessage_02: " + message_02);
                if (message_01 == message_02) {
                    System.out.println("\tmessage_01 and message_02 are identical");
                } else {
                    System.out.println("\tmessage_01 and message_02 are NOT identical");
                }
                message_02.setText(text_02);
                System.out.println("\tmessage_01: " + message_01);
                System.out.println("\tmessage_02: " + message_02);
                service.update(message_01.getId(), text_03);
                break;

            case 8:
                // Case 8: similar to Case 6
                // RESULT: Case 8 does NOT throw NonUniqueObjectException.
                message_01 = service.create(text_01);
                message_02 = service.get(message_01.getId());
                System.out.println("\tmessage_01: " + message_01);
                System.out.println("\tmessage_02: " + message_02);
                if (message_01 == message_02) {
                    System.out.println("\tmessage_01 and message_02 are identical");
                } else {
                    System.out.println("\tmessage_01 and message_02 are NOT identical");
                }
                message_02.setText(text_02);
                System.out.println("\tmessage_01: " + message_01);
                System.out.println("\tmessage_02: " + message_02);
                service.update(message_02);
                break;

            default:
                System.out.println("\tCase " + caseNumber + " was not implemented.");
        }
    }
}
