package top.yueshushu.learn.xxljob.select;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.service.StockCrawlerService;
import top.yueshushu.learn.util.MyDateUtil;

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
    @Autowired
    private StockCrawlerService stockCrawlerService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        //获取当前的股票信息。取第一个值.
        String code = s;
        Date now = DateUtil.date();
        // 对时间的一些处理验证

        if(!MyDateUtil.between930And15()){
            return ReturnT.SUCCESS;
        }
        stockCrawlerService.updateCodePrice(code);
        return ReturnT.SUCCESS;
    }

}
