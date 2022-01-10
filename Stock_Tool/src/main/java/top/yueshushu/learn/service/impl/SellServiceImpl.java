package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.yueshushu.learn.enumtype.*;
import top.yueshushu.learn.mode.ro.SellRo;
import top.yueshushu.learn.pojo.Config;
import top.yueshushu.learn.pojo.TradeEntrust;
import top.yueshushu.learn.pojo.TradePosition;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.*;
import top.yueshushu.learn.system.SystemConst;
import top.yueshushu.learn.util.BigDecimalUtil;
import top.yueshushu.learn.util.StockUtil;

/**
 * @ClassName:SellServiceImpl
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/8 11:17
 * @Version 1.0
 **/
@Service
@Log4j2(topic="卖出股票:")
@Transactional
public class SellServiceImpl implements SellService {
    @Autowired
    private TradeEntrustService tradeEntrustService;
    @Autowired
    private TradePositionService tradePositionService;
    @Autowired
    private ConfigService configService;
    @Override
    public OutputResult sell(SellRo sellRo) {
        //对非空的验证信息
        if(!StringUtils.hasText(sellRo.getCode())){
            return OutputResult.alert("请传入卖出的股票信息");
        }
        if(sellRo.getAmount()==null){
            return OutputResult.alert("请传入卖出的股票股数信息");
        }
        if(sellRo.getPrice()==null){
            return OutputResult.alert("请传入卖出的股票的价格信息");
        }
        //获取当前该股票的持仓数和可用数.
        TradePosition tradePosition = tradePositionService.getPositionByCode(
                sellRo.getUserId(),
                sellRo.getMockType(),
               sellRo.getCode()
        );
        if(tradePosition==null){
            return OutputResult.alert("没有持仓信息，无法卖出");
        }
        if(tradePosition.getUseAmount()<sellRo.getAmount()){
            return OutputResult.alert("份额不足，请检查目前持仓数量");
        }
        tradePosition.setUseAmount(
                tradePosition.getUseAmount()
                - sellRo.getAmount()
        );
        //更新
        tradePositionService.updateById(tradePosition);
        //获取对应的金额
        Config priceConfig = configService.getConfigByCode(
                sellRo.getUserId(),
                ConfigCodeType.TRANPRICE.getCode()
        );
        TradeEntrust tradeEntrust = new TradeEntrust();
        tradeEntrust.setCode(sellRo.getCode());
        tradeEntrust.setName(sellRo.getName());
        tradeEntrust.setEntrustDate(DateUtil.date());
        tradeEntrust.setDealType(DealType.SELL.getCode());
        tradeEntrust.setEntrustNum(sellRo.getAmount());
        tradeEntrust.setEntrustPrice(BigDecimalUtil.convertFour(sellRo.getPrice()));
        tradeEntrust.setEntrustStatus(EntrustStatusType.ING.getCode());
        tradeEntrust.setEntrustCode(StockUtil.generateEntrustCode());
        tradeEntrust.setUseMoney(SystemConst.DEFAULT_EMPTY);
        tradeEntrust.setTakeoutMoney(SystemConst.DEFAULT_EMPTY);

        tradeEntrust.setEntrustMoney(
                StockUtil.allMoney(
                        sellRo.getAmount(),
                        sellRo.getPrice()
                )
        );
        tradeEntrust.setHandMoney(
                StockUtil.getSellHandMoney(
                        sellRo.getAmount(),
                        sellRo.getPrice(),
                        BigDecimalUtil.toBigDecimal(priceConfig.getCodeValue())
                )
        );
        tradeEntrust.setTotalMoney(
                StockUtil.getSellMoney(
                        sellRo.getAmount(),
                        sellRo.getPrice(),
                        BigDecimalUtil.toBigDecimal(priceConfig.getCodeValue())
                )
        );
        tradeEntrust.setUserId(sellRo.getUserId());
        tradeEntrust.setEntrustType(EntrustType.HANDLER.getCode());
        tradeEntrust.setMockType(sellRo.getMockType());
        tradeEntrust.setFlag(DataFlagType.NORMAL.getCode());
        //放入一条记录到委托信息里面.
        tradeEntrustService.save(tradeEntrust);
        return OutputResult.success("卖出股票委托成功");
    }
}
