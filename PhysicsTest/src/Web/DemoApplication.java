package Web;

import Exceptions.WrongEncodeException;
import Exceptions.wrongUserInfoException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import mygame.Main;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import states.GameUIState;

/**
 * main方法：程序入口
 * 请在此类中处理
 * 显示小程序码 事件
 *
 * @author Mr.YAO
 */
public class DemoApplication {
    public static UserInfo userInfo = null;
    public static Object mutex = new Object();
    private static String access_token = null;
    private static boolean getUserInfoFromFile(){
        File file = new File("./userInfo");
        if(!file.exists() || !file.canRead())
            return false;
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        }
        String result;
        try {
            result = reader.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        Result<UserInfo> userInfoResult = JSON.parseObject(result, new TypeReference<Result<UserInfo>>(){});
        userInfo = userInfoResult.getData();
        return true;
    }
    public static byte[] registerUser() throws IOException {
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
    
    public static boolean hasLoggedIn(){
        synchronized(mutex){
            if(userInfo == null)
                return false;
            return true;
        }
    }
    public void saveUserInfo() throws IOException{
        File file = new File("./userInfo");
        if(!file.exists() || !file.canRead())
        {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(DemoApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        BufferedWriter writer = null;
        writer = new BufferedWriter(new FileWriter(file));
        String result = JSON.toJSONString(userInfo);
        writer.write(result);
    }
    public static void uploadData(String data)throws wrongUserInfoException{
        if(userInfo == null)
            throw new wrongUserInfoException();
        String returnVal = null;
        try {
            
            returnVal = cn.ac.mryao.http.HttpUtil.postForString("https://game.opengrade.cn/api/archive?access_token=" + access_token + "&userId=" + WebSocketClientHandler.userId, data);
        } catch (IOException ex) {
            Logger.getLogger(GameUIState.class.getName()).log(Level.SEVERE, null, ex);
        }
        Result<Object> objectResult = JSON.parseObject(returnVal, new TypeReference<Result<Object>>() {
        });
        String msg = objectResult.getMsg();
        System.out.println("msg = " + msg);
    }
    public static String downloadData()throws wrongUserInfoException{
        String str3=null;
        try {
            str3 = cn.ac.mryao.http.HttpUtil.getForString("https://game.opengrade.cn/api/archive?access_token=" + access_token + "&userId=" + WebSocketClientHandler.userId);
        } catch (IOException ex) {
            Logger.getLogger(GameUIState.class.getName()).log(Level.SEVERE, null, ex);
        }
        Result<String> loadResult = JSON.parseObject(str3, new TypeReference<Result<String>>() {
        });
        String data = loadResult.getData();
        System.out.println("data = " + data);
        return data;
    }
    public static boolean getAccessToken(){
        String str1=null;
        try {
            str1 = cn.ac.mryao.http.HttpUtil.postForString("https://game.opengrade.cn/api/access_token", "{\"clientId\":\"jmeclient\",\"clientSecret\":\"pkujava2020\"}");
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        Result<String> stringResult = JSON.parseObject(str1, new TypeReference<Result<String>>() {
        });
        access_token = stringResult.getData();
        System.out.println("access_token = " + access_token);
        return true;
   
        
        
    }
}
