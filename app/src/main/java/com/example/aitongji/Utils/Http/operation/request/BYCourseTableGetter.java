package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Course.Course;
import com.example.aitongji.Utils.Course.CourseTable;
import com.example.aitongji.Utils.Managers.ObserverManager;
import com.example.aitongji.Utils.Managers.ResourceManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Novemser on 2016/11/19.
 */
public class BYCourseTableGetter extends BYGenericGetter {

    private static final String COURSE_INFO_IDS = "http://4m3.tongji.edu.cn/eams/courseTableForStd.action";
    private static final String COURSE_INFO = "http://4m3.tongji.edu.cn/eams/courseTableForStd!courseTable.action";

    private String course_IDS;
    private String semester;
    private String course_table_str;


    @Override
    public void loadData() throws Exception {
        // 获取课表IDS
        Connection connectToCourse = Jsoup.connect(COURSE_INFO_IDS);
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            connectToCourse.cookie(entry.getKey(), entry.getValue());
        }
        Connection.Response courseIdsResponse = connectToCourse.
                method(Connection.Method.GET).
                timeout(10000).execute();
        String courseIDS_raw = courseIdsResponse.body();
        Pattern pattern = Pattern.compile("\"ids\",\"(.*?)\"");
        Matcher matcher = pattern.matcher(courseIDS_raw);
        if (matcher.find()) {
            course_IDS = matcher.group(1);
        }
        pattern = Pattern.compile("value:\"(.*?)\"");
        matcher = pattern.matcher(courseIDS_raw);
        if (matcher.find()) {
            semester = matcher.group(1);
        }

        Map<String, String> postValue = new HashMap<>();
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
        Connection.Response courseTableResponse =
                connectionCourseTable.
                        method(Connection.Method.POST).
                        data(postValue).
                        timeout(10000).
                        execute();
        Document document = courseTableResponse.parse();
        // 删除上面的课表
//        document.select("#manualArrangeCourseTable").remove();
        course_table_str = document.html();
//        course_table_str = document.select("").remove().html();
        CourseTable.setInstance(null);
        CourseTable courseTable = ResourceManager.getInstance().getCourseTable();
        ArrayList<String> course_raw = new ArrayList<>();
        Elements all_course_raw = courseTableResponse.parse().getElementsByClass("grid").select("tbody").select("tr");
        int cnt = 0;
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

                    for (int i = 0; i <= temp.length - 5; i = i + 5) {
                        courseTable.addCourse(course_name, temp[i], temp[i + 1], temp[i + 2], temp[i + 3], temp[i + 4]);
                    }
                    break;
                } else {
                    cnt++;
                }
            }
        }

        ObserverManager.getInstance().notifyRowChanged(1);

    }

    @Override
    public void refresh() throws Exception {

    }
}
