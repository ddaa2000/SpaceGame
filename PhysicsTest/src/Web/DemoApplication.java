package Web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;

/**
 * main方法：程序入口
 * 请在此类中处理
 * 显示小程序码 事件
 *
 * @author Mr.YAO
 */
public class DemoApplication {
    public static void httpConnection() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://auth.opengrade.cn/wxa/code");
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();
        String s = EntityUtils.toString(entity);
        EntityUtils.consume(entity);
        Result<WxaCode> wxaCodeResult = JSON.parseObject(s, new TypeReference<Result<WxaCode>>() {
        });
        WxaCode wxaCode = wxaCodeResult.getData();
        String scene = wxaCode.getScene();
        String code = wxaCode.getCode();
        System.out.println("code = " + code);
        System.out.println("scene = " + scene);
        WebSocketClient.start(scene);
    }
}
