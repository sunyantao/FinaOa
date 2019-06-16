package com.fr.data;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUpdate {
    public static void main(String[] args) {
        File uploadFile=new File("E:/GettingStarted.png");
        String tokenid = "";
        try {
            URL preUrl = new URL("http://127.0.0.1:8080/seeyon/rest/attachment?token="+tokenid+"&applicationCategory=0&extensions=&firstSave=true");
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
            StringBuffer sb = new StringBuffer();
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

}
