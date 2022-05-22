package top.yueshushu.learn.domainservice.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.domain.StockDo;
import top.yueshushu.learn.domain.StockHistoryDo;
import top.yueshushu.learn.domainservice.StockDomainService;
import top.yueshushu.learn.domainservice.StockHistoryDomainService;
import top.yueshushu.learn.mapper.StockDoMapper;
import top.yueshushu.learn.mapper.StockHistoryDoMapper;
import top.yueshushu.learn.mode.dto.StockPriceCacheDto;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * @Description TODO
 * @Author yuejianli
 * @Date 2022/5/20 23:23
 **/
@Service
@Slf4j
public class StockHistoryDomainServiceImpl extends ServiceImpl<StockHistoryDoMapper, StockHistoryDo>
        implements StockHistoryDomainService {
    @Resource
    private StockHistoryDoMapper stockHistoryDoMapper;

    @Override
    public List<StockHistoryDo> listStockHistoryAndDate(String code, DateTime startDate, DateTime endDate) {
        return stockHistoryDoMapper.listStockHistoryAndDate(
                code,startDate,endDate
        );
    }

    @Override
    public List<StockPriceCacheDto> listYesterdayClosePrice(List<String> codeList, Date yesDate) {
        return stockHistoryDoMapper.listClosePrice(
                codeList,yesDate
        );
    }

    @Override
    public StockHistoryDo getByCodeAndCurrDate(String code, DateTime currDate) {
        return stockHistoryDoMapper.getStockForDate(
                code,currDate
        );
    }
}
