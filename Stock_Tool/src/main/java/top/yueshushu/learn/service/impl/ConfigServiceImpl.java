package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import top.yueshushu.learn.common.Const;
import top.yueshushu.learn.enumtype.ConfigCodeType;
import top.yueshushu.learn.mode.ro.ConfigRo;
import top.yueshushu.learn.model.info.StockInfo;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.pojo.Config;
import top.yueshushu.learn.mapper.ConfigMapper;
import top.yueshushu.learn.pojo.Stock;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.ConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.util.PageUtil;

import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {
    @Autowired
    private ConfigMapper configMapper;
    @Override
    public OutputResult listConfig(ConfigRo configRo) {
        //先查询系统全部的配置。 数据量不多 ，可以采用前端分页的方式.
       List<Config> configDefaultList = configMapper.findByUid(
               Const.DEFAULT_NO,null
       );
       //查询当前用户的信息
        List<Config> configUserList = configMapper.findByUid(
                configRo.getUserId(),null
        );
        if(!CollectionUtils.isEmpty(configUserList)){
            //将默认的，转换成对应的map 形式
            Map<String,Config> configMap = configUserList.stream().collect(
                    Collectors.toMap(
                            Config::getCode,
                            n->n
                    )
            );
            //进行进行
            configDefaultList.forEach(
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
        List<Config> list = PageUtil.startPage(configDefaultList, configRo.getPageNum(),
                configRo.getPageSize());
        return OutputResult.success(new PageResponse<Config>((long) configDefaultList.size(),
                list));
    }

    @Override
    public Config getConfig(Integer userId, ConfigCodeType configCodeType) {
        if(null==configCodeType){
            return null;
        }
        return getConfigByCode(userId,configCodeType.getCode());
    }

    @Override
    public Config getConfigByCode(Integer userId, String code) {
        if(!StringUtils.hasText(code)){
            return null;
        }
        Config defaultConfig = configMapper.getConfig(
                Const.DEFAULT_NO,code
        );
        //查询当前用户的配置
        Config userConfig = configMapper.getConfig(
                userId,code
        );
        if(userConfig==null){
            return defaultConfig;
        }
        BeanUtils.copyProperties(userConfig,defaultConfig);
        return defaultConfig;
    }

    @Override
    public OutputResult update(ConfigRo configRo) {
        //根据id 去查询对应的记录信息
        Config config =getById(configRo.getId());
        if(null==config){
            return OutputResult.alert("不存在此配置记录信息");
        }
        //获取对应的code信息
        Config userConfig = getConfigByCode(configRo.getUserId(),config.getCode());
        //如果是用户的配置，则进行更新
        if(userConfig.isUserConfig()){
            userConfig.setCodeValue(configRo.getCodeValue());
            userConfig.setName(configRo.getName());
            userConfig.setCreateTime(DateUtil.date());
            configMapper.updateById(userConfig);
            return OutputResult.success();
        }
        //不是用户配置，则重新添加一份.
        Config addConfig = new Config();
        BeanUtils.copyProperties(userConfig,addConfig);
        addConfig.setId(null);
        addConfig.setCodeValue(config.getCodeValue());
        addConfig.setName(config.getName());
        addConfig.setCreateTime(DateUtil.date());
        addConfig.setUserId(configRo.getUserId());
        configMapper.insert(addConfig);
        return OutputResult.success();
    }

    @Override
    public OutputResult reset(ConfigRo configRo) {
        //根据id 去查询对应的记录信息
        Config config =getById(configRo.getId());
        if(null==config){
            return OutputResult.alert("不存在此配置记录信息");
        }
        //获取对应的code信息
        Config userConfig = getConfigByCode(configRo.getUserId(),config.getCode());
        //如果是用户的配置，则进行更新
        if(!userConfig.isUserConfig()){
            return OutputResult.alert("已经是系统默认配置,无法删除");
        }
        configMapper.deleteById(userConfig.getId());
        //删除
        return OutputResult.success();
    }
}
