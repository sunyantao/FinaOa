package com.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class BPMDemo {

    private final static String OA_URL = "http://10.2.8.17/seeyon/rest"; // OA地址
    //	private final static String OA_URL = "http://127.0.0.1:8080/seeyon/rest"; // OA地址
    private final static String TOKEN_URL = "/token"; // 获取token 链接
    private final static String REST_NAME = "restzh"; // rest 账号
    private final static String REST_PASSWORD = "123456"; // rest 密码
    private final static String TEMPLATE_CODE = "/ceshi"; // 表单模板编号
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
//        JSONObject json = JSONObject.parseObject(token);
//        String id = json.getString("id");
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
                InputStream is = null;
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

    public static void main(String[] args) {
        String token = getToken();
        System.out.println(token);
        Map<String, Object> map = new HashMap<String, Object>();
        /**
         * 字段数据
         */
//        map.put("报销人单位", "-4487763725295008330");
//        map.put("收支项目", "4-差旅费");
//        map.put("报销人", "-5395521423439542260");
//        map.put("事项申请单", "0001ZZ1000000008Z41S2");
//        map.put("单据编号", "264X2018121400877");
        map.put("field1", "数据1");
        map.put("field2", "数据2");
        String json = JSONObject.toJSONString(map);

        Map<String, Object> maps = new HashMap<String, Object>();
        maps.put("senderLoginName", "ceshi01");// 发起人登录名
//        maps.put("transfertype", "json");// 类型
//        maps.put("subject", "发起流程");// 主题
        maps.put("data", "测试流程发起");// 表单数据

        /**
         * 重复表数据，可以随意添加
         */
        List<Map<String, Object>> lists = new ArrayList<Map<String, Object>>();
        Map<String, Object> sub = new HashMap<String, Object>();
        sub.put("field3", "数据3");
        sub.put("field4", "数据4");
        sub.put("field5", "数据5");
        lists.add(sub);
        Map<String, Object> sub2 = new HashMap<String, Object>();
        sub2.put("field6", "数据6");
        lists.add(sub2);
        Map<String, Object> sub3 = new HashMap<String, Object>();
        sub3.put("field10", "数据10");
        sub3.put("field11", "数据11");
        lists.add(sub3);
//        maps.put("sub", lists); // 重复表数据

        String BMPUrl = OA_URL + BMP_URL + TEMPLATE_CODE + "?token=" + token;
        String result = httpPost(BMPUrl, maps);
        System.out.println(result); // 返回流程ID 数字串 ：6894416809679013731
    }
}
