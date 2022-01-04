package top.yueshushu.learn.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.api.TradeResultVo;
import top.yueshushu.learn.api.request.GetOrdersDataRequest;
import top.yueshushu.learn.api.request.GetStockListRequest;
import top.yueshushu.learn.api.response.GetOrdersDataResponse;
import top.yueshushu.learn.api.response.GetStockListResponse;
import top.yueshushu.learn.api.responseparse.DefaultResponseParser;
import top.yueshushu.learn.config.TradeClient;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.mapper.TradePositionMapper;
import top.yueshushu.learn.mode.ro.TradeEntrustRo;
import top.yueshushu.learn.mode.ro.TradePositionRo;
import top.yueshushu.learn.mode.vo.TradeEntrustVo;
import top.yueshushu.learn.mode.vo.TradeMoneyVo;
import top.yueshushu.learn.mode.vo.TradePositionVo;
import top.yueshushu.learn.pojo.TradeEntrust;
import top.yueshushu.learn.mapper.TradeEntrustMapper;
import top.yueshushu.learn.pojo.TradePosition;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradeEntrustService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.util.TradeUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
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
@Log4j2
public class TradeEntrustServiceImpl extends ServiceImpl<TradeEntrustMapper, TradeEntrust> implements TradeEntrustService {
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
            return OutputResult.alert("请传入交易盘的类型");
        }
        MockType mockType = MockType.getMockType(tradeEntrustRo.getMockType());
        if(null==mockType){
            return OutputResult.alert("不支持的交易盘的类型");
        }
        if(MockType.MOCK.getCode().equals(mockType.getCode())){
            return mockList(tradeEntrustRo);
        }
        //接下来，是正式盘的处理方式
        return realList(tradeEntrustRo);
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
            return OutputResult.alert("查询我的当日委托单失败");
        }
        List<GetOrdersDataResponse> data = tradeResultVo.getData();

        List<TradeEntrustVo> tradeEntrustVoList = new ArrayList<>();
        for(GetOrdersDataResponse getOrdersDataResponse:data){
            TradeEntrustVo tradeEntrustVo = new TradeEntrustVo();
            tradeEntrustVo.setCode(getOrdersDataResponse.getMmlb());
            tradeEntrustVoList.add(tradeEntrustVo);
        }
        return OutputResult.success(tradeEntrustVoList);
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
        QueryWrapper<TradeEntrust> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",tradeEntrustRo.getUserId());
        queryWrapper.eq("mock_type",tradeEntrustRo.getMockType());
        //根据用户去查询信息
        List<TradeEntrust> tradeEntrustList = tradeEntrustMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradeEntrustList)) {
            return OutputResult.alert("暂无该员工的虚拟盘资金信息");
        }
        List<TradeEntrustVo> tradePositionVoList = new ArrayList<>();
        tradeEntrustList.stream().forEach(
                n->{
                    TradeEntrustVo tradeEntrustVo = new TradeEntrustVo();
                    BeanUtils.copyProperties(n,tradeEntrustVo);
                    tradePositionVoList.add(tradeEntrustVo);
                }
        );
        return OutputResult.success(tradePositionVoList);
    }
}
