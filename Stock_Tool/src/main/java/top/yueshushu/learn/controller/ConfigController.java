package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.ConfigRo;
import top.yueshushu.learn.model.info.StockInfo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.service.ConfigService;
import top.yueshushu.learn.service.StockService;

/**
 * <p>
 * 全局性系统配置 我是自定义的
 * </p>
 *
 * @author 岳建立
 * @date 2022-01-02
 */
@RestController
@RequestMapping("/config")
@Api("系统配置参数")
public class ConfigController extends BaseController{
    @Autowired
    private ConfigService configService;
    @PostMapping("/list")
    @ApiOperation("查询配置参数")
    public OutputResult list(@RequestBody ConfigRo configRo) {
        configRo.setUserId(getUserId());
        return configService.listConfig(configRo);
    }
    @PostMapping("/update")
    @ApiOperation("修改配置参数")
    public OutputResult update(@RequestBody ConfigRo configRo) {
        configRo.setUserId(getUserId());
        return configService.update(configRo);
    }
    @PostMapping("/reset")
    @ApiOperation("自定义配置信息重置")
    public OutputResult reset(@RequestBody ConfigRo configRo) {
        configRo.setUserId(getUserId());
        return configService.reset(configRo);
    }
}
