package top.yueshushu.learn.service.impl;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.yueshushu.learn.enumtype.DealType;
import top.yueshushu.learn.enumtype.EntrustStatusType;
import top.yueshushu.learn.mode.ro.RevokeRo;
import top.yueshushu.learn.pojo.TradeEntrust;
import top.yueshushu.learn.pojo.TradeMoney;
import top.yueshushu.learn.pojo.TradePosition;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.RevokeService;
import top.yueshushu.learn.service.TradeEntrustService;
import top.yueshushu.learn.service.TradeMoneyService;
import top.yueshushu.learn.service.TradePositionService;
import top.yueshushu.learn.util.BigDecimalUtil;

/**
 * @ClassName:RevokeServiceImpl
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/8 16:56
 * @Version 1.0
 **/
@Service
@Slf4j(topic = "撤销委托单")
@Transactional
public class RevokeServiceImpl implements RevokeService {
    @Autowired
    private TradeMoneyService tradeMoneyService;
    @Autowired
    private TradeEntrustService tradeEntrustService;
    @Autowired
    private TradePositionService tradePositionService;
    @Override
    public OutputResult revoke(RevokeRo revokeRo) {
        if(revokeRo.getId()==null){
            return OutputResult.alert("请选择要撤销的委托单信息");
        }
        //查询单号信息
        TradeEntrust tradeEntrust = tradeEntrustService.getById(revokeRo.getId());
        if(null==tradeEntrust){
            return OutputResult.alert("传入的委托编号id不正确");
        }
        if(!tradeEntrust.getUserId().equals(revokeRo.getUserId())){
            return OutputResult.alert("你不能撤销别人的记录信息");
        }
        if(!EntrustStatusType.ING.getCode().equals(tradeEntrust.getEntrustStatus())){
            return OutputResult.alert("委托状态不是进行中");
        }
        //获取委托的类型
        Integer dealType = tradeEntrust.getDealType();
        if(DealType.BUY.getCode().equals(dealType)){
           return buyRevoke(tradeEntrust);
        }else{
            return sellRevoke(tradeEntrust);
        }
    }

    private OutputResult sellRevoke(TradeEntrust tradeEntrust) {
        //取消的话，改变这个记录的状态。
        tradeEntrust.setEntrustStatus(EntrustStatusType.REVOKE.getCode());
        //更新
        tradeEntrustService.updateById(tradeEntrust);

        TradePosition tradePosition = tradePositionService.getPositionByCode(
                tradeEntrust.getUserId(),
                tradeEntrust.getMockType(),
                tradeEntrust.getCode()
        );
        if(tradePosition==null){
            return OutputResult.alert("没有持仓信息，出现了异常");
        }
        tradePosition.setUseAmount(
                tradePosition.getUseAmount()
                        + tradeEntrust.getEntrustNum()
        );
        //更新
        tradePositionService.updateById(tradePosition);
        return OutputResult.success("撤销卖出委托成功");
    }

    private OutputResult buyRevoke(TradeEntrust tradeEntrust) {
        //取消的话，改变这个记录的状态。
        tradeEntrust.setEntrustStatus(EntrustStatusType.REVOKE.getCode());
        //更新
        tradeEntrustService.updateById(tradeEntrust);

        //将金额回滚
       TradeMoney tradeMoney =  tradeMoneyService.getByUid(
                tradeEntrust.getUserId(),
                tradeEntrust.getMockType()
        );
        if(null==tradeMoney){
            log.error("没有获取到用户的资金信息");
        }
        //获取到后，需要进行更新
        tradeMoney.setUseMoney(
                BigDecimalUtil.addBigDecimal(
                        tradeMoney.getUseMoney(),
                        tradeEntrust.getUseMoney()
                )
        );
        tradeMoney.setTakeoutMoney(
                BigDecimalUtil.addBigDecimal(
                        tradeMoney.getTakeoutMoney(),
                        tradeEntrust.getTakeoutMoney()
                )
        );
        tradeMoneyService.updateMoneyVoByid(
                tradeMoney
        );
        return OutputResult.success("撤销买的委托成功");
    }
}
