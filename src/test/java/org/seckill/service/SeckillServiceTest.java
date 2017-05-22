package org.seckill.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.seckill.dao.SeckillDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.exception.SeckillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by zengry on 2017/5/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-service.xml", "classpath:spring/spring-dao.xml"})
public class SeckillServiceTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void testGetSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
//        for(Seckill obj : list){
//            System.out.println(obj);
//        }
        logger.info("list{}", list);

    }

    @Test
    public void testQueryById() throws Exception {

        Seckill seckill = seckillService.queryById(1005);
        logger.info("seckill{}", seckill);

    }

    @Test
    public void testExecuKill() throws Exception {

        long seckillId = 1007;

        Exposer exposer = seckillService.exposerSeckillUrl(seckillId);
        String md5 = exposer.getMd5();

        if(exposer.isExposed()){
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(seckillId, 13554428990L, md5);
                System.out.println(seckillExecution.getStateInfo());
            } catch (SeckillException e) {
                logger.error(e.getMessage());
            }
        }else{
            logger.warn("exposer{}", exposer);
        }
    }


    @Test
    public void testExposerSeckillUrl() throws Exception {

    }

    @Test
    public void testExecuteSeckill() throws Exception {

    }



}