package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.api.TradeResultVo;
import top.yueshushu.learn.api.request.GetDealDataRequest;
import top.yueshushu.learn.api.request.GetHisDealDataRequest;
import top.yueshushu.learn.api.response.GetDealDataResponse;
import top.yueshushu.learn.api.response.GetHisDealDataResponse;
import top.yueshushu.learn.api.responseparse.DefaultResponseParser;
import top.yueshushu.learn.config.TradeClient;
import top.yueshushu.learn.enumtype.DataFlagType;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.mode.ro.TradeDealRo;
import top.yueshushu.learn.mode.vo.TradeDealVo;
import top.yueshushu.learn.domain.TradeDealDo;
import top.yueshushu.learn.mapper.TradeDealMapper;
import top.yueshushu.learn.domain.TradeEntrustDo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradeDealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.util.BigDecimalUtil;
import top.yueshushu.learn.util.StockUtil;
import top.yueshushu.learn.util.TradeUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 成交表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-03
 */
@Service
@Slf4j
public class TradeDealServiceImpl extends ServiceImpl<TradeDealMapper, TradeDealDo> implements TradeDealService {
    @Autowired
    private TradeDealMapper tradeDealMapper;
    @Resource
    private DefaultResponseParser defaultResponseParser;
    @Autowired
    private TradeUtil tradeUtil;
    @Autowired
    private TradeClient tradeClient;
    @Override
    public OutputResult listDeal(TradeDealRo tradeDealRo) {
        if(null==tradeDealRo.getMockType()){
            return OutputResult.buildAlert("请传入交易盘的类型");
        }
        MockType mockType = MockType.getMockType(tradeDealRo.getMockType());
        if(null==mockType){
            return OutputResult.buildAlert("不支持的交易盘的类型");
        }
        if(MockType.MOCK.getCode().equals(mockType.getCode())){
            return mockList(tradeDealRo);
        }
        //接下来，是正式盘的处理方式
        return realList(tradeDealRo);

    }

    @Override
    public void addDealRecord(TradeEntrustDo tradeEntrustDo) {
        TradeDealDo tradeDealDo = new TradeDealDo();
        tradeDealDo.setCode(tradeEntrustDo.getCode());
        tradeDealDo.setName(tradeEntrustDo.getName());
        tradeDealDo.setDealDate(DateUtil.date());
        tradeDealDo.setDealType(tradeEntrustDo.getDealType());
        tradeDealDo.setDealNum(tradeEntrustDo.getEntrustNum());
        tradeDealDo.setDealPrice(tradeEntrustDo.getEntrustPrice());
        tradeDealDo.setDealMoney(
                BigDecimalUtil.toBigDecimal(
                        tradeEntrustDo.getEntrustPrice(),
                        new BigDecimal(
                                tradeEntrustDo.getEntrustNum()
                        )
                )
        );
        tradeDealDo.setDealCode(
                StockUtil.generateDealCode()
        );
        tradeDealDo.setEntrustCode(tradeEntrustDo.getEntrustCode());
        tradeDealDo.setUserId(tradeEntrustDo.getUserId());
        tradeDealDo.setMockType(tradeEntrustDo.getMockType());
        tradeDealDo.setFlag(DataFlagType.NORMAL.getCode());
        tradeDealMapper.insert(tradeDealDo);
    }

    @Override
    public OutputResult history(TradeDealRo tradeDealRo) {
        if(null==tradeDealRo.getMockType()){
            return OutputResult.buildAlert("请传入交易盘的类型");
        }
        MockType mockType = MockType.getMockType(tradeDealRo.getMockType());
        if(null==mockType){
            return OutputResult.buildAlert("不支持的交易盘的类型");
        }
        if(MockType.MOCK.getCode().equals(mockType.getCode())){
            return mockHistoryList(tradeDealRo);
        }
        //接下来，是正式盘的处理方式
        return realHistoryList(tradeDealRo);
    }

    /**
     * 正式盘的处理方式
     * @param tradeDealRo
     * @return
     */
    private OutputResult realList(TradeDealRo tradeDealRo) {
        //获取响应信息
        TradeResultVo<GetDealDataResponse> tradeResultVo = getDealDataResponse(tradeDealRo.getUserId());
        if (!tradeResultVo.getSuccess()) {
            return OutputResult.buildAlert("查询我的持仓失败");
        }
        List<GetDealDataResponse> data = tradeResultVo.getData();

        List<TradeDealVo> tradeDealVoList = new ArrayList<>();
        for(GetDealDataResponse getDealDataResponse:data){
            TradeDealVo tradeDealVo = new TradeDealVo();
            tradeDealVo.setCode(getDealDataResponse.getCjbh());
            tradeDealVoList.add(tradeDealVo);
        }
        return OutputResult.buildSucc(tradeDealVoList);
    }

    /**
     * 获取响应的信息
     * @param userId
     * @return
     */
    private TradeResultVo<GetDealDataResponse> getDealDataResponse(Integer userId) {
        GetDealDataRequest request = new GetDealDataRequest(userId);
        String url = tradeUtil.getUrl(request);
        Map<String, String> header = tradeUtil.getHeader(request);

        List<Map<String, Object>> paramList = null;
        Map<String, Object> params = null;
        params = tradeUtil.getParams(request);
        log.debug("trade {} request: {}", request.getMethod(), params);
        String content = tradeClient.send(url, params, header);
        log.debug("trade {} response: {}", request.getMethod(), content);
        TradeResultVo<GetDealDataResponse> result= defaultResponseParser.parse(content,
                new TypeReference<GetDealDataResponse>(){});
        return result;
    }

    /**
     * 查询虚拟盘的信息
     * @param tradeDealRo
     * @return
     */
    private OutputResult mockList(TradeDealRo tradeDealRo) {
        Date now = DateUtil.date();
        Date beginNow = DateUtil.beginOfDay(now);
        QueryWrapper<TradeDealDo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",tradeDealRo.getUserId());
        queryWrapper.eq("mock_type",tradeDealRo.getMockType());
        queryWrapper.gt("deal_date",beginNow);
        queryWrapper.orderByDesc("id");
        //根据用户去查询信息
        List<TradeDealDo> tradeDealDoList = tradeDealMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradeDealDoList)) {
            return OutputResult.buildSucc(new ArrayList<TradeDealVo>());
        }
        List<TradeDealVo> tradeDealVoList = new ArrayList<>();
        tradeDealDoList.stream().forEach(
                n->{
                    TradeDealVo tradeDealVo = new TradeDealVo();
                    BeanUtils.copyProperties(n,tradeDealVo);
                    tradeDealVoList.add(tradeDealVo);
                }
        );
        return OutputResult.buildSucc(tradeDealVoList);
    }


    /**
     * 正式盘的处理方式
     * @param tradeDealRo
     * @return
     */
    private OutputResult realHistoryList(TradeDealRo tradeDealRo) {
        //获取响应信息
        TradeResultVo<GetHisDealDataResponse> tradeResultVo = getHisDealDataResponse(tradeDealRo.getUserId());
        if (!tradeResultVo.getSuccess()) {
            return OutputResult.buildAlert("查询我的持仓失败");
        }
        List<GetHisDealDataResponse> data = tradeResultVo.getData();

        List<TradeDealVo> tradeDealVoList = new ArrayList<>();
        for(GetHisDealDataResponse getDealDataResponse:data){
            TradeDealVo tradeDealVo = new TradeDealVo();
            tradeDealVo.setCode(getDealDataResponse.getCjbh());
            tradeDealVoList.add(tradeDealVo);
        }
        return OutputResult.buildSucc(tradeDealVoList);
    }

    /**
     * 获取响应的信息
     * @param userId
     * @return
     */
    private TradeResultVo<GetHisDealDataResponse> getHisDealDataResponse(Integer userId) {
        GetHisDealDataRequest request = new GetHisDealDataRequest(userId);
        request.setEt(DateUtil.format(new Date(), "yyyy-MM-dd"));
        Date et = new Date();
        et.setTime(et.getTime() - 7 * 24 * 3600 * 1000);
        request.setSt(DateUtil.format(et, "yyyy-MM-dd"));
        String url = tradeUtil.getUrl(request);
        Map<String, String> header = tradeUtil.getHeader(request);
        List<Map<String, Object>> paramList = null;
        Map<String, Object> params = null;
        params = tradeUtil.getParams(request);
        log.debug("trade {} request: {}", request.getMethod(), params);
        String content = tradeClient.send(url, params, header);
        log.debug("trade {} response: {}", request.getMethod(), content);
        TradeResultVo<GetHisDealDataResponse> result= defaultResponseParser.parse(content,
                new TypeReference<GetHisDealDataResponse>(){});
        return result;
    }

    /**
     * 查询虚拟盘的信息
     * @param tradeDealRo
     * @return
     */
    private OutputResult mockHistoryList(TradeDealRo tradeDealRo) {
        Date now = DateUtil.date();
        Date beginNow = DateUtil.beginOfDay(now);
        //获取14天前的日期
        Date before14Day = DateUtil.offsetDay(beginNow,-14);
        QueryWrapper<TradeDealDo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",tradeDealRo.getUserId());
        queryWrapper.eq("mock_type",tradeDealRo.getMockType());
        queryWrapper.gt("deal_date",before14Day);
        queryWrapper.lt("deal_date",now);
        queryWrapper.orderByDesc("id");
        //根据用户去查询信息
        List<TradeDealDo> tradeDealDoList = tradeDealMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradeDealDoList)) {
            return OutputResult.buildSucc(new ArrayList<TradeDealVo>());
        }
        List<TradeDealVo> tradeDealVoList = new ArrayList<>();
        tradeDealDoList.stream().forEach(
                n->{
                    TradeDealVo tradeDealVo = new TradeDealVo();
                    BeanUtils.copyProperties(n,tradeDealVo);
                    tradeDealVoList.add(tradeDealVo);
                }
        );
        return OutputResult.buildSucc(tradeDealVoList);
    }
}
