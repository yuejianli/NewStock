package top.yueshushu.learn.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.enumtype.DealType;
import top.yueshushu.learn.enumtype.EntrustStatusType;
import top.yueshushu.learn.mode.ro.DealRo;
import top.yueshushu.learn.pojo.TradeEntrust;
import top.yueshushu.learn.pojo.TradeMoney;
import top.yueshushu.learn.pojo.TradePosition;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.*;
import top.yueshushu.learn.system.SystemConst;
import top.yueshushu.learn.util.BigDecimalUtil;
import top.yueshushu.learn.util.StockRedisUtil;
import top.yueshushu.learn.util.StockUtil;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName:RevokeServiceImpl
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/8 16:56
 * @Version 1.0
 **/
@Service
@Log4j2(topic = "成交委托单")
@Transactional
public class DealServiceImpl implements DealService {
    @Autowired
    private TradeMoneyService tradeMoneyService;
    @Autowired
    private TradeEntrustService tradeEntrustService;
    @Autowired
    private TradePositionService tradePositionService;
    @Autowired
    private TradeDealService tradeDealService;
    @Autowired
    private StockRedisUtil stockRedisUtil;
    @Override
    @Async
    public OutputResult deal(DealRo dealRo) {
        if(dealRo.getId()==null){
            return OutputResult.alert("请选择要成交的委托单信息");
        }
        //查询单号信息
        TradeEntrust tradeEntrust = tradeEntrustService.getById(dealRo.getId());
        if(null==tradeEntrust){
            return OutputResult.alert("传入的委托编号id不正确");
        }
        if(!tradeEntrust.getUserId().equals(dealRo.getUserId())){
            return OutputResult.alert("你不能成交别人的记录信息");
        }
        if(!EntrustStatusType.ING.getCode().equals(tradeEntrust.getEntrustStatus())){
            return OutputResult.alert("委托状态不是进行中");
        }
        //获取委托的类型
        Integer dealType = tradeEntrust.getDealType();
        if(DealType.BUY.getCode().equals(dealType)){
           return buyDeal(tradeEntrust);
        }else{
            return sellDeal(tradeEntrust);
        }
    }

    @Override
    public void mockDealXxlJob(DealRo dealRo) {
        //获取当前所有的今日委托单信息，正在委托的.
        List<TradeEntrust> tradeEntrustList= tradeEntrustService.listNowRunEntruct(dealRo.getUserId(),
                dealRo.getMockType());
        if(CollectionUtils.isEmpty(tradeEntrustList)){
            return ;
        }
        //进行处理.
        for(TradeEntrust tradeEntrust:tradeEntrustList){
            //获取当前的股票
            String code = tradeEntrust.getCode();
            //获取信息
            BigDecimal price = stockRedisUtil.getPrice(code);
            if(price.compareTo(SystemConst.DEFAULT_DEAL_PRICE)<=0){
                //没有从缓存里面获取到价格
                return ;
            }
            if(DealType.BUY.getCode().equals(tradeEntrust.getDealType())){
                //买的时候，  当前价格 < 买入价格，则成交.
                if(price.compareTo(tradeEntrust.getEntrustPrice())<0){
                    DealRo newRo = new DealRo();
                    BeanUtils.copyProperties(dealRo,newRo);
                    newRo.setId(tradeEntrust.getId());
                    deal(newRo);
                    //重置昨天的价格 为当天买入的价格.
                    stockRedisUtil.setYesPrice(
                            code,price
                    );
                }
            }else{
                //买的时候，  当前价格 > 卖出价格，则成交.
                if(price.compareTo(tradeEntrust.getEntrustPrice())>0){
                    DealRo newRo = new DealRo();
                    BeanUtils.copyProperties(dealRo,newRo);
                    newRo.setId(tradeEntrust.getId());
                    deal(newRo);
                    //重置昨天的价格 为当天买入的价格.
                    stockRedisUtil.setYesPrice(
                            code,price
                    );
                }
            }
        }

    }

    private OutputResult sellDeal(TradeEntrust tradeEntrust) {
        //取消的话，改变这个记录的状态。
        tradeEntrust.setEntrustStatus(EntrustStatusType.SUCCESS.getCode());
        //更新
        tradeEntrustService.updateById(tradeEntrust);
        //成交了，金额不动。 动持仓信息
        //看持仓里面，是否有此信息.
        TradePosition tradePosition = tradePositionService.getPositionByCode(
                tradeEntrust.getUserId(),
                tradeEntrust.getMockType(),
                tradeEntrust.getCode()
        );
        if(null==tradePosition){
           log.error("没有持仓信息，出现异常");
           return OutputResult.error("没有持仓信息，出现异常");
        }

        //查询一下可用数量
        if(tradePosition.getAllAmount().equals(tradeEntrust.getEntrustNum())){
            //说明全卖完了
            log.info("股票{}进行清仓成交",tradePosition.getName());
            //需要删除
            tradePositionService.removeById(tradePosition.getId());
        }else{
            //修改成本价
            tradePosition.setAvgPrice(
                    StockUtil.calcSellAvgPrice(
                            tradePosition.getAllAmount(),
                            tradePosition.getAvgPrice(),
                            tradeEntrust.getTotalMoney(),
                            tradeEntrust.getEntrustNum()
                    )
            );
            //买入成功
            tradePosition.setAllAmount(
                    tradePosition.getAllAmount()-
                            tradeEntrust.getEntrustNum()
            );
        }
        tradePositionService.updateById(tradePosition);

        //对个人的资产，需要进行添加的操作.

        TradeMoney tradeMoney = tradeMoneyService.getByUid(
                tradeEntrust.getUserId(),
                tradeEntrust.getMockType()
        );
        if(null==tradeMoney){
            log.error("个人资产{} 信息出现异常",tradeEntrust.getUserId());
        }
        //增加
        tradeMoney.setUseMoney(
                BigDecimalUtil.addBigDecimal(
                        tradeMoney.getUseMoney(),
                        tradeEntrust.getTotalMoney()
                )
        );
        //市值金额减少
        tradeMoney.setMarketMoney(
                BigDecimalUtil.subBigDecimal(
                        tradeMoney.getMarketMoney(),
                        BigDecimalUtil.toBigDecimal(
                                tradeEntrust.getEntrustPrice(),
                                new BigDecimal(
                                        tradeEntrust.getEntrustNum()
                                )
                        )
                )
        );
        //总金额会去掉相关的手续费
        tradeMoney.setTotalMoney(
                BigDecimalUtil.subBigDecimal(
                        tradeMoney.getTotalMoney(),
                        tradeEntrust.getHandMoney()
                )
        );
        tradeMoneyService.updateMoneyVoByid(tradeMoney);
        //添加一条记录到成交表里面
        tradeDealService.addDealRecord(tradeEntrust);
        return OutputResult.success("成交卖的委托");
    }

    private OutputResult buyDeal(TradeEntrust tradeEntrust) {
        //取消的话，改变这个记录的状态。
        tradeEntrust.setEntrustStatus(EntrustStatusType.SUCCESS.getCode());
        //更新
        tradeEntrustService.updateById(tradeEntrust);
        //成交了，金额不动。 动持仓信息
        //看持仓里面，是否有此信息.
        TradePosition tradePosition = tradePositionService.getPositionByCode(
                tradeEntrust.getUserId(),
                tradeEntrust.getMockType(),
                tradeEntrust.getCode()
        );
        if(null==tradePosition){
            //新添加持仓信息
            tradePosition = new TradePosition();
            tradePosition.setCode(tradeEntrust.getCode());
            tradePosition.setName(tradeEntrust.getName());
            tradePosition.setAllAmount(tradeEntrust.getEntrustNum());
            tradePosition.setUseAmount(tradeEntrust.getEntrustNum());
            tradePosition.setAvgPrice(
                    BigDecimalUtil.div(
                            tradeEntrust.getTotalMoney(),
                            new BigDecimal(
                                    tradeEntrust.getEntrustNum()
                            )
                    )
            );
            tradePosition.setUserId(tradeEntrust.getUserId());
            tradePosition.setMockType(tradeEntrust.getMockType());
            tradePositionService.save(tradePosition);
        }else{
            //修改成本价
            tradePosition.setAvgPrice(
                    StockUtil.calcBuyAvgPrice(
                            tradePosition.getAllAmount(),
                            tradePosition.getAvgPrice(),
                            tradeEntrust.getTotalMoney(),
                            tradeEntrust.getEntrustNum()
                    )
            );
            //买入成功
            tradePosition.setAllAmount(
                    tradePosition.getAllAmount()+
                            tradeEntrust.getEntrustNum()
            );
            tradePositionService.updateById(tradePosition);
        }

        //对个人的资产，需要进行减少的操作.
        TradeMoney tradeMoney = tradeMoneyService.getByUid(
                tradeEntrust.getUserId(),
                tradeEntrust.getMockType()
        );
        if(null==tradeMoney){
            log.error("个人资产{} 信息出现异常",tradeEntrust.getUserId());
        }
        //市值金额增加
        tradeMoney.setMarketMoney(
                BigDecimalUtil.addBigDecimal(
                        tradeMoney.getMarketMoney(),
                        BigDecimalUtil.toBigDecimal(
                                tradeEntrust.getEntrustPrice(),
                                new BigDecimal(tradeEntrust.getEntrustNum())
                        )
                )
        );
        //总金额会去掉相关的手续费
        tradeMoney.setTotalMoney(
                BigDecimalUtil.subBigDecimal(
                        tradeMoney.getTotalMoney(),
                        tradeEntrust.getHandMoney()
                )
        );
        tradeMoneyService.updateById(tradeMoney);
        //添加一条记录到成交表里面
        tradeDealService.addDealRecord(tradeEntrust);
        return OutputResult.success("成交买的委托");
    }
}
