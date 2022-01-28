package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import top.yueshushu.learn.mode.ro.TradeRuleConditionRo;
import top.yueshushu.learn.pojo.TradeRuleCondition;
import top.yueshushu.learn.mapper.TradeRuleConditionMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.response.ResultCode;
import top.yueshushu.learn.service.TradeRuleConditionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 交易规则可使用的条件表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-26
 */
@Service
public class TradeRuleConditionServiceImpl extends ServiceImpl<TradeRuleConditionMapper, TradeRuleCondition> implements TradeRuleConditionService {

    @Override
    public OutputResult listCondition() {
        return OutputResult.success(listAll());
    }

    @Override
    public OutputResult updateCondition(TradeRuleConditionRo tradeRuleConditionRo) {
        //根据id 查询信息
        Integer id = tradeRuleConditionRo.getId();
        if(id==null){
            return OutputResult.alert(ResultCode.ALERT);
        }
        //查询是否有此信息
        TradeRuleCondition dbCondtion = getById(id);
        if(dbCondtion==null){
            return OutputResult.alert("传入的Id编号有误");
        }
        //进行修改
       dbCondtion.setName(
               tradeRuleConditionRo.getName()
       );
        dbCondtion.setDescription(
                tradeRuleConditionRo.getDescription()
        );
        dbCondtion.setUpdateTime(
                DateUtil.date()
        );
        updateById(dbCondtion);
        return OutputResult.success();
    }

    @Override
    public List<TradeRuleCondition> listAll() {
        return this.lambdaQuery()
                        .list();
    }
}
