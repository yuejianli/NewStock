package top.yueshushu.learn.xxljob.system;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.helper.DateHelper;
import top.yueshushu.learn.service.TradePositionService;

import javax.annotation.Resource;

/**
 * @ClassName:HolidayHandler
 * @Description 每天晚上8点半的时候，同步可用的持仓数量。
 * 将 总数量，同步到可用数量里面。
 * @Author 岳建立
 * @Date 2022/1/9 13:34
 * @Version 1.0
 **/
@Component
@JobHandler("positionUseAmountHandler")
@Slf4j(topic = "positionUseAmountHandler")
public class PositionUseAmountHandler extends IJobHandler {
    @Resource
    private TradePositionService tradePositionService;
    @Resource
    private DateHelper dateHelper;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        if (!dateHelper.isWorkingDay(DateUtil.date())){
            log.info("当前时间{}不是交易日，不需要同步",DateUtil.now());
            return ReturnT.SUCCESS;
        }
        log.info(">>>同步股票可用数量");
        tradePositionService.syncUseAmountByXxlJob();
        return ReturnT.SUCCESS;
    }

}
