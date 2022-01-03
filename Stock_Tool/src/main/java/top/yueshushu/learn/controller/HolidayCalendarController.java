package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.HolidayRo;
import top.yueshushu.learn.model.info.StockInfo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.service.HolidayCalendarService;
import top.yueshushu.learn.service.StockService;

/**
 * <p>
 * 法定假期表(只写入法定的类型) 我是自定义的
 * </p>
 *
 * @author 岳建立
 * @date 2022-01-02
 */
@RestController
@RequestMapping("/holidayCalendar")
@Api("节假日信息假期")
public class HolidayCalendarController {
    @Autowired
    private HolidayCalendarService holidayCalendarService;
    @PostMapping("/list")
    @ApiOperation("查询假期信息")
    public OutputResult list(@RequestBody HolidayRo holidayRo){
        return holidayCalendarService.listHoliday(holidayRo);
    }
    @PostMapping("/sync")
    @ApiOperation("同步假期")
    public OutputResult sync(@RequestBody HolidayRo holidayRo){
        return holidayCalendarService.syncYear(holidayRo.getYear());
    }

}
