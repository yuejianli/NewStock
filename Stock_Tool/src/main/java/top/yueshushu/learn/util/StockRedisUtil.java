package top.yueshushu.learn.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.common.Const;
import top.yueshushu.learn.mode.dto.StockPriceCacheDto;
import top.yueshushu.learn.system.SystemConst;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * @ClassName:StockUtil
 * @Description 股票的信息与缓存的工具类
 * @Author 岳建立
 * @Date 2022/1/8 9:51
 * @Version 1.0
 **/
@Component
public class StockRedisUtil {
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 更新股票的价格信息
     * @param code 股票编码
     * @param price 股票的价钱
     * @return
     */
    public boolean setPrice(String code,BigDecimal price){
        //放置股票的信息
        return redisUtil.set(buildKey(code),
                price);
    }

    /**
     * 获取股票的价格信息
     * @param code
     * @return
     */
    public BigDecimal getPrice(String code){
        //放置股票的信息
        return (BigDecimal) Optional.ofNullable(redisUtil.get(buildKey(code)))
                .orElse(
                        SystemConst.DEFAULT_EMPTY
                );
    }
    /**
     * 移除价钱
     * @param code
     * @return
     */
    public Boolean removePrice(String code){
        //放置股票的信息
        return redisUtil.remove(buildKey(code));
    }
    private String buildKey(String code){
        return Const.STOCK_PRICE+code;
    }
}
