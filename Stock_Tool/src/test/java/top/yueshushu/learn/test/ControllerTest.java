package top.yueshushu.learn.test;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import top.yueshushu.learn.mode.ro.TradeRuleConditionRo;
import top.yueshushu.learn.service.TradeRuleConditionService;

/**
 * @ClassName:ControllerTest
 * @Description TODO
 * @Author zk_yjl
 * @Date 2022/1/26 14:43
 * @Version 1.0
 * @Since 1.0
 **/
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@Log4j2
public class ControllerTest {
    @Autowired
    private TradeRuleConditionService tradeRuleConditionService;
    @Test
    public void listTest(){
        log.info(tradeRuleConditionService.listCondition());
    }
    @Test
    public void updateTest(){
        TradeRuleConditionRo tradeRuleConditionRo = new TradeRuleConditionRo();
        tradeRuleConditionRo.setId(1);
        tradeRuleConditionRo.setName("张三");
        tradeRuleConditionRo.setDescription("李四");
        log.info(tradeRuleConditionService.updateCondition(tradeRuleConditionRo));
    }
}
