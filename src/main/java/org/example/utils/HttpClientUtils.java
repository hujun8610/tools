package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author hujun
 * @version 1.0.0
 * @ClassName HttpClientUtils.java
 * @Description
 * @createTime 2021-01-27 10:29:00
 */
@Slf4j
@Component
public class HttpClientUtils {
    //连接超时：单位ms
    static private int MAX_CONNECT_TIMEOUT = 10000;

    //读取超时：单位ms
    static private int MAX_SO_TIMEOUT = 60000;

    //最大连接数
    static private int MAX_TOTAL_CONS = 2048;

    //单域名最大连接数
    static private int MAX_TOTAL_CONS_PER_HOST = 128;

    private volatile static MultiThreadedHttpConnectionManager connMgr;

    public String doHttpPost(String postUrl, String postXml, Map<String, String> requestHeadMap, String logUniqueFlag) {
        PostMethod post = null;
        try {
            post = new PostMethod(postUrl);
            post.setRequestEntity(new StringRequestEntity(postXml, "utf-8", "utf-8"));
            for (String headKey : requestHeadMap.keySet()) {
                post.setRequestHeader(headKey, requestHeadMap.get(headKey));
            }
            HttpClient httpclient = getHttpClient();
            int result = httpclient.executeMethod(post);
            if (result == HttpStatus.SC_OK) {
                return post.getResponseBodyAsString();
            } else {
                log.error("the response status [{}]", result);
            }
        } catch (Exception e) {
            log.error("{},访问URL连接异常", logUniqueFlag, e);
            throw new RuntimeException("访问URL连接异常:" + postUrl, new RuntimeException());
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        return "";
    }

    public static String doHttpGet(String url, Map<String, String> requestHeadMap) {
        GetMethod getMethod = null;
        try {
            getMethod = new GetMethod(url);
            if(Objects.nonNull(requestHeadMap)){
                for (String headKey : requestHeadMap.keySet()) {
                    getMethod.setRequestHeader(headKey, requestHeadMap.get(headKey));
                }
            }

            HttpClient httpclient = getHttpClient();

            int result = httpclient.executeMethod(getMethod);
            log.info("rightsJumpHttpGet reqUrl is [{}],the response code is {}", url, result);
            if (result == HttpStatus.SC_OK) {
                return getMethod.getResponseBodyAsString();
            } else {
                log.error("the response status [{}]", result);
            }
        } catch (Exception e) {
            log.error("访问URL连接异常:{}", e);
            throw new RuntimeException("访问 URL连接异常：" + url, e);
        } finally {
            if (getMethod != null) {
                getMethod.releaseConnection();
            }
        }
        return "";
    }

    public static String doHttpPostForm(String postUrl, Map<String, String> requestHeadMap, Map<String, Object> body, String logUniqueFlag) throws RuntimeException {
        PostMethod post = null;

        try {
            post = new PostMethod(postUrl);

            if(Objects.nonNull(requestHeadMap.keySet())){
                for (String headKey : requestHeadMap.keySet()) {
                    post.setRequestHeader(headKey, requestHeadMap.get(headKey));
                }
            }

            post.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");
            post.addRequestHeader("Connection", "close");

            List<NameValuePair> params = new ArrayList<>();
            body.forEach((k, v) -> {
                params.add(new NameValuePair(k, String.valueOf(v)));
            });

            NameValuePair[] requestBody = new NameValuePair[params.size()];
            params.toArray(requestBody);
            post.setRequestBody(requestBody);

            HttpClient httpclient = getHttpClient();

            int result = httpclient.executeMethod(post);
            if (result == HttpStatus.SC_OK) {
                return post.getResponseBodyAsString();
            } else {
                log.error("the response status [{}]", result);
            }
        } catch (Exception e) {
            log.error("{},访问URL连接异常:{}", logUniqueFlag, e);
            throw new RuntimeException("访问 URL连接异常：" + postUrl, e);
        } finally {
            if (post != null) {
                post.releaseConnection();
            }
        }
        return "";
    }

    /**
     * 忽略证书的方式获取httpClient
     */
    private static HttpClient getHttpClient() {
        HttpClient httpclient = new HttpClient();
        Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", myhttps);
        HttpClientParams hcp = new HttpClientParams();
        hcp.setSoTimeout(MAX_SO_TIMEOUT);
        hcp.setConnectionManagerTimeout(MAX_CONNECT_TIMEOUT);
        httpclient.setParams(hcp);
        httpclient.setHttpConnectionManager(getConnMgr());
        return httpclient;
    }

    public static MultiThreadedHttpConnectionManager getConnMgr() {

        if (connMgr == null) {
            synchronized (MultiThreadedHttpConnectionManager.class) {
                if (connMgr == null) {
                    connMgr = new MultiThreadedHttpConnectionManager();

                    // 设置client连接池参数
                    HttpConnectionManagerParams params = connMgr.getParams();
                    params.setConnectionTimeout(MAX_CONNECT_TIMEOUT);
                    params.setSoTimeout(MAX_SO_TIMEOUT);

                    //very important!!
                    params.setDefaultMaxConnectionsPerHost(MAX_TOTAL_CONS_PER_HOST);
                    //very important!!
                    params.setMaxTotalConnections(MAX_TOTAL_CONS);

                }
            }
        }
        return connMgr;
    }

}
