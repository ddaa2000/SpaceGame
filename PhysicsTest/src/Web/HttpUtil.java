package cn.ac.mryao.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * http工具类
 *
 * @author Mr.YAO
 */
public class HttpUtil {

    private static final CloseableHttpClient httpClient = HttpClients.createDefault();

    public static String getForString(String uri) throws IOException {
        HttpGet httpGet = new HttpGet(uri);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity);
        EntityUtils.consume(entity);
        return s;
    }

    public static String postForString(String uri, String params) throws IOException {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-type","application/json");
        StringEntity stringEntity = new StringEntity(params, "utf-8");
        stringEntity.setContentEncoding("UTF-8");
        httpPost.setEntity(stringEntity);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity);
        EntityUtils.consume(entity);
        return s;
    }
}
