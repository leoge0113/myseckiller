package com.cainiao.service.impl;

import com.cainiao.dao.SeckillDao;
import com.cainiao.dao.SuccessKilledDao;
import com.cainiao.dto.Exposer;
import com.cainiao.enumer.SeckillStatEnum;
import com.cainiao.exception.RepeatSeckillException;
import com.cainiao.exception.SeckillCloseException;
import com.cainiao.exception.SeckillException;
import com.cainiao.service.SeckillService;
import com.cainiao.dto.SeckillExecution;
import com.cainiao.entity.Seckill;
import com.cainiao.entity.SuccessKilled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
@Service
public class SeckillServiceImpl implements SeckillService {
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getSeckillById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    public Exposer exportSeckillUrl(long seckillId) {
        //we will add seckill to redis, in order
        Seckill seckill = seckillDao.queryById(seckillId);
        if (seckill == null){
            return new Exposer(false, seckillId);
        }else {
            //insert into redis

        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date now = new Date();
        if (startTime.getTime() > now.getTime() || endTime.getTime() < now.getTime()){
            return new Exposer(false, seckillId, now.getTime(), startTime.getTime(), endTime.getTime());
        }else{
            String md5 = getMD5(seckillId);
            return new Exposer(true, md5, seckillId);//? do not understand
        }
    }
    private String getMD5(long seckillId){
        String base = seckillId + "gsgdbgaf/qwrc";
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }
    @Transactional
    public SeckillExecution executeSeckill(long seckillId, long phone, String md5)
            throws SeckillException, SeckillCloseException, RepeatSeckillException {
        if (md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("Date was rewrited!");
        }
        try{
            Date now = new Date();
            int count = successKilledDao.insertSuccessKilled(seckillId, phone);
            System.out.printf("insert success killed count=%d",count);
            if (count <= 0){
                throw new RepeatSeckillException("seckill repeated!");
            }else{
                int updateCount = seckillDao.reduceNumber(seckillId,now);
                if (updateCount <= 0){
                    throw new SeckillCloseException("seckill is closed!");
                }else {
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, phone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw e1;
        }catch (RepeatSeckillException e2){
            throw e2;
        }catch (Exception e){
           // logger.error(e.getCause(),e);
            logger.error(e.getMessage(),e);
            throw new SeckillException("Inner error：" + e.getMessage());
        }
    }
}
