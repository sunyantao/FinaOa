package com.fr.data;

import com.alibaba.fastjson.JSONObject;
import com.fr.script.Calculator;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CustomSubmitJob extends DefinedSubmitJob {
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
    /**
     * 分隔符
     */
    public static final String FILE_BOUNDARY = "-----";

    /**
     * @param file
     * @return
     */
    private static byte[] getStartData(File file) throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("--");
        sb.append(FILE_BOUNDARY);
        sb.append("\r\n");
        sb.append("Content-Disposition: form-data; \r\n name=\"1\"; filename=\"" + file.getName() + "\"\r\n");
        sb.append("Content-Type: msoffice\r\n\r\n");
        return sb.toString().getBytes("UTF-8");
    }
    public static String upload(String tokenid){
        File uploadFile=new File("E:/GettingStarted.png");
        StringBuffer sb = new StringBuffer();
        try {
            URL preUrl = new URL(OA_URL +"/attachment/attachment?token="+tokenid+"&applicationCategory=0&extensions=&firstSave=true");
            // 设置请求头
            HttpURLConnection hc = (HttpURLConnection)preUrl.openConnection();
            hc.setDoOutput(true);
            hc.setUseCaches(false);
            hc.setRequestProperty("contentType", "charset=utf-8");
            hc.setRequestMethod("POST");
            hc.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + FILE_BOUNDARY);
            //
            DataOutputStream dos = new DataOutputStream(hc.getOutputStream());
            dos.write(getStartData(uploadFile));
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(uploadFile));
            int data = 0;
            while((data = input.read()) != -1) {
                dos.write(data);
            }
            dos.write(("\r\n--" + FILE_BOUNDARY + "--\r\n").getBytes());
            dos.flush();
            dos.close();

            InputStream is = hc.getInputStream();
            int ch;
            while((ch = is.read()) != -1) {
                sb.append((char)ch);
            }
            if(is != null)
                is.close();
            if(input != null)
                input.close();
            if(sb.toString() != null && !"".equals(sb.toString())) {
                System.out.println("附件上传成功！！ID:" + sb.toString());
            } else {
                System.out.println("附件上传失败！！");
            }
        }catch(Exception e) {
            System.out.println("附件上传失败！！错误信息：" + e.getMessage());
            e.printStackTrace();
        }
        return sb.toString();
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

    public static void main(String[] args) {
        String token = getToken();
        System.out.println(token);
        Map<String, Object> map = new HashMap<String, Object>(3);
        String html = upload(token);
        System.out.println(html);
//        Long attachId = Long.parseLong(upload(token));
        //字段数据
        map.put("field1", "addresss");
        map.put("field2", "department");
        map.put("field3", "taskNumber");
        Map<String, Object> maps = new HashMap<String, Object>(2);
        // 发起人登录名
        maps.put("senderLoginName", "test01");
        String json = JSONObject.toJSONString(map);
        // 类型
//        maps.put("transfertype", "json");
        // 表单数据
        maps.put("data", json);
//        maps.put("attachments",new Long[] {attachId});
//        maps.put("formContentAtt",formcontentatt);//表单附件控件

        String BMPUrl = OA_URL + BMP_URL + TEMPLATE_CODE + "?token=" + token;
        String result = httpPost(BMPUrl, maps);
        // 返回流程ID 数字串 ：6894416809679013731
        System.out.println(result);
    }

    public void doJob(Calculator calculator) throws Exception {
        String token = getToken();
        System.out.println(token);
        Map<String, Object> map = new HashMap<String, Object>(3);

        Map<String, String> values = calculator.getAttribute(PROPERTY_VALUE);
        String signUser = values.get("signUser");
        String address = values.get("address");
        String department = values.get("department");
        String taskNumber = values.get("taskNumber");

        //字段数据
        map.put("field1", address);
        map.put("field2", department);
        map.put("field2", taskNumber);
        Map<String, Object> maps = new HashMap<String, Object>(2);
        // 发起人登录名
        maps.put("senderLoginName", signUser);
        String json = JSONObject.toJSONString(map);
        // 类型
        maps.put("transfertype", "json");
        // 表单数据
        maps.put("data", json);

        String BMPUrl = OA_URL + BMP_URL + TEMPLATE_CODE + "?token=" + token;
        String result = httpPost(BMPUrl, maps);
        // 返回流程ID 数字串 ：6894416809679013731
        System.out.println(result);
    }
}