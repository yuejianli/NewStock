package top.yueshushu.learn.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.yueshushu.learn.common.Const;
import top.yueshushu.learn.pojo.User;
import top.yueshushu.learn.service.UserService;
import top.yueshushu.learn.util.RedisUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public abstract class BaseController {
    @Autowired
    private RedisUtil redisUtil;
    /**
     * 根据请求头获取对应的token 信息
     * @return
     */
    protected int getUserId() {
        //获取对应的 Authorization 值
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        String authorization = httpServletRequest.getHeader(Const.Authorization);
        if(!StringUtils.hasText(authorization)){
            return Const.DEFAULT_USER_ID;
        }
        //该值，从redis 里面获取信息
        User user =redisUtil.get(
                Const.TOKEN_USER+authorization
          );
        //返回用户的id
        return user==null?Const.DEFAULT_USER_ID:user.getId();
    }
}
