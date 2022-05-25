package top.yueshushu.learn.xxljob.user;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.helper.DateHelper;
import top.yueshushu.learn.mode.ro.BuyRo;
import top.yueshushu.learn.service.TradeStrategyService;

import javax.annotation.Resource;

/**
 * @ClassName:MockEntructHandler
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/9 18:18
 * @Version 1.0
 **/
@Component
@JobHandler("mockEntructHandler")
@Slf4j
public class MockEntructHandler extends IJobHandler {
    @Value("${xxlJobTime}")
    boolean xxlJobTime;
    @Resource
    private TradeStrategyService tradeStrategyService;
    @Resource
    private DateHelper dateHelper;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        if(xxlJobTime){
            if (!dateHelper.isTradeTime(DateUtil.date())) {
                return ReturnT.SUCCESS;
            }
        }
        log.info(">>>扫描当前的用户id 为{}",s);
        BuyRo buyRo = new BuyRo();
        buyRo.setMockType(MockType.MOCK.getCode());
        buyRo.setUserId(Integer.parseInt(s));
        tradeStrategyService.mockEntructXxlJob(buyRo);
        return ReturnT.SUCCESS;
    }
}
