package top.yueshushu.learn.domainservice.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.domain.TradePositionHistoryDo;
import top.yueshushu.learn.domain.UserDo;
import top.yueshushu.learn.domainservice.TradePositionHistoryDomainService;
import top.yueshushu.learn.domainservice.UserDomainService;
import top.yueshushu.learn.mapper.TradePositionHistoryDoMapper;
import top.yueshushu.learn.mapper.UserDoMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description TODO
 * @Author yuejianli
 * @Date 2022/5/20 23:23
 **/
@Service
@Slf4j
public class TradePositionHistoryDomainServiceImpl extends ServiceImpl<TradePositionHistoryDoMapper, TradePositionHistoryDo>
        implements TradePositionHistoryDomainService {
    @Resource
    private TradePositionHistoryDoMapper tradePositionHistoryDoMapper;

    @Override
    public List<TradePositionHistoryDo> listPositionHistoryAndDate(String code, DateTime startDate, DateTime endDate) {
        return tradePositionHistoryDoMapper.listPositionHistoryAndDateDesc(
                code,startDate,endDate
        );
    }

    @Override
    public void deleteByUserIdAndMockTypeAndDate(Integer userId, Integer mockType, DateTime currDate) {
         tradePositionHistoryDoMapper.deleteByUserIdAndMockTypeAndDate(
                userId,mockType,currDate
        );
    }
}
