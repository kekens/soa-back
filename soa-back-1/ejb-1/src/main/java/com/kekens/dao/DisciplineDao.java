package com.kekens.dao;

import com.kekens.model.Discipline;
import com.kekens.util.HibernateSessionFactoryUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;

public class DisciplineDao {

    public Discipline findById(int id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(Discipline.class, id);
        }
    }

    public void save(Discipline discipline) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(discipline);
        tx1.commit();
        session.close();
    }

    public void update(Discipline discipline) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(discipline);
        tx1.commit();
        session.close();
    }

    public void delete(Discipline discipline) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(discipline);
        tx1.commit();
        session.close();
    }

    public List<Discipline> findAll() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Discipline", Discipline.class).list();
        }
    }
}