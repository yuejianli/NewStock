package top.yueshushu.learn.mapper;

import org.apache.ibatis.annotations.Param;
import top.yueshushu.learn.pojo.Stock;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 股票信息基本表 Mapper 接口
 * </p>
 *
 * @author 岳建立  自定义的
 * @since 2022-01-02
 */
public interface StockMapper extends BaseMapper<Stock> {
    /**
     * 根据编码和类型进行查询
     *
     * @param code
     * @param exchange
     * @return
     */
    List<Stock> selectByCodeAndType(@Param("code") String code, @Param("exchange") Integer exchange);

    /**
     * 全部删除
     */
    void deleteAll();

    /**
     * 根据关键字进行查询
     *
     * @param keyword
     * @return
     */
    List<Stock> selectByKeyword(@Param("keyword") String keyword);
}
