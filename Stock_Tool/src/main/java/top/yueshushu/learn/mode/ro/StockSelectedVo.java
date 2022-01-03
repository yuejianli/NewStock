package top.yueshushu.learn.mode.ro;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName:StockSelectedVo
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/3 8:11
 * @Version 1.0
 **/
@Data
@ApiModel("股票自选展示Vo")
public class StockSelectedVo implements Serializable {
    @ApiModelProperty("id编号")
    private Integer id;
    @ApiModelProperty("股票编码")
    private String stockCode;
    @ApiModelProperty("股票名称")
    private String stockName;
    @ApiModelProperty("添加日期")
    private Date createTime;
}
