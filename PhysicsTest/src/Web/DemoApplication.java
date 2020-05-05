package Web;

import Exceptions.WrongEncodeException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
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
    public static byte[] httpConnection() throws IOException {
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
        String[] split = code.split(",");
        byte[] bytes;
        if(split.length==2){
            bytes = Base64.getDecoder().decode(split[1]);
        }
        else
        {
            throw new WrongEncodeException();
        }
        File file = new File("./","Image.jpeg");
        file.createNewFile();
        try (OutputStream outputstream = new FileOutputStream(file)) {
            outputstream.write(bytes);
        }
        System.out.println("code = " + code);
        System.out.println("scene = " + scene);
        WebSocketClient.start(scene);
        return bytes;
    }
}
