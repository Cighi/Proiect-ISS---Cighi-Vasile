package org.iss.Repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.iss.Domain.Employee;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

public class EmployeeRepository implements IEmployeeRepository {
    @Override
    public Employee findByCredentials(String username,String password) {
        try(Session session = HibernateUtils.getSessionFactory().openSession()){
            return session.createQuery("from Employee where username=?1 and password=?2",Employee.class).setParameter(1,username).setParameter(2,password).getSingleResultOrNull();
        }
    }

    @Override
    public Employee findOne(Long aLong) {
        try(Session session=HibernateUtils.getSessionFactory().openSession()){
            return session.find(Employee.class, aLong);
        }
    }

    @Override
    public Collection<Employee> findAll() {
       try(Session session = HibernateUtils.getSessionFactory().openSession()){
           Query<Employee> query = session.createQuery("FROM Employee ", Employee.class);
           return query.list();
       }
    }

    @Override
    public Employee add(Employee employee) {
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            session.persist(employee);
            session.flush();
        });
        return employee;
    }

    @Override
    public Boolean delete(Long aLong) {
        AtomicBoolean deleted = new AtomicBoolean(false);
        HibernateUtils.getSessionFactory().inTransaction(session -> {
            Employee employee = session.find(Employee.class, aLong);
            if(employee!=null) {
                session.remove(employee);
                deleted.set(true);
            }
        });
        return deleted.get();

    }

    @Override
    public Employee update(Employee entity) {
        HibernateUtils.getSessionFactory().inTransaction(session -> session.merge(entity));
        return entity;
    }
}
