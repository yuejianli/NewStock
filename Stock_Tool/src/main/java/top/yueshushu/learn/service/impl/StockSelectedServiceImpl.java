package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.assembler.StockSelectedAssembler;
import top.yueshushu.learn.common.ResultCode;
import top.yueshushu.learn.domainservice.StockSelectedDomainService;
import top.yueshushu.learn.entity.StockSelected;
import top.yueshushu.learn.enumtype.DataFlagType;
import top.yueshushu.learn.mode.ro.IdRo;
import top.yueshushu.learn.mode.vo.StockSelectedVo;
import top.yueshushu.learn.mode.ro.StockSelectedRo;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.domain.StockSelectedDo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 股票自选表,是用户自己选择的 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
@Slf4j(topic = "自选股票")
public class StockSelectedServiceImpl implements StockSelectedService {

    @Resource
    private StockSelectedAssembler stockSelectedAssembler;
    @Resource
    private StockSelectedDomainService stockSelectedDomainService;
    @Transactional(rollbackFor = Exception.class)
    @Override
    public StockSelected add(StockSelectedRo stockSelectedRo,String stockName) {
        StockSelected stockSelected = stockSelectedAssembler.doToEntity(
                stockSelectedDomainService.getByUserIdAndCodeAndStatus(
                        stockSelectedRo.getUserId(),
                        stockSelectedRo.getStockCode(),
                        null
                )
        );
        if (stockSelected == null) {
            log.info("用户{}没有该股票 {} 的记录，进行添加", stockSelectedRo.getUserId(),stockSelectedRo.getStockCode());
            stockSelected = new StockSelected();
            stockSelected.setStockCode(stockSelectedRo.getStockCode());
            stockSelected.setStockName(
                   stockName
            );
            stockSelected.setUserId(stockSelectedRo.getUserId());
            stockSelected.setCreateTime(DateUtil.date());
            stockSelected.setStatus(DataFlagType.NORMAL.getCode());
            stockSelected.setFlag(DataFlagType.NORMAL.getCode());

            stockSelectedDomainService.save(
                    stockSelectedAssembler.entityToDo(
                            stockSelected
                    )
            );
        } else {
            log.info("用户{} 有股票 {}自选记录,只能现在被删除了,对这条记录进行修改", stockSelectedRo.getUserId(),
                    stockSelectedRo.getStockCode());
            stockSelected.setCreateTime(DateUtil.date());
            stockSelected.setStatus(DataFlagType.NORMAL.getCode());
            //进行更新
            stockSelectedDomainService.updateById(
                    stockSelectedAssembler.entityToDo(
                            stockSelected
                    )
            );
        }
        // 重新查询一下,获取最新的携带着 id的数据
       return stockSelectedAssembler.doToEntity(
                stockSelectedDomainService.getByUserIdAndCodeAndStatus(
                        stockSelectedRo.getUserId(),
                        stockSelectedRo.getStockCode(),
                        DataFlagType.NORMAL.getCode()
                )
        );
    }
    /**
     * 看是否超过最大的数量，如果超过，则返回 true
     * 如果没有超过，返回 false, 允许添加。
     * @param userId 用户编号
     * @param maxSelectedNum 最大的自选数量
     * @return 看是否超过最大的数量，如果超过，则返回 true
     */
    private boolean checkMaxCount(Integer userId,Integer maxSelectedNum) {
        //获取当前用户所拥有的数量.
        QueryWrapper<StockSelectedDo> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("flag", DataFlagType.NORMAL.getCode());
        int nowUseCount = stockSelectedDomainService.countByUserIdAndStatus(
                userId,
                DataFlagType.NORMAL.getCode()
        );
        //进行比较，返回
        return maxSelectedNum >= nowUseCount ? false : true;
    }

    @Override
    public OutputResult delete(IdRo idRo, int userId) {
        StockSelected stockSelected = stockSelectedAssembler.doToEntity(
                stockSelectedDomainService.getById(idRo.getId())
        );
        return deleteByInfo(stockSelected,userId);
    }

    public OutputResult deleteByInfo(StockSelected stockSelected,Integer userId){
        //如果不存在，或者用户编号不一致
        if (stockSelected == null || !stockSelected.getUserId().equals(userId)){
            return OutputResult.buildAlert(ResultCode.STOCK_SELECTED_NO_RECORD);
        }
        if (DataFlagType.DELETE.getCode().equals(stockSelected.getStatus())){
            return OutputResult.buildAlert(ResultCode.STOCK_SELECTED_HAVE_DISABLE);
        }

        //否则的话，修改成删除状态
        stockSelected.setStatus(
                DataFlagType.DELETE.getCode()
        );
        stockSelectedDomainService.updateById(
                stockSelectedAssembler.entityToDo(
                        stockSelected
                )
        );
        return OutputResult.buildSucc(stockSelected.getJobId());
    }
    @Override
    public OutputResult pageSelected(StockSelectedRo stockSelectedRo) {
        PageHelper.startPage(stockSelectedRo.getPageNum(), stockSelectedRo.getPageSize());
        List<StockSelectedVo> pageResultList = listSelf(
                stockSelectedRo.getUserId(),
                stockSelectedRo.getKeyword()
        );
        if (CollectionUtils.isEmpty(pageResultList)) {
            return OutputResult.buildSucc(
                    PageResponse.emptyPageResponse()
            );
        }
        PageInfo pageInfo = new PageInfo<>(pageResultList);
        return OutputResult.buildSucc(new PageResponse<>(
                pageInfo.getTotal(), pageInfo.getList()
        ));
    }

    @Override
    public OutputResult validateAdd(StockSelectedRo stockSelectedRo, int maxSelectedNum) {
        StockSelected stockSelected = stockSelectedAssembler.doToEntity(
                stockSelectedDomainService.getByUserIdAndCodeAndStatus(
                        stockSelectedRo.getUserId(),
                        stockSelectedRo.getStockCode(),
                        DataFlagType.NORMAL.getCode()
                )
        );
        if (stockSelected != null &&
                DataFlagType.NORMAL.getCode().equals(
                        stockSelected.getStatus()
                )) {
            log.info("用户{}添加股票 {} 到自选失败，因为已经存在了",stockSelectedRo.getUserId(),
                    stockSelectedRo.getStockCode());
            return OutputResult.buildAlert(ResultCode.STOCK_SELECTED_EXISTS);
        }
        //看是否超过最大的数量
        if (checkMaxCount(stockSelectedRo.getUserId(),maxSelectedNum)) {
            return OutputResult.buildAlert(ResultCode.STOCK_SELECTED_MAX_LIMIT);
        }
        return OutputResult.buildSucc();
    }

    @Override
    public void updateSelected(StockSelected stockSelected) {
         stockSelectedDomainService.updateById(
                stockSelectedAssembler.entityToDo(
                        stockSelected
                )
        );
    }

    @Override
    public Map<String, String> listStockCodeByUserId(Integer userId) {

        List<StockSelectedDo> stockSelectedDoList = stockSelectedDomainService.listByUserIdAndCodeAndStatus(
                userId, null, DataFlagType.NORMAL.getCode()
        );
        return stockSelectedDoList.stream().collect(
                Collectors.toMap(
                        StockSelectedDo::getStockCode,
                        StockSelectedDo::getStockName,
                        (o,n)->n
                )
        );
    }

    @Override
    public OutputResult deleteByCode(StockSelectedRo stockSelectedRo) {
       StockSelected stockSelected = stockSelectedAssembler.doToEntity(
               stockSelectedDomainService.getByUserIdAndCodeAndStatus(
                       stockSelectedRo.getUserId(),
                       stockSelectedRo.getStockCode(),
                       null
               )
       );
       return deleteByInfo(stockSelected,stockSelected.getUserId());
    }

    @Override
    public void syncDayHistory() {
        //查询出所有的自选表里面的股票记录信息
        //List<String> codeList = stockSelectedDoMapper.findCodeList(null);
        //for (String code : codeList) {
        //    //对股票进行同步
        //    StockRo stockRo = new StockRo();
        //    stockRo.setType(
        //            SyncStockHistoryType.SELF.getCode()
        //    );
        //    Date now = DateUtil.date();
        //    //获取上一天的记录
        //    Date beforeOne = DateUtil.offsetDay(now, -1);
        //    stockRo.setStartDate(
        //            DateUtil.format(beforeOne, "yyyy-MM-dd hh:mm:ss")
        //    );
        //    stockRo.setEndDate(
        //            DateUtil.format(now, "yyyy-MM-dd hh:mm:ss")
        //    );
        //    stockRo.setCode(code);
        //    stockRo.setExchange(ExchangeType.SH.getCode());
        //    stockCrawlerService.stockHistoryAsync(
        //            stockRo
        //    );
        //}
    }

    @Override
    public void cacheClosePrice() {
        ////查询出所有的自选表里面的股票记录信息
        //List<String> codeList = stockSelectedDoMapper.findCodeList(null);
        ////获取相关的信息，进行处理。
        //List<StockPriceCacheDto> priceCacheDtoList = stockHistoryService.listClosePrice(codeList);
        ////循环设置缓存信息
        //if (CollectionUtils.isEmpty(priceCacheDtoList)) {
        //    log.error(">>>未查询出昨天的价格记录，对应的股票信息是:{}", codeList);
        //    return;
        //}
        //for (StockPriceCacheDto priceCacheDto : priceCacheDtoList) {
        //    stockRedisUtil.setYesPrice(priceCacheDto.getCode(), priceCacheDto.getPrice());
        //}
    }

    @Override
    public List<StockSelectedVo> listSelf(Integer userId, String keyword) {
        //查询所有的股票信息
        List<StockSelectedDo> stockSelectedDoList =
                stockSelectedDomainService.listByUserIdAndKeyword(userId, keyword);
        if (CollectionUtils.isEmpty(stockSelectedDoList)) {
            return Collections.EMPTY_LIST;
        }
        List<StockSelectedVo> stockSelectedVoList = new ArrayList<>(stockSelectedDoList.size());
        stockSelectedDoList.forEach(
                n -> {
                    stockSelectedVoList.add(
                            stockSelectedAssembler.entityToVo(
                                    stockSelectedAssembler.doToEntity(n)
                            )
                    );
                }
        );
        return stockSelectedVoList;
    }
}
