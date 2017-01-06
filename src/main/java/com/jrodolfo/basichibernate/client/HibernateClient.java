package com.jrodolfo.basichibernate.client;

import com.jrodolfo.basichibernate.entity.Message;
import com.jrodolfo.basichibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-05.
 */
public class HibernateClient {

    static final String textOne = "text 1";
    static final String textTwo = "text 2";
    static final String textThree = "text 3";

    public static void main(String[] args) {
        deleteMessages();
        Long idOne = createMessage(textOne);
        Long idTwo = createMessage(textTwo);
        updateMessage(idTwo, textThree);
        deleteMessage(idOne);
    }

    private static void deleteMessages() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();
        try {
            txn.begin();
            final List<?> listOfMessages = session.createCriteria(Message.class).list();
            Message message;
            for (Object obj : listOfMessages) {
                message = (Message) obj;
                System.out.println("\n\tdeleteMessages() - deleting message: " + message + "\n");
                session.delete(obj);
            }
            txn.commit();
        } catch (Exception e) {
            if (txn != null) {
                txn.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static Long createMessage(String text) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Message message = new Message(text);
        Long id = (Long) session.save(message);
        session.getTransaction().commit();
        System.out.println("\n\tcreateMessage(): " + message + "\n");
        session.close();
        return id;
    }

    private static void updateMessage(long id, String text) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();
        try {
            txn.begin();
            Message message = (Message) session.get(Message.class, id);
            System.out.println("\n\tupdateMessage() - before change: " + message);
            message.setText(text);
            System.out.println("\n\tupdateMessage() - after change: " + message + "\n");
            txn.commit();
        } catch (Exception e) {
            if (txn != null) {
                txn.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private static void deleteMessage(long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();
        try {
            txn.begin();
            Message message = (Message) session.get(Message.class, id);
            if (message != null) {
                System.out.println("\n\tdeleteMessage(): " + message + "\n");
                session.delete(message);
            }
            txn.commit();
        } catch (Exception e) {
            if (txn != null) {
                txn.rollback();
            }
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
