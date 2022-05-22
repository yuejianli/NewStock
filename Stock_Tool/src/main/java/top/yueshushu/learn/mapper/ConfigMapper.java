package top.yueshushu.learn.mapper;

import org.apache.ibatis.annotations.Param;
import top.yueshushu.learn.domain.ConfigDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 全局性系统配置 Mapper 接口
 * </p>
 *
 * @author 岳建立  自定义的
 * @since 2022-01-02
 */
public interface ConfigMapper extends BaseMapper<ConfigDo> {
    /**
     * 根据员工编号，查询对应的系统配置信息
     *
     * @param userId
     * @param code
     * @return
     */
    List<ConfigDo> findByUid(@Param("userId") Integer userId, @Param("code") String code);

    /**
     * 获取单个配置信息
     *
     * @param userId
     * @param code
     * @return
     */
    ConfigDo getConfig(@Param("userId") Integer userId, @Param("code") String code);
}
