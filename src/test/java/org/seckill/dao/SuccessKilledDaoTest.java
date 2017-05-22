package org.seckill.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by zengry on 2017/5/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void testInsertSuccessKilled() throws Exception {

        int count = successKilledDao.insertSuccessKilled(1005, 13554428770L);
        System.out.println(count);

    }

    @Test
    public void testQueryByIdWithSeckill() throws Exception {

        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(1005);
        Seckill seckill = successKilled.getSeckill();

        System.out.println(seckill.getSeckillId());
        System.out.println(seckill);


    }
}