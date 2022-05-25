package top.yueshushu.learn.xxljob.system;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.helper.DateHelper;
import top.yueshushu.learn.service.StockCrawlerService;
import top.yueshushu.learn.service.StockSelectedService;
import top.yueshushu.learn.util.MyDateUtil;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @ClassName:StockPriceHandler
 * @Description 股票获取当前的价格
 * @Author 岳建立
 * @Date 2022/1/9 13:34
 * @Version 1.0
 **/
@Component
@JobHandler("stockPriceHandler")
@Slf4j(topic = "stockPriceHandler")
public class StockPriceHandler extends IJobHandler {
    @Resource
    private StockSelectedService stockSelectedService;
    @Resource
    private DateHelper dateHelper;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        //获取当前的股票信息。取第一个值.
        String code = s;
        if (!dateHelper.isTradeTime(DateUtil.date())) {
            return ReturnT.SUCCESS;
        }
        stockSelectedService.updateSelectedCodePrice(null);
        return ReturnT.SUCCESS;
    }

}
