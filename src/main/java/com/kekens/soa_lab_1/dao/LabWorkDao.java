package com.kekens.soa_lab_1.dao;

import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.util.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class LabWorkDao {

    public LabWork findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(LabWork.class, id);
    }

    public void save(LabWork labWork) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(labWork);
        tx1.commit();
        session.close();
    }

    public void update(LabWork labWork) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(labWork);
        tx1.commit();
        session.close();
    }

    public void delete(LabWork labWork) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(labWork);
        tx1.commit();
        session.close();
    }

    public List<LabWork> findAll() {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().createQuery("FROM LabWork", LabWork.class).list();
    }


}
