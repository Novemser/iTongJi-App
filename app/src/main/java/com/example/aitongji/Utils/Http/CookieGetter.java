package com.example.aitongji.Utils.Http;

import com.example.aitongji.Utils.Managers.ResourceManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Novemser on 2016/11/14.
 */
public class CookieGetter implements INetResourceGetter {
    public static void main(String[] args) {

    }

    private final String step1 = "http://4m3.tongji.edu.cn/eams/samlCheck";
    private Map<String, String> postData = new HashMap<>();
    private Map<String, String> cookies = new HashMap<>();

    @Override
    public void loadData() throws Exception {
        cookies.clear();
        postData.clear();

        Connection connection = Jsoup.connect(step1);
        Connection.Response response = connection.execute();

        Document doc = connection.get();
        for (Map.Entry<String, String> entry : response.cookies().entrySet()) {
            cookies.put(entry.getKey(), entry.getValue());
        }

        String step2 = doc.select("meta").attr("content").substring(6);

        connection = Jsoup
                .connect(step2);
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            connection.cookie(entry.getKey(), entry.getValue());
        }

        response = connection.execute();
        doc = response.parse();
        for (Map.Entry<String, String> entry : response.cookies().entrySet()) {
            cookies.put(entry.getKey(), entry.getValue());
        }

        String step3 = "https://ids.tongji.edu.cn:8443" + doc.select("form").attr("action");

        connection = Jsoup
                .connect(step3);
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            connection.cookie(entry.getKey(), entry.getValue());
        }

        response = connection.execute();
        doc = response.parse();
        for (Map.Entry<String, String> entry : response.cookies().entrySet()) {
            cookies.put(entry.getKey(), entry.getValue());
        }

        String step4 = doc.select("form").attr("action");

        postData.put("option", "credential");
        postData.put("Ecom_User_ID", ResourceManager.getInstance().getUserName());
        postData.put("Ecom_Password", ResourceManager.getInstance().getUserPwd());
        postData.put("submit", "登录");

        connection = Jsoup
                .connect(step4)
                .method(Connection.Method.POST)
                .data(postData);
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            connection.cookie(entry.getKey(), entry.getValue());
        }

        response = connection.execute();
        Pattern pattern = Pattern.compile("top.location.href=\'(.*?)\'");
        Matcher matcher = pattern.matcher(response.parse().html());
        for (Map.Entry<String, String> entry : response.cookies().entrySet()) {
            cookies.put(entry.getKey(), entry.getValue());
        }

        if (matcher.find()) {
            String step5 = matcher.group(1);

            connection = Jsoup.connect(step5);
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                connection.cookie(entry.getKey(), entry.getValue());
            }
            response = connection.execute();
            doc = response.parse();

            for (Map.Entry<String, String> entry : response.cookies().entrySet()) {
                cookies.put(entry.getKey(), entry.getValue());
            }


            String step6 = doc.select("form").attr("action");

            String SAMLResponse = doc.select("form").select("input").get(0).attr("value");
            String RelayState = doc.select("form").select("input").get(1).attr("value");

            postData.put("SAMLResponse", SAMLResponse);
            postData.put("RelayState", RelayState);
            // 默认Servername必须改成是s60
            connection = Jsoup
                    .connect(step6)
                    .method(Connection.Method.POST)
                    .data(postData);
            for (Map.Entry<String, String> entry : cookies.entrySet()) {
                connection.cookie(entry.getKey(), entry.getValue());
            }
            response = connection.execute();
            doc = response.parse();
            System.out.println(doc);
        }
    }

    @Override
    public void refresh() {

    }
}
