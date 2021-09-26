package com.kekens.soa_lab_1.dao;

import com.kekens.soa_lab_1.model.Coordinates;
import com.kekens.soa_lab_1.model.Discipline;
import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.util.LabWorkFilterConfiguration;
import com.kekens.soa_lab_1.util.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
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

    public List<LabWork> findAllFiltering(LabWorkFilterConfiguration labWorkFilterConfiguration) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<LabWork> criteriaQuery = criteriaBuilder.createQuery(LabWork.class);
            Root<LabWork> from = criteriaQuery.from(LabWork.class);
            Join<LabWork, Coordinates> joinCoordinates = from.join("coordinates");
            Join<LabWork, Discipline> joinDiscipline = from.join("discipline");

            // TODO METHOD SET ORDER
            if (labWorkFilterConfiguration.sortingParams != null) {
                List<Order> orderList = new ArrayList<>();

                for (String sortParam : labWorkFilterConfiguration.sortingParams) {
                    String[] args = sortParam.split("-");

                    if ((args[0].startsWith("coordinates_")) || (args[0].startsWith("discipline_"))) {
                        boolean isCoordinates = args[0].startsWith("coordinates_");
                        Join join = isCoordinates ? joinCoordinates : joinDiscipline;
                        args[0] = isCoordinates ? args[0].replaceAll("coordinates_", "") : args[0].replaceAll("discipline_", "");

                        if ((args.length == 1) || ((args.length == 2) && (args[1].equals("asc")))) {
                            orderList.add(criteriaBuilder.asc(join.get(args[0])));
                        } else if (args.length == 2) {
                            orderList.add(criteriaBuilder.desc(join.get(args[0])));
                        }
                    } else {
                        if ((args.length == 1) || ((args.length == 2) && (args[1].equals("asc")))) {
                            orderList.add(criteriaBuilder.asc(from.get(sortParam)));
                        } else if (args.length == 2) {
                            orderList.add(criteriaBuilder.desc(from.get(args[0])));
                        }
                    }
                }

                criteriaQuery.orderBy(orderList);
            }

            Predicate predicate = labWorkFilterConfiguration.getPredicate(from, joinCoordinates, joinDiscipline, criteriaBuilder);
            criteriaQuery.where(predicate);

            TypedQuery<LabWork> typedQuery = session.createQuery(criteriaQuery);
            typedQuery.setFirstResult((labWorkFilterConfiguration.pageIndex - 1) * labWorkFilterConfiguration.pageSize);
            typedQuery.setMaxResults(labWorkFilterConfiguration.pageSize);

            return typedQuery.getResultList();
        }
    }

}
