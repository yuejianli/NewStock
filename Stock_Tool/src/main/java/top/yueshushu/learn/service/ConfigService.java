package top.yueshushu.learn.service;

import top.yueshushu.learn.enumtype.ConfigCodeType;
import top.yueshushu.learn.mode.ro.ConfigRo;
import top.yueshushu.learn.model.info.StockInfo;
import top.yueshushu.learn.pojo.Config;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;

/**
 * <p>
 * 全局性系统配置 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
public interface ConfigService extends IService<Config> {
    /**
     * 查询该员工对应的系统配置.
     * 如果没有配置，使用系统默认的配置信息.
     * @param configRo
     * @return
     */
    OutputResult listConfig(ConfigRo configRo);
    /**
     * 查询配置信息
     * @param userId
     * @param configCodeType
     * @return
     */
    Config getConfig(Integer userId,ConfigCodeType configCodeType);
    /**
     * 查询配置信息
     * @param userId
     * @param code
     * @return
     */
    Config getConfigByCode(Integer userId,String code);

    /**
     * 修改配置信息
     * @param configRo
     * @return
     */
    OutputResult update(ConfigRo configRo);

    /**
     * 重置配置参数信息
     * @param configRo
     * @return
     */
    OutputResult reset(ConfigRo configRo);
}
