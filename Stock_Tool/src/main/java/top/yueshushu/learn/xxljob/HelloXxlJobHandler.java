package top.yueshushu.learn.xxljob;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName:HelloXxlJob
 * @Description TODO
 * @Author zk_yjl
 * @Date 2021/11/23 19:06
 * @Version 1.0
 * @Since 1.0
 **/
@Component
@JobHandler("helloXxlJobHandler")
@Slf4j
public class HelloXxlJobHandler extends IJobHandler {
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        log.info("Handler: {} >>>>开始执行策略",DateUtil.now());
        return ReturnT.SUCCESS;
    }
}
