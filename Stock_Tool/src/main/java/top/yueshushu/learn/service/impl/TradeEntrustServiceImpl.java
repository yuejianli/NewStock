package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.api.TradeResultVo;
import top.yueshushu.learn.api.request.GetHisOrdersDataRequest;
import top.yueshushu.learn.api.request.GetOrdersDataRequest;
import top.yueshushu.learn.api.response.GetHisOrdersDataResponse;
import top.yueshushu.learn.api.response.GetOrdersDataResponse;
import top.yueshushu.learn.api.responseparse.DefaultResponseParser;
import top.yueshushu.learn.config.TradeClient;
import top.yueshushu.learn.enumtype.EntrustStatusType;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.mode.ro.TradeEntrustRo;
import top.yueshushu.learn.mode.vo.TradeEntrustVo;
import top.yueshushu.learn.domain.TradeEntrustDo;
import top.yueshushu.learn.mapper.TradeEntrustMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradeEntrustService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.util.TradeUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-03
 */
@Service
@Slf4j
public class TradeEntrustServiceImpl extends ServiceImpl<TradeEntrustMapper, TradeEntrustDo> implements TradeEntrustService {
    @Autowired
    private TradeEntrustMapper tradeEntrustMapper;
    @Resource
    private DefaultResponseParser defaultResponseParser;
    @Autowired
    private TradeUtil tradeUtil;
    @Autowired
    private TradeClient tradeClient;
    @Override
    public OutputResult listEntrust(TradeEntrustRo tradeEntrustRo) {
        if(null==tradeEntrustRo.getMockType()){
            return OutputResult.buildAlert("请传入交易盘的类型");
        }
        MockType mockType = MockType.getMockType(tradeEntrustRo.getMockType());
        if(null==mockType){
            return OutputResult.buildAlert("不支持的交易盘的类型");
        }
        if(MockType.MOCK.getCode().equals(mockType.getCode())){
            return mockList(tradeEntrustRo);
        }
        //接下来，是正式盘的处理方式
        return realList(tradeEntrustRo);
    }

    @Override
    public OutputResult history(TradeEntrustRo tradeEntrustRo) {
        if(null==tradeEntrustRo.getMockType()){
            return OutputResult.buildAlert("请传入交易盘的类型");
        }
        MockType mockType = MockType.getMockType(tradeEntrustRo.getMockType());
        if(null==mockType){
            return OutputResult.buildAlert("不支持的交易盘的类型");
        }
        if(MockType.MOCK.getCode().equals(mockType.getCode())){
            return mockHistoryList(tradeEntrustRo);
        }
        //接下来，是正式盘的处理方式
        return realHistoryList(tradeEntrustRo);
    }

    @Override
    public List<TradeEntrustDo> listNowRunEntruct(Integer userId, Integer mockType) {
        Date now = DateUtil.date();
        Date beginNow = DateUtil.beginOfDay(now);
        QueryWrapper<TradeEntrustDo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("mock_type",mockType);
        queryWrapper.gt("entrust_date",beginNow);
        queryWrapper.eq("entrust_status", EntrustStatusType.ING.getCode());
        queryWrapper.orderByDesc("id");
        //根据用户去查询信息
        List<TradeEntrustDo> tradeEntrustDoList = tradeEntrustMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradeEntrustDoList)) {
            return new ArrayList<TradeEntrustDo>();
        }
        return tradeEntrustDoList;
    }

    /**
     * 正式盘的处理方式
     * @param tradeEntrustRo
     * @return
     */
    private OutputResult realList(TradeEntrustRo tradeEntrustRo) {
        //获取响应信息
        TradeResultVo<GetOrdersDataResponse> tradeResultVo = getOrdersDataResponse(tradeEntrustRo.getUserId());
        if (!tradeResultVo.getSuccess()) {
            return OutputResult.buildAlert("查询我的当日委托单失败");
        }
        List<GetOrdersDataResponse> data = tradeResultVo.getData();

        List<TradeEntrustVo> tradeEntrustVoList = new ArrayList<>();
        for(GetOrdersDataResponse getOrdersDataResponse:data){
            TradeEntrustVo tradeEntrustVo = new TradeEntrustVo();
            tradeEntrustVo.setCode(getOrdersDataResponse.getMmlb());
            tradeEntrustVoList.add(tradeEntrustVo);
        }
        return OutputResult.buildSucc(tradeEntrustVoList);
    }

    /**
     * 获取响应的信息
     * @param userId
     * @return
     */
    private TradeResultVo<GetOrdersDataResponse> getOrdersDataResponse(Integer userId) {
        GetOrdersDataRequest request = new GetOrdersDataRequest(userId);
        String url = tradeUtil.getUrl(request);
        Map<String, String> header = tradeUtil.getHeader(request);
        Map<String, Object> params = null;
        params = tradeUtil.getParams(request);
        log.debug("trade {} request: {}", request.getMethod(), params);
        String content = tradeClient.send(url, params, header);
        log.debug("trade {} response: {}", request.getMethod(), content);
        TradeResultVo<GetOrdersDataResponse> result= defaultResponseParser.parse(content,
                new TypeReference<GetOrdersDataResponse>(){});
        return result;
    }

    /**
     * 查询虚拟盘的信息
     * @param tradeEntrustRo
     * @return
     */
    private OutputResult mockList(TradeEntrustRo tradeEntrustRo) {
        Date now = DateUtil.date();
        Date beginNow = DateUtil.beginOfDay(now);
        QueryWrapper<TradeEntrustDo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",tradeEntrustRo.getUserId());
        queryWrapper.eq("mock_type",tradeEntrustRo.getMockType());
        queryWrapper.gt("entrust_date",beginNow);
        queryWrapper.orderByDesc("id");
        //根据用户去查询信息
        List<TradeEntrustDo> tradeEntrustDoList = tradeEntrustMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradeEntrustDoList)) {
           return OutputResult.buildSucc(
                   new ArrayList<TradeEntrustVo>()
           );
        }
        List<TradeEntrustVo> tradePositionVoList = new ArrayList<>();
        tradeEntrustDoList.stream().forEach(
                n->{
                    TradeEntrustVo tradeEntrustVo = new TradeEntrustVo();
                    BeanUtils.copyProperties(n,tradeEntrustVo);
                    tradePositionVoList.add(tradeEntrustVo);
                }
        );
        return OutputResult.buildSucc(tradePositionVoList);
    }


    /**********对历史记录的处理*************/
    /**
     * 正式盘的历史处理方式
     * @param tradeEntrustRo
     * @return
     */
    private OutputResult realHistoryList(TradeEntrustRo tradeEntrustRo) {
        //获取响应信息
        TradeResultVo<GetHisOrdersDataResponse> tradeResultVo =
                getHistoryOrdersDataResponse(tradeEntrustRo.getUserId());
        if (!tradeResultVo.getSuccess()) {
            return OutputResult.buildAlert("查询我的历史委托单失败");
        }
        List<GetHisOrdersDataResponse> data = tradeResultVo.getData();

        List<TradeEntrustVo> tradeEntrustVoList = new ArrayList<>();
        for(GetHisOrdersDataResponse getOrdersDataResponse:data){
            TradeEntrustVo tradeEntrustVo = new TradeEntrustVo();
            tradeEntrustVo.setCode(getOrdersDataResponse.getMmlb());
            tradeEntrustVoList.add(tradeEntrustVo);
        }
        return OutputResult.buildSucc(tradeEntrustVoList);
    }

    /**
     * 获取历史响应信息
     * @param userId
     * @return
     */
    private TradeResultVo<GetHisOrdersDataResponse> getHistoryOrdersDataResponse(Integer userId) {
        GetHisOrdersDataRequest request = new GetHisOrdersDataRequest(userId);
        request.setEt(DateUtil.format(new Date(), "yyyy-MM-dd"));
        Date et = new Date();
        et.setTime(et.getTime() - 7 * 24 * 3600 * 1000);
        request.setSt(DateUtil.format(et, "yyyy-MM-dd"));
        String url = tradeUtil.getUrl(request);
        Map<String, String> header = tradeUtil.getHeader(request);
        Map<String, Object> params = null;
        params = tradeUtil.getParams(request);
        log.debug("trade {} request: {}", request.getMethod(), params);
        String content = tradeClient.send(url, params, header);
        log.debug("trade {} response: {}", request.getMethod(), content);
        TradeResultVo<GetHisOrdersDataResponse> result= defaultResponseParser.parse(content,
                new TypeReference<GetHisOrdersDataResponse>(){});
        return result;
    }

    /**
     * 查询虚拟盘的信息
     * @param tradeEntrustRo
     * @return
     */
    private OutputResult mockHistoryList(TradeEntrustRo tradeEntrustRo) {
        Date now = DateUtil.date();
        Date beginNow = DateUtil.beginOfDay(now);
        //获取14天前的日期
        Date before14Day = DateUtil.offsetDay(beginNow,-14);
        QueryWrapper<TradeEntrustDo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",tradeEntrustRo.getUserId());
        queryWrapper.eq("mock_type",tradeEntrustRo.getMockType());
        queryWrapper.gt("entrust_date",before14Day);
        queryWrapper.lt("entrust_date",now);
        queryWrapper.orderByDesc("id");
        //根据用户去查询信息
        List<TradeEntrustDo> tradeEntrustDoList = tradeEntrustMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradeEntrustDoList)) {
            return OutputResult.buildSucc(
                    new ArrayList<TradeEntrustVo>()
            );
        }
        List<TradeEntrustVo> tradePositionVoList = new ArrayList<>();
        tradeEntrustDoList.stream().forEach(
                n->{
                    TradeEntrustVo tradeEntrustVo = new TradeEntrustVo();
                    BeanUtils.copyProperties(n,tradeEntrustVo);
                    tradePositionVoList.add(tradeEntrustVo);
                }
        );
        return OutputResult.buildSucc(tradePositionVoList);
    }
}
