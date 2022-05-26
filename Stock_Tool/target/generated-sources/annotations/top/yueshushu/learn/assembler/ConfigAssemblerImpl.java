package top.yueshushu.learn.assembler;

import javax.annotation.Generated;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.domain.ConfigDo;
import top.yueshushu.learn.entity.Config;
import top.yueshushu.learn.mode.vo.ConfigVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2022-05-25T11:30:44+0800",
    comments = "version: 1.5.0.Beta1, compiler: javac, environment: Java 1.8.0_102 (Oracle Corporation)"
)
@Component
public class ConfigAssemblerImpl implements ConfigAssembler {

    @Override
    public Config doToEntity(ConfigDo ConfigDo) {
        if ( ConfigDo == null ) {
            return null;
        }

        Config config = new Config();

        config.setId( ConfigDo.getId() );
        config.setCode( ConfigDo.getCode() );
        config.setName( ConfigDo.getName() );
        config.setCodeValue( ConfigDo.getCodeValue() );
        config.setUserId( ConfigDo.getUserId() );
        config.setCreateTime( ConfigDo.getCreateTime() );
        config.setFlag( ConfigDo.getFlag() );

        return config;
    }

    @Override
    public ConfigDo entityToDo(Config Config) {
        if ( Config == null ) {
            return null;
        }

        ConfigDo configDo = new ConfigDo();

        configDo.setId( Config.getId() );
        configDo.setCode( Config.getCode() );
        configDo.setName( Config.getName() );
        configDo.setCodeValue( Config.getCodeValue() );
        configDo.setUserId( Config.getUserId() );
        configDo.setCreateTime( Config.getCreateTime() );
        configDo.setFlag( Config.getFlag() );

        return configDo;
    }

    @Override
    public ConfigVo entityToVo(Config Config) {
        if ( Config == null ) {
            return null;
        }

        ConfigVo configVo = new ConfigVo();

        configVo.setId( Config.getId() );
        configVo.setCode( Config.getCode() );
        configVo.setName( Config.getName() );
        configVo.setCodeValue( Config.getCodeValue() );
        configVo.setUserId( Config.getUserId() );
        configVo.setCreateTime( Config.getCreateTime() );
        configVo.setFlag( Config.getFlag() );

        return configVo;
    }
}
