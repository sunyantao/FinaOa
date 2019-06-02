import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Title Test.java
 * @Package 
 * @Description 
 * @author sunyantao
 * @date 2019/3/12
 * @version V1.0
 */
public class Test {
    public static void main(String[] args) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
        String format = sdf.format(new Date());
        System.out.println(format);

    }
    /*
    package test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.seeyon.v3x.dee.TransformException;

    public class CallRest {
        public static void main(String[] args) {
            try {
                String isA8 = "1";// 1是A8接口服务器，0是工具端接口服务器
                int timeOut = 30;// 超市时间单位：s
                String url = "http://127.0.0.1:8080";// 服务器地址
                HttpURLConnection conn = null;
                String token = "";
                Map<String, Object> jheader = new HashMap<String, Object>();
                if ("1".equals(isA8)) {
                    String userName = "rest";// A8rest用户（需要用A8system管理员用户创建并授权）
                    String passWord = "123456";// A8rest用户密码
                    // A8接口验证token获取接口地址
                    String getToken = url + "/seeyon/rest/token/" + userName + "/" + passWord;
                    // 创建请求链接
                    conn = (HttpURLConnection) new URL(getToken).openConnection();
                    conn.setRequestMethod("GET");// 设置请求方法
                    conn.setConnectTimeout(timeOut * 1000);// 设置请求超时时间
                    int code = conn.getResponseCode();// 获取请求结果状态码
                    String jsonResult = "";
                    if (code == 200) {// 结果状态码为200表示调用接口成功
                        BufferedReader inStream = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        jsonResult = getResponseString(inStream);
                        token = jsonResult;
                        if (jsonResult.contains("{")) {
                            JSONObject result = JSONObject.parseObject(jsonResult);
                            token = result.getString("id");
                        }
                        jheader.put("token", token);
                        jheader.remove("userName");
                        jheader.remove("passWord");
                    }
                }
                String taskNo = "csdyrw";// 任务编号（任务创建时选填）
                // dee任务调用接口地址
                String getRest = url + "/seeyon/rest/dee/task/" + taskNo;
                conn = (HttpURLConnection) new URL(getRest).openConnection();
                // 设置头消息（存放token、请求参数类型等）
                conn.setRequestProperty("Content-type", "application/json");
                for (Entry<String, Object> entry : jheader.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), (String) entry.getValue());
                }
                String urlType = "POST";// 请求类型（DEE任务调用只有GET[无参]和POST[有参]）
                conn.setRequestMethod(urlType);
                conn.setConnectTimeout(timeOut * 1000);
                conn.setDoOutput(true);// 是否输入参数
                // body参数
                Map<String, Object> bodys = new HashMap<String, Object>();
                bodys.put("printStr", "测试任务已被调用！");
                if (!"GET".equals(urlType)) {
                    SerializerFeature[] features = {};
                    byte[] bypes = JSONObject.toJSONBytes(bodys, features);
                    conn.getOutputStream().write(bypes);// 输入参数
                }
                int code = conn.getResponseCode();
                String jsonResult = "";
                if (code == 200) {
                    BufferedReader inStream = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    jsonResult = getResponseString(inStream);
                } else {
                    throw new TransformException("访问rest接口出错，错误代码：" + code);
                }
                System.out.println(jsonResult);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        *//**
         * 获取返回结果并转化为字符串
         *//*
        private static String getResponseString(BufferedReader inStream) throws Exception {
            String result = "";
            String lines = "";
            while ((lines = inStream.readLine()) != null) {
                result += lines;
            }
            return result;
        }
    }*/
}
