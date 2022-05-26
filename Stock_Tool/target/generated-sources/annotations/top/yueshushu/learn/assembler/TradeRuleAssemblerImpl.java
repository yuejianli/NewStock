package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.TradeRuleDo;
import top.yueshushu.learn.entity.TradeRule;
import top.yueshushu.learn.mode.ro.TradeRuleRo;
import top.yueshushu.learn.mode.vo.TradeRuleVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T11:30:44+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class TradeRuleAssemblerImpl implements TradeRuleAssembler {

    @Override
    public TradeRule doToEntity(TradeRuleDo TradeRuleDo) {
        if ( TradeRuleDo == null ) {
            return null;
        }

        TradeRule tradeRule = new TradeRule();

        tradeRule.setId( TradeRuleDo.getId() );
        tradeRule.setName( TradeRuleDo.getName() );
        tradeRule.setConditionCode( TradeRuleDo.getConditionCode() );
        tradeRule.setConditionType( TradeRuleDo.getConditionType() );
        tradeRule.setRuleValueType( TradeRuleDo.getRuleValueType() );
        tradeRule.setRuleValue( TradeRuleDo.getRuleValue() );
        tradeRule.setTradeNum( TradeRuleDo.getTradeNum() );
        tradeRule.setTradeValueType( TradeRuleDo.getTradeValueType() );
        tradeRule.setTradePrice( TradeRuleDo.getTradePrice() );
        tradeRule.setRuleType( TradeRuleDo.getRuleType() );
        tradeRule.setStatus( TradeRuleDo.getStatus() );
        tradeRule.setUserId( TradeRuleDo.getUserId() );
        tradeRule.setMockType( TradeRuleDo.getMockType() );
        tradeRule.setCreateTime( TradeRuleDo.getCreateTime() );
        tradeRule.setUpdateTime( TradeRuleDo.getUpdateTime() );
        tradeRule.setFlag( TradeRuleDo.getFlag() );

        return tradeRule;
    }

    @Override
    public TradeRuleDo entityToDo(TradeRule TradeRule) {
        if ( TradeRule == null ) {
            return null;
        }

        TradeRuleDo tradeRuleDo = new TradeRuleDo();

        tradeRuleDo.setId( TradeRule.getId() );
        tradeRuleDo.setName( TradeRule.getName() );
        tradeRuleDo.setConditionCode( TradeRule.getConditionCode() );
        tradeRuleDo.setConditionType( TradeRule.getConditionType() );
        tradeRuleDo.setRuleValueType( TradeRule.getRuleValueType() );
        tradeRuleDo.setRuleValue( TradeRule.getRuleValue() );
        tradeRuleDo.setTradeNum( TradeRule.getTradeNum() );
        tradeRuleDo.setTradeValueType( TradeRule.getTradeValueType() );
        tradeRuleDo.setTradePrice( TradeRule.getTradePrice() );
        tradeRuleDo.setRuleType( TradeRule.getRuleType() );
        tradeRuleDo.setStatus( TradeRule.getStatus() );
        tradeRuleDo.setUserId( TradeRule.getUserId() );
        tradeRuleDo.setMockType( TradeRule.getMockType() );
        tradeRuleDo.setCreateTime( TradeRule.getCreateTime() );
        tradeRuleDo.setUpdateTime( TradeRule.getUpdateTime() );
        tradeRuleDo.setFlag( TradeRule.getFlag() );

        return tradeRuleDo;
    }

    @Override
    public TradeRuleVo entityToVo(TradeRule TradeRule) {
        if ( TradeRule == null ) {
            return null;
        }

        TradeRuleVo tradeRuleVo = new TradeRuleVo();

        tradeRuleVo.setId( TradeRule.getId() );
        tradeRuleVo.setName( TradeRule.getName() );
        tradeRuleVo.setConditionCode( TradeRule.getConditionCode() );
        tradeRuleVo.setRuleValueType( TradeRule.getRuleValueType() );
        tradeRuleVo.setRuleValue( TradeRule.getRuleValue() );
        tradeRuleVo.setTradeNum( TradeRule.getTradeNum() );
        tradeRuleVo.setTradeValueType( TradeRule.getTradeValueType() );
        tradeRuleVo.setTradePrice( TradeRule.getTradePrice() );
        tradeRuleVo.setRuleType( TradeRule.getRuleType() );
        tradeRuleVo.setStatus( TradeRule.getStatus() );
        tradeRuleVo.setCreateTime( TradeRule.getCreateTime() );
        tradeRuleVo.setUpdateTime( TradeRule.getUpdateTime() );

        return tradeRuleVo;
    }

    @Override
    public TradeRule roToEntity(TradeRuleRo tradeRuleRo) {
        if ( tradeRuleRo == null ) {
            return null;
        }

        TradeRule tradeRule = new TradeRule();

        tradeRule.setId( tradeRuleRo.getId() );
        tradeRule.setName( tradeRuleRo.getName() );
        tradeRule.setConditionCode( tradeRuleRo.getConditionCode() );
        tradeRule.setConditionType( tradeRuleRo.getConditionType() );
        tradeRule.setRuleValueType( tradeRuleRo.getRuleValueType() );
        tradeRule.setRuleValue( tradeRuleRo.getRuleValue() );
        tradeRule.setTradeNum( tradeRuleRo.getTradeNum() );
        tradeRule.setTradeValueType( tradeRuleRo.getTradeValueType() );
        tradeRule.setTradePrice( tradeRuleRo.getTradePrice() );
        tradeRule.setRuleType( tradeRuleRo.getRuleType() );
        tradeRule.setUserId( tradeRuleRo.getUserId() );
        tradeRule.setMockType( tradeRuleRo.getMockType() );

        return tradeRule;
    }
}
