package com.tobdev.qywxthird.utils;

import com.alibaba.fastjson.JSONObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URL;
import java.util.Map;

@Configuration
public class RestUtils {

    private static final RestTemplate restTemplate = new RestTemplate();

    public static JSONObject get(String url, Map<String,String> urlParams){

        return get(url);
    }

    //在处理企业微信某些参数时有问题
    public static JSONObject get(String url){
        return get(URI.create(url));
    }

    private static JSONObject get(URI uri){
        ResponseEntity<JSONObject> responseEntity =restTemplate.getForEntity(uri,JSONObject.class);
        serverIsRight(responseEntity);   //判断服务器返回状态码
        return responseEntity.getBody();
    }

    public static JSONObject post(String url,Map<String,String> urlParams,JSONObject json){
        //组装url
        return post(urlToUri(url,urlParams),json);
    }

    public static JSONObject post(String url,JSONObject json){
        //组装url
        //组装ur
        return post(URI.create(url),json);
    }

    private static JSONObject post(URI uri,JSONObject json){
        //组装url
        //组装ur
        //设置提交json格式数据
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<JSONObject> request = new HttpEntity(json, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(uri,request,JSONObject.class);
        serverIsRight(responseEntity);  //判断服务器返回状态码
        return responseEntity.getBody();
    }

    private static URI urlToUri(String url,Map<String,String> urlParams){
        //设置提交json格式数据
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);
        for(Map.Entry<String,String> entry : urlParams.entrySet())  {
            uriBuilder.queryParam((String)entry.getKey(),  (String) entry.getValue()) ;
        }
        return  uriBuilder.build(true).toUri();
    }

    public static JSONObject upload(String url,MultiValueMap formParams){
        RestTemplate restTemplate = new RestTemplate();
        //设置表单提交
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formParams, headers);
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(url,request,JSONObject.class);
        serverIsRight(responseEntity);  //判断服务器返回状态码
        return responseEntity.getBody();
    }

    private static void serverIsRight(ResponseEntity responseEntity){
        if(responseEntity.getStatusCodeValue()==200){
//            System.out.println("服务器请求成功：{}"+responseEntity.getStatusCodeValue());
        }else {
            System.out.println("服务器请求异常：{}"+responseEntity.getStatusCodeValue());
        }
    }


}
