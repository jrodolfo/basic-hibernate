package com.jrodolfo.basichibernate.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
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

    private static SessionFactory buildSessionFactory() {
        try {
            StandardServiceRegistry standardRegistry =
                    new StandardServiceRegistryBuilder().configure("hibernate.cfg.xml").build();
            Metadata metaData =
                    new MetadataSources(standardRegistry).getMetadataBuilder().build();
            return metaData.getSessionFactoryBuilder().build();
        } catch (Throwable e) {
            if (logger != null) {
                logger.error("SessionFactory creation failed: " + e);
            } else {
                System.err.println("SessionFactory creation failed: " + e);
            }
            throw new ExceptionInInitializerError(e);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

}



