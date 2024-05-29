package org.iss.Repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.iss.Domain.Bug;
import org.iss.Domain.Employee;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

public class BugRepository implements IBugRepository {
    @Override
    public Bug findOne(Long aLong) {
        return null;
    }

    @Override
    public Collection<Bug> findAll() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            Query<Bug> query = session.createQuery("FROM Bug ", Bug.class);
            return query.list();
        }
    }

    @Override
    public Bug add(Bug entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            session.persist(entity);
            session.flush();
        });
        return entity;
    }

    @Override
    public Boolean delete(Long aLong) {
        AtomicBoolean deleted = new AtomicBoolean(false);
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Bug bug = session.find(Bug.class, aLong);
            if (bug != null) {
                session.remove(bug);
                deleted.set(true);
            }
        });
        return deleted.get();
    }

    @Override
    public Bug update(Bug entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            session.merge(entity);
        });
        return entity;
    }
}
