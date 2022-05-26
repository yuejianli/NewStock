package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.TradeRuleConditionDo;
import top.yueshushu.learn.entity.TradeRuleCondition;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T11:30:44+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class TradeRuleConditionAssemblerImpl implements TradeRuleConditionAssembler {

    @Override
    public TradeRuleCondition doToEntity(TradeRuleConditionDo TradeRuleConditionDo) {
        if ( TradeRuleConditionDo == null ) {
            return null;
        }

        TradeRuleCondition tradeRuleCondition = new TradeRuleCondition();

        tradeRuleCondition.setId( TradeRuleConditionDo.getId() );
        tradeRuleCondition.setCode( TradeRuleConditionDo.getCode() );
        tradeRuleCondition.setName( TradeRuleConditionDo.getName() );
        tradeRuleCondition.setDescription( TradeRuleConditionDo.getDescription() );
        tradeRuleCondition.setCreateTime( TradeRuleConditionDo.getCreateTime() );
        tradeRuleCondition.setUpdateTime( TradeRuleConditionDo.getUpdateTime() );
        tradeRuleCondition.setFlag( TradeRuleConditionDo.getFlag() );

        return tradeRuleCondition;
    }

    @Override
    public TradeRuleConditionDo entityToDo(TradeRuleCondition TradeRuleCondition) {
        if ( TradeRuleCondition == null ) {
            return null;
        }

        TradeRuleConditionDo tradeRuleConditionDo = new TradeRuleConditionDo();

        tradeRuleConditionDo.setId( TradeRuleCondition.getId() );
        tradeRuleConditionDo.setCode( TradeRuleCondition.getCode() );
        tradeRuleConditionDo.setName( TradeRuleCondition.getName() );
        tradeRuleConditionDo.setDescription( TradeRuleCondition.getDescription() );
        tradeRuleConditionDo.setCreateTime( TradeRuleCondition.getCreateTime() );
        tradeRuleConditionDo.setUpdateTime( TradeRuleCondition.getUpdateTime() );
        tradeRuleConditionDo.setFlag( TradeRuleCondition.getFlag() );

        return tradeRuleConditionDo;
    }
}
