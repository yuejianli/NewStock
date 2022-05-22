package top.yueshushu.learn.domainservice;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.domain.UserDo;

/**
 * @Description 用户的操作
 * @Author yuejianli
 * @Date 2022/5/20 23:23
 **/
public interface UserDomainService extends IService<UserDo> {
    /**
     * 根据账户查询用户的信息
     * @param account 账号
     * @return 根据账户查询用户的信息
     */
    UserDo getByAccount(String account);

    /**
     * 更新用户信息
     * @param userDo 传递过来的用户对象
     */
    void updateUser(UserDo userDo);
}
