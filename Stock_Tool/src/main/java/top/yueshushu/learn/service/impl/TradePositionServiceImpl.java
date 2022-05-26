package top.yueshushu.learn.service.impl;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.api.TradeResultVo;
import top.yueshushu.learn.api.request.GetStockListRequest;
import top.yueshushu.learn.api.response.GetStockListResponse;
import top.yueshushu.learn.api.responseparse.DefaultResponseParser;
import top.yueshushu.learn.config.TradeClient;
import top.yueshushu.learn.domain.StockSelectedDo;
import top.yueshushu.learn.domainservice.TradePositionHistoryDomainService;
import top.yueshushu.learn.entity.StockSelected;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.enumtype.SelectedType;
import top.yueshushu.learn.mapper.StockSelectedDoMapper;
import top.yueshushu.learn.mode.vo.StockSelectedVo;
import top.yueshushu.learn.mode.ro.TradePositionRo;
import top.yueshushu.learn.mode.vo.TradePositionVo;
import top.yueshushu.learn.domain.TradePositionDo;
import top.yueshushu.learn.mapper.TradePositionMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradePositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.system.SystemConst;
import top.yueshushu.learn.util.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 我的持仓表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-03
 */
@Service
@Slf4j(topic = "持仓信息")
public class TradePositionServiceImpl extends ServiceImpl<TradePositionMapper, TradePositionDo> implements TradePositionService {
    @Autowired
    private TradePositionMapper tradePositionMapper;
    @Resource
    private DefaultResponseParser defaultResponseParser;
    @Autowired
    private TradeUtil tradeUtil;
    @Autowired
    private TradeClient tradeClient;
    @Autowired
    private StockRedisUtil stockRedisUtil;
    @Autowired
    private StockSelectedDoMapper stockSelectedDoMapper;

    @Resource
    private TradePositionHistoryDomainService tradePositionHistoryDomainService;
    @Override
    public OutputResult listPosition(TradePositionRo tradePositionRo) {
        if(null==tradePositionRo.getMockType()){
            return OutputResult.buildAlert("请传入交易盘的类型");
        }
        MockType mockType = MockType.getMockType(tradePositionRo.getMockType());
        if(null==mockType){
            return OutputResult.buildAlert("不支持的交易盘的类型");
        }
        OutputResult outputResult = null;
        if(MockType.MOCK.getCode().equals(mockType.getCode())){
            outputResult = mockList(tradePositionRo);
        }else{
            //接下来，是正式盘的处理方式
            outputResult = realList(tradePositionRo);
        }
        if(!outputResult.getSuccess()){
            return outputResult;
        }
        //获取里面对应的信息
       List<TradePositionVo>  tradePositionVoList= (List<TradePositionVo>) outputResult.getData();
       //最后的结果处理
       List<TradePositionVo> result = new ArrayList<>();
       if(!CollectionUtils.isEmpty(tradePositionVoList)){
           //进行获取，补全相关的信息
           for(TradePositionVo tradePositionVo:tradePositionVoList){
               //进行补充数据
               BigDecimal price = stockRedisUtil.getPrice(tradePositionVo.getCode());
               tradePositionVo.setPrice(price);
               //设置总的市值
               tradePositionVo.setAllMoney(
                       StockUtil.allMoney(
                               tradePositionVo.getAllAmount(),
                               price
                       )
               );
               //设置浮动盈亏
               tradePositionVo.setFloatMoney(
                       StockUtil.floatMoney(
                               tradePositionVo.getAvgPrice(),
                               price,
                               tradePositionVo.getAllAmount()
                       )
               );
               tradePositionVo.setFloatProportion(
                       StockUtil.floatProportion(
                               tradePositionVo.getAvgPrice(),
                               price,
                               tradePositionVo.getAllAmount()
                       )
               );
               tradePositionVo.setSelectType(SelectedType.POSITION.getCode());
           }
           result.addAll(tradePositionVoList);
       }
       if(!SelectedType.POSITION.getCode().equals(tradePositionRo.getSelectType())){
           //持仓的股票信息
           List<String> positionCodeList = result.stream().map(
                   TradePositionVo::getCode
           ).collect(Collectors.toList());

           //查询该员工对应的自选基金
           List<StockSelectedDo> stockInfoList=
                   stockSelectedDoMapper.listByUserIdAndKeyword(tradePositionRo.getUserId(),
                           null);
           //对自选基金进行处理,如果有的话，就不用处理了。
           if(!CollectionUtils.isEmpty(stockInfoList)){
               for(StockSelectedDo stockSelectedVo:stockInfoList){
                   if(positionCodeList.contains(stockSelectedVo.getStockCode())){
                       continue;
                   }
                   //如果没有的话，就进行相关的查询，组装.
                   TradePositionVo tradePositionVo = getTradePositionVoBySelected(
                           null,tradePositionRo.getMockType()
                   );
                   result.add(tradePositionVo);
               } }
       }
       return OutputResult.buildSucc(result);
        //return PageUtil.pageResult(result,tradePositionRo.getPageNum(),tradePositionRo.getPageSize());
    }

    @Override
    public TradePositionDo getPositionByCode(Integer userId,
                                             Integer mockType,
                                             String code) {
        QueryWrapper<TradePositionDo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("mock_type",mockType);
        queryWrapper.eq("code",code);
        //根据用户去查询信息
        List<TradePositionDo> tradePositionDoList = tradePositionMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradePositionDoList)) {
            return null;
        }
        return tradePositionDoList.get(0);
    }

    @Override
    public void syncUseAmountByXxlJob() {
        //更新所有的数据
        tradePositionMapper.syncUseAmountByXxlJob();
    }

    @Override
    public void savePositionHistory(Integer userId, MockType mock) {
        //1. 将今天的记录删除掉
        DateTime now = DateUtil.date();
        tradePositionHistoryDomainService.deleteByUserIdAndMockTypeAndDate(
                userId,mock.getCode(),now
        );

        //查看当前的持仓信息

        // 进行保存



    }

    /**
     * 构建组装成 持仓信息对象
     * @param stockSelectedVo
     * @return
     */
    private TradePositionVo getTradePositionVoBySelected(StockSelectedVo stockSelectedVo,
                                                         int mockType) {
        TradePositionVo tradePositionVo = new TradePositionVo();
        tradePositionVo.setCode(stockSelectedVo.getStockCode());
        tradePositionVo.setName(stockSelectedVo.getStockName());
        tradePositionVo.setAllAmount(0);
        tradePositionVo.setUseAmount(0);
        tradePositionVo.setAvgPrice(
                SystemConst.DEFAULT_EMPTY
        );
        BigDecimal price = stockRedisUtil.getPrice(stockSelectedVo.getStockCode());
        tradePositionVo.setPrice(price);
        tradePositionVo.setAllMoney( SystemConst.DEFAULT_EMPTY);
        tradePositionVo.setFloatMoney( SystemConst.DEFAULT_EMPTY);
        tradePositionVo.setFloatProportion(SystemConst.DEFAULT_EMPTY);
        tradePositionVo.setMockType(mockType);
        tradePositionVo.setSelectType(SelectedType.SELECTED.getCode());
        return tradePositionVo;
    }

    /**
     * 正式盘的处理方式
     * @param tradePositionRo
     * @return
     */
    private OutputResult realList(TradePositionRo tradePositionRo) {
        //获取响应信息
        TradeResultVo<GetStockListResponse> tradeResultVo = getStockListResponse(tradePositionRo.getUserId());
        if (!tradeResultVo.getSuccess()) {
            return OutputResult.buildAlert("查询我的持仓失败");
        }
        List<GetStockListResponse> data = tradeResultVo.getData();

        List<TradePositionVo> tradePositionVoList = new ArrayList<>();
        for(GetStockListResponse getStockListResponse:data){
            TradePositionVo tradePositionVo = new TradePositionVo();
            tradePositionVo.setCode(getStockListResponse.getKysl());
            tradePositionVo.setMockType(MockType.REAL.getCode());
            tradePositionVoList.add(tradePositionVo);
        }
        return OutputResult.buildSucc(tradePositionVoList);
    }

    /**
     * 获取响应的信息
     * @param userId
     * @return
     */
    private TradeResultVo<GetStockListResponse> getStockListResponse(Integer userId) {
        GetStockListRequest request = new GetStockListRequest(userId);
        String url = tradeUtil.getUrl(request);
        Map<String, String> header = tradeUtil.getHeader(request);
        Map<String, Object> params = null;
        params = tradeUtil.getParams(request);
        log.debug("trade {} request: {}", request.getMethod(), params);
        String content = tradeClient.send(url, params, header);
        log.debug("trade {} response: {}", request.getMethod(), content);
        TradeResultVo<GetStockListResponse> result= defaultResponseParser.parse(content,
                new TypeReference<GetStockListResponse>(){});
        return result;
    }

    /**
     * 查询虚拟盘的信息
     * @param tradePositionRo
     * @return
     */
    private OutputResult mockList(TradePositionRo tradePositionRo) {
        QueryWrapper<TradePositionDo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",tradePositionRo.getUserId());
        queryWrapper.eq("mock_type",tradePositionRo.getMockType());
        //根据用户去查询信息
        List<TradePositionDo> tradePositionDoList = tradePositionMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradePositionDoList)) {
            return OutputResult.buildSucc(
                    new ArrayList<TradePositionVo>()
            );
        }
        List<TradePositionVo> tradePositionVoList = new ArrayList<>();
        tradePositionDoList.stream().forEach(
                n->{
                    TradePositionVo tradePositionVo = new TradePositionVo();
                    BeanUtils.copyProperties(n,tradePositionVo);
                    tradePositionVoList.add(tradePositionVo);
                }
        );
        return OutputResult.buildSucc(tradePositionVoList);
    }
}
