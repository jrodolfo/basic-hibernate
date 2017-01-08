package com.jrodolfo.basichibernate.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

/**
 * Created by Rod Oliveira (jrodolfo.com) on 2017-01-05
 */
public class HibernateUtil {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
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

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}



