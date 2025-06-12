package com.steer.hibernate.pojo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

public class CustomerTest {

    private SessionFactory sf;

    @Before
    public void init(){
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure("/hibernate/hibernate.cfg.xml").build();
        sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    @Test
    public void test_insert(){
        try (Session session = sf.openSession()){
            Transaction tx = session.beginTransaction();
            Customer customer = new Customer();
            customer.setName("张三");
            customer.setAge(3);
            customer.setAddress("杭州");
            session.save(customer);
            tx.commit();
        }
    }
}

