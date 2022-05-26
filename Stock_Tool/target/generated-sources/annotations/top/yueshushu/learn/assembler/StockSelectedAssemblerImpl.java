package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.StockSelectedDo;
import top.yueshushu.learn.entity.StockSelected;
import top.yueshushu.learn.mode.vo.StockSelectedVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T11:30:44+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class StockSelectedAssemblerImpl implements StockSelectedAssembler {

    @Override
    public StockSelected doToEntity(StockSelectedDo stockSelectedDo) {
        if ( stockSelectedDo == null ) {
            return null;
        }

        StockSelected stockSelected = new StockSelected();

        stockSelected.setId( stockSelectedDo.getId() );
        stockSelected.setStockCode( stockSelectedDo.getStockCode() );
        stockSelected.setStockName( stockSelectedDo.getStockName() );
        stockSelected.setUserId( stockSelectedDo.getUserId() );
        stockSelected.setCreateTime( stockSelectedDo.getCreateTime() );
        stockSelected.setJobId( stockSelectedDo.getJobId() );
        stockSelected.setNotes( stockSelectedDo.getNotes() );
        stockSelected.setStatus( stockSelectedDo.getStatus() );
        stockSelected.setFlag( stockSelectedDo.getFlag() );

        return stockSelected;
    }

    @Override
    public StockSelectedDo entityToDo(StockSelected stock) {
        if ( stock == null ) {
            return null;
        }

        StockSelectedDo stockSelectedDo = new StockSelectedDo();

        stockSelectedDo.setId( stock.getId() );
        stockSelectedDo.setStockCode( stock.getStockCode() );
        stockSelectedDo.setStockName( stock.getStockName() );
        stockSelectedDo.setUserId( stock.getUserId() );
        stockSelectedDo.setCreateTime( stock.getCreateTime() );
        stockSelectedDo.setJobId( stock.getJobId() );
        stockSelectedDo.setNotes( stock.getNotes() );
        stockSelectedDo.setStatus( stock.getStatus() );
        stockSelectedDo.setFlag( stock.getFlag() );

        return stockSelectedDo;
    }

    @Override
    public StockSelectedVo entityToVo(StockSelected stock) {
        if ( stock == null ) {
            return null;
        }

        StockSelectedVo stockSelectedVo = new StockSelectedVo();

        stockSelectedVo.setId( stock.getId() );
        stockSelectedVo.setStockCode( stock.getStockCode() );
        stockSelectedVo.setStockName( stock.getStockName() );
        stockSelectedVo.setNotes( stock.getNotes() );
        stockSelectedVo.setCreateTime( stock.getCreateTime() );

        return stockSelectedVo;
    }
}
