package com.kekens.soaback1.dao;

import com.kekens.soaback1.model.Coordinates;
import com.kekens.soaback1.model.Discipline;
import com.kekens.soaback1.model.LabWork;
import com.kekens.soaback1.util.HibernateSessionFactoryUtil;
import com.kekens.soaback1.util.LabWorkFilterUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

public class LabWorkDao {

    public LabWork findById(int id) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.get(LabWork.class, id);
        }
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
        session.delete(labWork);
        tx1.commit();
        session.close();
    }

    public List<LabWork> findAll() {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM LabWork", LabWork.class).list();
        }
    }

    public List<LabWork> findAllFiltering(LabWorkFilterUtil labWorkFilterUtil) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<LabWork> criteriaQuery = criteriaBuilder.createQuery(LabWork.class);
            Root<LabWork> from = criteriaQuery.from(LabWork.class);
            Join<LabWork, Coordinates> joinCoordinates = from.join("coordinates");
            Join<LabWork, Discipline> joinDiscipline = from.join("discipline");

            criteriaQuery.orderBy(labWorkFilterUtil.setOrder(from, joinCoordinates, joinDiscipline, criteriaBuilder));

            Predicate predicate = labWorkFilterUtil.getPredicate(from, joinCoordinates, joinDiscipline, criteriaBuilder);
            criteriaQuery.where(predicate);

            int size = Integer.parseInt(labWorkFilterUtil.pageSize);
            int index = Integer.parseInt(labWorkFilterUtil.pageIndex);

            TypedQuery<LabWork> typedQuery = session.createQuery(criteriaQuery);
            typedQuery.setFirstResult((index - 1) * size);
            typedQuery.setMaxResults(size);

            return typedQuery.getResultList();
        }
    }

}
