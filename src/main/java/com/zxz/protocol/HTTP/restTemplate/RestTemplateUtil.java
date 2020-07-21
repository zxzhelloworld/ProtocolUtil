package com.zxz.protocol.HTTP.restTemplate;

import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Author: zhangxiaozhou
 * @Date: 2020/7/20 15:18
 * @Des:
 */
public class RestTemplateUtil {
    /**
     * 实际执行请求的template
     */
    private static RestTemplate restTemplate=new RestTemplate();

    /**
     * json请求
     * @param url
     * @param entity
     * @param cls
     * @param <T>
     * @return
     */
    public static <T>T post(String url, Object entity, Class<T> cls){
        HttpEntity<Object> request = new HttpEntity<>(entity);
        return executePost(url,request,cls);
    }

    /**
     * 表单请求
     * @param url
     * @param params
     * @param cls
     * @param <T>
     * @return
     */
    public static <T>T formPost(String url, MultiValueMap<String, Object> params, Class<T> cls){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<Object> request = new HttpEntity<>(params,headers);
        return executePost(url,request,cls);
    }

    /**
     * post 请求
     * @param url
     * @param request
     * @param cls
     * @param <T>
     * @return
     */
    private static <T>T executePost(String url, HttpEntity<Object> request, Class<T> cls){
        ResponseEntity<T> exchange = restTemplate.exchange(url, HttpMethod.POST, request, cls);
        return exchange.getBody();
    }

    /**
     * get 请求
     * @param url
     * @param params
     * @param cls
     * @param <T>
     * @return
     */
    public static  <T>T get(String url, Map<String,Object> params, Class<T> cls){
        StringBuilder urlBuiler=new StringBuilder(url);
        if(!CollectionUtils.isEmpty(params)){
            urlBuiler.append("?");
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                urlBuiler.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
            }
        }
        return restTemplate.getForObject(urlBuiler.toString(), cls);
    }
}
