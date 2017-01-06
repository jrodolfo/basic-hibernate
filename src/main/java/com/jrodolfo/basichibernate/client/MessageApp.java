package com.jrodolfo.basichibernate.client;

import com.jrodolfo.basichibernate.service.MessageService;
import org.hibernate.NonUniqueObjectException;

import java.io.Serializable;


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

        // we want to create a situation where we get NonUniqueObjectException
        try {
            generateNonUniqueObjectException();
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
    public static void generateNonUniqueObjectException() throws NonUniqueObjectException {
        throw new NonUniqueObjectException(new Serializable(){}, "Artificially creating an org.hibernate.NonUniqueObjectException exception.");
    }
}
