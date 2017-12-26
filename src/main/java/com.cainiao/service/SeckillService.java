package com.cainiao.service;

import com.cainiao.dto.Exposer;
import com.cainiao.dto.SeckillExecution;
import com.cainiao.entity.Seckill;
import com.cainiao.exception.RepeatSeckillException;
import com.cainiao.exception.SeckillCloseException;
import com.cainiao.exception.SeckillException;

import java.util.List;

public interface SeckillService {
    List<Seckill> getSeckillList();
    Seckill getSeckillById(long seckillId);
    Exposer exportSeckillUrl(long seckillId);
    SeckillExecution executeSeckill(long seckillId, long phone, String md5)
            throws SeckillException,SeckillCloseException,RepeatSeckillException;
}
