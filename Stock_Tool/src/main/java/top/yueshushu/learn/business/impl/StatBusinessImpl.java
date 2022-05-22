package top.yueshushu.learn.business.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.business.StatBusiness;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockStatRo;

/**
 * @Description 菜单实现编排层
 * @Author yuejianli
 * @Date 2022/5/20 23:54
 **/
@Service
@Slf4j
public class StatBusinessImpl implements StatBusiness {

    @Override
    public OutputResult getWeekStat(StockStatRo stockStatRo) {
        return null;
    }

    @Override
    public OutputResult getCharStat(StockStatRo stockStatRo) {
        return null;
    }
}
