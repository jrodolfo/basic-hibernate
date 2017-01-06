package com.jrodolfo.basichibernate.dao;

import com.jrodolfo.basichibernate.entity.Message;
import com.jrodolfo.basichibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-06.
 */
public class MessageDao {

    public Long createMessage(String text) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Message message = new Message(text);
        Long id = (Long) session.save(message);
        session.getTransaction().commit();
        System.out.println("\n\tcreateMessage(): " + message + "\n");
        session.close();
        return id;
    }

    public void saveMessages(List<Message> messageList) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        for (Message message : messageList) {
            System.out.println("\n\tsaveMessages(): " + message + "\n");
            session.save(message);
        }
        session.getTransaction().commit();
        session.close();
    }

    public void updateMessage(long id, String text) {
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

    public void deleteMessage(long id) {
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

    public void deleteMessages() {
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
}
