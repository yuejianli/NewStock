package top.yueshushu.learn.api.request;

import lombok.Data;
import top.yueshushu.learn.enumtype.TradeMethodType;

/**
 * 我的持仓
 */
@Data
public class GetStockListRequest extends BaseTradeRequest {

    public GetStockListRequest(int userId) {
        super(userId);
    }

    @Override
    public String getMethod() {
        return TradeMethodType.GetStockList.getCode();
    }

}
