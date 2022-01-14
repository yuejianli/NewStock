package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.common.XxlJobConst;
import top.yueshushu.learn.enumtype.ConfigCodeType;
import top.yueshushu.learn.enumtype.DataFlagType;
import top.yueshushu.learn.enumtype.ExchangeType;
import top.yueshushu.learn.enumtype.SyncStockHistoryType;
import top.yueshushu.learn.mode.dto.StockPriceCacheDto;
import top.yueshushu.learn.mode.ro.IdRo;
import top.yueshushu.learn.mode.ro.StockSelectedVo;
import top.yueshushu.learn.mode.vo.StockSelectedRo;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.pojo.Config;
import top.yueshushu.learn.pojo.Stock;
import top.yueshushu.learn.pojo.StockSelected;
import top.yueshushu.learn.mapper.StockSelectedMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.util.StockRedisUtil;
import top.yueshushu.learn.util.StockUtil;

import java.util.*;

/**
 * <p>
 * 股票自选表,是用户自己选择的 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
@Log4j2(topic = "自选股票")
public class StockSelectedServiceImpl extends ServiceImpl<StockSelectedMapper, StockSelected> implements StockSelectedService {
    @Autowired
    private StockSelectedMapper stockSelectedMapper;
    @Autowired
    private StockService stockService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private XxlJobService xxlJobService;
    @Autowired
    private StockRedisUtil stockRedisUtil;
    @Autowired
    private StockCrawlerService stockCrawlerService;
    @Autowired
    private StockHistoryService stockHistoryService;
    @Override
    public OutputResult add(StockSelectedRo stockSelectedRo) {
        //看是否已经加入自选，如果已经添加，则不需要添加.
        QueryWrapper<StockSelected> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stock_code",stockSelectedRo.getStockCode());
        queryWrapper.eq("user_id",stockSelectedRo.getUserId());

        StockSelected stockSelected = getSelectedByWrapper(queryWrapper);
        if(stockSelected!=null&&
                DataFlagType.NORMAL.getCode().equals(
                        stockSelected.getFlag()
                )){
            return OutputResult.alert("已经存在自选表里面，不需要重复添加");
        }
        //看是否超过最大的数量
        if(checkMaxCount(stockSelectedRo.getUserId())){
            return OutputResult.alert("已经超过允许自选的最大数量");
        }
        if(stockSelected==null){
            log.info("没有股票自选记录 {}，进行添加",stockSelectedRo.getStockCode());
            stockSelected = new StockSelected();
            Integer jobId = addJob(stockSelectedRo);
            Stock stock = stockService.selectByCode(stockSelectedRo.getStockCode());
            stockSelected.setStockCode(stockSelectedRo.getStockCode());
            stockSelected.setStockName(
                    stock==null?"":stock.getName()
            );
            stockSelected.setUserId(stockSelectedRo.getUserId());
            stockSelected.setCreateTime(DateUtil.date());
            stockSelected.setJobId(jobId);
            stockSelected.setFlag(DataFlagType.NORMAL.getCode());
            stockSelectedMapper.insert(stockSelected);
            //设置历史记录
            //获取相关的信息，进行处理。
            List<String> codeList = new ArrayList<>();
            codeList.add(stockSelectedRo.getStockCode());
            List<StockPriceCacheDto> priceCacheDtoList = stockHistoryService.listClosePrice(codeList);
            //循环设置缓存信息
            if(CollectionUtils.isEmpty(priceCacheDtoList)){
                log.error(">>>未查询出昨天的价格记录，对应的股票信息是:{}",codeList);
            }
            for(StockPriceCacheDto priceCacheDto:priceCacheDtoList){
                stockRedisUtil.setYesPrice(priceCacheDto.getCode(),priceCacheDto.getPrice());
            }
        }else{
            log.info("有股票自选记录 {}，进行修改",stockSelectedRo.getStockCode());
            stockSelected.setCreateTime(DateUtil.date());
            stockSelected.setFlag(DataFlagType.NORMAL.getCode());
            //进行更新
            stockSelectedMapper.updateById(stockSelected);
            //将任务启动
            xxlJobService.enableJob(stockSelected.getJobId());
        }
        return OutputResult.success();
    }


    /**
     * 看是否超过最大的数量，如果超过，则返回 true, 不允许添加。
     * 如果没有超过，返回 false, 允许添加。
     * @param userId
     * @return
     */
    private boolean checkMaxCount(Integer userId) {
        //获取配置信息
        Config config = configService.getConfigByCode(userId, ConfigCodeType.SELECT_MAX_NUM.getCode());
        //获取信息
        Integer maxCount = Integer.parseInt(
                Optional.ofNullable(config.getCodeValue())
                .orElse("20")
        );
        //获取当前用户所拥有的数量.
        QueryWrapper<StockSelected> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("flag",DataFlagType.NORMAL.getCode());
        int nowUseCount = Optional.ofNullable(stockSelectedMapper.selectCount(queryWrapper)).orElse(0);
        //进行比较，返回
        return maxCount>=nowUseCount?false:true;
    }
    @Override
    public OutputResult delete(IdRo idRo, int userId) {
        QueryWrapper<StockSelected> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",idRo.getIds());
        queryWrapper.eq("user_id",userId);
        queryWrapper.eq("flag",DataFlagType.NORMAL.getCode());
        //进行删除
        //获取对应的自选信息.
        StockSelected stockSelected = getSelectedByWrapper(queryWrapper);
        if(stockSelected== null){
            return OutputResult.alert("已经被移出自选了");
        }
        //否则的话，修改成删除状态
        stockSelected.setFlag(
                DataFlagType.DELETE.getCode()
        );
        stockSelectedMapper.updateById(stockSelected);
        //需要暂停任务
        xxlJobService.disableJob(
                stockSelected.getJobId()
        );
        stockRedisUtil.removePrice(stockSelected.getStockCode());
        return OutputResult.success();
    }

    private StockSelected getSelectedByWrapper(Wrapper<StockSelected> wrapper){
        List<StockSelected> stockSelectedList = stockSelectedMapper.selectList(wrapper);
        if(CollectionUtils.isEmpty(stockSelectedList)){
            return null;
        }
        return stockSelectedList.get(0);
    }

    @Override
    public OutputResult listSelected(StockSelectedRo stockSelectedRo) {
        PageHelper.startPage(stockSelectedRo.getPageNum(),stockSelectedRo.getPageSize());
        List<StockSelectedVo> stockInfoList=
                stockSelectedMapper.selectByKeyword(stockSelectedRo.getUserId(),
                        stockSelectedRo.getKeyword());
        PageInfo pageInfo=new PageInfo<StockSelectedVo>(stockInfoList);
        return OutputResult.success(new PageResponse<StockSelectedVo>(pageInfo.getTotal(),
                pageInfo.getList()));
    }

    @Override
    public OutputResult deleteByCode(StockSelectedRo stockSelectedRo) {
        QueryWrapper<StockSelected> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("stock_code",stockSelectedRo.getStockCode());
        queryWrapper.eq("user_id",stockSelectedRo.getUserId());
        queryWrapper.eq("flag",DataFlagType.NORMAL.getCode());
        //进行删除
        //获取对应的自选信息.
        StockSelected stockSelected = getSelectedByWrapper(queryWrapper);
        if(stockSelected== null){
            return OutputResult.alert("已经被移出自选了");
        }
        //否则的话，修改成删除状态
        stockSelected.setFlag(
                DataFlagType.DELETE.getCode()
        );
        stockSelectedMapper.updateById(stockSelected);
        //需要暂停任务
        xxlJobService.disableJob(
                stockSelected.getJobId()
        );
        stockRedisUtil.removePrice(stockSelected.getStockCode());
        return OutputResult.success();

    }

    @Override
    public void syncDayHistory() {
        //查询出所有的自选表里面的股票记录信息
        List<String> codeList = stockSelectedMapper.findCodeList(null);
        for(String code:codeList){
            //对股票进行同步
            StockRo stockRo = new StockRo();
            stockRo.setType(
                    SyncStockHistoryType.SELF.getCode()
            );
            Date now = DateUtil.date();
            //获取上一天的记录
            Date beforeOne = DateUtil.offsetDay(now,-1);
            stockRo.setStartDate(
                    DateUtil.format(beforeOne,"yyyy-MM-dd hh:mm:ss")
            );
            stockRo.setEndDate(
                    DateUtil.format(now,"yyyy-MM-dd hh:mm:ss")
            );
            stockRo.setCode(code);
            stockRo.setExchange(ExchangeType.SH.getCode());
            stockCrawlerService.stockHistoryAsync(
                    stockRo
            );
        }
    }

    @Override
    public void cacheClosePrice() {
        //查询出所有的自选表里面的股票记录信息
        List<String> codeList = stockSelectedMapper.findCodeList(null);
        //获取相关的信息，进行处理。
       List<StockPriceCacheDto> priceCacheDtoList = stockHistoryService.listClosePrice(codeList);
       //循环设置缓存信息
        if(CollectionUtils.isEmpty(priceCacheDtoList)){
            log.error(">>>未查询出昨天的价格记录，对应的股票信息是:{}",codeList);
            return ;
        }
        for(StockPriceCacheDto priceCacheDto:priceCacheDtoList){
            stockRedisUtil.setYesPrice(priceCacheDto.getCode(),priceCacheDto.getPrice());
        }
    }

    // Integer addJob(String cron, String jobDesc, Integer group,
    // String jobHandler, String creator, Integer executorParam);
    /**
     * 添加任务
     * @param stockSelectedRo
     * @return
     */
    private Integer addJob(StockSelectedRo stockSelectedRo) {
        return xxlJobService.addJob(
                XxlJobConst.SELECTED_SCAN_CRON,
                stockSelectedRo.getUserId()+"自选股票"+stockSelectedRo.getStockCode(),
                XxlJobConst.JOB_SELECTED_GROUP,
                XxlJobConst.SELECTED_SCAN_HANDLER,
                stockSelectedRo.getUserId()+"",
                stockSelectedRo.getStockCode()
        );
    }
}
