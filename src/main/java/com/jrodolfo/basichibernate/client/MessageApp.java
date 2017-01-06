package com.jrodolfo.basichibernate.client;

import com.jrodolfo.basichibernate.dao.MessageDao;
import com.jrodolfo.basichibernate.entity.Message;
import com.jrodolfo.basichibernate.service.MessageService;
import com.jrodolfo.basichibernate.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-05.
 */
public class MessageApp {

    static final String textOne = "text 1";
    static final String textTwo = "text 2";
    static final String textThree = "text 3";

    public static void main(String[] args) {
        MessageService service = new MessageService();
        service.deleteAll();
        Long idOne = service.create(textOne);
        Long idTwo = service.create(textTwo);
        service.update(idTwo, textThree);
        service.delete(idOne);
    }
}
