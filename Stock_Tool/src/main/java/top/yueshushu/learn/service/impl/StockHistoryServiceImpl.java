package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import top.yueshushu.learn.mode.dto.StockPriceCacheDto;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.pojo.StockHistory;
import top.yueshushu.learn.mapper.StockHistoryMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.service.StockHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.vo.stock.StockHistoryVo;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 股票的历史交易记录表 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
public class StockHistoryServiceImpl extends ServiceImpl<StockHistoryMapper, StockHistory> implements StockHistoryService {
    @Autowired
    private StockHistoryMapper stockHistoryMapper;
    @Override
    public OutputResult history(StockRo stockRo) {
        PageHelper.startPage(stockRo.getPageNum(),stockRo.getPageSize());
        List<StockHistoryVo> stockHistoryList= stockHistoryMapper.getStockHistoryAndDate(stockRo.getCode()
                , DateUtil.parse(
                        stockRo.getStartDate(),"yyyy-MM-dd"
                ),
                DateUtil.parse(
                        stockRo.getEndDate(),"yyyy-MM-dd"
                ));
        PageInfo pageInfo=new PageInfo<StockHistoryVo>(stockHistoryList);
        return OutputResult.success(new PageResponse<>(
                pageInfo.getTotal(),pageInfo.getList()
        ));
    }

    @Override
    public List<StockPriceCacheDto> listClosePrice(List<String> codeList) {
        Date yesDate = DateUtil.yesterday();
        return stockHistoryMapper.listClosePrice(codeList,yesDate);
    }


}
