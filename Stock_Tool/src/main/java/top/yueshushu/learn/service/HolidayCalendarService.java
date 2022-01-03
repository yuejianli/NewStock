package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.ro.HolidayRo;
import top.yueshushu.learn.pojo.HolidayCalendar;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;

/**
 * <p>
 * 法定假期表(只写入法定的类型) 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
public interface HolidayCalendarService extends IService<HolidayCalendar> {
    /**
     * 查询假期信息
     * @param holidayRo
     * @return
     */
    OutputResult listHoliday(HolidayRo holidayRo);
    /**
     * 同步该年的假期信息
     * @return
     */
    OutputResult syncYear(Integer year);
}
