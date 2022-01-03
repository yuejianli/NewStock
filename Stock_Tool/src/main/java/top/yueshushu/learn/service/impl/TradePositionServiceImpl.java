package top.yueshushu.learn.service.impl;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.api.TradeResultVo;
import top.yueshushu.learn.api.request.GetAssetsRequest;
import top.yueshushu.learn.api.request.GetStockListRequest;
import top.yueshushu.learn.api.response.GetAssetsResponse;
import top.yueshushu.learn.api.response.GetStockListResponse;
import top.yueshushu.learn.api.responseparse.DefaultResponseParser;
import top.yueshushu.learn.config.TradeClient;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.mode.ro.TradeMoneyRo;
import top.yueshushu.learn.mode.ro.TradePositionRo;
import top.yueshushu.learn.mode.vo.TradeMoneyVo;
import top.yueshushu.learn.mode.vo.TradePositionVo;
import top.yueshushu.learn.pojo.TradeMoney;
import top.yueshushu.learn.pojo.TradePosition;
import top.yueshushu.learn.mapper.TradePositionMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradePositionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.util.TradeUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 我的持仓表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-03
 */
@Service
@Log4j2
public class TradePositionServiceImpl extends ServiceImpl<TradePositionMapper, TradePosition> implements TradePositionService {
    @Autowired
    private TradePositionMapper tradePositionMapper;
    @Resource
    private DefaultResponseParser defaultResponseParser;
    @Autowired
    private TradeUtil tradeUtil;
    @Autowired
    private TradeClient tradeClient;
    @Override
    public OutputResult listPosition(TradePositionRo tradePositionRo) {
        if(null==tradePositionRo.getMockType()){
            return OutputResult.alert("请传入交易盘的类型");
        }
        MockType mockType = MockType.getMockType(tradePositionRo.getMockType());
        if(null==mockType){
            return OutputResult.alert("不支持的交易盘的类型");
        }
        if(MockType.MOCK.getCode().equals(mockType.getCode())){
            return mockList(tradePositionRo);
        }
        //接下来，是正式盘的处理方式
        return realList(tradePositionRo);
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
            return OutputResult.alert("查询我的持仓失败");
        }
        List<GetStockListResponse> data = tradeResultVo.getData();

        List<TradePositionVo> tradePositionVoList = new ArrayList<>();
        for(GetStockListResponse getStockListResponse:data){
            TradePositionVo tradePositionVo = new TradePositionVo();
            tradePositionVo.setCode(getStockListResponse.getKysl());
            tradePositionVoList.add(tradePositionVo);
        }
        return OutputResult.success(tradePositionVoList);
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
        QueryWrapper<TradePosition> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",tradePositionRo.getUserId());
        queryWrapper.eq("mock_type",tradePositionRo.getMockType());
        //根据用户去查询信息
        List<TradePosition> tradePositionList = tradePositionMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradePositionList)) {
            return OutputResult.alert("暂无该员工的虚拟盘资金信息");
        }
        List<TradePositionVo> tradePositionVoList = new ArrayList<>();
        tradePositionList.stream().forEach(
                n->{
                    TradePositionVo tradePositionVo = new TradePositionVo();
                    BeanUtils.copyProperties(n,tradePositionVo);
                    tradePositionVoList.add(tradePositionVo);
                }
        );
        return OutputResult.success(tradePositionVoList);
    }
}
