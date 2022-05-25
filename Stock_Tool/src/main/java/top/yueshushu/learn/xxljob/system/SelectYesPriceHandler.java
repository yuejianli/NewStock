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
 * @ClassName:SelectYesPriceHandler
 * @Description 每天早上8点查询昨夜的价格
 * @Author 岳建立
 * @Date 2022/1/9 18:27
 * @Version 1.0
 **/
@Component
@JobHandler("selectYesPriceHandler")
@Slf4j(topic = "selectYesPriceHandler")
public class SelectYesPriceHandler extends IJobHandler {
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
        log.info(">>> {} 时,获取股票的收盘价信息", DateUtil.now());
        stockSelectedService.cacheClosePrice();
        return ReturnT.SUCCESS;
    }
}
