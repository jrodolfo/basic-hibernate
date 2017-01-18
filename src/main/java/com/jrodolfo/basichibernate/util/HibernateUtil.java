package com.jrodolfo.basichibernate.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

/**
 * Class to create Hibernate session
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-05
 */
public class HibernateUtil {

    private final static SessionFactory sessionFactory = buildSessionFactory();
    private final static Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    // hibernate 4
    private static SessionFactory buildSessionFactory() {
        // import org.hibernate.boot.registry.StandardServiceRegistry;
        // import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
        try {
            Configuration config = new Configuration().configure("hibernate.cfg.xml");
            StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(config.getProperties())
                    .build();
            return config.buildSessionFactory(standardServiceRegistry);
        }
        catch (Throwable e) {
            System.err.println("SessionFactory creation failed: " + e);
            throw new ExceptionInInitializerError(e);
        }
    }

    // hibernate 3
//    private static SessionFactory buildSessionFactory() {
//
//        try {
//            return new Configuration().configure().buildSessionFactory();
//        } catch (Throwable e) {
//            System.err.println("SessionFactory creation failed: " + e);
//            throw new ExceptionInInitializerError(e);
//        }
//    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}



