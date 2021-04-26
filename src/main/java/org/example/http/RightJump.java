package org.example.http;


import lombok.extern.slf4j.Slf4j;
import org.example.utils.RightsJumpHttpClientUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hujun
 * @version 1.0.0
 * @ClassName RightJump.java
 * @Description
 * @createTime 2021-01-27 10:50:00
 */
@Component
@Slf4j
public class RightJump {
    public static String JSON_CONTENT_TYPE = "application/json";
    @Resource
    private RightsJumpHttpClientUtils clientUtils;

    @PostConstruct
    public String sendPostRequest() throws Exception{
        String url = "http://119.3.74.221:9000/server/hello/add";
        String payLoad = "{\n" +
                "    \"name\":\"youyun\",\n" +
                "    \"age\":11,\n" +
                "    \"weight\":22,\n" +
                "    \"id\":3\n" +
                "}";

        String resp = clientUtils.doPostRequest(url,JSON_CONTENT_TYPE,payLoad);
        log.info("the sendPost request resp [{}]",resp);
        Thread.sleep(3000);

        String getUrl = "http://119.3.74.221:9000/server/hello/getToken?appKey=key&secret=stest";
        resp = clientUtils.doGetRequest(getUrl);
        log.info("the get request resp [{}]",resp);
        Thread.sleep(3000);

        String formUrl = "http://119.3.74.221:9000/server/hello/testForm";
        Map<String,Object> data = new HashMap<>();
        data.put("name","hujun");
        data.put("age",10);
        resp = clientUtils.doPostFormData(formUrl,"application/x-www-form-urlencoded",data);
        log.info("the form request resp [{}]",resp);
        Thread.sleep(3000);


        log.info("the sendPost request resp [{}]",resp);
        return resp;
    }



}
