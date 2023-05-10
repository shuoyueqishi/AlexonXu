package com.alexon.http.utils;

import com.alexon.model.utils.AppContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Map;

@Component
@DependsOn("appContextUtil")
@Slf4j
public class HttpUtil {

    private static RestTemplate restTemplate;

    private HttpUtil() {
    }

    @PostConstruct
    public void init() {
        if (restTemplate == null) {
            restTemplate = AppContextUtil.getBean("remoteRestTemplate", RestTemplate.class);
        }
    }

    /**
     * get请求
     *
     * @param url 请求地址 如： http://127.0.0.1:80
     * @param params 参数
     * @return 返回值
     */
    public static String doGet(String url, Map<String, String> params) {
        url = buildGetParams(url, params);
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        return responseEntity.getBody();
    }

    /**
     * get请求带Header
     *
     * @param url 请求地址 如： http://127.0.0.1:80
     * @param params 参数
     * @return 返回值
     */
    public static Object doGetWithHeader(String url, Map<String, String> params) {
        url = buildGetParams(url, params);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity=new HttpEntity<>("parameters",headers);
        ResponseEntity<Object> exchange = restTemplate.exchange(url, HttpMethod.GET, httpEntity, Object.class, params);
        return exchange.getBody();
    }

    private static String buildGetParams(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && !params.isEmpty()) {
            sb.append("/?");
            for (Map.Entry<String, String> map : params.entrySet()) {
                sb.append(map.getKey())
                        .append("=")
                        .append(map.getValue())
                        .append("&");
            }
            url = sb.substring(0, sb.length() - 1);
        }
        return url;
    }

    /**
     * post请求
     *
     * @param url 如： http://127.0.0.1:80
     * @param httpHeaders httpHeaders
     * @param body body
     * @return 返回值
     */
    public static String doPost(String url, HttpHeaders httpHeaders, Map<String, String> body) {
        MediaType type = MediaType.parseMediaType("application/json;charset=UTF-8");
        httpHeaders.setContentType(type);
        HttpEntity<Map<String, String>> httpEntity = new HttpEntity<>(body, httpHeaders);
        ResponseEntity<String> apiResponse = restTemplate.postForEntity(url, httpEntity, String.class);
        return apiResponse.getBody();
    }

    /**
     * 删除请求
     * @param url URL
     * @param param 参数
     */
    public static void doDelete(String url,Map<String,String> param) {
        restTemplate.delete(url,param);
    }

    /**
     * PUT请求
     * @param url URL
     * @param body 请求体
     */
    public static void doPut(String url,Map<String,String> body){
        restTemplate.put(url,body);
    }

    /**
     * 拼接GET请求的路径查询参数
     *
     * @param obj obj
     * @return 参数拼接结果
     */
    public static String buildPathUrl(Object obj) {
        Class cls = obj.getClass();
        Field[] fields = cls.getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append("?");
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            try {
                if (field.get(obj) != null) {
                    log.debug("属性名:" + field.getName() + " 属性值:" + field.get(obj));
                    sb.append(field.getName()).append("=").append(field.get(obj)).append("&");
                }
            } catch (IllegalAccessException e) {
                log.error("get field error:" + e);
            }
        }
        sb.deleteCharAt(sb.lastIndexOf("&"));
        return sb.toString();
    }
}


