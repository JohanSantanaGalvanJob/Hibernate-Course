package com.example;

import com.example.model.Employee;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

/*
CRUD:
- CREATE
- READ
- UPDATE
- DELETE
 */

public class HibernateTest {
    @Test
    void persist() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Employee employee1 = new Employee("employee1",23);
        Employee employee2 = new Employee("employee2",25);

        session.persist(employee1);
        session.persist(employee2);
        session.getTransaction().commit();
        session.close();
    }

    @Test
    void retrieve() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Employee employee1 = session.find(Employee.class, 1L);
        System.out.println(employee1);
    }
    @Test
    void update() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Employee employee1 = new Employee("employee1", 23);
        employee1.setId(1L);
        employee1.setAge(30);
        session.beginTransaction();
        session.merge(employee1);
        session.getTransaction().commit();
        System.out.println(employee1);
    }
    @Test
    void delete() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        session.remove(session.find(Employee.class,1L));
        session.getTransaction().commit();
        System.out.println("Deleted");
    }
}
