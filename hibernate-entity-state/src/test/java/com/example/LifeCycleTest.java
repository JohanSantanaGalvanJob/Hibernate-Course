package com.example;


import com.example.model.Address;
import com.example.model.Author;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

/*
1. Transient
2. Managed
3. Detached
4. Removed
 */
public class LifeCycleTest {
    @Test
    void transientState() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Address address1 = new Address("address1","address1","address1");
        System.out.println(session.contains(address1));

        //Cuando no es managed se crean problemas al asociarse con otra entidad.
        Author author1 = new Author("sos","sos@gmail.com",null);
        session.beginTransaction();
        session.persist(author1);
        session.getTransaction().commit();

    }

    @Test
    void detachedState() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Address address1 = new Address("address1","address1","address1");
        address1.setId(1L);

        // PersistentObjectException: detached entity passed to persist: com.example.model.Address
        // Como hemos puesto datos desde la BBDD, al persistir no encuentra el historial de persistencias y por eso nos da error
        session.beginTransaction();
        //session.persist(address1);
        session.merge(address1);
        session.getTransaction().commit();

    }

    @Test
    void detachedState2() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Address address1 = new Address("address1","address1","address1");
        address1.setId(1L);

        // PersistentObjectException: detached entity passed to persist: com.example.model.Address
        // Como hemos puesto datos desde la BBDD, al persistir no encuentra el historial de persistencias y por eso nos da error
        session.beginTransaction();
        //session.persist(address1);
        session.merge(address1);
        session.getTransaction().commit();
        session.detach(address1);
        address1.setCity("Barcelona");

        session.beginTransaction();
        // Nos da error
        //session.persist(address1);
        session.merge(address1);
        session.getTransaction().commit();
    }

    @Test
    void managedState() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Address address1 = new Address("address1","address1","address1");
        session.beginTransaction();
        session.persist(address1);
        session.getTransaction().commit();
        System.out.println(session.contains(address1));
    }

    @Test
    void removedState() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Address address1 = new Address("address1","address1","address1");
        address1.setId(1L);
        session.beginTransaction();
        session.remove(address1);
        session.getTransaction().commit();
        System.out.println(session.contains(address1));
    }

    @Test
    void differentSessions() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Address address1 = session.find(Address.class, 1L);
        System.out.println(session.contains(address1));
        session.close();
        session = HibernateUtil.getSessionFactory().openSession();
        System.out.println(session.contains(address1));

    }

    @Test
    void load() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Address address1 = session.getReference(Address.class, 1L); // No carga el objeto hasta que se llama a uno de sus atributos
        System.out.println("====================");
        System.out.println(address1.getCountry());
    }
}
