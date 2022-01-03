package top.yueshushu.learn.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.mode.vo.StockVo;
import top.yueshushu.learn.model.info.StockInfo;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.pojo.Stock;
import top.yueshushu.learn.mapper.StockMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.service.StockService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 股票信息基本表 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
@Log4j2
public class StockServiceImpl extends ServiceImpl<StockMapper, Stock> implements StockService {
    @Autowired
    private StockMapper stockMapper;
    @Override
    public OutputResult listStock(StockRo stockRo) {
        PageHelper.startPage(stockRo.getPageNum(),stockRo.getPageSize());
        List<Stock> stockInfoList= stockMapper.selectByKeyword(stockRo.getKeyword());
        PageInfo pageInfo=new PageInfo<Stock>(stockInfoList);
        return OutputResult.success(new PageResponse<Stock>(pageInfo.getTotal(),
                pageInfo.getList()));
    }
    @Override
    public Stock selectByCode(String code) {
        List<Stock> stockList = stockMapper.selectByCodeAndType(code, null);
        if(CollectionUtils.isEmpty(stockList)){
            return null;
        }
        return stockList.get(stockList.size()-1);
    }

    @Override
    public boolean existStockCode(String code) {
        List<Stock> stockList = stockMapper.selectByCodeAndType(code, null);
        if(CollectionUtils.isEmpty(stockList)){
            return false;
        }
        return true;
    }

    @Override
    public OutputResult<StockVo> getStockInfo(String code) {
        List<Stock> stockInfoList= stockMapper.selectByKeyword(code);
        if(CollectionUtils.isEmpty(stockInfoList)){
            return OutputResult.alert("没有此股票代码信息");
        }
        Stock stock = stockInfoList.get(stockInfoList.size()-1);
        //获取信息
        StockVo stockVo = new StockVo();
        BeanUtils.copyProperties(stock,stockVo);
        return OutputResult.success(stockVo);
    }
}
