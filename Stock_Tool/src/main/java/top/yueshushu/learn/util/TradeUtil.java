package top.yueshushu.learn.util;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import top.yueshushu.learn.api.request.BaseTradeRequest;
import top.yueshushu.learn.api.request.GetAssetsRequest;
import top.yueshushu.learn.common.Const;
import top.yueshushu.learn.pojo.TradeMethod;
import top.yueshushu.learn.pojo.TradeUser;
import top.yueshushu.learn.service.TradeMethodService;
import top.yueshushu.learn.service.TradeUserService;
import top.yueshushu.learn.service.impl.TradeApiServiceImpl;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:TradeUtil
 * @Description 交易使用的 工具类，将 TradeApiServiceImpl 拆开使用
 * @Author 岳建立
 * @Date 2022/1/3 17:52
 * @Version 1.0
 **/
@Component
public class TradeUtil {
    @Autowired
    private TradeMethodService tradeMethodService;
    @Autowired
    private TradeUserService tradeUserService;
    /**
     * 获取请求地址
     * @param request
     * @return
     */
    public String getUrl(BaseTradeRequest request) {
        TradeMethod tradeMethod = tradeMethodService.getMethodByCode(request.getMethod());
        Assert.notNull(tradeMethod,"方法"+request.getMethod()+"对应的交易方法不能为空");
        TradeUser tradeUser = tradeUserService.getTradeUserById(request.getUserId());
        Assert.notNull(tradeUser,"登录用户"+request.getUserId()+"对应的交易用户不能为空");
        String url = tradeMethod.getUrl();
        Assert.notNull(url,"方法"+tradeMethod.getCode()+"对应的url网址不能为空");
        return MessageFormat.format(url,tradeUser.getValidateKey());
    }

    /**
     * 获取请求的信息
     * @param request
     * @return
     */
    public Map<String, String> getHeader(BaseTradeRequest request) {
        TradeUser tradeUser = tradeUserService.getTradeUserById(request.getUserId());
        Assert.notNull(tradeUser,"登录用户"+request.getUserId()+"对应的交易用户不能为空");
        HashMap<String, String> header = new HashMap<>();
        String cookie= tradeUser.getCookie();
        Assert.notNull(cookie,"交易用户"+request.getUserId()+"没有cookie,未登录");
        header.put("cookie",cookie);
        return header;
    }

    /**
     * 获取参数
     * @param request
     * @return
     */
    public Map<String, Object> getParams(BaseTradeRequest request) {
        Map<Object, Object> beanMap = new BeanMap(request);
        HashMap<String, Object> params = new HashMap<>();
        beanMap.entrySet().stream().filter(entry -> !Const.IgnoreList.contains(entry.getKey()))
                .forEach(entry -> params.put(String.valueOf(entry.getKey()), entry.getValue()));
        return params;
    }
}
