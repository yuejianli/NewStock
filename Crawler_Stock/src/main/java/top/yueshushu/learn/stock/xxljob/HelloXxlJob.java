package top.yueshushu.learn.stock.xxljob;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.executor.XxlJobExecutor;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
public class HelloXxlJob {
    /**
     * 只可以这样调用
     * @date 2021/11/23 19:48
     * @author zk_yjl
     * @param
     * @return void
     */
    @XxlJob("helloXxlJob")
    public void execute() throws Exception {
        log.info("Bean: >>>>开始执行策略"+ DateUtil.now());
    }
}
