package top.yueshushu.learn.stock.domainservice.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.stock.domain.StockHistoryDo;
import top.yueshushu.learn.stock.domainservice.StockHistoryDomainService;
import top.yueshushu.learn.stock.mapper.StockHistoryDoMapper;

import javax.annotation.Resource;

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
    public void deleteAsyncRangeDateData(String code, String startDate, String endDate) {
        stockHistoryDoMapper.deleteAsyncRangeDateData(
                code,
                DateUtil.parse(
                        startDate,"yyyyMMdd"
                ),
                DateUtil.parse(
                        endDate,"yyyyMMdd"
                )
        );
    }
}
