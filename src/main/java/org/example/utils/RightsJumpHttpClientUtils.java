package org.example.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author hujun
 * @version 1.0.0
 * @ClassName RightsJumpHttpClientUtils.java
 * @Description
 * @createTime 2021-01-27 15:21:00
 */
@Component
public class RightsJumpHttpClientUtils {
    public static String JSON_CONTENT_TYPE = "application/json";

    @Value("${cmic.forward.api.url}")
    private String cmicForwardApiUrl;
    @Value("${cmic.forward.api.key}")
    private String cmicForwardApiKey;

    private static String CMIC_FORWARD_API_URL;
    private static String CMIC_FORWARD_API_KEY;

    @Resource
    private HttpClientUtils httpClientUtils;

    @PostConstruct
    public void initParam() {
        CMIC_FORWARD_API_URL = cmicForwardApiUrl;
        CMIC_FORWARD_API_KEY = cmicForwardApiKey;
    }

    public String doPostRequest(String url, String contentType, String data) {
        // 封装url和head头部信息
        RightsJumpReq rightsJumpReq = new RightsJumpReq();
        rightsJumpReq.setUrl(url);
        // 加密
        String internalSign = MD5Utils.encode(JSON.toJSONString(rightsJumpReq) + CMIC_FORWARD_API_KEY);
        // 本次请求头部设置
        Map<String, String> requestHeadMap = new HashMap<>();
        requestHeadMap.put("Content-Type", contentType.replace(";", "") + ";charset=utf-8");
        requestHeadMap.put("internalProxyParam", JSON.toJSONString(rightsJumpReq));
        requestHeadMap.put("internalProxySign", internalSign);
        String response = httpClientUtils.doHttpPost(CMIC_FORWARD_API_URL, data, requestHeadMap, "");
        return response;
    }


    public String doGetRequest(String url) {
        // 封装url和head头部信息
        RightsJumpReq rightsJumpReq = new RightsJumpReq();
        rightsJumpReq.setUrl(url);
        // 加密
        String internalSign = MD5Utils.encode(JSON.toJSONString(rightsJumpReq) + CMIC_FORWARD_API_KEY);
        // 本次请求头部设置
        Map<String, String> requestHeadMap = new HashMap<>();
        requestHeadMap.put("Content-Type", JSON_CONTENT_TYPE + ";charset=utf-8");
        requestHeadMap.put("internalProxyParam", JSON.toJSONString(rightsJumpReq));
        requestHeadMap.put("internalProxySign", internalSign);

        String response = HttpClientUtils.doHttpGet(CMIC_FORWARD_API_URL, requestHeadMap);
        return response;
    }

    public  String doPostFormData(String url,String contentType,Map<String,Object> body){
        RightsJumpReq rightsJumpReq = new RightsJumpReq();
        rightsJumpReq.setUrl(url);
        String internalSign = MD5Utils.encode(JSON.toJSONString(rightsJumpReq) + CMIC_FORWARD_API_KEY);
        Map<String, String> requestHeadMap = new HashMap<>();
        requestHeadMap.put("Content-Type", contentType.replace(";", "") + ";charset=utf-8");
        requestHeadMap.put("internalProxyParam", JSON.toJSONString(rightsJumpReq));
        requestHeadMap.put("internalProxySign", internalSign);

        String response = HttpClientUtils.doHttpPostForm(CMIC_FORWARD_API_URL,requestHeadMap,body,"");
        return response;
    }
}
