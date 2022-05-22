package top.yueshushu.learn.stock;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

/**
 * @Description 爬虫程序
 * @Author 岳建立
 * @Date 2021/11/7 16:08
 **/
@SpringBootApplication
@MapperScan("top.yueshushu.learn.stock.mapper")
@EnableAsync
public class CrawlerApp {
    public static void main(String[] args) {
        SpringApplication.run(CrawlerApp.class,args);
    }
}
