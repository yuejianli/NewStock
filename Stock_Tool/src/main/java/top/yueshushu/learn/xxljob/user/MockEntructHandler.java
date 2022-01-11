package top.yueshushu.learn.xxljob.user;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.mode.ro.BuyRo;
import top.yueshushu.learn.mode.ro.DealRo;
import top.yueshushu.learn.pojo.TradeEntrust;
import top.yueshushu.learn.service.BuyService;
import top.yueshushu.learn.service.DealService;
import top.yueshushu.learn.service.TradeEntrustService;
import top.yueshushu.learn.service.TradeStrategyService;

/**
 * @ClassName:MockEntructHandler
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/9 18:18
 * @Version 1.0
 **/
@Component
@JobHandler("mockEntructHandler")
@Log4j2
public class MockEntructHandler extends IJobHandler {
    @Autowired
    private TradeStrategyService tradeStrategyService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info(">>>扫描当前的用户id 为{}",s);
        BuyRo buyRo = new BuyRo();
        buyRo.setMockType(MockType.MOCK.getCode());
        buyRo.setUserId(Integer.parseInt(s));
        tradeStrategyService.mockEntructXxlJob(buyRo);
        return ReturnT.SUCCESS;
    }
}
