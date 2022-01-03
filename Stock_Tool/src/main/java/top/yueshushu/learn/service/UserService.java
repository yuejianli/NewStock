package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.ro.UserRo;
import top.yueshushu.learn.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;

/**
 * <p>
 * 登录用户表 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
public interface UserService extends IService<User> {
    /**
     * 用户登录信息
     * @param userRo
     * @return
     */
    OutputResult login(UserRo userRo);
    /**
     * 转换登录用户的密码信息
     * @param password
     * @return
     */
    OutputResult convertPs(String password);
}
