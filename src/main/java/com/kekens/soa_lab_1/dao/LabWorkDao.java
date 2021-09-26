package com.kekens.soa_lab_1.dao;

import com.kekens.soa_lab_1.model.Coordinates;
import com.kekens.soa_lab_1.model.Discipline;
import com.kekens.soa_lab_1.model.LabWork;
import com.kekens.soa_lab_1.util.FilterConfiguration;
import com.kekens.soa_lab_1.util.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

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

    public List<LabWork> findAllFiltering(FilterConfiguration filterConfiguration) {
        try (Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<LabWork> criteriaQuery = criteriaBuilder.createQuery(LabWork.class);
            Root<LabWork> from = criteriaQuery.from(LabWork.class);
            Join<LabWork, Coordinates> joinCoordinates = from.join("coordinates");
            Join<LabWork, Discipline> joinDiscipline = from.join("discipline");

            if (filterConfiguration.sortingParams != null) {
                List<Order> orderList = new ArrayList<>();

                for (String sortParam : filterConfiguration.sortingParams) {
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



            return session.createQuery(criteriaQuery).list();
        }
    }

}
