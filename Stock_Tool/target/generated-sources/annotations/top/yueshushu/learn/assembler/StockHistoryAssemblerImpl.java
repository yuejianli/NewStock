package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.StockHistoryDo;
import top.yueshushu.learn.entity.StockHistory;
import top.yueshushu.learn.mode.vo.StockHistoryVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T11:30:44+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class StockHistoryAssemblerImpl implements StockHistoryAssembler {

    @Override
    public StockHistory doToEntity(StockHistoryDo stockHistoryDo) {
        if ( stockHistoryDo == null ) {
            return null;
        }

        StockHistory stockHistory = new StockHistory();

        stockHistory.setId( stockHistoryDo.getId() );
        stockHistory.setCode( stockHistoryDo.getCode() );
        stockHistory.setName( stockHistoryDo.getName() );
        stockHistory.setCurrDate( stockHistoryDo.getCurrDate() );
        stockHistory.setLowestPrice( stockHistoryDo.getLowestPrice() );
        stockHistory.setOpeningPrice( stockHistoryDo.getOpeningPrice() );
        stockHistory.setYesClosingPrice( stockHistoryDo.getYesClosingPrice() );
        stockHistory.setAmplitude( stockHistoryDo.getAmplitude() );
        stockHistory.setAmplitudeProportion( stockHistoryDo.getAmplitudeProportion() );
        stockHistory.setTradingVolume( stockHistoryDo.getTradingVolume() );
        stockHistory.setTradingValue( stockHistoryDo.getTradingValue() );
        stockHistory.setClosingPrice( stockHistoryDo.getClosingPrice() );
        stockHistory.setHighestPrice( stockHistoryDo.getHighestPrice() );
        stockHistory.setFlag( stockHistoryDo.getFlag() );

        return stockHistory;
    }

    @Override
    public StockHistoryDo entityToDo(StockHistory stockHistory) {
        if ( stockHistory == null ) {
            return null;
        }

        StockHistoryDo stockHistoryDo = new StockHistoryDo();

        stockHistoryDo.setId( stockHistory.getId() );
        stockHistoryDo.setCode( stockHistory.getCode() );
        stockHistoryDo.setName( stockHistory.getName() );
        stockHistoryDo.setCurrDate( stockHistory.getCurrDate() );
        stockHistoryDo.setLowestPrice( stockHistory.getLowestPrice() );
        stockHistoryDo.setOpeningPrice( stockHistory.getOpeningPrice() );
        stockHistoryDo.setYesClosingPrice( stockHistory.getYesClosingPrice() );
        stockHistoryDo.setAmplitude( stockHistory.getAmplitude() );
        stockHistoryDo.setAmplitudeProportion( stockHistory.getAmplitudeProportion() );
        stockHistoryDo.setTradingVolume( stockHistory.getTradingVolume() );
        stockHistoryDo.setTradingValue( stockHistory.getTradingValue() );
        stockHistoryDo.setClosingPrice( stockHistory.getClosingPrice() );
        stockHistoryDo.setHighestPrice( stockHistory.getHighestPrice() );
        stockHistoryDo.setFlag( stockHistory.getFlag() );

        return stockHistoryDo;
    }

    @Override
    public StockHistoryVo entityToVo(StockHistory stockHistory) {
        if ( stockHistory == null ) {
            return null;
        }

        StockHistoryVo stockHistoryVo = new StockHistoryVo();

        stockHistoryVo.setId( stockHistory.getId() );
        stockHistoryVo.setCode( stockHistory.getCode() );
        stockHistoryVo.setName( stockHistory.getName() );
        stockHistoryVo.setCurrDate( stockHistory.getCurrDate() );
        stockHistoryVo.setLowestPrice( stockHistory.getLowestPrice() );
        stockHistoryVo.setOpeningPrice( stockHistory.getOpeningPrice() );
        stockHistoryVo.setYesClosingPrice( stockHistory.getYesClosingPrice() );
        stockHistoryVo.setAmplitude( stockHistory.getAmplitude() );
        stockHistoryVo.setAmplitudeProportion( stockHistory.getAmplitudeProportion() );
        stockHistoryVo.setTradingVolume( stockHistory.getTradingVolume() );
        stockHistoryVo.setTradingValue( stockHistory.getTradingValue() );
        stockHistoryVo.setClosingPrice( stockHistory.getClosingPrice() );
        stockHistoryVo.setHighestPrice( stockHistory.getHighestPrice() );
        stockHistoryVo.setFlag( stockHistory.getFlag() );

        return stockHistoryVo;
    }
}
