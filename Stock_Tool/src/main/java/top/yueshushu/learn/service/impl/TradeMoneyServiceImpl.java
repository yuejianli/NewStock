package top.yueshushu.learn.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.api.TradeResultVo;
import top.yueshushu.learn.api.request.BaseTradeListRequest;
import top.yueshushu.learn.api.request.GetAssetsRequest;
import top.yueshushu.learn.api.request.GetStockListRequest;
import top.yueshushu.learn.api.response.GetAssetsResponse;
import top.yueshushu.learn.api.response.GetStockListResponse;
import top.yueshushu.learn.api.responseparse.DefaultResponseParser;
import top.yueshushu.learn.api.responseparse.ResponseParser;
import top.yueshushu.learn.config.TradeClient;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.mode.ro.TradeMoneyRo;
import top.yueshushu.learn.mode.vo.TradeMoneyVo;
import top.yueshushu.learn.pojo.TradeMoney;
import top.yueshushu.learn.mapper.TradeMoneyMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.TradeApiService;
import top.yueshushu.learn.service.TradeMoneyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.util.TradeUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 资金表 自定义的
 * </p>
 *
 * @author 两个蝴蝶飞
 * @since 2022-01-03
 */
@Service
@Slf4j
public class TradeMoneyServiceImpl extends ServiceImpl<TradeMoneyMapper, TradeMoney> implements TradeMoneyService {
    @Autowired
    private TradeMoneyMapper tradeMoneyMapper;
    @Resource
    private DefaultResponseParser defaultResponseParser;
    @Autowired
    private TradeUtil tradeUtil;
    @Autowired
    private TradeClient tradeClient;
    @Override
    public OutputResult listMoney(TradeMoneyRo tradeMoneyRo) {
        if(null==tradeMoneyRo.getMockType()){
            return OutputResult.alert("请传入交易盘的类型");
        }
        MockType mockType = MockType.getMockType(tradeMoneyRo.getMockType());
        if(null==mockType){
            return OutputResult.alert("不支持的交易盘的类型");
        }
        if(MockType.MOCK.getCode().equals(mockType.getCode())){
            return mockList(tradeMoneyRo);
        }
        //接下来，是正式盘的处理方式
        return realList(tradeMoneyRo);
    }

    @Override
    public void updateMoneyVoByid(TradeMoneyVo tradeMoneyVo) {
        TradeMoney tradeMoney = new TradeMoney();
        BeanUtils.copyProperties(tradeMoneyVo,tradeMoney);
        //进行修改
        tradeMoneyMapper.updateById(tradeMoney);
    }

    @Override
    public TradeMoney getByUid(Integer userId, Integer mockType) {

        QueryWrapper<TradeMoney> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("mock_type",mockType);
        //根据用户去查询信息
        List<TradeMoney> tradeMoneyList = tradeMoneyMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradeMoneyList)) {
            return null;
        }
        return tradeMoneyList.get(0);
    }

    @Override
    public void updateMoneyVoByid(TradeMoney tradeMoney) {
        tradeMoneyMapper.updateById(tradeMoney);
    }

    /**
     * 正式盘的处理方式
     * @param tradeMoneyRo
     * @return
     */
    private OutputResult realList(TradeMoneyRo tradeMoneyRo) {
        //获取响应信息
        TradeResultVo<GetAssetsResponse> tradeResultVo = getAssetsResponse(tradeMoneyRo.getUserId());
        TradeMoneyVo tradeMoneyVo = new TradeMoneyVo();
        if (!tradeResultVo.getSuccess()) {
          return OutputResult.alert("查询总资产失败");
        }
        List<GetAssetsResponse> data = tradeResultVo.getData();
        GetAssetsResponse response = data.get(0);
        tradeMoneyVo.setUseMoney(new BigDecimal(response.getKyzj()));
        tradeMoneyVo.setMarketMoney(new BigDecimal(response.getDjzj()));
        tradeMoneyVo.setTotalMoney(new BigDecimal(response.getZzc()));
        tradeMoneyVo.setTakeoutMoney(new BigDecimal(response.getKqzj()));
        tradeMoneyVo.setMockType(MockType.REAL.getCode());
        return OutputResult.success(tradeMoneyVo);
    }

    /**
     * 获取响应的信息
     * @param userId
     * @return
     */
    private TradeResultVo<GetAssetsResponse> getAssetsResponse(Integer userId) {
        GetAssetsRequest request = new GetAssetsRequest(userId);
        String url = tradeUtil.getUrl(request);
        Map<String, String> header = tradeUtil.getHeader(request);
        Map<String, Object> params = null;
        params = tradeUtil.getParams(request);
        log.debug("trade {} request: {}", request.getMethod(), params);
        String content = tradeClient.send(url, params, header);
        log.debug("trade {} response: {}", request.getMethod(), content);
        TradeResultVo<GetAssetsResponse> result= defaultResponseParser.parse(content,
                new TypeReference<GetAssetsResponse>(){});
        return result;
    }

    /**
     * 查询虚拟盘的信息
     * @param tradeMoneyRo
     * @return
     */
    private OutputResult mockList(TradeMoneyRo tradeMoneyRo) {
        QueryWrapper<TradeMoney> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",tradeMoneyRo.getUserId());
        queryWrapper.eq("mock_type",tradeMoneyRo.getMockType());
        //根据用户去查询信息
        List<TradeMoney> tradeMoneyList = tradeMoneyMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(tradeMoneyList)) {
            return OutputResult.alert("暂无该员工的虚拟盘资金信息");
        }
        TradeMoney tradeMoney = tradeMoneyList.get(0);
        TradeMoneyVo tradeMoneyVo = new TradeMoneyVo();
        BeanUtils.copyProperties(tradeMoney,tradeMoneyVo);
        return OutputResult.success(tradeMoneyVo);
    }
}
