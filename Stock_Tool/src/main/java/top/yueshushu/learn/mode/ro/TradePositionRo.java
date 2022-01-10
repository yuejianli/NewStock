package top.yueshushu.learn.mode.ro;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.yueshushu.learn.page.PageRo;

import java.io.Serializable;

/**
 * @ClassName:PoistionRo
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/3 17:00
 * @Version 1.0
 **/
@Data
@ApiModel("我的持仓展示信息")
public class TradePositionRo extends TradeRo implements Serializable {
    @ApiModelProperty("查询的类型 0为持仓和自选 1为只看持仓")
    private Integer selectType;
}
