package com.example;

import com.example.model.Address;
import com.example.model.Author;
import org.hibernate.Session;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class OneToOneTest {

    @Test
    void oneToOne() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        insertData();
        Author author1 = session.find(Author.class,1L);

        System.out.println(author1.getAddress());
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

        session.getTransaction().commit();

    }
}
