import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.seeyon.client.CTPRestClient;
import com.seeyon.client.CTPServiceClientManager;
import com.utils.HttpClientUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class JunitTest {
    @Test
    public void loginTest(){
        String url = "http://10.2.8.17/seeyon/rest/token";
        Map<String,String> data = new HashMap();
        data.put("userName", "restzh");
        data.put("password", "123456");
        String responseBody = HttpClientUtils.httpPost(url, data, "utf-8", HttpClientUtils.DEFAULT_TIME_OUT);
        Map<String, String> responseMap = JSON.parseObject(responseBody, new TypeReference<TreeMap<String, String>>() {});
        String toJSONString = JSONObject.toJSONString(responseMap);
        System.out.println(toJSONString);
    }
    @Test
    public void aTest(){
        CTPServiceClientManager clientManager = CTPServiceClientManager.getInstance("http://10.2.8.17");
        CTPRestClient client = clientManager.getRestClient();
        final String token = client.get("token/restzh/123456", String.class,"text/plain");
        System.out.println(token);
        if(client.authenticate("restzh", "123456")){

        }
        Map data = new HashMap() {
            {
//                put("templateCode","ceshi");
                put("token",token);
                put("senderLoginName", "restzh");
                put("subject", "这个是用Map方式发的");
                put("data", "正文内容");
            }
        };
        Long flowId1 = client.post("flow/ceshi" ,data, Long.class);
        System.out.println(flowId1);
        /*String url="http://10.2.8.17";
        String restLoginName="restzh";
        String restPassword="123456";
        CTPRestClient client= MeetingDistributeUtil.getRestResource();
        final String token = client.get("token/" + restLoginName + "/" + restPassword, String.class,"text/plain");
        final String senderLoginName=orgManager.getMemberById(startMemberId).getLoginName();
        String templateCode=getTempleteCodeById(templetId);
        if(client.authenticate(restLoginName, restPassword)){
            String data = client.get("flow/data/"+summaryId, String.class);//-6074085048046957774为已发流程ID
            final String dataXml=MeetingDistributeUtil.getDataXMl(data, memberId);
            @SuppressWarnings({ "serial", "unchecked" })
            Map info = new HashMap() {
                {
                    put("subject", "ceshi");
                    put("token", token);
                    put("data", dataXml);
                    put("attachments",fieldIds);
                    put("senderLoginName", senderLoginName);
                    put("param",type);
                }
            };
            Long flowId1 = client.post("flow/"+templateCode ,info, Long.class);
        }*/
    }
    @Test
    public void login2Test(){
        String strURL = "http://10.2.8.17/seeyon/rest/token";
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("userName", "restzh");
        paramMap.put("password", "123456");
        String s = HttpClientUtils.postMap(strURL, paramMap);
        System.out.println(s);
    }
    @Test
    public void startFlowTest(){
        String strURL = "http://10.2.8.17/seeyon/rest/flow/ceshi?token=e596a2a9-ce37-4c3e-8d85-1fca9add59b6";
        Map<String, String> paramMap = new HashMap<String, String>();
        //token--必传
//        paramMap.put("token", "e596a2a9-ce37-4c3e-8d85-1fca9add59b6");
        //模板编号--必传
//        paramMap.put("templateCode", "ceshi");
        //登录名--必传
        paramMap.put("senderLoginName", "restzh");
        //协同的标题--必传
        paramMap.put("subject", "测试发起");
        //HTML正文流程为html内容--必传
        paramMap.put("data", "www.baidu.com");
        //附件，Long型List，值为附件的Id
//        paramMap.put("attachments", "restzh");
        //为控制是否流程发送。0：缺省值，发送，进入下一节点的待办（如果需要选人则保存到待发）1：不发送，保存到待发
//        paramMap.put("param", "restzh");
        //(V6.1增加)data格式，xml:表示data为XML格式；json：表示data为json格式
//        paramMap.put("transfertype", "restzh");
        //(V6.1增加)(V6.1增加) 表单附件 组件传入ID参数
//        paramMap.put("formContentAtt", "restzh");
        //(V6.1增加)发起人单位编码（用于发起人兼职多单位情况，用不同单位角色发起流程）
//        paramMap.put("accountCode", "restzh");
        String s = HttpClientUtils.postMap(strURL, paramMap);
        System.out.println(s);
    }
    @Test
    public void uploadAttachTest(){
        String strURL = "http://10.2.8.17/seeyon/rest/attachment/attachment";
        Map<String, String> paramMap = new HashMap<String, String>();
        //token--必传
        paramMap.put("token", "123456");
        //附件--必传
        paramMap.put("file", "restzh");
        //应用分类--必传
        paramMap.put("applicationCategory", "0");
        //
        paramMap.put("extensions", "");
        //首次保存--必传
        paramMap.put("firstSave", "true");
        String s = HttpClientUtils.postMap(strURL, paramMap);
        System.out.println(s);
    }
    @Test
    public void downloadFileTest(){
        String strURL = "http://ip:port/seeyon/rest/attachment/file/{ctp_file_ID}?fileName={文件名}&token={}";
        Map<String, String> paramMap = new HashMap<String, String>();
        //token--必传
        paramMap.put("token", "123456");
        //附件--必传
        paramMap.put("file", "restzh");
        //应用分类--必传
        paramMap.put("applicationCategory", "0");
        //
        paramMap.put("extensions", "");
        //首次保存--必传
        paramMap.put("firstSave", "true");
        String s = HttpClientUtils.postMap(strURL, paramMap);
        System.out.println(s);
    }
}
