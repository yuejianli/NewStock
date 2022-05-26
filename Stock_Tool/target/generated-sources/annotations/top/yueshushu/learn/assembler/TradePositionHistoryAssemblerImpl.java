package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.TradePositionHistoryDo;
import top.yueshushu.learn.entity.TradePositionHistory;
import top.yueshushu.learn.mode.vo.TradePositionHistoryVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-26T20:42:25+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class TradePositionHistoryAssemblerImpl implements TradePositionHistoryAssembler {

    @Override
    public TradePositionHistory doToEntity(TradePositionHistoryDo tradePositionHistoryDo) {
        if ( tradePositionHistoryDo == null ) {
            return null;
        }

        TradePositionHistory tradePositionHistory = new TradePositionHistory();

        tradePositionHistory.setId( tradePositionHistoryDo.getId() );
        tradePositionHistory.setCode( tradePositionHistoryDo.getCode() );
        tradePositionHistory.setName( tradePositionHistoryDo.getName() );
        tradePositionHistory.setCurrDate( tradePositionHistoryDo.getCurrDate() );
        tradePositionHistory.setAllAmount( tradePositionHistoryDo.getAllAmount() );
        tradePositionHistory.setUseAmount( tradePositionHistoryDo.getUseAmount() );
        tradePositionHistory.setAvgPrice( tradePositionHistoryDo.getAvgPrice() );
        tradePositionHistory.setPrice( tradePositionHistoryDo.getPrice() );
        tradePositionHistory.setAllMoney( tradePositionHistoryDo.getAllMoney() );
        tradePositionHistory.setFloatMoney( tradePositionHistoryDo.getFloatMoney() );
        tradePositionHistory.setFloatProportion( tradePositionHistoryDo.getFloatProportion() );
        tradePositionHistory.setUserId( tradePositionHistoryDo.getUserId() );
        tradePositionHistory.setMockType( tradePositionHistoryDo.getMockType() );

        return tradePositionHistory;
    }

    @Override
    public TradePositionHistoryDo entityToDo(TradePositionHistory tradePositionHistory) {
        if ( tradePositionHistory == null ) {
            return null;
        }

        TradePositionHistoryDo tradePositionHistoryDo = new TradePositionHistoryDo();

        tradePositionHistoryDo.setId( tradePositionHistory.getId() );
        tradePositionHistoryDo.setCode( tradePositionHistory.getCode() );
        tradePositionHistoryDo.setName( tradePositionHistory.getName() );
        tradePositionHistoryDo.setCurrDate( tradePositionHistory.getCurrDate() );
        tradePositionHistoryDo.setAllAmount( tradePositionHistory.getAllAmount() );
        tradePositionHistoryDo.setUseAmount( tradePositionHistory.getUseAmount() );
        tradePositionHistoryDo.setAvgPrice( tradePositionHistory.getAvgPrice() );
        tradePositionHistoryDo.setPrice( tradePositionHistory.getPrice() );
        tradePositionHistoryDo.setAllMoney( tradePositionHistory.getAllMoney() );
        tradePositionHistoryDo.setFloatMoney( tradePositionHistory.getFloatMoney() );
        tradePositionHistoryDo.setFloatProportion( tradePositionHistory.getFloatProportion() );
        tradePositionHistoryDo.setUserId( tradePositionHistory.getUserId() );
        tradePositionHistoryDo.setMockType( tradePositionHistory.getMockType() );

        return tradePositionHistoryDo;
    }

    @Override
    public TradePositionHistoryVo entityToVo(TradePositionHistory tradePositionHistory) {
        if ( tradePositionHistory == null ) {
            return null;
        }

        TradePositionHistoryVo tradePositionHistoryVo = new TradePositionHistoryVo();

        tradePositionHistoryVo.setId( tradePositionHistory.getId() );
        tradePositionHistoryVo.setCode( tradePositionHistory.getCode() );
        tradePositionHistoryVo.setName( tradePositionHistory.getName() );
        tradePositionHistoryVo.setCurrDate( tradePositionHistory.getCurrDate() );
        tradePositionHistoryVo.setAllAmount( tradePositionHistory.getAllAmount() );
        tradePositionHistoryVo.setUseAmount( tradePositionHistory.getUseAmount() );
        tradePositionHistoryVo.setAvgPrice( tradePositionHistory.getAvgPrice() );
        tradePositionHistoryVo.setPrice( tradePositionHistory.getPrice() );
        tradePositionHistoryVo.setAllMoney( tradePositionHistory.getAllMoney() );
        tradePositionHistoryVo.setFloatMoney( tradePositionHistory.getFloatMoney() );
        tradePositionHistoryVo.setFloatProportion( tradePositionHistory.getFloatProportion() );
        tradePositionHistoryVo.setMockType( tradePositionHistory.getMockType() );

        return tradePositionHistoryVo;
    }
}
