package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.TradeRuleStockDo;
import top.yueshushu.learn.entity.TradeRuleStock;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T10:42:46+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class TradeRuleStockAssemblerImpl implements TradeRuleStockAssembler {

    @Override
    public TradeRuleStock doToEntity(TradeRuleStockDo TradeRuleStockDo) {
        if ( TradeRuleStockDo == null ) {
            return null;
        }

        TradeRuleStock tradeRuleStock = new TradeRuleStock();

        tradeRuleStock.setId( TradeRuleStockDo.getId() );
        tradeRuleStock.setRuleId( TradeRuleStockDo.getRuleId() );
        tradeRuleStock.setStockCode( TradeRuleStockDo.getStockCode() );
        tradeRuleStock.setCreateTime( TradeRuleStockDo.getCreateTime() );

        return tradeRuleStock;
    }

    @Override
    public TradeRuleStockDo entityToDo(TradeRuleStock TradeRuleStock) {
        if ( TradeRuleStock == null ) {
            return null;
        }

        TradeRuleStockDo tradeRuleStockDo = new TradeRuleStockDo();

        tradeRuleStockDo.setId( TradeRuleStock.getId() );
        tradeRuleStockDo.setRuleId( TradeRuleStock.getRuleId() );
        tradeRuleStockDo.setStockCode( TradeRuleStock.getStockCode() );
        tradeRuleStockDo.setCreateTime( TradeRuleStock.getCreateTime() );

        return tradeRuleStockDo;
    }
}
