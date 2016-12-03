package com.example.aitongji.Utils.Http.operation.xuanke;

import android.util.Log;

import com.example.aitongji.Model.StudentGPASubject;
import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.GPA.CourseGPA;
import com.example.aitongji.Utils.GPA.Semester;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.example.aitongji.Utils.Managers.ObserverManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/19.
 */
public class XKOP5 extends Operation {

    private StudentGPASubject gpa_table = manager.getGPATable();
    private Semester semester = manager.getSemester();

    public XKOP5(List<Operation> operations, SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(operations, successCallBack, failCallBack);
    }

    @Override
    public void perform() {

        // 根据权限id拉出绩点表
        NetWorkManager.getInstance().getXuanKeHttpClient()
                .get(getNextUrl(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d(getClass().getName(), "Getting GPA table successfully!");
                        manager.setGPATable(new StudentGPASubject());
                        gpa_table = manager.getGPATable();
                        try {
                            Document document = Jsoup.parse(new String(responseBody, "gb2312"));
                            Elements elements = document.select("table").get(1).select("tr");

                            int index = 0;
                            int table_start = 0;
                            for (int i = 0; i < elements.size(); i++) {
//                                                                        Log.d(TAG, elements.get(i).text());
                                String temp_data = elements.get(i).text();

                                if (index == 0) {
                                    String[] temp = temp_data.split(" ");
                                    gpa_table.student_id = temp[3];
                                    gpa_table.student_name = temp[4];
                                    gpa_table.acadmy = temp[5];
                                    gpa_table.major = temp[6];
                                    index++;
                                } else if (index == 1) {
                                    Pattern pt = Pattern.compile("(\\d*\\.*\\d*)");
                                    Matcher mc = pt.matcher(temp_data);
                                    int tmp_cnt = 0;
                                    while (mc.find()) {
                                        if (!mc.group(1).equals("")) {
                                            switch (tmp_cnt) {
                                                case 0:
                                                    gpa_table.total_gpa = Float.parseFloat(mc.group(1));
                                                    break;
                                                case 1:
                                                    gpa_table.grade_majored = Float.parseFloat(mc.group(1));
                                                    break;
                                                case 2:
                                                    gpa_table.grade_get = Float.parseFloat(mc.group(1));
                                                    break;
                                                case 3:
                                                    gpa_table.failed_courses = Integer.parseInt(mc.group(1));
                                                    break;
                                                default:
                                                    break;
                                            }
                                            tmp_cnt++;
                                        }
                                    }
                                    index++;
                                } else if (temp_data.endsWith("学期")) {
                                    semester = new Semester();
                                    semester.semaster_info = temp_data;
                                    gpa_table.semesters.add(semester);

                                    table_start = 1;
                                } else if (table_start == 1) {
                                    table_start = 2;
                                } else if (table_start == 2) {
                                    if (temp_data.startsWith("本学期平均绩点")) {
//                                                                                Log.d(TAG + " Semaster GPA", temp_data.split(":")[1]);
                                        semester.semaster_gpa = Float.parseFloat(temp_data.split(":")[1]);

                                        table_start = 0;
                                    } else {
                                        String[] tmp_course = temp_data.split(" ");
//                                                                                Log.d(TAG, "Course have " + tmp_course.length + " attributes.");

                                        if (tmp_course.length == 9) {
                                            CourseGPA courseGPA = new CourseGPA();
                                            courseGPA.course_id = tmp_course[0];
                                            courseGPA.course_name = tmp_course[1];
                                            courseGPA.evaluation = tmp_course[2];
                                            courseGPA.grade = Float.parseFloat(tmp_course[3]);
                                            courseGPA.gpa = Float.parseFloat(tmp_course[4]);
                                            courseGPA.is_pass = tmp_course[5];
                                            courseGPA.grade_feature = tmp_course[6];
                                            courseGPA.major_info = tmp_course[7];
                                            courseGPA.publish_time = tmp_course[8];
                                            semester.courseGPAs.add(courseGPA);
                                        }
                                    }
                                }
                            }

                            ObserverManager.getInstance().notifyRowChanged(2);
                            defaultSuccessCallBack.onSuccess(this.getClass());
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                            defaultFailCallBack.onFailure(this.getClass());
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        manager.setGPATable(null);
                        Log.e(getClass().getName(), "Failed!");
                        defaultFailCallBack.onFailure(this.getClass());
                    }
                });
    }
}
