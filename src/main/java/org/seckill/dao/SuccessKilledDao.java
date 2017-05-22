package org.seckill.dao;

import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SuccessKilled;

/**
 * Created by zengry on 2017/5/21.
 */
public interface SuccessKilledDao {

    /**
     * 插入购买明细
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId")long seckillId, @Param("userPhone")long userPhone);


    /**
     * 查询购买明细及对应秒杀产品信息
     * @param seckilled
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);


}
