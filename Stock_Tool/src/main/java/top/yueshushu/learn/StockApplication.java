package top.yueshushu.learn;

import lombok.extern.log4j.Log4j2;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import top.yueshushu.log.EnableMyLog;

/**
 * @ClassName:StockApplication
 * @Description 股票小工具
 * @Author 岳建立
 * @Date 2021/4/26 12:01
 * @Version 1.0
 **/
@SpringBootApplication
@MapperScan("top.yueshushu.learn.mapper")
@Log4j2
@EnableMyLog
//开启缓存
@EnableCaching
@EnableAsync
public class StockApplication {
    public static void main(String[] args) {
        SpringApplication.run(StockApplication.class,args);
        log.info(">>>股票小工具启动成功");
    }
}
