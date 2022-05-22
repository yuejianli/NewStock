package top.yueshushu.learn.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.yueshushu.learn.mode.dto.XxlJobContentDto;
import top.yueshushu.learn.service.XxlJobService;

import javax.annotation.Resource;
import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class XxlJobServiceImpl implements XxlJobService {

    @Value("${strategy.job.add.url}")
    private String jobAddUrl;

    @Value("${strategy.job.update.url}")
    private String jobUpdateUrl;

    @Value("${strategy.job.enable.url}")
    private String jobEnableUrl;

    @Value("${strategy.job.disable.url}")
    private String jobDisableUrl;

    @Value("${strategy.job.remove.url}")
    private String jobRemoveUrl;

    @Value("${xxl.job.admin.username}")
    private String username;

    @Value("${xxl.job.admin.password}")
    private String password;
    @Value("${xxl.job.admin.addresses}")
    private String addresses;

    private static String cookie;


    @Resource
    private RestTemplate restTemplate;

    @Override
    public Integer addJob(String cron, String jobDesc, Integer group, String jobHandler, String creator,
        String executorParam) {
        HttpEntity requestBody = generateRequestBody(group, jobDesc, cron, jobHandler, creator, executorParam, null);
        ResponseEntity<String> responseEntity = this.restTemplate.postForEntity(jobAddUrl, requestBody
                ,String.class);
        XxlJobContentDto result = JSON.parseObject(responseEntity.getBody(), XxlJobContentDto.class);
        if (result.getCode() == 200) {
            log.info("create remote xxl job successfully." + responseEntity.getBody());
            String content = result.getContent();
            Integer jobId = Integer.parseInt(content);
            return jobId;
        } else {
            log.error("failed to create remote apply job." + responseEntity.getBody());
            return null;
        }
    }

    @Override
    public void updateJob(String cron, String jobDesc, Integer group, String jobHandler, String creator,
        String executorParam, Integer jobId) {
        HttpEntity requestBody = generateRequestBody(group, jobDesc, cron, jobHandler, creator, executorParam, jobId);
        this.restTemplate.postForEntity(jobUpdateUrl, requestBody, String.class);
    }

    @Override
    public void enableJob(Integer jobId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("id", jobId);
        if (StrUtil.isBlank(cookie)) {
            cookie = getCookie();
        }
        headers.add("cookie",cookie);
        HttpEntity requestBody = new HttpEntity(map, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(jobEnableUrl, requestBody, String.class);
        XxlJobContentDto result = JSON.parseObject(responseEntity.getBody(), XxlJobContentDto.class);
        if (result.getCode() == 200) {
            log.info("enable remote apply jobs successfully." + responseEntity.getBody());
        } else {
            log.error("failed to delete remote apply jobs." + responseEntity.getBody());
        }
    }

    @Override
    public void disableJob(Integer jobId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("id", jobId);
        if (StrUtil.isBlank(cookie)) {
            cookie = getCookie();
        }
        headers.add("cookie",cookie);
        HttpEntity requestBody = new HttpEntity(map, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(jobDisableUrl, requestBody, String.class);
        XxlJobContentDto result = JSON.parseObject(responseEntity.getBody(), XxlJobContentDto.class);
        if (result.getCode() == 200) {
            log.info("disable remote apply jobs successfully." + responseEntity.getBody());
        } else {
            log.error("failed to disable remote apply jobs." + responseEntity.getBody());
        }
    }

    @Override
    public void removeJob(Integer jobId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("id", jobId);
        if (StrUtil.isBlank(cookie)) {
            cookie = getCookie();
        }
        headers.add("cookie",cookie);
        HttpEntity requestBody = new HttpEntity(map, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(jobRemoveUrl, requestBody, String.class);
        XxlJobContentDto result = JSON.parseObject(responseEntity.getBody(), XxlJobContentDto.class);
        if (result.getCode() == 200) {
            log.info("remove remote apply jobs successfully." + responseEntity.getBody());
        } else {
            log.error("failed to remove remote apply jobs." + responseEntity.getBody());
        }
    }

    private HttpEntity generateRequestBody(Integer group, String jobDesc, String cron, String executorHandler,
                                           String creator, String param, Integer id) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        if (StrUtil.isBlank(cookie)) {
            cookie = getCookie();
        }
        headers.add("cookie",cookie);
        MultiValueMap map = new LinkedMultiValueMap();
        map.add("jobGroup", group);
        map.add("jobDesc", jobDesc);
        map.add("executorRouteStrategy", "FIRST");
        map.add("cronGen_display", cron);
        map.add("jobCron", cron);
        map.add("glueType", "BEAN");
        map.add("executorHandler", executorHandler);
        map.add("executorBlockStrategy", "SERIAL_EXECUTION");
        map.add("childJobId", "");
        map.add("executorTimeout", 0);
        map.add("executorFailRetryCount", 0);
        map.add("author", creator);
        map.add("alarmEmail", "");
        map.add("executorParam", param);
        map.add("glueRemark", "GLUE代码初始化");
        map.add("glueSource", "");
        map.add("triggerStatus", 1);

        if (id != null) {
            map.add("id", id);
        }

        HttpEntity requestBody = new HttpEntity(map, headers);
        return requestBody;
    }

    /**
     * 获取登录cookie
     *
     * @return
     */
    private String getCookie() {
        Map<String, Object> paramsMap = new HashMap();
        paramsMap.put("userName", username);
        paramsMap.put("password", password);
        HttpResponse response = HttpRequest.post(String.format("%s%s", addresses,"/login"))
                .form(paramsMap).execute();
        if (HttpStatus.HTTP_OK != response.getStatus()) {
            throw new RuntimeException(String.format("xxl-job-admin登录失败:statusCode=%s", response.getStatus()));
        }

        List<HttpCookie> cookies = response.getCookies();

        if (cookies.isEmpty()) {
            throw new RuntimeException(String.format("xxl-job-admin登录失败:[userName=%s,password=%s]", username, password));
        }

        return cookies.stream().map(cookie -> cookie.toString()).collect(Collectors.joining());
    }
}
