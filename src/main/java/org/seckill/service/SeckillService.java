package org.seckill.service;

/**
 * Created by zengry on 2017/5/22.
 */

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;

import java.util.List;

/**
 * 业务接口：站在 “使用者” 角度设计接口
 * 1） 方法定义粒度
 * 2） 参数
 * 3） 返回类型、异常
 */
public interface SeckillService {

    /**
     * 所有秒杀产品列表
     * @return
     */
    List<Seckill> getSeckillList();


    /**
     * 秒杀产品详情
     * @param seckillId
     * @return
     */
    Seckill queryById(long seckillId);


    /**
     * 秒杀开始时输出秒杀接口地址
     * 否则输出系统时间和秒杀开始时间
     * @param seckillId
     */
    Exposer exposerSeckillUrl(long seckillId);


    /**
     * 执行秒杀操作
     * @param seckillId
     * @param phone
     * @param md5
     */
    SeckillExecution executeSeckill(long seckillId, long phone, String md5)
        throws SeckillException, RepeatKillException, SeckillCloseException;









}
