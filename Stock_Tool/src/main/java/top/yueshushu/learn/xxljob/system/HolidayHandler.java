package top.yueshushu.learn.xxljob.system;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.service.HolidayCalendarService;

import javax.annotation.Resource;

/**
 * @ClassName:HolidayHandler
 * @Description 每年1月1日时，同步假期数据
 * @Author 岳建立
 * @Date 2022/1/9 13:34
 * @Version 1.0
 **/
@Component
@JobHandler("holidayHandler")
@Slf4j(topic = "HolidayHandler")
public class HolidayHandler extends IJobHandler {
    @Resource
    private HolidayCalendarService holidayCalendarService;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
       //当前的年
        int year = DateUtil.thisYear();
        log.info(">>>同步 {}年的假期数据",year);
        holidayCalendarService.syncYear(year);
        return ReturnT.SUCCESS;
    }

}
