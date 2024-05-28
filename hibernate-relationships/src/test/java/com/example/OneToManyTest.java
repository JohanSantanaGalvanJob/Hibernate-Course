package com.example;

import com.example.model.Address;
import com.example.model.Author;
import com.example.model.Book;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class OneToManyTest {
    @Test
    void oneToMany() {
        insertData();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Author book1 = session.find(Author.class,1L);
        System.out.println(book1.getBooks());
    }

    void insertData(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        Address address1 = new Address("AV Escaleritas", "Las Palmas", "España");
        Address address2 = new Address("address2", "address2", "address2");
        session.persist(address1);
        session.persist(address2);

        Author author1 = new Author("Jesus", "jesus@gmail.com",
                LocalDate.of(2003,11,19) );
        Author author2 = new Author("autor2", "autor2@gmail.com",
                LocalDate.of(2002,9,11) );
        author1.setAddress(address1);
        author2.setAddress(address2);
        session.persist(author1);
        session.persist(author2);

        Book book1 = new Book("book1",19.99,450,true,author1);
        Book book2 = new Book("book2",18.99,150,false,author1);
        Book book3 = new Book("book3",19.99,450,true,author1);
        Book book4 = new Book("book4",18.99,150,false,author2);

        session.persist(book1);
        session.persist(book2);
        session.persist(book3);
        session.persist(book4);

        session.getTransaction().commit();

    }
}
