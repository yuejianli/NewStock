package top.yueshushu.learn.stock.domainservice;

import com.baomidou.mybatisplus.extension.service.IService;
import top.yueshushu.learn.stock.domain.StockDo;

import java.util.List;

/**
 * @Description 股票的操作
 * @Author yuejianli
 * @Date 2022/5/20 23:23
 **/
public interface StockDomainService extends IService<StockDo> {
    /**
     * 根据股票的编码获取信息
     * @param code 股票的编码
     * @return 根据股票的编码获取信息
     */
    StockDo getByCode(String code);

    /**
     * 根据股票的编码获取信息
     * @param fullCode 股票的全编码
     * @return 根据股票的编码获取信息
     */
    StockDo getByFullCode(String fullCode);

    /**
     * 查询所有的股票列表的编码集合
     * @return 查询所有的股票列表的编码集合
     */
    List<String> listAllCode();
}
