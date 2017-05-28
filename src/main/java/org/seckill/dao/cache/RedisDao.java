package org.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/**
 * Created by zengry on 2017/5/28.
 */
public class RedisDao {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private final JedisPool jedisPool;

    //反序列化工具
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip, int port){
        jedisPool = new JedisPool(ip, port);
    }

    public Seckill getSeckill(long seckillId){
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckillId;
                //redis 没有实现内部序列化操作
                //取值过程： get(key.byte[]) -> (Object) byte[] ->反序列化 -》Object
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes != null){
                    //反序列化
                    Seckill seckill = schema.newMessage();
                    ProtobufIOUtil.mergeFrom(bytes, seckill, schema);
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }


    public String putSeckill(Seckill seckill){
        //序列化对象
        //object -> 序列化 ->byte[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:" + seckill.getSeckillId();
                byte[] bytes = ProtobufIOUtil.toByteArray(seckill, schema,
                        LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60 * 60;// 1小时
                String result = jedis.set(key.getBytes(), bytes);
                return result;
            }finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
