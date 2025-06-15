package com.steer.jpahibernateimpl.pojo;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class CustomerTest {
    private static final Logger log = LoggerFactory.getLogger(CustomerTest.class);
    private EntityManagerFactory factory;

    @Before
    public void before(){
        factory = Persistence.createEntityManagerFactory("MyJpa");
    }

    @Test
    public void test_insert(){
        //相当于session
        EntityManager entityManager = factory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        Customer customer = new Customer();
        customer.setName("赵六");
        entityManager.persist(customer);

        tx.commit();

    }

    @Test
    public void test_lazy_query() throws InterruptedException {
        EntityManager entityManager = factory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();
        //立即查用find方法
        Customer customer = entityManager.getReference(Customer.class, 3L);
        log.info(">>>>>>>>>>>");
        Thread.sleep(5000);
        //这时才执行查询SQL
        log.info("加载>>>>"+customer);

        tx.commit();
    }

    /**
     * 若设置了ID,则根据ID执行SELECT,数据存在则更新，不存在则插入
     *
     * 若未设置ID,则直接插入
     */
    @Test
    public void test_merge(){
        //相当于session
        EntityManager entityManager = factory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        Customer customer = new Customer();
        customer.setId(4L);
        customer.setName("钱七");
        entityManager.merge(customer);

        tx.commit();
    }

    @Test
    public void test_delete(){
        EntityManager entityManager = factory.createEntityManager();

        EntityTransaction tx = entityManager.getTransaction();
        tx.begin();

        Customer customer = entityManager.find(Customer.class, 4L);
        //只能删除持久态的对象，不能删除临时态的对象（如new Customer）
        entityManager.remove(customer);

        tx.commit();
    }
}
