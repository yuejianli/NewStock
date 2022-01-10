package top.yueshushu.learn.controller;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.yueshushu.learn.mode.ro.BuyRo;
import top.yueshushu.learn.mode.ro.RevokeRo;
import top.yueshushu.learn.response.OutputResult;
import top.yueshushu.learn.service.BuyService;
import top.yueshushu.learn.service.RevokeService;

/**
 * <p>
 * 撤销委托单
 * </p>
 *
 * @author 两个蝴蝶飞
 * @date 2022-01-03
 */
@RestController
@RequestMapping("/revoke")
@ApiModel("撤销委托单")
public class RevokeController extends BaseController {
    @Autowired
    private RevokeService revokeService;
    @PostMapping("/revoke")
    @ApiOperation("撤销委托信息")
    public OutputResult revoke(@RequestBody RevokeRo revokeRo){
        revokeRo.setUserId(getUserId());
        return revokeService.revoke(revokeRo);
    }


}
