package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.IdRo;
import top.yueshushu.learn.mode.ro.StockSelectedRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.StockSelectedService;

/**
 * <p>
 * 股票自选表,是用户自己选择的 我是自定义的
 * </p>
 *
 * @author 岳建立
 * @date 2022-01-02
 */
@RestController
@RequestMapping("/stockSelected")
@Api("自选表")
public class StockSelectedController extends BaseController{
    @Autowired
    private StockSelectedService stockSelectedService;

    @PostMapping("/list")
    @ApiOperation("查询自选表信息")
    public OutputResult list(@RequestBody StockSelectedRo stockSelectedRo){
        stockSelectedRo.setUserId(getUserId());
        return stockSelectedService.listSelected(stockSelectedRo);
    }

    @PostMapping("/add")
    @ApiOperation("添加到自选表")
    public OutputResult add(@RequestBody StockSelectedRo stockSelectedRo){
        stockSelectedRo.setUserId(getUserId());
        return stockSelectedService.add(stockSelectedRo);
    }
    @PostMapping("/delete")
    @ApiOperation("批量移出自选表")
    public OutputResult delete(@RequestBody IdRo idRo){
        return stockSelectedService.delete(idRo,getUserId());
    }
    @PostMapping("/deleteByCode")
    @ApiOperation("根据股票code进行移除")
    public OutputResult deleteByCode(@RequestBody StockSelectedRo stockSelectedRo){
        stockSelectedRo.setUserId(getUserId());
        return stockSelectedService.deleteByCode(stockSelectedRo);
    }
}
