package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import top.yueshushu.learn.mode.ro.HolidayRo;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.pojo.HolidayCalendar;
import top.yueshushu.learn.mapper.HolidayCalendarMapper;
import top.yueshushu.learn.pojo.Stock;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.HolidayCalendarService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 法定假期表(只写入法定的类型) 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
public class HolidayCalendarServiceImpl extends ServiceImpl<HolidayCalendarMapper, HolidayCalendar> implements HolidayCalendarService {
    @Autowired
    private HolidayCalendarMapper holidayCalendarMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public OutputResult listHoliday(HolidayRo holidayRo) {
        PageHelper.startPage(holidayRo.getPageNum(),holidayRo.getPageSize());
        List<HolidayCalendar> holidayCalendarList= holidayCalendarMapper.selectByYear(holidayRo.getYear());
        PageInfo pageInfo=new PageInfo<HolidayCalendar>(holidayCalendarList);
        return OutputResult.success(new PageResponse<HolidayCalendar>(pageInfo.getTotal(),
                pageInfo.getList()));
    }
    @Override
    public OutputResult syncYear(Integer year) {
        Map<?, ?> data = restTemplate.getForObject("http://tool.bitefu.net/jiari/?d=" + year, Map.class);

        @SuppressWarnings("unchecked")
        Map<String, Integer> dateInfo = (Map<String, Integer>) data.get(String.valueOf(year));
        List<HolidayCalendar> list = dateInfo.entrySet().stream().filter(entry -> entry.getValue() != 0).map(entry -> {
            Date date;
            try {
                date = DateUtils.parseDate(year + entry.getKey(), "yyyyMMdd");
            } catch (ParseException e) {
                throw new IllegalArgumentException(e);
            }
            HolidayCalendar holidayCalendar = new HolidayCalendar();
            holidayCalendar.setHolidayDate(date);
            holidayCalendar.setCreateTime(DateUtil.date());
            holidayCalendar.setDateType(3);
            return holidayCalendar;
        }).collect(Collectors.toList());
        holidayCalendarMapper.deleteByYear(year);
        saveBatch(list);
        return OutputResult.success();
    }
}
