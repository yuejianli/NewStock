package top.yueshushu.learn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.yueshushu.learn.domainservice.TradeMethodDomainService;
import top.yueshushu.learn.enumtype.TradeMethodType;
import top.yueshushu.learn.domain.TradeMethodDo;
import top.yueshushu.learn.mapper.TradeMethodDoMapper;
import top.yueshushu.learn.service.TradeMethodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 交易，包括爬虫所使用的url信息 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
@Slf4j
public class TradeMethodServiceImpl implements TradeMethodService {
    @Resource
    private TradeMethodDomainService tradeMethodDomainService;
    @Override
    public TradeMethodDo getMethod(TradeMethodType tradeMethodType) {
        if(null==tradeMethodType){
            return null;
        }
        return getMethodByCode(tradeMethodType.getCode());
    }

    @Override
    public TradeMethodDo getMethodByCode(String methodCode) {
        if(!StringUtils.hasText(methodCode)){
            return null;
        }
        return tradeMethodDomainService.getMethodByCode(methodCode);
    }
}
