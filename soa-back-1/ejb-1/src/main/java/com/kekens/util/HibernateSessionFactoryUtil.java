package com.kekens.util;

import com.kekens.model.Discipline;
import com.kekens.model.LabWork;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

public class HibernateSessionFactoryUtil {

    private static SessionFactory sessionFactory;

    private HibernateSessionFactoryUtil() {}

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(LabWork.class);
                configuration.addAnnotatedClass(Discipline.class);
                configuration.setProperty(Environment.URL, System.getenv("DB_URL"));
                configuration.setProperty(Environment.USER, System.getenv("DB_USERNAME"));
                configuration.setProperty(Environment.PASS, System.getenv("DB_PASSWORD"));
                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());
            } catch (Exception e) {
                System.out.println("Исключение!" + e);
            }
        }
        return sessionFactory;
    }
}
