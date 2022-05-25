package top.yueshushu.learn.controller;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.DealRo;
import top.yueshushu.learn.mode.ro.RevokeRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.DealService;
import top.yueshushu.learn.service.RevokeService;

/**
 * <p>
 * 成交委托单的处理
 * </p>
 *
 * @author 两个蝴蝶飞
 * @date 2022-01-03
 */
@RestController
@RequestMapping("/revoke")
@ApiModel("成交委托单")
public class DealController extends BaseController {
    @Autowired
    private DealService dealService;
    @PostMapping("/deal")
    @ApiOperation("成交委托信息")
    public OutputResult deal(@RequestBody DealRo dealRo){
        dealRo.setUserId(getUserId());
        return dealService.deal(dealRo);
    }


}
