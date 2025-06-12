package com.steer.hibernate.pojo;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.Before;
import org.junit.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class CustomerTest {
    private static final Logger log = LoggerFactory.getLogger(CustomerTest.class);
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

    /**
     * id存在则更新,更新的是所有字段
     */
    @Test
    public void test_save_or_update(){
        try (Session session = sf.openSession()){
            Transaction tx = session.beginTransaction();
            Customer customer = new Customer();
            customer.setId(1L);
            customer.setName("李四");
            session.saveOrUpdate(customer);
            tx.commit();
        }
    }

    @Test
    public void test_query(){
        try (Session session = sf.openSession()){
            Transaction tx = session.beginTransaction();
            Customer customer = session.find(Customer.class, 1L);
            log.info(customer.toString());
            tx.commit();
        }
    }

    @Test
    public void test_lazy_query(){
        try (Session session = sf.openSession()){
            Transaction tx = session.beginTransaction();
            Customer customer = session.load(Customer.class, 1L);
            log.info(">>>>>>>>>>>");
            Thread.sleep(5000);
            //这时才执行查询SQL
            log.info("加载>>>>"+customer);
            tx.commit();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_query_by_hql(){
        try (Session session = sf.openSession()){
            Transaction tx = session.beginTransaction();
            String hql = "FROM Customer WHERE id = :cid";
            List<Customer> list = session.createQuery(hql,Customer.class).setParameter("cid",2L).getResultList();
            log.info(list.toString());
            tx.commit();
        }
    }



    @Test
    public void test_delete(){
        try (Session session = sf.openSession()){
            Transaction tx = session.beginTransaction();
            Customer customer = new Customer();
            customer.setId(1L);
            session.delete(customer);
            tx.commit();
        }
    }
}

