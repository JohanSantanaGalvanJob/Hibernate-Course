package com.example;

import com.example.model.Address;
import com.example.model.Author;
import com.example.model.Book;
import com.example.model.Category;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class ManyToManyTest {
    @Test
    void manyToMany() {
        insertData();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Book book1 = session.find(Book.class,1L);
        System.out.println(book1.getCategories());
    }

    void insertData(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Category cat1 = new Category("Fantasy", 13);
        Category cat2 = new Category("cat2", 13);
        Category cat3 = new Category("cat3", 13);
        Category cat4 = new Category("cat4", 13);

        Book book1 = new Book("book1",19.99,450,true,null);
        Book book2 = new Book("book2",18.99,150,false,null);
        Book book3 = new Book("book3",19.99,450,true,null);
        Book book4 = new Book("book4",18.99,150,false,null);

        session.persist(cat1);
        session.persist(cat2);
        session.persist(cat3);
        session.persist(cat4);

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
