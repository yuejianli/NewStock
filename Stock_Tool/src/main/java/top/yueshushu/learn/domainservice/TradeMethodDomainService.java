package top.yueshushu.learn.domainservice;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.domain.TradeMethodDo;

/**
 * @Description 交易方法的操作
 * @Author yuejianli
 * @Date 2022/5/21 06:19
 **/
public interface TradeMethodDomainService extends IService<TradeMethodDo> {
    /**
     * 根据请求方法的编号获取对应的请求方法信息
     * @param methodCode 请求的方法标识
     * @return 返回请求的方法信息
     */
    TradeMethodDo getMethodByCode(String methodCode);
}
