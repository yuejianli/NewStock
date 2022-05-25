package top.yueshushu.learn.xxljob.system;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.helper.DateHelper;
import top.yueshushu.learn.service.StockSelectedService;

import javax.annotation.Resource;

/**
 * @ClassName:StockHistoryHandler
 * @Description 同步自选表里面的历史记录信息
 * @Author 岳建立
 * @Date 2022/1/9 17:50
 * @Version 1.0
 **/
@Component
@JobHandler("stockHistoryHandler")
@Slf4j(topic = "stockHistoryHandler")
public class StockHistoryHandler extends IJobHandler {
    @Resource
    private StockSelectedService stockSelectedService;
    @Resource
    private DateHelper dateHelper;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        if (!dateHelper.isWorkingDay(DateUtil.date())){
            log.info("当前时间{}不是交易日，不需要同步",DateUtil.now());
            return ReturnT.SUCCESS;
        }
        log.info(">>> {} 时,更新目前自选表里面的历史表记录信息", DateUtil.now());
        stockSelectedService.syncDayHistory();
        return ReturnT.SUCCESS;
    }
}
