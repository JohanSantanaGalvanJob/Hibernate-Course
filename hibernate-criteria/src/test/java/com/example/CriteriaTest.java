package com.example;

import com.example.model.Author;
import com.example.model.Book;
import jdk.swing.interop.SwingInterOpUtils;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

public class CriteriaTest {

    @Test
    void findAll() {
        insertData();
        var session = HibernateUtil.getSessionFactory().openSession();
        var builder = session.getCriteriaBuilder();
        var criteria = builder.createQuery(Author.class);
        criteria.select(criteria.from(Author.class));
        var authors = session.createQuery(criteria).list();
        authors.forEach(System.out::println);
    }

    @Test
    void findById() {
        insertData();
        var session = HibernateUtil.getSessionFactory().openSession();
        var builder = session.getCriteriaBuilder();
        var criteria = builder.createQuery(Author.class);

        var authors = criteria.from(Author.class);
        criteria.select(authors).where(builder.equal(authors.get("id"),1L));
        Author author = session.createQuery(criteria).getSingleResult();
        System.out.println(author);
    }

    @Test
    void findByEmailLike() {
        insertData();
        var session = HibernateUtil.getSessionFactory().openSession();
        var builder = session.getCriteriaBuilder();
        var criteria = builder.createQuery(Author.class);

        var authors = criteria.from(Author.class);
        criteria.select(authors).where(builder.like(authors.get("email"),"%email.com"));
        var authorsFromDB = session.createQuery(criteria).getResultList();
        authorsFromDB.forEach(System.out::println);
    }

    @Test
    void findByPriceGreaterThan() {
        insertData();
        var session = HibernateUtil.getSessionFactory().openSession();
        var builder = session.getCriteriaBuilder();
        var criteria = builder.createQuery(Book.class);

        var books = criteria.from(Book.class);
        criteria.select(books).where(builder.greaterThanOrEqualTo(books.get("price"),15));
        var result = session.createQuery(criteria).getResultList();
        result.forEach(System.out::println);
    }

    @Test
    void findBetween() {
        insertData();
        var session = HibernateUtil.getSessionFactory().openSession();
        var builder = session.getCriteriaBuilder();
        var criteria = builder.createQuery(Author.class);

        var authors = criteria.from(Author.class);
        criteria.select(authors).where(builder.between(
                authors.get("birthDate"),
                LocalDate.of(1992,1,1),
                LocalDate.of(1996,1,1)
        ));
        var authorsFromDB = session.createQuery(criteria).getResultList();
        authorsFromDB.forEach(System.out::println);
    }

    @Test
    void findByMultipleWhere() {
        insertData();
        var session = HibernateUtil.getSessionFactory().openSession();
        var builder = session.getCriteriaBuilder();
        var criteria = builder.createQuery(Book.class);

        var books = criteria.from(Book.class);
        var priceMedium = builder.greaterThanOrEqualTo(books.get("price"),15);
        var numPagesFilter = builder.lessThanOrEqualTo(books.get("numPages"),80);
        criteria.select(books).where(builder.and(priceMedium,numPagesFilter));
        var result = session.createQuery(criteria).getResultList();
        result.forEach(System.out::println);
    }

    @Test
    void multiSelect() {
        insertData();
        var session = HibernateUtil.getSessionFactory().openSession();
        var builder = session.getCriteriaBuilder();
        var criteria = builder.createQuery(Object[].class);

        var root = criteria.from(Book.class);
        criteria.multiselect(root.get("title"), root.get("price"));
        List<Object[]> results = session.createQuery(criteria).getResultList();
        for (Object[] result : results){
            System.out.println("Title: " + result[0] + " Price: " + result[1]);
        }
    }

    void insertData(){
        var session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        Author author1 = new Author("a12","a12author1@gmail.com", LocalDate.of(1998,1,1));
        Author author2 = new Author("a22","author2@gmail.com", LocalDate.of(1990,1,1));
        Author author3 = new Author("a32","author3@email.com", LocalDate.of(1995,1,1));
        Author author4 = new Author("a42","author4@email.com", LocalDate.of(1996,1,1));

        session.persist(author1);
        session.persist(author2);
        session.persist(author3);
        session.persist(author4);

        Book book1 = new Book("Capitan PitoChocho",12.99,40,true,author1);
        Book book2 = new Book("Capitan PitoChocho 2",14.99,60,true,author2);
        Book book3 = new Book("Capitan PitoChocho 3",15.99,70,true,author3);
        Book book4 = new Book("Capitan PitoChocho 4",20.99,100,true,author4);

        session.persist(book1);
        session.persist(book2);
        session.persist(book3);
        session.persist(book4);

        session.getTransaction().commit();
    }
}
