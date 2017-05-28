package org.seckill.web;

import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.dto.SeckillResult;
import org.seckill.entity.Seckill;
import org.seckill.enums.SeckillStateEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillCloseException;
import org.seckill.exception.SeckillException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by zengry on 2017/5/22.
 */


@Controller
@RequestMapping("/seckill")
public class SeckillController {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;


    /**
     * 秒杀产品列表
     * @param model
     * @return
     */
    @RequestMapping(value="/list", method = RequestMethod.GET)
    public String list(Model  model){

        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list", list);
        return "list";
    }


    /**
     * 产品详情
     * @param seckillId
     * @param model
     * @return
     */
    @RequestMapping(value="/{seckillId}/detail", method = RequestMethod.GET)
    public String detail(@PathVariable("seckillId") Long seckillId, Model model){
        if(seckillId == null){
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.queryById(seckillId);
        if(seckill == null){
            return "forwad:/seckill/list";
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }



    @RequestMapping(value="/{seckillId}/exposer",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<Exposer> exposer(@PathVariable("seckillId") Long seckillId){

        SeckillResult<Exposer> result;

        try {
            Exposer exposer = seckillService.exposerSeckillUrl(seckillId);
            result = new SeckillResult<Exposer>(true, exposer);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result = new SeckillResult<Exposer>(false, e.getMessage());
        }
        return result;
    }




    @RequestMapping(value = "/{seckillId}/{md5}/execute",
                    method = RequestMethod.POST,
                    produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(
            @PathVariable("seckillId") Long seckillId,
            @CookieValue("killPhone") Long phone,
            @PathVariable("md5") String md5){

        if(phone == null){
            return new SeckillResult<SeckillExecution>(false, "用户未注册");
        }
        try {
//            SeckillExecution execution = seckillService.executeSeckill(seckillId, phone, md5);

            //通过存储过程调用
            SeckillExecution execution = seckillService.executeSeckillByProcedure(seckillId, phone, md5);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillCloseException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.END);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (RepeatKillException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(true, execution);
        } catch (SeckillException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStateEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(true, execution);
        }
    }


    @RequestMapping(value="/time/now", method = RequestMethod.GET)
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return new SeckillResult<Long>(true, now.getTime());
    }

}
