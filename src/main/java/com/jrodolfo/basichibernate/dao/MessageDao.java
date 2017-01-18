package com.jrodolfo.basichibernate.dao;

import com.jrodolfo.basichibernate.entity.Message;
import com.jrodolfo.basichibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * DAO Class to access the database
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-06
 */
public class MessageDao {

    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public Message get(long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        return (Message) session.get(Message.class, id);
    }

    public List<Message> getAll() {
        return HibernateUtil.getSessionFactory().openSession().createCriteria(Message.class).list();
    }

    public Message create(String text) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Message message = new Message(text);
        session.save(message);
        session.getTransaction().commit();
        logger.debug(message.toString());
        session.close();
        return message;
    }

    public void save(List<Message> messageList) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        for (Message message : messageList) {
            logger.debug(message.toString());
            session.save(message);
        }
        session.getTransaction().commit();
        session.close();
    }

    public void update(long id, String text) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();
        try {
            txn.begin();
            Message message = (Message) session.get(Message.class, id);
            logger.debug(message.toString());
            message.setText(text);
            logger.debug(message.toString());
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

    public void update(Message message) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();
        try {
            txn.begin();
            session.update(message);
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

    public void delete(long id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();
        try {
            txn.begin();
            Message message = (Message) session.get(Message.class, id);
            if (message != null) {
                logger.debug(message.toString());
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

    public void deleteAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction txn = session.getTransaction();
        try {
            txn.begin();
            final List<?> listOfMessages = session.createCriteria(Message.class).list();
            Message message;
            for (Object obj : listOfMessages) {
                message = (Message) obj;
                logger.debug(message.toString());
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
