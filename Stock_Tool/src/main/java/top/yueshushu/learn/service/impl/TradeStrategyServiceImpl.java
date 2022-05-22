package top.yueshushu.learn.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.mapper.StockSelectedDoMapper;
import top.yueshushu.learn.mode.ro.BuyRo;
import top.yueshushu.learn.mode.ro.SellRo;
import top.yueshushu.learn.service.BuyService;
import top.yueshushu.learn.service.SellService;
import top.yueshushu.learn.service.TradeStrategyService;
import top.yueshushu.learn.util.BigDecimalUtil;
import top.yueshushu.learn.util.StockRedisUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName:TradeStrategyServiceImpl
 * @Description TODO
 * @Author zk_yjl
 * @Date 2022/1/11 20:33
 * @Version 1.0
 * @Since 1.0
 **/
@Service
@Slf4j
public class TradeStrategyServiceImpl implements TradeStrategyService {
    @Autowired
    private StockSelectedDoMapper stockSelectedDoMapper;
    @Autowired
    private BuyService buyService;
    @Autowired
    private SellService sellService;
    @Autowired
    private StockRedisUtil stockRedisUtil;
    public static final BigDecimal subPrice = new BigDecimal("2.00");
    @Override
    public void mockEntructXxlJob(BuyRo buyRo) {
        List<String> codeList = stockSelectedDoMapper.findCodeList(null);
        //查询该员工最开始的收盘价
        for(String code:codeList){
            //获取昨天的价格
            BigDecimal yesPrice = stockRedisUtil.getYesPrice(code);
            //获取今天的价格
            BigDecimal currentPrice = stockRedisUtil.getPrice(code);
            //+ 相差 5，就卖
            if(BigDecimalUtil.subBigDecimal(
                    yesPrice,
                    subPrice
            ).compareTo(currentPrice)>=0){
                //开始买
                BuyRo mockBuyRo = new BuyRo();
                mockBuyRo.setUserId(buyRo.getUserId());
                mockBuyRo.setMockType(buyRo.getMockType());
                mockBuyRo.setCode(code);
                mockBuyRo.setAmount(100);
                mockBuyRo.setName("模拟买入");
                mockBuyRo.setPrice(
                        BigDecimalUtil.subBigDecimal(
                                yesPrice,
                                subPrice
                        )
                );
                log.info(">>>可以买入股票{}",code);
                buyService.buy(mockBuyRo);
            }

            if(BigDecimalUtil.subBigDecimal(
                    currentPrice,
                    subPrice
            ).compareTo(yesPrice)>=0){
                //开始买
                SellRo sellRo = new SellRo();
                sellRo.setUserId(buyRo.getUserId());
                sellRo.setMockType(buyRo.getMockType());
                sellRo.setCode(code);
                sellRo.setAmount(100);
                sellRo.setName("模拟卖出");
                sellRo.setPrice(
                        BigDecimalUtil.addBigDecimal(
                              yesPrice,
                              subPrice
                        )
                );
                log.info(">>>可以卖出股票{}",code);
                sellService.sell(sellRo);
            }


        }
    }
}
