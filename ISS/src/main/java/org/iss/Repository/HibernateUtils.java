package org.iss.Repository;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.iss.Domain.Bug;
import org.iss.Domain.Employee;

public class HibernateUtils {
    private static SessionFactory sessionFactory;
    public static SessionFactory getSessionFactory(){
        if(sessionFactory==null || sessionFactory.isClosed())
            sessionFactory = createNewSessionFactory();
        return sessionFactory;
    }

    private static SessionFactory createNewSessionFactory() {
        sessionFactory = new Configuration()
                .addAnnotatedClass(Employee.class)
                .addAnnotatedClass(Bug.class)
                .buildSessionFactory();
        return sessionFactory;
    }
    public static void closeSessionFactory(){
        if(sessionFactory!=null)
            sessionFactory.close();
    }
}
