package top.yueshushu.learn.xxljob.system;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.service.StockSelectedService;

/**
 * @ClassName:StockHistoryHandler
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/9 17:50
 * @Version 1.0
 **/
@Component
@JobHandler("stockHistoryHandler")
@Slf4j(topic = "stockHistoryHandler")
public class StockHistoryHandler extends IJobHandler {
    @Autowired
    private StockSelectedService stockSelectedService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info(">>>更新目前自选表里面的历史表记录信息");
        stockSelectedService.syncDayHistory();
        return ReturnT.SUCCESS;
    }
}
