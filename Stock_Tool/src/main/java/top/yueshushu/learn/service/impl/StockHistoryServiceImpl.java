package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.assembler.StockHistoryAssembler;
import top.yueshushu.learn.common.Const;
import top.yueshushu.learn.domain.StockDo;
import top.yueshushu.learn.domainservice.StockHistoryDomainService;
import top.yueshushu.learn.entity.Stock;
import top.yueshushu.learn.entity.StockHistory;
import top.yueshushu.learn.mode.dto.StockPriceCacheDto;
import top.yueshushu.learn.mode.vo.StockHistoryVo;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.domain.StockHistoryDo;
import top.yueshushu.learn.mapper.StockHistoryDoMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.ro.stock.StockRo;
import top.yueshushu.learn.service.StockHistoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 股票的历史交易记录表 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
public class StockHistoryServiceImpl  implements StockHistoryService {
    @Resource
    private StockHistoryDomainService stockHistoryDomainService;
    @Resource
    private StockHistoryAssembler stockHistoryAssembler;
    @Override
    public OutputResult pageHistory(StockRo stockRo) {
        PageHelper.startPage(stockRo.getPageNum(),stockRo.getPageSize());
        List<StockHistoryDo> stockHistoryDoList= stockHistoryDomainService.listStockHistoryAndDate(stockRo.getCode()
                , DateUtil.parse(
                        stockRo.getStartDate(), Const.SIMPLE_DATE_FORMAT
                ),
                DateUtil.parse(
                        stockRo.getEndDate(),Const.SIMPLE_DATE_FORMAT
                ));

        if (CollectionUtils.isEmpty(stockHistoryDoList)){
            return OutputResult.buildSucc(
                    PageResponse.emptyPageResponse()
            );
        }
        List<StockHistoryVo> pageResultList = new ArrayList<>(stockHistoryDoList.size());
        stockHistoryDoList.forEach(
                n->{
                    pageResultList.add(
                            stockHistoryAssembler.entityToVo(
                                    stockHistoryAssembler.doToEntity(n)
                            )
                    );
                }
        );

        PageInfo pageInfo=new PageInfo<>(pageResultList);
        return OutputResult.buildSucc(new PageResponse<>(
                pageInfo.getTotal(),pageInfo.getList()
        ));
    }

    @Override
    public List<StockPriceCacheDto> listClosePrice(List<String> codeList) {
        Date yesDate = DateUtil.yesterday();
        return stockHistoryDomainService.listYesterdayClosePrice(codeList,yesDate);
    }

    @Override
    public StockHistoryVo getVoByCodeAndCurrDate(String code, DateTime currDate) {
        return stockHistoryAssembler.entityToVo(
                stockHistoryAssembler.doToEntity(
                        stockHistoryDomainService.getByCodeAndCurrDate(
                                code,currDate
                        )
                )
        );
    }
}
