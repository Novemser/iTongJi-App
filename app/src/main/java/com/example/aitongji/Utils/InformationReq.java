package com.example.aitongji.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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

import cz.msebera.android.httpclient.cookie.Cookie;

/**
 * Created by Novemser on 2016/2/11.
 */
public class InformationReq {
    private static final String BASE_URL = "http://4m3.tongji.edu.cn/";
    private static final String loginUrl_v2 = "http://tjis.tongji.edu.cn:58080/amserver/UI/Login";
    private static final String urlTest1 = "http://4m3.tongji.edu.cn/eams/home!welcome.action";
    private static final String CARD_INFO = "http://urp.tongji.edu.cn/index.portal?.pn=p84_p468_p469";
    private static final String CARD_DETAIL = "http://urp.tongji.edu.cn/index.portal?.p=Znxjb20ud2lzY29tLnBvcnRhbC5zaXRlLnYyLmltcGwuRnJhZ21lbnRXaW5kb3d8ZjEwODJ8dmlld3xub3JtYWx8YWN0aW9uPXBlcnNvbmFsU2FsYXJ5UXVlcnk_";
    private static final String COURSE_INFO = "http://4m3.tongji.edu.cn/eams/courseTableForStd!courseTable.action";
    private static final String COURSE_INFO_IDS = "http://4m3.tongji.edu.cn/eams/courseTableForStd.action";

    private String time_today = null;
    private String time_week = null;
    private String card_rest = null;
    private String course_IDS = null;
    private String course_table_str = null;
    private ArrayList<String> info_title = new ArrayList<>();
    private ArrayList<String> info_time = new ArrayList<>();
    private ArrayList<String> info_id = new ArrayList<>();

    private Bundle bundle;

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

                    // 匹配卡内余额
                    Connection connectCardInfo = Jsoup.connect(CARD_INFO);
                    for (Map.Entry<String, String> entry : cookies.entrySet()) {
                        // 设置cookies
                        connectCardInfo.cookie(entry.getKey(), entry.getValue());
                    }
                    Connection.Response cardInfoResponse = connectCardInfo.method(Connection.Method.GET).timeout(10000).execute();
                    CharSequence card_info_raw = cardInfoResponse.parse().body().getElementsByClass("portletContent").html();
                    pattern = Pattern.compile("<td>(.+?)</td>");
                    matcher = pattern.matcher(card_info_raw);
                    int cnt = 0;
                    while (matcher.find()) {
                        if (cnt == 12) {
                            card_rest = matcher.group(1);
                        }
                        cnt++;
                    }

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
                        // 设置cookies
                        connectToCourse.cookie(entry.getKey(), entry.getValue());
                    }
                    Connection.Response courseIdsResponse = connectToCourse.method(Connection.Method.GET).timeout(10000).execute();
                    String courseIDS_raw = courseIdsResponse.parse().html();
                    pattern = Pattern.compile("\"ids\",\"(.*?)\"");
                    matcher = pattern.matcher(courseIDS_raw);
                    if (matcher.find()) {
                        course_IDS = matcher.group(1);
                    }

                    postValue = new HashMap<>();
                    postValue.put("ignoreHead", "1");
                    postValue.put("setting.kind", "std");
                    postValue.put("semester.id", "101");
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
//                    e.printStackTrace();
                    System.out.println(e.getCause());
                }

                return false;
            }

            private void setBundle() {
                bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("password", password);
                bundle.putStringArrayList("infoTitle", info_title);
                bundle.putStringArrayList("infoTime", info_time);
                bundle.putString("timeToday", time_today);
                bundle.putString("timeWeek", time_week);
                bundle.putString("cardRest", card_rest);
                bundle.putStringArrayList("info_id", info_id);
                bundle.putString("course_table_str", course_table_str);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (aBoolean) {
                    if (successCallback != null) {
                        successCallback.onSuccess(bundle);
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
        void onSuccess(Bundle data);
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
