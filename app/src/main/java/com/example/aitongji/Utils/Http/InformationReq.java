package com.example.aitongji.Utils.Http;

import android.os.AsyncTask;
import android.os.Looper;

import com.example.aitongji.Utils.Course.CourseTable;
import com.example.aitongji.Utils.DataBundle;
import com.example.aitongji.Utils.Global;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/2/11.
 */
public class InformationReq {
    private static final String loginUrl_v2 = "http://tjis.tongji.edu.cn:58080/amserver/UI/Login";
    private static final String urlTest1 = "http://4m3.tongji.edu.cn/eams/home!welcome.action";
    private static final String CARD_INFO = "http://urp.tongji.edu.cn/index.portal?.pn=p84_p468_p469";
    private static final String CARD_DETAIL = "http://urp.tongji.edu.cn/index.portal?.p=Znxjb20ud2lzY29tLnBvcnRhbC5zaXRlLnYyLmltcGwuRnJhZ21lbnRXaW5kb3d8ZjEwODJ8dmlld3xub3JtYWx8YWN0aW9uPXBlcnNvbmFsU2FsYXJ5UXVlcnk_";
    private static final String COURSE_INFO = "http://4m3.tongji.edu.cn/eams/courseTableForStd!courseTable.action";
    private static final String COURSE_INFO_IDS = "http://4m3.tongji.edu.cn/eams/courseTableForStd.action";
    private static final String CARD_LOGIN_1 = "http://urp.tongji.edu.cn/";
    private static final String CARD_LOGIN_2 = "http://urp.tongji.edu.cn/index.portal";
    private static final String CARD_LOGIN = "https://ids.tongji.edu.cn:8443/nidp/idff/sso?sid=0&sid=0";
    private static final String CARD_LOGIN_3 = "https://ids.tongji.edu.cn:8443/nidp/idff/sso?sid=0";

    private String time_today = null;
    private String time_week = null;
    private String card_rest = null;
    private String course_IDS = null;
    private String course_table_str = null;
    private String semester = null;
    private ArrayList<String> info_title = new ArrayList<>();
    private ArrayList<String> info_time = new ArrayList<>();
    private ArrayList<String> info_id = new ArrayList<>();

    private DataBundle dataBundle;

    public InformationReq(final String username, final String password, final SuccessCallback successCallback, final FailureCallback failureCallback) {
        new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... params) {
                Map<String, String> postValue = new HashMap<>();
                postValue.put("goto", "http://4m3.tongji.edu.cn:80/eams/home.action");
                postValue.put("gotoOnFail", "http://4m3.tongji.edu.cn:80/eams/login.action?error=1&username=333&password=37693cfc748049e45d87b8c7d8b9aacd");
                postValue.put("Login.Token1", username);
                postValue.put("Login.Token2", password);
                postValue.put("session_locale", "zh_CN");
                postValue.put("submitBtn", "登陆");

                Connection.Response response;
                try {
                    response = Jsoup.connect(loginUrl_v2).method(Connection.Method.POST).data(postValue).timeout(10000).execute();
                    Pattern pattern = Pattern.compile("用户和密码错误");
                    Matcher matcher = pattern.matcher(response.parse().html());
                    if (matcher.find()) {
                        return false;
                    }

                    // 登陆后保存cookies
                    Map<String, String> cookies = response.cookies();

                    // 匹配第几周
                    final Connection connect = Jsoup.connect(urlTest1);
                    for (Map.Entry<String, String> entry : cookies.entrySet()) {
                        connect.cookie(entry.getKey(), entry.getValue());
                    }
                    response = connect.method(Connection.Method.GET).timeout(10000).execute();
                    Document doc = response.parse();
                    Elements elements = doc.body().getElementsByClass("modulebody");
                    pattern = Pattern.compile("第(.+?)周");
                    matcher = pattern.matcher(elements.text());
                    if (matcher.find()) {
                        time_week = matcher.group(1);
                    }

                    // 匹配信息通知的id
                    pattern = Pattern.compile("getNewNoticeInfo\\('(.+?)'\\);");
                    matcher = pattern.matcher(response.body());
                    while (matcher.find()) {
                        info_id.add(matcher.group(1));
                    }

                    // 异步请求绩点
                    final SyncHttpClient syncHttpClient = new SyncHttpClient();

                    // 设置Cookie容器，每次请求都需要清除以前保留的cookie
                    PersistentCookieStore persistentCookieStore = new PersistentCookieStore(Global.getContext());
                    persistentCookieStore.clear();
                    syncHttpClient.setCookieStore(persistentCookieStore);
                    syncHttpClient.get(CARD_LOGIN_1, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            System.out.println("Suc:" + statusCode);
                            syncHttpClient.get(CARD_LOGIN_2, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                                    final RequestParams params = new RequestParams();
                                    params.put("option", "credential");
                                    params.put("target", "http://urp.tongji.edu.cn/index.portal");
                                    params.put("Ecom_User_ID", username);
                                    params.put("Ecom_Password", password);
                                    params.put("submit", "登陆");

                                    syncHttpClient.post(CARD_LOGIN, params, new AsyncHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                            System.out.println("Succeed:" + statusCode);
//                                            for (Header header : headers) {
//                                                System.out.println(header.getName() + " " + header.getValue());
//                                            }

                                            syncHttpClient.get(CARD_LOGIN_3, new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                    // 登陆成功
//                                                    System.out.println("Succeed:" + statusCode);
//                                                    for (Header header : headers) {
//                                                        System.out.println(header.getName() + " " + header.getValue());
//                                                    }
//                                                    System.out.println(new String(responseBody));

                                                    syncHttpClient.get(CARD_INFO, new AsyncHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                                                            System.out.println("Card info getting Succeed:" + statusCode);
//                                                            System.out.println(new String(responseBody));

                                                            CharSequence card_info_raw = Jsoup.parse(new String(responseBody))
                                                                    .body().getElementsByClass("portletContent").html();
                                                            Pattern pattern = Pattern.compile("<td>(.+?)</td>");
                                                            Matcher matcher = pattern.matcher(card_info_raw);
                                                            int cnt = 0;
                                                            while (matcher.find()) {
                                                                if (cnt == 12) {
                                                                    card_rest = matcher.group(1);
                                                                }
                                                                cnt++;
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                                                            System.out.println("Card info getting failed:" + statusCode);
//                                                            error.printStackTrace();

                                                        }
                                                    });
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                                                    System.out.println("Fail:" + statusCode);
//                                                    error.printStackTrace();

                                                }
                                            });

                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                                            System.out.println("Fail:" + statusCode);
//                                            error.printStackTrace();

                                        }
                                    });

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                                    System.out.println("Fai:" + statusCode);
//                                    error.printStackTrace();

                                }
                            });
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                            System.out.println("Fai:" + statusCode);
//                            error.printStackTrace();

                        }
                    });
                    // 匹配卡内余额
//                    Connection.Response cardLogin_1 = Jsoup.connect(CARD_LOGIN_1).method(Connection.Method.GET).timeout(10000).execute();
//                    Map<String, String> cookies_2 = cardLogin_1.cookies();
//                    cookies_2.put("first", "false");
//                    System.out.println(cardLogin_1.statusCode());
//
//                    Connection connectLogin_2 = Jsoup.connect(CARD_LOGIN_2);
//                    connectLogin_2.header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
//                    connectLogin_2.header("Host", "urp.tongji.edu.cn");
//                    connectLogin_2.header("Referer", "http://urp.tongji.edu.cn/");
//                    connectLogin_2.header("Upgrade-Insecure-Requests", "1");
//                    connectLogin_2.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36");
//                    for (Map.Entry<String, String> entry : cookies_2.entrySet()) {
//                        System.out.println("Name:" + entry.getKey() + " Value:" + entry.getValue());
//                        connectLogin_2.cookie(entry.getKey(), entry.getValue());
//                    }
//
//                    Connection.Response cardLogin_2 = connectLogin_2.method(Connection.Method.GET).timeout(10000).execute();
//                    System.out.println(cardLogin_2.parse().html());


//                    HashMap<String, String> map = new HashMap<>();
//                    map.put("option", "credential");
//                    map.put("target", "http://urp.tongji.edu.cn/index.portal");
//                    map.put("Ecom_User_ID", username);
//                    map.put("Ecom_Password", password);
//                    map.put("submit", "登陆");
//
//                    Connection.Response loginResp = Jsoup.connect(CARD_LOGIN).method(Connection.Method.POST).data(map).timeout(10000).execute();
//                    Connection connectionLogin = Jsoup.connect(CARD_LOGIN_2);
//                    for (Map.Entry<String, String> entry : cookies_2.entrySet()) {
//                        // 设置cookies
//                        connectionLogin.cookie(entry.getKey(), entry.getValue());
//                    }
//                    response = connectionLogin.method(Connection.Method.GET).timeout(10000).execute();
//                    Log.d("IMP Code", String.valueOf(response.statusCode()));
//                    for (Map.Entry<String, String> entry : response.cookies().entrySet()) {
//                        System.out.println("Name:" + entry.getKey() + " Value:" + entry.getValue());
//                    }

//                    Connection connectCardInfo = Jsoup.connect(CARD_INFO);
//                    for (Map.Entry<String, String> entry : cookies.entrySet()) {
//                        // 设置cookies
//                        connectCardInfo.cookie(entry.getKey(), entry.getValue());
//                    }
//                    Connection.Response cardInfoResponse = connectCardInfo.method(Connection.Method.GET).timeout(10000).execute();
//                    CharSequence card_info_raw = cardInfoResponse.parse().body().getElementsByClass("portletContent").html();
//                    pattern = Pattern.compile("<td>(.+?)</td>");
//                    matcher = pattern.matcher(card_info_raw);
//                    int cnt = 0;
//                    while (matcher.find()) {
//                        if (cnt == 12) {
//                            card_rest = matcher.group(1);
//                        }
//                        cnt++;
//                    }

                    // 匹配最近交易额
//                    connectCardInfo = Jsoup.connect(CARD_DETAIL);
//                    for (Map.Entry<String, String> entry : cookies.entrySet()) {
//                        // 设置cookies
//                        connectCardInfo.cookie(entry.getKey(), entry.getValue());
//                    }
//                    postValue = new HashMap<String, String>();
//                    postValue.put("name", "1");
//                    postValue.put("startDate", "2016-01-01");
//                    postValue.put("endDate", "2016-01-23");
//                    postValue.put("submitQuery", "查询");
//
//                    cardInfoResponse = connectCardInfo
//                            .method(Connection.Method.POST)
//                            .data(postValue)
//                            .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
//                            .header("Accept-Encoding", "gzip,deflate,sdch")
//                            .header("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6")
//                            .header("Cache-Control", "max-age=0")
//                            .header("Connection", "keep-alive")
//                            .header("Content-Length", "77")
//                            .header("Host", "urp.tongji.edu.cn")
//                            .header("Origin", "http://urp.tongji.edu.cn")
//                            .header("Referer", "http://urp.tongji.edu.cn/index.portal?.pn=p84_p468_p470")
//                            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.59 Safari/537.36")
//                            .timeout(100000)
//                            .execute();
//
//                    card_info_raw = cardInfoResponse.parse().html();
//                    System.out.println(card_info_raw);
//                    System.out.println(cardInfoResponse.statusCode());

                    int cnt = 0;
                    // 匹配信息标题
                    pattern = Pattern.compile(";\">(.+?)</a></td>");
                    matcher = pattern.matcher(response.body());

                    while (matcher.find()) {
                        String tmp = ToDBC(matcher.group(1));
                        info_title.add(tmp);

                    }

                    pattern = Pattern.compile("[0-9]{4}-[0-9]+-[0-9]+");
                    matcher = pattern.matcher(response.body());
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    cnt = 0;
                    while (matcher.find()) {
                        Date date = dateFormat.parse(matcher.group());
                        String str = dateFormat.format(date);
                        if (cnt != 0) {
                            info_time.add(str.replace("2016-", "").replace("2015-", "").replace("-", "."));
                        } else {
                            time_today = str;
                        }
                        cnt++;
                    }

                    // 获取课表IDS
                    Connection connectToCourse = Jsoup.connect(COURSE_INFO_IDS);
                    for (Map.Entry<String, String> entry : cookies.entrySet()) {
                        connectToCourse.cookie(entry.getKey(), entry.getValue());
                    }
                    Connection.Response courseIdsResponse = connectToCourse.method(Connection.Method.GET).timeout(10000).execute();
                    String courseIDS_raw = courseIdsResponse.body();
                    pattern = Pattern.compile("\"ids\",\"(.*?)\"");
                    matcher = pattern.matcher(courseIDS_raw);
                    if (matcher.find()) {
                        course_IDS = matcher.group(1);
                    }
                    pattern = Pattern.compile("value:\"(.*?)\"");
                    matcher = pattern.matcher(courseIDS_raw);
                    if (matcher.find()) {
                        semester = matcher.group(1);
                    }

                    postValue = new HashMap<>();
                    postValue.put("ignoreHead", "1");
                    postValue.put("setting.kind", "std");
                    postValue.put("semester.id", semester);
                    postValue.put("ids", course_IDS);

                    // 获取课表
                    Connection connectionCourseTable = Jsoup.connect(COURSE_INFO);
                    for (Map.Entry<String, String> entry : cookies.entrySet()) {
                        // 设置cookies
                        connectionCourseTable.cookie(entry.getKey(), entry.getValue());
                    }
                    Connection.Response courseTableResponse = connectionCourseTable.method(Connection.Method.POST).data(postValue).timeout(10000).execute();
                    Document document = courseTableResponse.parse();
                    // 删除上面的课表
//                    document.select("#manualArrangeCourseTable").remove();
                    course_table_str = document.html();

//                    course_table_str = courseTableResponse.parse().select("").remove().html();
                    CourseTable.setInstance(null);
                    CourseTable courseTable = CourseTable.getInstance();
                    ArrayList<String> course_raw = new ArrayList<>();
                    Elements all_course_raw = courseTableResponse.parse().getElementsByClass("grid").select("tbody").select("tr");
                    for (Element element : all_course_raw) {
                        cnt = 0;
                        String course_name = "";
                        for (Element element1 : element.select("td")) {
                            if (cnt == 2) {
                                course_name = element1.text();
                            }

                            if (cnt == 8) {
                                String[] temp = element1.text().split(" ");
                                Collections.addAll(course_raw, temp);
                                for (int i = 0; i < temp.length; i = i + 5) {
                                    courseTable.addCourse(course_name, temp[i], temp[i + 1], temp[i + 2], temp[i + 3], temp[i + 4]);
                                }
                                break;
                            } else {
                                cnt++;
                            }
                        }
                    }

//                    for (ArrayList<Course> courses : courseTable.course_table) {
//                        System.out.println("Size of today: " + courses.size());
//                        for (Course cour : courses) {
//                            System.out.println(cour.course_name + " \t\t" + cour.week_num + cour.classroom + " \t" + cour.teacher_name + " \t" + cour.start_time + " \t" + cour.end_time);
//                        }
//                    }
//                    System.out.println(all_course_raw.get(1));
//                    System.out.println(courseTableResponse.parse().getElementsByClass("grid").select("tbody").select("tr"));

                    // 将取得的数据打包
                    setBundle();

                    return true;
                } catch (IOException | ParseException e) {
                    e.printStackTrace();
                }

                return false;
            }

            private void setBundle() {
                dataBundle = new DataBundle(username, password, time_today, time_week, card_rest, course_table_str, info_title, info_time, info_id);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    if (successCallback != null) {
                        successCallback.onSuccess(dataBundle);
                    }
                } else {
                    if (failureCallback != null) {
                        failureCallback.onFailure();
                    }
                }
                super.onPostExecute(aBoolean);
            }
        }.execute();
    }

    public interface SuccessCallback {
        void onSuccess(DataBundle dataBundle);
    }

    public interface FailureCallback {
        void onFailure();
    }

    /***
     * 半角转换为全角
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

}
