package top.yueshushu.learn.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.api.TradeResultVo;
import top.yueshushu.learn.api.request.AuthenticationRequest;
import top.yueshushu.learn.api.response.AuthenticationResponse;
import top.yueshushu.learn.api.responseparse.DefaultResponseParser;
import top.yueshushu.learn.common.Const;
import top.yueshushu.learn.config.TradeClient;
import top.yueshushu.learn.enumtype.DataFlagType;
import top.yueshushu.learn.enumtype.TradeMethodType;
import top.yueshushu.learn.mode.ro.TradeUserRo;
import top.yueshushu.learn.mode.vo.TradeUserVo;
import top.yueshushu.learn.pojo.TradeMethod;
import top.yueshushu.learn.pojo.TradeUser;
import top.yueshushu.learn.mapper.TradeUserMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.MenuService;
import top.yueshushu.learn.service.TradeMethodService;
import top.yueshushu.learn.service.TradeUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 交易用户信息 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
@Slf4j
public class TradeUserServiceImpl extends ServiceImpl<TradeUserMapper, TradeUser> implements TradeUserService {
    @Autowired
    private TradeUserMapper tradeUserMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private TradeMethodService tradeMethodService;
    @Autowired
    private TradeClient tradeClient;
    @Resource
    private DefaultResponseParser defaultResponseParser;
    @Override
    public OutputResult login(TradeUserRo tradeUserRo) {
        //根据id,去获取对应的真实账户
        OutputResult validateResult = validLogin(tradeUserRo);
        if(null!=validateResult){
            return validateResult;
        }
        //根据id 去查询对应的交易账户
        TradeUser tradeUser = getTradeUserById(tradeUserRo.getId());
        if(null==tradeUser){
            return OutputResult.error("没有关联交易用户");
        }
        //关联的用户
        AuthenticationRequest request = new AuthenticationRequest(tradeUser.getUserId());
        request.setPassword(tradeUserRo.getPassword());
        request.setIdentifyCode(tradeUserRo.getIdentifyCode());
        request.setRandNumber(tradeUserRo.getRandNum());
        //获取登录验证的方法
        TradeMethod tradeMethod = tradeMethodService.getMethodByCode(request.getMethod());

        Map<String, Object> params = getParams(request);
        params.put("userId", tradeUser.getAccount());
        TradeResultVo<AuthenticationResponse> resultVo = null;
        try {
            tradeClient.openSession();
            String content = tradeClient.sendNewInstance(tradeMethod.getUrl(), params);
            resultVo = defaultResponseParser.parse(content, new TypeReference<AuthenticationResponse>() {});
            if (resultVo.getSuccess()) {
                TradeMethod authCheckTradeMethod =
                        tradeMethodService.getMethodByCode(TradeMethodType.AuthenticationCheckRequest.getCode());

                String content2 = tradeClient.sendNewInstance(authCheckTradeMethod.getUrl(), new HashMap<>());
                String validateKey = getValidateKey(content2);

                AuthenticationResponse response = new AuthenticationResponse();
                response.setCookie(tradeClient.getCurrentCookie());
                response.setValidateKey(validateKey);
                resultVo.setData(Arrays.asList(response));
            }
        } finally {
            tradeClient.destoryCurrentSession();
        }
        TradeUserVo tradeUserVo = new TradeUserVo();
        if (resultVo.getSuccess()) {
            AuthenticationResponse response = resultVo.getData().get(0);
            tradeUser.setCookie(response.getCookie());
            tradeUser.setValidateKey(response.getValidateKey());
            tradeUserMapper.updateById(tradeUser);
        }
        tradeUserVo.setUserId(tradeUserRo.getId());
        return OutputResult.success(tradeUserVo);
    }

    @Override
    public TradeUser getTradeUserById(int userId) {
        QueryWrapper<TradeUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("flag", DataFlagType.NORMAL.getCode());
        List<TradeUser> tradeUseList = tradeUserMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(tradeUseList)){
           return null;
        }
        return tradeUseList.get(0);
    }

    /**
     * 验证登录
     * @param tradeUserRo
     * @return
     */
    private OutputResult validLogin(TradeUserRo tradeUserRo) {
        return null;
    }

    /**
     * 组装参数
     * @param request
     * @return
     */
    private Map<String, Object> getParams(Object request) {
        Map<Object, Object> beanMap = new BeanMap(request);
        HashMap<String, Object> params = new HashMap<>();
        beanMap.entrySet().stream().filter(entry -> !Const.IgnoreList.contains(entry.getKey()))
                .forEach(entry -> params.put(String.valueOf(entry.getKey()), entry.getValue()));
        return params;
    }

    /**
     * 获取验证信息
     * @param content
     * @return
     */
    private String getValidateKey(String content) {
        String key = "input id=\"em_validatekey\" type=\"hidden\" value=\"";
        int inputBegin = content.indexOf(key) + key.length();
        int inputEnd = content.indexOf("\" />", inputBegin);
        String validateKey = content.substring(inputBegin, inputEnd);
        return validateKey;
    }
}
