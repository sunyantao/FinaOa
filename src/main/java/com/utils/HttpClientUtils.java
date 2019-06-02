package com.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author sunyantao
 * @version V1.0
 * @Title HttpClientUtils.java
 * @Package com.utils
 * @Description http工具类
 * @date 2019/3/12
 */
public class HttpClientUtils {
    //设置默认超时时间为60s
    public static final int DEFAULT_TIME_OUT = 60 * 1000;

    public static String httpPost(String url, Map<String, String> params, String charset, int timeout) {

        if (url == null || url.equals("")) {
            return null;
        }

        String result = null;

        //超时设置
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();

        //参数组装
        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            pairs.add(new BasicNameValuePair(key, formatStr(value)));
        }

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = null;
        String responseBody = null;
        CloseableHttpResponse response = null;

        try {
            httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairs);
            urlEncodedFormEntity.setContentType("application/json");
            httpPost.setEntity(urlEncodedFormEntity);
            httpPost.setHeader("Content-type", "application/json");
            response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                httpPost.abort();
                throw new RuntimeException("HttpClient,error status code :" + statusCode);
            }

            HttpEntity entity = response.getEntity();
            responseBody = EntityUtils.toString(entity, charset);
            result = responseBody;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接,释放资源
                if (response != null) {
                    response.close();
                }
                if (httpClient != null) {
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    /**
     * 发送post请求，参数用map接收
     *
     * @param strURL 地址
     * @param paramMap 参数
     * @return 返回值
     */
    public static String postMap(String strURL, Map<String, String> paramMap) {
        OutputStreamWriter out = null;
        InputStream is = null;
        String result = null;
        try {
            // 创建连接
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            // 设置请求方式
            connection.setRequestMethod("POST");
            // 设置接收数据的格式
            connection.setRequestProperty("Accept", "application/json");
            // 设置发送数据的格式
            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("token","6bca18b3-6d8b-441e-aa5c-3caa95daa2e1");
            connection.connect();
            // utf-8编码
            out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            out.append(JSONObject.toJSONString(paramMap));
            out.flush();
            out.close();
            // 读取响应
            is = connection.getInputStream();
            // 获取长度
            int length = connection.getContentLength();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[512];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                result = new String(data, "UTF-8");
                System.out.println("主机返回:" + result);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public static String uploadFile(String tokenid,String url){
        File uploadFile=new File("D:/test.doc");
        StringBuffer sb = new StringBuffer();
        try {
            URL preUrl = new URL("http://10.2.8.17/seeyon/rest/attachment?token="+tokenid+"&applicationCategory=0&extensions=&firstSave=true");
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
    /** 分隔符 */
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


    public static String formatStr(String text) {
        return (text == null ? "" : text.trim());
    }
}
