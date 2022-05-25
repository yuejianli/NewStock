package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.StockDo;
import top.yueshushu.learn.entity.Stock;
import top.yueshushu.learn.mode.vo.StockVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T10:42:46+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class StockAssemblerImpl implements StockAssembler {

    @Override
    public Stock doToEntity(StockDo stockDo) {
        if ( stockDo == null ) {
            return null;
        }

        Stock stock = new Stock();

        stock.setId( stockDo.getId() );
        stock.setCode( stockDo.getCode() );
        stock.setName( stockDo.getName() );
        stock.setExchange( stockDo.getExchange() );
        stock.setFullCode( stockDo.getFullCode() );
        stock.setCreateTime( stockDo.getCreateTime() );
        stock.setCreateUser( stockDo.getCreateUser() );
        stock.setFlag( stockDo.getFlag() );

        return stock;
    }

    @Override
    public StockDo entityToDo(Stock stock) {
        if ( stock == null ) {
            return null;
        }

        StockDo stockDo = new StockDo();

        stockDo.setId( stock.getId() );
        stockDo.setCode( stock.getCode() );
        stockDo.setName( stock.getName() );
        stockDo.setExchange( stock.getExchange() );
        stockDo.setFullCode( stock.getFullCode() );
        stockDo.setCreateTime( stock.getCreateTime() );
        stockDo.setCreateUser( stock.getCreateUser() );
        stockDo.setFlag( stock.getFlag() );

        return stockDo;
    }

    @Override
    public StockVo entityToVo(Stock stock) {
        if ( stock == null ) {
            return null;
        }

        StockVo stockVo = new StockVo();

        stockVo.setId( stock.getId() );
        stockVo.setCode( stock.getCode() );
        stockVo.setName( stock.getName() );
        stockVo.setExchange( stock.getExchange() );
        stockVo.setFullCode( stock.getFullCode() );
        stockVo.setCreateTime( stock.getCreateTime() );
        stockVo.setCreateUser( stock.getCreateUser() );
        stockVo.setFlag( stock.getFlag() );

        return stockVo;
    }
}
