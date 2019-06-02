package com.fr.data;

import com.alibaba.fastjson.JSONObject;
import com.fr.script.AbstractFunction;
import com.fr.script.AbstractFunction;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestSubmitFunc extends AbstractFunction {
    private final static String OA_URL = "http://10.2.8.17/seeyon/rest"; // OA地址
    private final static String TOKEN_URL = "/token"; // 获取token 链接
    private final static String REST_NAME = "restzh"; // rest 账号
    private final static String REST_PASSWORD = "123456"; // rest 密码
    private final static String TEMPLATE_CODE = "/testflow"; // 表单模板编号
    private final static String BMP_URL = "/flow"; // 发起表单流程链接

    public static String getToken() {
        String token = "";
        HttpURLConnection connection = null;
        InputStream in = null;
        BufferedReader br = null;
        try {
            URL url = new URL(OA_URL + TOKEN_URL + "/" + REST_NAME + "/" + REST_PASSWORD);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                in = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                StringBuffer sb = new StringBuffer();
                String temp = "";
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
                token = sb.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
            connection.disconnect();
        }
        return token;
    }

    public static String httpPost(String BMPUrl, Map<String, Object> maps) {
        String result = "";
        if (maps == null) {
            return null;
        } else {
            try {
                URL url = new URL(BMPUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setInstanceFollowRedirects(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                connection.connect();
                OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
                String json = JSONObject.toJSONString(maps);
                System.out.println("" + json);
                out.append(json);
                out.flush();
                out.close();
                InputStream is;
                if (connection.getResponseCode() == 200) {
                    is = connection.getInputStream();
                } else {
                    is = connection.getErrorStream();
                    System.out.println("请求出错，错误编号：" + connection.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                String temp = null;
                StringBuffer sb = new StringBuffer();
                while ((temp = br.readLine()) != null) {
                    sb.append(temp);
                }
                result = sb.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public Object run(Object[] args) {
        String token = getToken();
        Map<String, Object> maps = new HashMap<>(2);
        // 发起人登录名
        maps.put("senderLoginName", "test01");
        // 类型
        maps.put("transfertype", "json");
        // 表单数据
        maps.put("data", "aaaa");

        String BMPUrl = OA_URL + BMP_URL + TEMPLATE_CODE + "?token=" + token;
        String result = httpPost(BMPUrl, maps);
        // 返回流程ID 数字串 ：6894416809679013731
        System.out.println(result);
        return result;
    }
}