package top.yueshushu.learn.business.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.business.StockBusiness;
import top.yueshushu.learn.business.StockSelectedBusiness;
import top.yueshushu.learn.common.ResultCode;
import top.yueshushu.learn.common.XxlJobConst;
import top.yueshushu.learn.entity.Stock;
import top.yueshushu.learn.entity.StockSelected;
import top.yueshushu.learn.helper.DateHelper;
import top.yueshushu.learn.mode.ro.IdRo;
import top.yueshushu.learn.mode.ro.StockSelectedRo;
import top.yueshushu.learn.mode.vo.StockHistoryVo;
import top.yueshushu.learn.mode.vo.StockVo;
import top.yueshushu.learn.model.info.StockInfo;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.service.*;
import top.yueshushu.learn.util.PageUtil;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Description 自选股票实现编排层
 * @Author yuejianli
 * @Date 2022/5/20 23:54
 **/
@Service
@Slf4j
public class StockSelectedBusinessImpl implements StockSelectedBusiness {
    @Resource
    private StockSelectedService stockSelectedService;
    @Resource
    private ConfigService configService;
    @Resource
    private StockService stockService;
    @Resource
    private XxlJobService xxlJobService;
    @Resource
    private StockHistoryService stockHistoryService;
    @Resource
    private DateHelper dateHelper;

    @Override
    public OutputResult listSelected(StockSelectedRo stockSelectedRo) {
        return stockSelectedService.pageSelected(
                stockSelectedRo
        );
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OutputResult add(StockSelectedRo stockSelectedRo) {
        //1. 验证股票是否已经存在
        int maxSelectedNum = configService.getMaxSelectedNumByUserId(
                stockSelectedRo.getUserId()
        );
        // 根据股票的编码，获取相应的股票记录信息
        Stock stock = stockService.selectByCode(stockSelectedRo.getStockCode());
        if (null == stock){
            return OutputResult.buildAlert(
                    ResultCode.STOCK_CODE_NO_EXIST
            );
        }
        // 验证添加用户
        OutputResult addValidateResult = stockSelectedService
                .validateAdd(stockSelectedRo,maxSelectedNum);
        if (!addValidateResult.getSuccess()){
            return addValidateResult;
        }
        //获取到股票的名称，进行添加到股票自选对象里面.
        StockSelected stockSelected = stockSelectedService.add(
                stockSelectedRo,stock.getName()
        );
        // 对定时任务进行处理
        if (null == stockSelected.getJobId()){
           Integer jobId = addJob(stockSelected.getUserId(),stockSelected.getStockCode());
           if ( jobId == null){
               throw new RuntimeException();
           }
           //否则，更新 任务编号
            stockSelected.setJobId(jobId);
            stockSelectedService.updateSelected(
                    stockSelected
            );
        }else{
            //定时任务开启
            xxlJobService.enableJob(
                    stockSelected.getJobId()
            );
        }
        // 处理缓存信息
        return OutputResult.buildSucc(
                ResultCode.SUCCESS
        );
    }

    @Override
    public OutputResult delete(IdRo idRo, int userId) {
        // 删除相关的记录
       OutputResult deleteResult = stockSelectedService.delete(
                idRo,userId
        );
        if (!deleteResult.getSuccess()){
            return deleteResult;
        }
        // 获取定时任务编号
        Integer jobId = (Integer) deleteResult.getData();
        if (jobId !=null){
            xxlJobService.disableJob(jobId);
        }
        return OutputResult.buildSucc();
    }

    @Override
    public OutputResult deleteByCode(StockSelectedRo stockSelectedRo) {
        // 删除相关的记录
        OutputResult deleteResult = stockSelectedService.deleteByCode(
                stockSelectedRo
        );
        if (!deleteResult.getSuccess()){
            return deleteResult;
        }
        // 获取定时任务编号
        Integer jobId = (Integer) deleteResult.getData();
        if (jobId !=null){
            xxlJobService.disableJob(jobId);
        }
        return OutputResult.buildSucc();
    }

    @Override
    public OutputResult yesHistory(StockSelectedRo stockSelectedRo) {
        //1. 查询出当前用户下所有正常可用的股票列表
        Map<String,String> stockMap = stockSelectedService.listStockCodeByUserId(
                stockSelectedRo.getUserId()
        );
        List<String> stockList = new ArrayList<>(stockMap.keySet());
        if (CollectionUtils.isEmpty(stockList)){
            return OutputResult.buildSucc(
                    PageResponse.emptyPageResponse()
            );
        }
        //不为空的话，进行拆分.
        List<String> list = PageUtil.startPage(
                stockList,
                stockSelectedRo.getPageNum(),
                stockSelectedRo.getPageSize()
        );
        //如果这个时候为空的话，进行处理
        if (CollectionUtils.isEmpty(stockList)){
            return OutputResult.buildSucc(
                    new PageResponse((long) stockList.size(),
                            Collections.emptyList())
            );
        }
        //查询最近的一天，非周末，非节假日
        DateTime beforeLastWorking = dateHelper.getBeforeLastWorking();

        List<StockHistoryVo> stockHistoryVoList = new ArrayList<>(list.size());
        for (String code: list){
            StockHistoryVo stockHistoryVo = stockHistoryService.getVoByCodeAndCurrDate(
                    code, beforeLastWorking
            );
            if (null == stockHistoryVo){
                stockHistoryVo = new StockHistoryVo();
                stockHistoryVo.setCode(
                        code
                );
                stockHistoryVo.setName(
                        stockMap.get(
                                code
                        )
                );
                stockHistoryVo.setCurrDate(
                        beforeLastWorking.toLocalDateTime()
                );
            }
            stockHistoryVoList.add(
                    stockHistoryVo
            );
        }
        return OutputResult.buildSucc(
                new PageResponse((long) stockList.size(),
                        stockHistoryVoList)
        );
    }
    /**
     *Integer addJob(String cron, String jobDesc, Integer group,
     * String jobHandler, String creator, Integer executorParam);
     */
    /**
     * 添加自选定时任务
     * @param userId 系统用户编号
     * @param stockCode 股票编码
     * @return 返回添加后的定时任务编号
     */
    private Integer addJob(Integer userId,String stockCode) {
        return xxlJobService.addJob(
                XxlJobConst.SELECTED_SCAN_CRON,
                userId + "_自选股票" + stockCode,
                XxlJobConst.JOB_SELECTED_GROUP,
                XxlJobConst.SELECTED_SCAN_HANDLER,
                userId + "",
                stockCode
        );
    }
}
