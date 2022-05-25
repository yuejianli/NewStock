package top.yueshushu.learn.xxljob.user;

import cn.hutool.core.date.DateUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import top.yueshushu.learn.business.DealBusiness;
import top.yueshushu.learn.enumtype.EntrustType;
import top.yueshushu.learn.enumtype.MockType;
import top.yueshushu.learn.helper.DateHelper;
import top.yueshushu.learn.mode.ro.DealRo;

import javax.annotation.Resource;

/**
 * @ClassName:MockEntrustHandler
 * @Description 对单个用户进行操作，获取其委托信息.
 * @Author 岳建立
 * @Date 2022/1/9 17:25
 * @Version 1.0
 **/
@Component
@JobHandler("mockDealHandler")
@Slf4j
public class MockDealHandler extends IJobHandler {
    @Value("${xxlJobTime}")
    boolean xxlJobTime;
    @Resource
    private DealBusiness dealBusiness;
    @Resource
    private DateHelper dateHelper;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        if (xxlJobTime){
            if (!dateHelper.isTradeTime(DateUtil.date())) {
                return ReturnT.SUCCESS;
            }
        }

        log.info(">>>扫描当前的用户id 为{}",s);
        DealRo dealRo = new DealRo();
        dealRo.setMockType(MockType.MOCK.getCode());
        dealRo.setUserId(Integer.parseInt(s));
        dealRo.setEntrustType(EntrustType.AUTO.getCode());
        dealBusiness.mockDealXxlJob(dealRo);
        return ReturnT.SUCCESS;
    }
}
