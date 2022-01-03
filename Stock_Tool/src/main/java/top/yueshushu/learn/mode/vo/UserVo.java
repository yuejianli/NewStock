package top.yueshushu.learn.mode.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName:UserVo
 * @Description TODO
 * @Author 岳建立
 * @Date 2022/1/2 10:55
 * @Version 1.0
 **/
@Data
@ApiModel("用户登录后展示信息")
public class UserVo implements Serializable {
    @ApiModelProperty("自增主键")
    private Integer id;
    @ApiModelProperty("用户登录账号")
    private String account;
    @ApiModelProperty("用户的昵称")
    private String name;
    @ApiModelProperty("登录后生成的token")
    private String token;
    @ApiModelProperty("用户手机")
    private String phone;
    @ApiModelProperty("用户的邮箱")
    private String email;
    @ApiModelProperty("用户创建时间")
    private Date createTime;
    @ApiModelProperty("最后登录时间")
    private Date lastLoginTime;
    @ApiModelProperty("是否禁用 1是正常 0为 禁用")
    private Integer status;
    @ApiModelProperty("是否可用 1是可用 0是删除")
    private Integer flag;
}
