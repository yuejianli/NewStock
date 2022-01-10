package top.yueshushu.learn.test;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.learn.mode.ro.TradePositionRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradePositionService;
import top.yueshushu.learn.util.StockRedisUtil;

import java.math.BigDecimal;

/**
 * @ClassName:StockUtilTest
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/8 10:00
 * @Version 1.0
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Log4j2
public class TradePositionTest {
    @Autowired
    private TradePositionService tradePositionService;
    @Test
    public void listTest(){
        TradePositionRo tradePositionRo = new TradePositionRo();
        tradePositionRo.setUserId(1);
        tradePositionRo.setPageNum(1);
        tradePositionRo.setPageSize(10);
        tradePositionRo.setMockType(1);

        OutputResult outputResult = tradePositionService.listPosition(tradePositionRo);
        log.info(outputResult);
    }
}
