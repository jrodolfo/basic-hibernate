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
     * This method create a scenario to show this happening. Look to cases 1 and 6.
     * @throws NonUniqueObjectException
     */
    private static void createNonUniqueObjectException(int caseNumber) throws NonUniqueObjectException {

        final Message message_01;
        final Message message_02;
        final List<Message> messageList;
        final Long id_01;

        System.out.println("\n\t====== Running Case " + caseNumber + " ======\n");

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
                compare(message_01, message_02);
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
                compare(message_01, message_02);
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
                compare(message_01, message_01);
                service.save(messageList);
                break;

            case 5:
                // Case 5: retrieve all rows. Change the id of element 2,
                // so that it is the same as of element 1. Persist all rows.
                // RESULT: Case 5 does NOT throw NonUniqueObjectException.
                messageList = service.getAll();
                if (messageList.size() > 1) {
                    message_01 = messageList.get(1);
                    id_01 = message_01.getId();
                    message_02 = messageList.get(2);
                    message_02.setId(id_01);
                    compare(message_01, message_02);
                    service.save(messageList);
                } else {
                    System.out.println("\tCase 5: now able to run this case " +
                            "because we need to retrieve at least two rows in the table.");
                }

                break;

            case 6:
                // Case 6: Based on a case described on book
                //
                //     Java Persistence with Hibernate
                //     by Christian Bauer and Gavin King
                //     Manning Publications
                //
                // "Merging of a detached object is an alternative approach.
                // It can be complementary to or can replace reattachment.
                // Merging was first introduced in Hibernate to deal with a
                // particular case where reattachment was no longer
                // sufficient (the old name for the merge() method in
                // Hibernate 2.x was saveOrUpdateCopy()). Look at the
                // following code, which tries to reattach a detached object:
                //
                //     item.getId(); // The database identity is "1234"
                //     item.setDescription(...);
                //     Session session = sessionFactory.openSession();
                //     Transaction tx = session.beginTransaction();
                //     Item item2 = (Item) session.get(Item.class, new Long(1234));
                //     session.update(item); // Throws exception!
                //     tx.commit();
                //     session.close();
                //
                // Given is a detached item object with the database identity 1234. After modifying
                // it, you try to reattach it to a new Session. However, before reattachment, another
                // instance that represents the same database row has already been loaded into the
                // persistence context of that Session. Obviously, the reattachment through
                // update() clashes with this already persistent instance, and a NonUniqueObjectException
                // is thrown. The error message of the exception is A persistent instance with
                // the same database identifier is already associated with the Session! Hibernate can’t decide
                // which object represents the current state.
                //
                // You can resolve this situation by reattaching the item first; then, because the
                // object is in persistent state, the retrieval of item2 is unnecessary. This is straightforward
                // in a simple piece of code such as the example, but it may be impossible to
                // refactor in a more sophisticated application. After all, a client sent the detached
                // object to the persistence layer to have it managed, and the client may not (and
                // should not) be aware of the managed instances already in the persistence context."
                //
                // RESULT: Case 6 throws NonUniqueObjectException.
                message_01 = service.create(text_01);
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                message_02 = (Message) session.get(Message.class, message_01.getId());

                // Up to this point, both objects are identical and equal:
                compare(message_01, message_02);
                message_02.setText(text_02);
                // Now they are NOT identical and NOT equal, i.e. a new message_02
                // was created by Hibernate, behind the scene:
                compare(message_01, message_02);

                session.update(message_01); // Throws exception!
                session.getTransaction().commit();
                session.close();
                break;

            case 7:
                // Case 7: similar to Case 6
                // RESULT: Case 7 does NOT throw NonUniqueObjectException.
                message_01 = service.create(text_01);
                message_02 = service.get(message_01.getId());
                compare(message_01, message_02);
                message_02.setText(text_02);
                compare(message_01, message_02);
                service.update(message_01.getId(), text_03);
                break;

            case 8:
                // Case 8: similar to Case 6
                // RESULT: Case 8 does NOT throw NonUniqueObjectException.
                message_01 = service.create(text_01);
                message_02 = service.get(message_01.getId());
                checkIdentity(message_01, message_02);
                message_02.setText(text_02);
                compare(message_01, message_02);
                service.update(message_02);
                break;

            default:
                System.out.println("\tCase " + caseNumber + " was not implemented.");
        }
    }

    private static void compare(Message message_01, Message message_02) {
        checkIdentity(message_01, message_02);
        checkEquality(message_01, message_02);
    }

    private static boolean checkIdentity(Message message_01, Message message_02) {
        System.out.println("Checking if the following objects are identical:");
        System.out.println("\tmessage_01: " + message_01);
        System.out.println("\tmessage_02: " + message_02);
        boolean areIdentical = message_01.equals(message_02);
        if (areIdentical) {
            System.out.println("\tmessage_01 and message_02 are identical");
        } else {
            System.out.println("\tmessage_01 and message_02 are NOT identical");
        }
        return areIdentical;
    }

    private static boolean checkEquality(Message message_01, Message message_02) {
        System.out.println("Checking if the following objects are equal:");
        System.out.println("\tmessage_01: " + message_01);
        System.out.println("\tmessage_02: " + message_02);
        boolean areEqual = message_01.equals(message_02);
        if (areEqual) {
            System.out.println("\tmessage_01 and message_02 are equal");
        } else {
            System.out.println("\tmessage_01 and message_02 are NOT equal");
        }
        return areEqual;
    }

}
