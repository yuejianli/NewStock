package top.yueshushu.learn.mapper;

import org.apache.ibatis.annotations.Param;
import top.yueshushu.learn.mode.vo.StockSelectedVo;
import top.yueshushu.learn.pojo.StockSelected;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 股票自选表,是用户自己选择的 Mapper 接口
 * </p>
 *
 * @author 岳建立  自定义的
 * @since 2022-01-02
 */
public interface StockSelectedMapper extends BaseMapper<StockSelected> {
    /**
     * 根据关键字查询该员工配置的自选列表信息
     *
     * @param userId
     * @param keyword
     * @return
     */
    List<StockSelectedVo> selectByKeyword(@Param("userId") Integer userId, @Param("keyword") String keyword);

    /**
     * 查询所有的股票代码编号，用于同步
     *
     * @return
     */
    List<String> findCodeList(@Param("userId") Integer userId);
}
