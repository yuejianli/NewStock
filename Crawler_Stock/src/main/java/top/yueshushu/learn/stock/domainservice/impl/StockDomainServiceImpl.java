package top.yueshushu.learn.stock.domainservice.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.yueshushu.learn.stock.domain.StockDo;
import top.yueshushu.learn.stock.domainservice.StockDomainService;
import top.yueshushu.learn.stock.mapper.StockDoMapper;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description TODO
 * @Author yuejianli
 * @Date 2022/5/20 23:23
 **/
@Service
@Slf4j
public class StockDomainServiceImpl extends ServiceImpl<StockDoMapper, StockDo>
        implements StockDomainService {
    @Resource
    private StockDoMapper stockDoMapper;
    @Override
    public StockDo getByCode(String code) {
        List<StockDo> list = this.lambdaQuery()
                .eq(
                        StockDo::getCode, code
                ).list();
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }
    @Override
    public StockDo getByFullCode(String fullCode) {
        List<StockDo> list = this.lambdaQuery()
                .eq(
                        StockDo::getFullCode, fullCode
                ).list();
        if (CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }
    @Override
    public List<String> listAllCode() {
        return stockDoMapper.listAllCode();
    }
}
