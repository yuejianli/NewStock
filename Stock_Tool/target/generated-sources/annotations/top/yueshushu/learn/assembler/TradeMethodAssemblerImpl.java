package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.TradeMethodDo;
import top.yueshushu.learn.entity.TradeMethod;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T11:30:44+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class TradeMethodAssemblerImpl implements TradeMethodAssembler {

    @Override
    public TradeMethod doToEntity(TradeMethodDo tradeMethodDo) {
        if ( tradeMethodDo == null ) {
            return null;
        }

        TradeMethod tradeMethod = new TradeMethod();

        tradeMethod.setId( tradeMethodDo.getId() );
        tradeMethod.setCode( tradeMethodDo.getCode() );
        tradeMethod.setName( tradeMethodDo.getName() );
        tradeMethod.setUrl( tradeMethodDo.getUrl() );
        tradeMethod.setDescription( tradeMethodDo.getDescription() );
        tradeMethod.setCreateTime( tradeMethodDo.getCreateTime() );
        tradeMethod.setUpdateTime( tradeMethodDo.getUpdateTime() );
        tradeMethod.setFlag( tradeMethodDo.getFlag() );

        return tradeMethod;
    }

    @Override
    public TradeMethodDo entityToDo(TradeMethod menu) {
        if ( menu == null ) {
            return null;
        }

        TradeMethodDo tradeMethodDo = new TradeMethodDo();

        tradeMethodDo.setId( menu.getId() );
        tradeMethodDo.setCode( menu.getCode() );
        tradeMethodDo.setName( menu.getName() );
        tradeMethodDo.setUrl( menu.getUrl() );
        tradeMethodDo.setDescription( menu.getDescription() );
        tradeMethodDo.setCreateTime( menu.getCreateTime() );
        tradeMethodDo.setUpdateTime( menu.getUpdateTime() );
        tradeMethodDo.setFlag( menu.getFlag() );

        return tradeMethodDo;
    }
}
