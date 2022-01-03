package top.yueshushu.learn.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import top.yueshushu.learn.mode.ro.IdRo;
import top.yueshushu.learn.mode.ro.StockSelectedVo;
import top.yueshushu.learn.mode.vo.StockSelectedRo;
import top.yueshushu.learn.page.PageResponse;
import top.yueshushu.learn.pojo.Stock;
import top.yueshushu.learn.pojo.StockSelected;
import top.yueshushu.learn.mapper.StockSelectedMapper;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.StockSelectedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.service.StockService;

import java.util.List;
import java.util.Optional;
import java.util.OptionalLong;

/**
 * <p>
 * 股票自选表,是用户自己选择的 自定义的
 * </p>
 *
 * @author 岳建立
 * @since 2022-01-02
 */
@Service
public class StockSelectedServiceImpl extends ServiceImpl<StockSelectedMapper, StockSelected> implements StockSelectedService {
    @Autowired
    private StockSelectedMapper stockSelectedMapper;
    @Autowired
    private StockService stockService;
    @Override
    public OutputResult add(StockSelectedRo stockSelectedRo) {
        //看是否已经加入自选，如果已经添加，则不需要添加.
        QueryWrapper<StockSelected> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("stock_code",stockSelectedRo.getStockCode());
        queryWrapper.eq("user_id",stockSelectedRo.getUserId());
        //进行查询
        Integer selectCount = stockSelectedMapper.selectCount(queryWrapper);
        if(selectCount!=null&&selectCount>0){
            return OutputResult.alert("已经存在自选表里面，不需要重复添加");
        }
        //未添加，则添加
        StockSelected stockSelected = new StockSelected();
        Stock stock = stockService.selectByCode(stockSelectedRo.getStockCode());
        stockSelected.setStockCode(stockSelectedRo.getStockCode());
        stockSelected.setStockName(
                stock==null?"":stock.getName()
        );
        stockSelected.setUserId(stockSelectedRo.getUserId());
        stockSelected.setCreateTime(DateUtil.date());
        stockSelectedMapper.insert(stockSelected);
        return OutputResult.success();
    }

    @Override
    public OutputResult delete(IdRo idRo, int userId) {
        QueryWrapper<StockSelected> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id",idRo.getIds());
        queryWrapper.eq("user_id",userId);
        //进行删除
        stockSelectedMapper.delete(queryWrapper);
        return OutputResult.success();
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
        //进行删除
        stockSelectedMapper.delete(queryWrapper);
        return OutputResult.success();
    }
}
