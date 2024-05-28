package com.example;

import com.example.model.Address;
import com.example.model.Author;
import com.example.model.Book;
import com.example.model.Category;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class QueriesTest {

    @Test
    void findAll() {
        insertData();
        Session session = HibernateUtil.getSessionFactory().openSession();
        String jpql = "SELECT a FROM Author a";

       var query = session.createQuery(jpql, Author.class);
        query.list().forEach(System.out::println);

    }

    @Test
    void findByEmail() {
        insertData();
        Session session = HibernateUtil.getSessionFactory().openSession();
        String jpql = "SELECT a FROM Author a WHERE a.email = :email";

        var query = session.createQuery(jpql, Author.class);
        query.setParameter("email","auth1@gmail.com");

        var author = query.getSingleResult();
        System.out.println(author);

    }

    @Test
    void findByIdIn() {
        insertData();
        Session session = HibernateUtil.getSessionFactory().openSession();
        String jpql = "SELECT a FROM Author a WHERE a.id IN :ids";

        var query = session.createQuery(jpql, Author.class);
        query.setParameter("ids", List.of(1L,3L));

        query.list().forEach(System.out::println);

    }

    @Test
    void findByAddressCity() {
        insertData();
        Session session = HibernateUtil.getSessionFactory().openSession();
        String jpql = "SELECT a FROM Author a WHERE a.address.city = :city";

        var query = session.createQuery(jpql, Author.class);
        query.setParameter("city","Madrid");

        query.list().forEach(System.out::println);
    }

    @Test
    void count() {
        insertData();
        Session session = HibernateUtil.getSessionFactory().openSession();
        String jpql = "SELECT count(a.id) FROM Author a";

        var query = session.createQuery(jpql, Long.class);
        Long count = query.getSingleResult();
        System.out.println("Número autores: "+count);
    }

    @Test
    void update() {
        insertData();
        Session session = HibernateUtil.getSessionFactory().openSession();
        String jpql = "update Author a set a.name = :name where a.id = :id";
        session.beginTransaction();

        int query = session.createMutationQuery(jpql)
                .setParameter("name","Modificado")
                .setParameter("id",1)
                .executeUpdate();

        session.getTransaction().commit();
        System.out.println("Authors updated: "+query);
    }

    @Test
    void findByDates() {
        insertData();
        Session session = HibernateUtil.getSessionFactory().openSession();
        String jpql = "SELECT a FROM Author a WHERE a.birthDate BETWEEN :start AND :end";

        var query = session.createQuery(jpql, Author.class);
        query.setParameter("start",LocalDate.of(1980,1,1));
        query.setParameter("end",LocalDate.of(1995,1,1));

        query.list().forEach(System.out::println);
    }

    void insertData(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Category cat1 = new Category("Fantasy", 13);
        Category cat2 = new Category("cat2", 13);
        Category cat3 = new Category("cat3", 13);
        Category cat4 = new Category("cat4", 13);

        Address address1 = new Address("address1","Madrid","address1");
        Address address2 = new Address("address2","address2","address2");
        Address address3 = new Address("address3","Barcelona","address3");
        Address address4 = new Address("address4","Madrid","address4");

        Author auth1 = new Author("Fantasy", "auth1@gmail.com", LocalDate.of(1980,1,1));
        Author auth2 = new Author("cat2", "auth2@gmail.com",LocalDate.of(1995,1,1));
        Author auth3 = new Author("cat3", "auth3@gmail.com",LocalDate.of(1970,1,1));
        Author auth4 = new Author("cat4", "auth4@gmail.com",LocalDate.of(1992,1,1));

        Book book1 = new Book("book1",19.99,450,true,null);
        Book book2 = new Book("book2",18.99,150,false,null);
        Book book3 = new Book("book3",19.99,450,true,null);
        Book book4 = new Book("book4",18.99,150,false,null);

        session.persist(cat1);
        session.persist(cat2);
        session.persist(cat3);
        session.persist(cat4);

        session.persist(address1);
        session.persist(address2);
        session.persist(address3);
        session.persist(address4);

        auth1.setAddress(address1);
        auth2.setAddress(address2);
        auth3.setAddress(address3);
        auth4.setAddress(address4);

        session.persist(auth1);
        session.persist(auth2);
        session.persist(auth3);
        session.persist(auth4);

        book1.getCategories().add(cat1);
        book1.getCategories().add(cat2);
        book1.getCategories().add(cat3);

        book2.getCategories().add(cat4);
        book2.getCategories().add(cat2);
        book2.getCategories().add(cat3);

        book3.getCategories().add(cat2);

        session.persist(book1);
        session.persist(book2);
        session.persist(book3);
        session.persist(book4);

        session.getTransaction().commit();

    }
}
