package com.coupon.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * HttpClient工具类
 */
@Slf4j
public class HttpClientUtils {


    // 默认字符集
    private static final String ENCODING = "UTF-8";

    /**
     * @Title: sendPost
     * @Description: TODO(发送post请求)
     * @param url 请求地址
     * @param headers 请求头
     * @param data 请求实体
     * @param encoding 字符集
     * @return String
     * @throws
     */
    public static String sendPost(String url, Map<String, String> headers, JSONObject data, String encoding) {
        log.info("进入post请求方法...");
        log.info("请求入参：URL= " + url);
        log.info("请求入参：headers=" + JSON.toJSONString(headers));
        log.info("请求入参：data=" + JSON.toJSONString(data));
        // 请求返回结果
        String resultJson = null;
        // 创建Client
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建HttpPost对象
        HttpPost httpPost = new HttpPost();

        try {
            // 设置请求地址
            httpPost.setURI(new URI(url));
            // 设置请求头
            if (headers != null) {
                Header[] allHeader = new BasicHeader[headers.size()];
                int i = 0;
                for (Map.Entry<String, String> entry: headers.entrySet()){
                    allHeader[i] = new BasicHeader(entry.getKey(), entry.getValue());
                    i++;
                }
                httpPost.setHeaders(allHeader);
            }
            // 设置实体
            httpPost.setEntity(new StringEntity(JSON.toJSONString(data)));
            // 发送请求,返回响应对象
            CloseableHttpResponse response = client.execute(httpPost);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            if (status == HttpStatus.SC_OK) {
                // 获取响应结果
                resultJson = EntityUtils.toString(response.getEntity(), encoding);
            } else {
                log.error("响应失败，状态码：" + status);
            }

        } catch (Exception e) {
            log.error("发送post请求失败", e);
        } finally {
            httpPost.releaseConnection();
        }
        return resultJson;
    }

    /**
     * @Title: sendPost
     * @Description: TODO(发送post请求，请求数据默认使用json格式，默认使用UTF-8编码)
     * @param url 请求地址
     * @param data 请求实体
     * @return String
     * @throws
     */
    public static String sendPost(String url, JSONObject data) {
        // 设置默认请求头
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");

        return sendPost(url, headers, data, ENCODING);
    }

    /**
     * @Title: sendPost
     * @Description: TODO(发送post请求，请求数据默认使用json格式，默认使用UTF-8编码)
     * @param url 请求地址
     * @param params 请求实体
     * @return String
     * @throws
     */
    public static String sendPost(String url,Map<String,Object> params){
        // 设置默认请求头
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("content-type", "application/json");
        // 将map转成json
        JSONObject data = JSONObject.parseObject(JSON.toJSONString(params));
        return sendPost(url,headers,data,ENCODING);
    }

    /**
     * @Title: sendPost
     * @Description: TODO(发送post请求，请求数据默认使用UTF-8编码)
     * @param url 请求地址
     * @param headers 请求头
     * @param data 请求实体
     * @return String
     * @throws
     */
    public static String sendPost(String url, Map<String, String> headers, JSONObject data) {
        return sendPost(url, headers, data, ENCODING);
    }

    /**
     * @Title: sendPost
     * @Description:(发送post请求，请求数据默认使用UTF-8编码)
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求实体
     * @return String
     * @throws
     */
    public static String sendPost(String url,Map<String,String> headers,Map<String,String> params){
        // 将map转成json
        JSONObject data = JSONObject.parseObject(JSON.toJSONString(params));
        return sendPost(url,headers,data,ENCODING);
    }

    /**
     * @Title: sendGet
     * @Description: TODO(发送get请求)
     * @param url 请求地址
     * @param params 请求参数
     * @param encoding 编码
     * @return String
     * @throws
     */
    public static String sendGet(String url,Map<String,Object> params,String encoding){
        log.info("进入get请求方法...");
        log.info("请求入参：URL= " + url);
        log.info("请求入参：params=" + JSON.toJSONString(params));
        // 请求结果
        String resultJson = null;
        // 创建client
        CloseableHttpClient client = HttpClients.createDefault();
        // 创建HttpGet
        HttpGet httpGet = new HttpGet();
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            // 封装参数
            if(params != null){
                for (String key : params.keySet()) {
                    builder.addParameter(key, params.get(key).toString());
                }
            }
            URI uri = builder.build();
            log.info("请求地址："+ uri);
            // 设置请求地址
            httpGet.setURI(uri);
            // 发送请求，返回响应对象
            CloseableHttpResponse response = client.execute(httpGet);
            // 获取响应状态
            int status = response.getStatusLine().getStatusCode();
            if(status == HttpStatus.SC_OK){
                // 获取响应数据
                resultJson = EntityUtils.toString(response.getEntity(), encoding);
            }else{
                log.error("响应失败，状态码：" + status);
            }
        } catch (Exception e) {
            log.error("发送get请求失败",e);
        } finally {
            httpGet.releaseConnection();
        }
        return resultJson;
    }
    /**
     * @Title: sendGet
     * @Description: TODO(发送get请求)
     * @param url 请求地址
     * @param params 请求参数
     * @return String
     * @throws
     */
    public static String sendGet(String url,Map<String,Object> params){
        return sendGet(url,params,ENCODING);
    }
    /**
     * @Title: sendGet
     * @Description: TODO(发送get请求)
     * @param url 请求地址
     * @return String
     */
    public static String sendGet(String url){
        return sendGet(url,null,ENCODING);
    }

}
