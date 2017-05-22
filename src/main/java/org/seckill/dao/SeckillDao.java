package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.Seckill;

import java.util.Date;
import java.util.List;

/**
 * Created by zengry on 2017/5/21.
 */
public interface SeckillDao {


    /**
     * 减库存
     * @param seckillid
     * @param killTime
     * @return
     */
    int reduceNumber(@Param("seckillId") long seckillid, @Param("killTime") Date killTime);


    /**
     * 根据ID查询秒杀产品
     * @param seckillId
     * @return
     */
    Seckill queryById(@Param("seckillId") long seckillId);


    /**
     * 根据偏移量查询秒杀产品列表
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit")  int limit);





}
