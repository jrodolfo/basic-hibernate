package com.jrodolfo.basichibernate.util;

import com.jrodolfo.basichibernate.entity.Message;
import com.jrodolfo.basichibernate.service.MessageService;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import static com.jrodolfo.basichibernate.util.MathUtil.getRandomLong;

/**
 * Class to create some Hibernate common exceptions
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-10
 */
public class ExceptionUtil {

    private final static MessageService service = new MessageService();
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final static String textOne = "text 1";
    private final static String textTwo = "text 2";
    private final static String textThree = "text 3";

    public static void getNonUniqueObjectException() {
        final int maxNumOfCases = 8;
        for (int i = 1; i <= maxNumOfCases; i++) {
            try {
                createNonUniqueObjectException(i);
            } catch (NonUniqueObjectException e) {
                logger.debug("\n\n\t****** Got NonUniqueObjectException on Case " + i + " ******\n\n");
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

        final Message messageOne;
        final Message messageTwo;
        final List<Message> messageList;
        final Long idOne;
        logger.debug("\n\n\t====== Running Case " + caseNumber + " ======\n");

        switch (caseNumber) {

            case 1:
                // Case 1: artificially create an org.hibernate.NonUniqueObjectException exception.
                // RESULT: Case 1 throws NonUniqueObjectException.
                throw new NonUniqueObjectException(new Serializable(){}, "Artificially creating an " +
                        "org.hibernate.NonUniqueObjectException exception.");

            case 2:
                // Case 2: create two objects with the same id (same primary key).
                // RESULT: Case 2 does NOT throw NonUniqueObjectException
                final Long commonId = getRandomLong(1_000_000, 2_000_000);
                messageOne = new Message(commonId, textOne);
                messageTwo = new Message(commonId, textTwo);
                messageList = new ArrayList<>();
                messageList.add(messageOne);
                messageList.add(messageTwo);
                messageOne.compare(messageTwo);
                service.save(messageList);
                break;

            case 3:
                // Case 3: create two objects with the same id (same primary key) but
                // they are NOT the same object, then try to save them at the same
                // time (i.e. same session).
                // RESULT:  Case 3 does NOT throw NonUniqueObjectException.
                messageOne = service.create(textOne);
                messageTwo = service.get(messageOne.getId());
                messageList = new ArrayList<>();
                messageList.add(messageOne);
                messageList.add(messageTwo);
                messageOne.compare(messageTwo);
                service.save(messageList);
                break;

            case 4:
                // Case 4: create two objects with the same id (same primary key) and
                // they are the same object, and then save them at the same time (i.e. same session).
                // RESULT: Case 4 does NOT throw NonUniqueObjectException.
                messageOne = service.create(textOne);
                messageList = new ArrayList<>();
                messageList.add(messageOne);
                messageList.add(messageOne);
                messageOne.compare(messageOne);
                service.save(messageList);
                break;

            case 5:
                // Case 5: retrieve all rows. Change the id of element 2,
                // so that it is the same as of element 1. Persist all rows.
                // RESULT: Case 5 does NOT throw NonUniqueObjectException.
                messageList = service.getAll();
                if (messageList.size() > 1) {
                    messageOne = messageList.get(1);
                    idOne = messageOne.getId();
                    messageTwo = messageList.get(2);
                    messageTwo.setId(idOne);
                    messageOne.compare(messageTwo);
                    service.save(messageList);
                } else {
                    logger.debug("\tCase 5: now able to run this case " +
                            "because we need to retrieve at least two rows in the table.");
                }
                break;

            case 6:
                // Case 6: Based on a case described in this book:
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
                messageOne = service.create(textOne);
                Session session = HibernateUtil.getSessionFactory().openSession();
                session.beginTransaction();
                messageTwo = (Message) session.get(Message.class, messageOne.getId());
                messageOne.compare(messageTwo);
                messageTwo.setText(textTwo);
                messageOne.compare(messageTwo);
                session.update(messageOne); // Throws exception!
                session.getTransaction().commit();
                session.close();
                // The problem here is: messageOne was in detached hibernate state (its persistent
                // hibernate state lasted only during the hibernate session that create it, which was
                // closed at the end). Then we have messageTwo that points to the same row in the table
                // as messageOne. The object messageTwo is in persistent hibernate state. We change one
                // field of messageTwo and then we try to update messageOne, and this sequence throws
                // NonUniqueObjectException. If we have both object being kept in a Set, then we should
                // always implement equals() for the class Message (which is part of the contract for
                // using Java Collections). Otherwise, if we don't implement the equals() and then
                // we ended up trying to update all objects from this Set and if they are equal,
                // we will have this exception. More info can be found here:
                // https://docs.jboss.org/hibernate/stable/core.old/reference/en/html/persistent-classes-equalshashcode.html
                break;

            case 7:
                // Case 7: similar to Case 6
                // RESULT: Case 7 does NOT throw NonUniqueObjectException.
                messageOne = service.create(textOne);
                messageTwo = service.get(messageOne.getId());
                messageOne.compare(messageTwo);
                messageTwo.setText(textTwo);
                messageOne.compare(messageTwo);
                service.update(messageOne.getId(), textThree);
                break;

            case 8:
                // Case 8: similar to Case 6
                // RESULT: Case 8 does NOT throw NonUniqueObjectException.
                messageOne = service.create(textOne);
                messageTwo = service.get(messageOne.getId());
                messageOne.compare(messageTwo);
                messageTwo.setText(textTwo);
                messageOne.compare(messageTwo);
                service.update(messageTwo);
                break;

            default:
                logger.debug("\tCase " + caseNumber + " was not implemented.");
        }
    }
}
