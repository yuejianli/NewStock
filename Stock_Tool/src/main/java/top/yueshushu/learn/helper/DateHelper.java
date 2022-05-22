package top.yueshushu.learn.helper;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.common.Const;
import top.yueshushu.learn.domainservice.HolidayCalendarDomainService;
import top.yueshushu.learn.service.cache.HolidayCalendarCacheService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description 日期帮助类
 * @Author yuejianli
 * @Date 2022/5/21 22:53
 **/
@Component
public class DateHelper {
    @Resource
    private HolidayCalendarCacheService holidayCalendarCacheService;

    /**
     * 获取最近的一个工作日 即股票展示的昨天记录
     * @return 返回最近一个工作日
     */
    public DateTime getBeforeLastWorking(){
        //1. 查询出当前年的全部的假期数据
        Date yesterdayDate = DateUtil.yesterday();
        List<String> holidayDateList = holidayCalendarCacheService.listHolidayDateByYear(
                DateUtil.year(yesterdayDate)
        );
        //根据当前的日期往前推送
        Date now = DateUtil.date();
        for (int i=-1;i >= -30;i--){
            //获取当前的日期
            DateTime tempDate = DateUtil.offsetDay(
                    now,i
            );
            //如果是周末，则跳过
            if (DateUtil.isWeekend(tempDate)){
                continue;
            }
            // 日期转换
            String formatDate = DateUtil.format(
                    tempDate,
                    Const.SIMPLE_DATE_FORMAT
            );
            if (holidayDateList.contains(formatDate)){
                continue;
            }
            return tempDate;
        }
        return null;
    }
}
