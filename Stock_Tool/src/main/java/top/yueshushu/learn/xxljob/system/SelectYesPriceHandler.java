package top.yueshushu.learn.xxljob.system;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.service.StockSelectedService;
import top.yueshushu.learn.service.TradePositionService;

/**
 * @ClassName:SelectYesPriceHandler
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/9 18:27
 * @Version 1.0
 **/
@Component
@JobHandler("selectYesPriceHandler")
@Log4j2(topic = "selectYesPriceHandler")
public class SelectYesPriceHandler extends IJobHandler {
    @Autowired
    private StockSelectedService stockSelectedService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info(">>>获取股票的收盘价信息");
        stockSelectedService.cacheClosePrice();
        return ReturnT.SUCCESS;
    }
}
