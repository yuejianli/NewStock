package top.yueshushu.learn.mode.ro;

import cn.hutool.core.date.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.yueshushu.learn.page.PageRo;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName:HolidayRo
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/3 12:58
 * @Version 1.0
 **/
@Data
@ApiModel("假期信息Ro")
public class HolidayRo extends PageRo implements Serializable {
    @ApiModelProperty("年")
    private Integer year;

    public Integer getYear(){
        if(this.year==null||this.year==0){
            return DateUtil.thisYear();
        }
        return year;
    }
}
