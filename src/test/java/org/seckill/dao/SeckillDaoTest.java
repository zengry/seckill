package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zengry on 2017/5/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    @Resource
    private SeckillDao seckillDao;

    @Test
    public void testReduceNumber() throws Exception {

        int count = seckillDao.reduceNumber(1005, new Date());
        System.out.println(count);

    }

    @Test
    public void testQueryById() throws Exception {

        Seckill seckill = seckillDao.queryById(1005);
        System.out.println(seckill);



    }

    @Test
    public void testQueryAll() throws Exception {

        List<Seckill> list = seckillDao.queryAll(0, 5);
        for(Seckill obj : list){
            System.out.println(obj);
        }

    }
}