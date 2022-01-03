package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.yueshushu.learn.common.Const;
import top.yueshushu.learn.enumtype.DataFlagType;
import top.yueshushu.learn.mode.ro.UserRo;
import top.yueshushu.learn.mode.vo.UserVo;
import top.yueshushu.learn.pojo.User;
import top.yueshushu.learn.mapper.UserMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.util.RedisUtil;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <p>
 * 登录用户表 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
@Log4j2
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 用户登录
     * @param userRo
     * @return
     */
    @Override
    public OutputResult login(UserRo userRo) {
        OutputResult validateResult = validLogin(userRo);
        if(null!=validateResult){
            return validateResult;
        }
        //进行登录操作, 根据账号进行查询
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account",userRo.getAccount());
        queryWrapper.eq("flag", DataFlagType.NORMAL.getCode());
        //进行查询
        List<User> userList = userMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(userList)){
            return OutputResult.error("用户名账号不正确");
        }
        //查询密码信息
        User user = userList.get(0);
        //查询密码
        MD5 md5 = MD5.create();
        md5.setSalt(Const.SALT.getBytes());
        String encryPs = md5.digestHex16(userRo.getPassword().getBytes());
        if(!encryPs.equals(user.getPassword())){
            return OutputResult.error("用户名或者密码不正确");
        }
        //密码正确之后，返回用户的相关信息。
        UserVo showUserVo = new UserVo();
        BeanUtils.copyProperties(user,showUserVo);
        String token = UUID.randomUUID().toString().replace("-", "");
        showUserVo.setToken(token);

        //更新员工的信息
        user.setLastLoginTime(DateUtil.date());
        user.setToken(token);
        userMapper.updateById(user);

        putTokenToRedis(user,token);

        return OutputResult.success(showUserVo);
    }

    @Override
    public OutputResult convertPs(String password) {
        if(!StringUtils.hasText(password)){
            return OutputResult.alert("密码不能为空");
        }
        MD5 md5 = MD5.create();
        md5.setSalt(Const.SALT.getBytes());
        String encryPs = md5.digestHex16(password.getBytes());
        return OutputResult.success(encryPs);
    }


    /**
     * 验证用户登录
     * @param userRo
     * @return
     */
    private OutputResult validLogin(UserRo userRo){
        return null;
    }

    public void putTokenToRedis(User user, String token) {
        //获取该员工以前的token 值
        String beforeToken = redisUtil.get(
                Const.getCacheKeyPrefix(user.getId())+
                Const.AUTH
        );
        //删除以前的token
        if(StringUtils.hasText(beforeToken)){
            redisUtil.delByKey(
                    Const.TOKEN_USER+beforeToken
            );
        }
        //设置员工对应的token
        redisUtil.set(
                Const.getCacheKeyPrefix(user.getId())+Const.AUTH,
                token
        );
        redisUtil.set(
                Const.TOKEN_USER+token,
                user
        );
    }
}
