package top.yueshushu.learn.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import top.yueshushu.learn.stock.domain.StockDo;
import top.yueshushu.learn.stock.entity.Stock;

import java.util.List;

/**
 * @Description 股票的Mapper
 * @Author 岳建立
 * @Date 2021/11/14 0:03
 **/
public interface StockDoMapper extends BaseMapper<StockDo> {
    /**
     * 查询所有的股票编码列表集合
     *
     * @return 查询所有的股票编码列表集合
     */
    List<String> listAllCode();
}
