package top.yueshushu.learn.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.yueshushu.learn.enumtype.TradeMethodType;
import top.yueshushu.learn.pojo.TradeMethod;
import top.yueshushu.learn.mapper.TradeMethodMapper;
import top.yueshushu.learn.service.TradeMethodService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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
@Log4j2
public class TradeMethodServiceImpl extends ServiceImpl<TradeMethodMapper, TradeMethod> implements TradeMethodService {
    @Autowired
    private TradeMethodMapper tradeMethodMapper;
    @Override
    public TradeMethod getMethod(TradeMethodType tradeMethodType) {
        if(null==tradeMethodType){
            return null;
        }
        return getMethodByCode(tradeMethodType.getCode());
    }

    @Override
    public TradeMethod getMethodByCode(String methodCode) {
        if(!StringUtils.hasText(methodCode)){
            return null;
        }
        //获取相关的方法信息
        QueryWrapper<TradeMethod> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("code",methodCode);
        List<TradeMethod> tradeMethodList = tradeMethodMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(tradeMethodList)){
            log.error("交易方法:查询的交易方法{}不存在",methodCode);
            return null;
        }
        return tradeMethodList.get(0);
    }
}
