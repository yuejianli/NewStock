package top.yueshushu.learn.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;

/**
 * @ClassName:RestConfig
 * @Description TODO
 * @Author 岳建立
 * @Date 2021/11/12 23:06
 * @Version 1.0
 **/
@Configuration
public class DruidConfig {
    /*
     * 解决druid 日志报错：discard long time none received connection:xxx
     * */
    @PostConstruct
    public void setProperties(){
        System.setProperty("druid.mysql.usePingMethod","false");
    }
}
