package org.seckill.exception;

/**
 * 重复秒杀异常
 * Created by zengry on 2017/5/22.
 */
public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
