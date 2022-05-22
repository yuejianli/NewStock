package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.yueshushu.learn.common.Const;
import top.yueshushu.learn.enumtype.ConfigCodeType;
import top.yueshushu.learn.mode.ro.ConfigRo;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.domain.ConfigDo;
import top.yueshushu.learn.mapper.ConfigMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.ConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.util.PageUtil;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 全局性系统配置 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, ConfigDo> implements ConfigService {
    @Autowired
    private ConfigMapper configMapper;
    @Override
    public OutputResult listConfig(ConfigRo configRo) {
        //先查询系统全部的配置。 数据量不多 ，可以采用前端分页的方式.
       List<ConfigDo> configDoDefaultList = configMapper.findByUid(
               Const.DEFAULT_NO,null
       );
       //查询当前用户的信息
        List<ConfigDo> configDoUserList = configMapper.findByUid(
                configRo.getUserId(),null
        );
        if(!CollectionUtils.isEmpty(configDoUserList)){
            //将默认的，转换成对应的map 形式
            Map<String, ConfigDo> configMap = configDoUserList.stream().collect(
                    Collectors.toMap(
                            ConfigDo::getCode,
                            n->n
                    )
            );
            //进行进行
            configDoDefaultList.forEach(
                    n->{
                        if(configMap.containsKey(n.getCode())){
                            //进行复制,id 不复制
                            BeanUtils.copyProperties(
                                    configMap.get(n.getCode()),n,"id"
                            );
                        }
                    }
            );
        }
        List<ConfigDo> list = PageUtil.startPage(configDoDefaultList, configRo.getPageNum(),
                configRo.getPageSize());
        return OutputResult.buildSucc(new PageResponse<ConfigDo>((long) configDoDefaultList.size(),
                list));
    }

    @Override
    public ConfigDo getConfig(Integer userId, ConfigCodeType configCodeType) {
        if(null==configCodeType){
            return null;
        }
        return getConfigByCode(userId,configCodeType.getCode());
    }

    @Override
    public ConfigDo getConfigByCode(Integer userId, String code) {
        if(!StringUtils.hasText(code)){
            return null;
        }
        ConfigDo defaultConfigDo = configMapper.getConfig(
                Const.DEFAULT_NO,code
        );
        //查询当前用户的配置
        ConfigDo userConfigDo = configMapper.getConfig(
                userId,code
        );
        if(userConfigDo ==null){
            return defaultConfigDo;
        }
        BeanUtils.copyProperties(userConfigDo, defaultConfigDo);
        return defaultConfigDo;
    }

    @Override
    public OutputResult update(ConfigRo configRo) {
        //根据id 去查询对应的记录信息
        ConfigDo configDo =getById(configRo.getId());
        if(null== configDo){
            return OutputResult.buildAlert("不存在此配置记录信息");
        }
        //获取对应的code信息
        ConfigDo userConfigDo = getConfigByCode(configRo.getUserId(), configDo.getCode());
        //如果是用户的配置，则进行更新
        if(userConfigDo.isUserConfig()){
            userConfigDo.setCodeValue(configRo.getCodeValue());
            userConfigDo.setName(configRo.getName());
            userConfigDo.setCreateTime(DateUtil.date());
            configMapper.updateById(userConfigDo);
            return OutputResult.buildSucc();
        }
        //不是用户配置，则重新添加一份.
        ConfigDo addConfigDo = new ConfigDo();
        BeanUtils.copyProperties(userConfigDo, addConfigDo);
        addConfigDo.setId(null);
        addConfigDo.setCodeValue(configDo.getCodeValue());
        addConfigDo.setName(configDo.getName());
        addConfigDo.setCreateTime(DateUtil.date());
        addConfigDo.setUserId(configRo.getUserId());
        configMapper.insert(addConfigDo);
        return OutputResult.buildSucc();
    }

    @Override
    public OutputResult reset(ConfigRo configRo) {
        //根据id 去查询对应的记录信息
        ConfigDo configDo =getById(configRo.getId());
        if(null== configDo){
            return OutputResult.buildAlert("不存在此配置记录信息");
        }
        //获取对应的code信息
        ConfigDo userConfigDo = getConfigByCode(configRo.getUserId(), configDo.getCode());
        //如果是用户的配置，则进行更新
        if(!userConfigDo.isUserConfig()){
            return OutputResult.buildAlert("已经是系统默认配置,无法删除");
        }
        configMapper.deleteById(userConfigDo.getId());
        //删除
        return OutputResult.buildSucc();
    }

    @Override
    public int getMaxSelectedNumByUserId(Integer userId) {
        //获取配置信息
        ConfigDo configDo = getConfigByCode(userId, ConfigCodeType.SELECT_MAX_NUM.getCode());
        //获取信息
        return Integer.parseInt(
                Optional.ofNullable(configDo.getCodeValue())
                        .orElse("20")
        );
    }
}
