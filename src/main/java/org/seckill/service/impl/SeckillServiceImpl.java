package org.seckill.service.impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SuccessKilled;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * 接口实现类
 * Created by zengry on 2017/5/22.
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String salt = "&%#&*^&HYTT@GDHA(&^D$D#";

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 5);
    }

    public Seckill queryById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exposerSeckillUrl(long seckillId) {

        Seckill seckill = seckillDao.queryById(seckillId);
        if(seckill == null){
            return new Exposer(false, seckillId);
        }

        Date nowTime = new Date();
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        if(nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()){
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(),
                    endTime.getTime());
        }

        String md5 = generateMd5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    /**
     * 生成MD5密文
     * @param seckillId
     * @return
     */
    private String generateMd5(long seckillId){
        return DigestUtils.md5DigestAsHex((seckillId + "$" + salt).getBytes());
    }

    /**
     * 使用注解控制事物方法的优点
     * 1.明确标注事务方法的编程风格，可以使得开发团队达成一致
     * 2.可以保证事务方法的执行时间尽可能的短，不要穿插其他的网络操作，例如：Http请求，RPC等。
     *   如果确实需要，将相关操作剥离至方法之外。
     * 3.不是所有的方法都需要事务，比如：只有一条修改操作，或 只读操作。
     * @param seckillId
     * @param phone
     * @param md5
     * @return
     * @throws SeckillException
     * @throws RepeatKillException
     * @throws SeckillCloseException
     */
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long phone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(generateMd5(seckillId))){
            throw new SeckillException("seckill data rewrited");
        }

        //执行秒杀：减库存 + 记录购买明细
        try {
            //减库存
            Date killTime = new Date();
            int updateCount = seckillDao.reduceNumber(seckillId, killTime);
            if(updateCount <= 0){
                throw new SeckillCloseException("seckill is closed");
            }else{
                //记录购买明细
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, phone);
                if(insertCount <= 0){
                    throw new RepeatKillException("seckill repeats kill");
                }else{
                    SuccessKilled successKilled
                            = successKilledDao.queryByIdWithSeckill(seckillId, phone);
                    return new SeckillExecution(seckillId, SeckillStateEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseException e1) {
            throw e1;
        } catch (RepeatKillException e2) {
            throw e2;
        } catch (SeckillException e){
            throw new SeckillException("seckill inner error : " + e.getMessage());
        }


    }
}




















