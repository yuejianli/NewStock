package top.yueshushu.learn.pojo;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 成交表
 * </p>
 *
 * @author 两个蝴蝶飞 自定义的
 * @since 2022-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("trade_deal")
public class TradeDeal implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 股票编号
     */
    @TableField("code")
    private String code;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 成交时间
     */
    @TableField("deal_date")
    private Date dealDate;

    /**
     * 成交类型 1为买 2为卖
     */
    @TableField("deal_type")
    private Integer dealType;

    /**
     * 成交数量
     */
    @TableField("deal_num")
    private Integer dealNum;

    /**
     * 成交价格
     */
    @TableField("deal_price")
    private BigDecimal dealPrice;

    /**
     * 成交金额
     */
    @TableField("deal_money")
    private BigDecimal dealMoney;

    /**
     * 成交编号
     */
    @TableField("deal_code")
    private String dealCode;

    /**
     * 委托编号
     */
    @TableField("entrust_code")
    private String entrustCode;

    /**
     * 关联用户
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 类型 1为虚拟 0为正式
     */
    @TableField("mock_type")
    private Integer mockType;

    /**
     * 类型 1为正常 0为删除
     */
    @TableField("flag")
    private Integer flag;


}
