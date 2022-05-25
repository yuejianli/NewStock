package top.yueshushu.learn.xxljob.system;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.helper.DateHelper;
import top.yueshushu.learn.service.TradePositionService;
import top.yueshushu.learn.service.UserService;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName:SelectYesPriceHandler
 * @Description 查询昨夜的价格
 * @Author 岳建立
 * @Date 2022/1/9 18:27
 * @Version 1.0
 **/
@Component
@JobHandler("tradePositionHistoryHandler")
@Slf4j(topic = "tradePositionHistoryHandler")
public class TradePositionHistoryHandler extends IJobHandler {
    @Resource
    private TradePositionService tradePositionService;
    @Resource
    private UserService userService;
    @Resource
    private DateHelper dateHelper;
    @Override
    public ReturnT<String> execute(String s) throws Exception {

        if (!dateHelper.isWorkingDay(DateUtil.date())){
            log.info("当前时间{}不是交易日，不需要同步",DateUtil.now());
            return ReturnT.SUCCESS;
        }
        log.info(">>> {} 时,保存股票的当前持仓信息", DateUtil.now());
        List<Integer> userIdList =  userService.listUserId();
        //设置类型为虚拟
        userIdList.parallelStream().forEach(
                userId->{
                    tradePositionService.savePositionHistory(
                        userId,MockType.MOCK
                    );
                    tradePositionService.savePositionHistory(
                            userId,MockType.REAL
                    );
                }
        );
        return ReturnT.SUCCESS;
    }
}
