package com.cainiao.service;

import com.cainiao.dto.Exposer;
import com.cainiao.dto.SeckillExecution;
import com.cainiao.entity.Seckill;
import com.cainiao.exception.RepeatSeckillException;
import com.cainiao.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml",
"classpath:spring/spring-service.xml"})

public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> seckillList = seckillService.getSeckillList();
        System.out.println(seckillList);
    }

    @Test
    public void getSeckillById() throws Exception {
        long seckillId = 1;
        Seckill seckill = seckillService.getSeckillById(seckillId);
        System.out.println(seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
    }
    @Test public void testSeckillLogic() throws  Exception{
        long seckillId = 1;
        Exposer exposer = seckillService.exportSeckillUrl(1);
        if (exposer.isExposed()){
            long phone = 15996200988L;
            try {
                SeckillExecution seckillExecution = seckillService.executeSeckill(exposer.getSeckillId(), phone, exposer.getMd5());
                System.out.println("*****success*****");
            }catch (SeckillCloseException e){
                e.printStackTrace();
            }catch (RepeatSeckillException e1){
                e1.printStackTrace();
            }catch (Exception e2){
                e2.printStackTrace();
            }
        }else{
            System.out.println("seckill is not start\n");
            System.out.println(exposer);
        }
    }
    @Test
    public void executeSeckill() throws Exception {
    }

}