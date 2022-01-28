package top.yueshushu.learn.service;

import top.yueshushu.learn.mode.ro.IdRo;
import top.yueshushu.learn.mode.ro.StockSelectedRo;
import top.yueshushu.learn.mode.vo.StockSelectedVo;
import top.yueshushu.learn.pojo.StockSelected;
import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.response.OutputResult;

import java.util.List;

/**
 * <p>
 * 股票自选表,是用户自己选择的 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
public interface StockSelectedService extends IService<StockSelected> {
    /**
     * 添加到自选表里面
     * @param stockSelectedRo
     * @return
     */
    OutputResult add(StockSelectedRo stockSelectedRo);

    /**
     * 批量移除自选数据信息
     * @param idRo
     * @param userId
     * @return
     */
    OutputResult delete(IdRo idRo, int userId);

    /**
     * 查询股票自选信息
     * @param stockSelectedRo
     * @return
     */
    OutputResult listSelected(StockSelectedRo stockSelectedRo);

    /**
     * 根据股票的code 进行移除
     * @param stockSelectedRo
     * @return
     */
    OutputResult deleteByCode(StockSelectedRo stockSelectedRo);

    /**
     * 更新历史表记录信息，
     * 从自选表里面拿出数据。
     */
    void syncDayHistory();

    /**
     * 处理股票的收盘价信息
     */
    void cacheClosePrice();
    /**
     * 查询所有的股票信息
     * @date 2022/1/27 14:16
     * @author zk_yjl
     * @param userId
     * @return java.util.List<top.yueshushu.learn.mode.vo.StockSelectedVo>
     */
    List<StockSelectedVo> listSelf(Integer userId,String keyword);

}
