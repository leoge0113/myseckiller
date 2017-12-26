package com.cainiao.dao;
import org.apache.ibatis.annotations.Param;
import com.cainiao.entity.Seckill;

import java.util.Date;
import java.util.List;

public interface SeckillDao {
    /*
    We need to use @Param when there are more than one paramter, otherwise ...
    org.mybatis.spring.MyBatisSystemException: nested exception is
    org.apache.ibatis.binding.BindingException: Parameter 'seckillId' not found.
     Available parameters are [0, killTime, param1, param2]
     */
    int reduceNumber(@Param ("seckillId") long seckillId, @Param("killTime") Date killTime);
    Seckill queryById(long seckillId);
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);

}
