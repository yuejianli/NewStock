package top.yueshushu.learn.service.cache.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.yueshushu.learn.common.Const;
import top.yueshushu.learn.entity.User;
import top.yueshushu.learn.service.cache.StockCacheService;
import top.yueshushu.learn.service.cache.UserCacheService;
import top.yueshushu.learn.util.RedisUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 * @Description 股票相关的缓存实现
 * @Author yuejianli
 * @Date 2022/5/20 23:38
 **/
@Service
@Slf4j
public class StockCacheServiceImpl implements StockCacheService {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void setNowCachePrice(String code, BigDecimal price) {
        redisUtil.set(Const.STOCK_PRICE+code,price);
    }

    @Override
    public BigDecimal getNowCachePrice(String code) {
        return redisUtil.get(Const.STOCK_PRICE+code);
    }

    @Override
    public void setYesterdayCloseCachePrice(String code, BigDecimal price) {
        redisUtil.set(Const.STOCK_YES_PRICE+code,price);
    }

    @Override
    public BigDecimal getYesterdayCloseCachePrice(String code) {
        return redisUtil.get(Const.STOCK_YES_PRICE+code);
    }
}
