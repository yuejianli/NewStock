package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.TradeUserDo;
import top.yueshushu.learn.entity.TradeUser;
import top.yueshushu.learn.mode.vo.TradeUserVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T10:42:46+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class TradeUserAssemblerImpl implements TradeUserAssembler {

    @Override
    public TradeUser doToEntity(TradeUserDo tradeUserDo) {
        if ( tradeUserDo == null ) {
            return null;
        }

        TradeUser tradeUser = new TradeUser();

        tradeUser.setId( tradeUserDo.getId() );
        tradeUser.setAccount( tradeUserDo.getAccount() );
        tradeUser.setPassword( tradeUserDo.getPassword() );
        tradeUser.setSalt( tradeUserDo.getSalt() );
        tradeUser.setUserId( tradeUserDo.getUserId() );
        tradeUser.setCookie( tradeUserDo.getCookie() );
        tradeUser.setValidateKey( tradeUserDo.getValidateKey() );
        tradeUser.setCreateTime( tradeUserDo.getCreateTime() );
        tradeUser.setUpdateTime( tradeUserDo.getUpdateTime() );
        tradeUser.setFlag( tradeUserDo.getFlag() );

        return tradeUser;
    }

    @Override
    public TradeUserDo entityToDo(TradeUser tradeUser) {
        if ( tradeUser == null ) {
            return null;
        }

        TradeUserDo tradeUserDo = new TradeUserDo();

        tradeUserDo.setId( tradeUser.getId() );
        tradeUserDo.setAccount( tradeUser.getAccount() );
        tradeUserDo.setPassword( tradeUser.getPassword() );
        tradeUserDo.setSalt( tradeUser.getSalt() );
        tradeUserDo.setUserId( tradeUser.getUserId() );
        tradeUserDo.setCookie( tradeUser.getCookie() );
        tradeUserDo.setValidateKey( tradeUser.getValidateKey() );
        tradeUserDo.setCreateTime( tradeUser.getCreateTime() );
        tradeUserDo.setUpdateTime( tradeUser.getUpdateTime() );
        tradeUserDo.setFlag( tradeUser.getFlag() );

        return tradeUserDo;
    }

    @Override
    public TradeUserVo entityToVo(TradeUser tradeUser) {
        if ( tradeUser == null ) {
            return null;
        }

        TradeUserVo tradeUserVo = new TradeUserVo();

        tradeUserVo.setUserId( tradeUser.getUserId() );

        return tradeUserVo;
    }
}
