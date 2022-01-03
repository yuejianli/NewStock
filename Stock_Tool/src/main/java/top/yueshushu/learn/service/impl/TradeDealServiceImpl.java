package top.yueshushu.learn.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.api.TradeResultVo;
import top.yueshushu.learn.api.request.GetStockListRequest;
import top.yueshushu.learn.api.response.GetDealDataResponse;
import top.yueshushu.learn.api.response.GetStockListResponse;
import top.yueshushu.learn.api.responseparse.DefaultResponseParser;
import top.yueshushu.learn.config.TradeClient;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.mapper.TradePositionMapper;
import top.yueshushu.learn.mode.ro.TradeDealRo;
import top.yueshushu.learn.mode.ro.TradePositionRo;
import top.yueshushu.learn.mode.vo.TradeDealVo;
import top.yueshushu.learn.mode.vo.TradeMoneyVo;
import top.yueshushu.learn.mode.vo.TradePositionVo;
import top.yueshushu.learn.pojo.TradeDeal;
import top.yueshushu.learn.mapper.TradeDealMapper;
import top.yueshushu.learn.pojo.TradePosition;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradeDealService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.util.TradeUtil;

import javax.annotation.Resource;
import java.util.ArrayList;
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
@Log4j2
public class TradeDealServiceImpl extends ServiceImpl<TradeDealMapper, TradeDeal> implements TradeDealService {
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
            return OutputResult.alert("请传入交易盘的类型");
        }
        MockType mockType = MockType.getMockType(tradeDealRo.getMockType());
        if(null==mockType){
            return OutputResult.alert("不支持的交易盘的类型");
        }
        if(MockType.MOCK.getCode().equals(mockType.getCode())){
            return mockList(tradeDealRo);
        }
        //接下来，是正式盘的处理方式
        return realList(tradeDealRo);

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
            return OutputResult.alert("查询我的持仓失败");
        }
        List<GetDealDataResponse> data = tradeResultVo.getData();

        List<TradeDealVo> tradeDealVoList = new ArrayList<>();
        for(GetDealDataResponse getDealDataResponse:data){
            TradeDealVo tradeDealVo = new TradeDealVo();
            tradeDealVo.setCode(getDealDataResponse.getCjbh());
            tradeDealVoList.add(tradeDealVo);
        }
        return OutputResult.success(tradeDealVoList);
    }

    /**
     * 获取响应的信息
     * @param userId
     * @return
     */
    private TradeResultVo<GetDealDataResponse> getDealDataResponse(Integer userId) {
        GetStockListRequest request = new GetStockListRequest(userId);
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
        QueryWrapper<TradeDeal> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",tradeDealRo.getUserId());
        queryWrapper.eq("mock_type",tradeDealRo.getMockType());
        //根据用户去查询信息
        List<TradeDeal> tradeDealList = tradeDealMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradeDealList)) {
            return OutputResult.alert("暂无该员工的虚拟盘资金信息");
        }
        List<TradeDealVo> tradeDealVoList = new ArrayList<>();
        tradeDealList.stream().forEach(
                n->{
                    TradeDealVo tradeDealVo = new TradeDealVo();
                    BeanUtils.copyProperties(n,tradeDealVo);
                    tradeDealVoList.add(tradeDealVo);
                }
        );
        return OutputResult.success(tradeDealVoList);
    }
}
