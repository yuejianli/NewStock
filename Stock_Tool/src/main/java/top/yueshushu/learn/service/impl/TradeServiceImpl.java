package top.yueshushu.learn.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import top.yueshushu.learn.api.response.GetDealDataResponse;
import top.yueshushu.learn.api.response.GetHisDealDataResponse;
import top.yueshushu.learn.api.response.GetOrdersDataResponse;
import top.yueshushu.learn.api.response.GetStockListResponse;
import top.yueshushu.learn.model.info.StockInfo;
import top.yueshushu.learn.pojo.StockSelected;
import top.yueshushu.learn.pojo.TradeMethod;
import top.yueshushu.learn.pojo.TradeUser;
import top.yueshushu.learn.service.StockCrawlerService;
import top.yueshushu.learn.service.StockService;
import top.yueshushu.learn.service.TradeService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TradeServiceImpl implements TradeService {

}
