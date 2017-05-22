package org.seckill.exception;

/**
 * Created by zengry on 2017/5/22.
 */
public class SeckillException extends RuntimeException {


    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}
