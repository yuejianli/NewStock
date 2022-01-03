package top.yueshushu.learn.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import top.yueshushu.learn.mode.ro.IdRo;
import top.yueshushu.learn.mode.ro.UserRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockStatRo;
import top.yueshushu.learn.service.MenuService;
import top.yueshushu.learn.service.UserService;

/**
 * <p>
 * 登录用户表 我是自定义的
 * </p>
 *
 * @author 岳建立
 * @date 2022-01-02
 */
@RestController
@RequestMapping("/user")
@Api("用户信息")
public class UserController extends BaseController{
    @Autowired
    private UserService userService;
    @Autowired
    private MenuService menuService;
    @PostMapping("/login")
    @ApiOperation("用户登录信息")
    public OutputResult login(@RequestBody UserRo userRo){
        return userService.login(userRo);
    }


    @PostMapping("/getMenuListByUid")
    @ApiOperation("获取用户的菜单信息")
    public OutputResult getMenuListByUid(){
        return menuService.getMenuListByUid(getUserId());
    }

    @GetMapping("/convertPs")
    @ApiOperation("转换登录用户的密码")
    public OutputResult convertPs(String password){
        return userService.convertPs(password);
    }
}
