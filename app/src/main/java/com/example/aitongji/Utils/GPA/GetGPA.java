package com.example.aitongji.Utils.GPA;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.example.aitongji.Home.MainActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;
import com.novemser.ocrtest.BP;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 3/7/2016.
 * 获取学生的GPA,成功返回一张GPA表，失败返回null
 */

public class GetGPA {
    private final static String CHECK_IMAGE = "http://xuanke.tongji.edu.cn/CheckImage";
    private final static String URL_LOGIN = "http://tjis2.tongji.edu.cn:58080/amserver/UI/Login";
    private final static String LOGIN_TREE = "http://xuanke.tongji.edu.cn/tj_login/loginTree.jsp";
    private String GPA_QUERY = "http://xuanke.tongji.edu.cn/tj_login/redirect.jsp?link=/tj_xuankexjgl/score/query/student/cjcx.jsp?";
    private String qxid;
    private String mkid;
    private ArrayList<Bitmap> pieces;
    private String result;
    private BP bp;
    private Bitmap bitmap;
    private String TAG = "GAP fetch";
    private StudentGPA gpa_table;
    private SemasterGPA semasterGPA;
    private CourseGPA courseGPA;

    public GetGPA(final String username, final String password, final SuccessCallback successCallback, final FailureCallback failureCallback) {
        new AsyncTask<Void, Void, StudentGPA>() {
            @Override
            protected StudentGPA doInBackground(Void... params) {
                pieces = new ArrayList<>();
                gpa_table = new StudentGPA();
                // 从Assets文件夹读取BP神经网络
                final AssetManager assetManager = MainActivity.getContext().getAssets();
                try {
                    ObjectInputStream ois = new ObjectInputStream(assetManager.open("bp.dat"));
                    bp = (BP) ois.readObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 异步请求绩点
                final SyncHttpClient syncHttpClient = new SyncHttpClient();

                // 设置Cookie容器，每次请求都需要清除以前保留的cookie
                PersistentCookieStore persistentCookieStore = new PersistentCookieStore(MainActivity.getContext());
                persistentCookieStore.clear();
                syncHttpClient.setCookieStore(persistentCookieStore);

                // 1.获取验证码
                syncHttpClient.get(CHECK_IMAGE, new AsyncHttpResponseHandler(Looper.getMainLooper()) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        bitmap = BitmapFactory.decodeByteArray(responseBody, 0, responseBody.length);

                        if (bitmap != null) {
                            spiltCheckImage(bitmap);
                        }

                        result = "";
                        for (Bitmap item : pieces) {
                            double[] input = new double[96];
                            int cnt = 0;
                            for (int i = item.getHeight() - 1; i >= 0; i--)
                                for (int j = item.getWidth() - 1; j >= 0; j--) {
                                    int R = Color.red(item.getPixel(j, i));
                                    if (R > 200) {
                                        input[cnt++] = 1.0;
                                    } else {
                                        input[cnt++] = 0.0;
                                    }
                                }
                            double[] res = bp.test(input);
                            result += getMax(res);
                        }

                        // 2.尝试登陆
                        // post表单设置
                        final RequestParams params = new RequestParams();
                        params.put("Login.Token1", username);
                        params.put("Login.Token2", password);
                        params.put("T3", result);
                        params.put("goto", "http://xuanke.tongji.edu.cn/pass.jsp?checkCode=" + result);
                        params.put("gotoOnFail", "http://xuanke.tongji.edu.cn/deny.jsp?checkCode=" + result + "&account=1452681&password=BED0648254DF3D9BA9350683817275CE");


                        syncHttpClient.post(URL_LOGIN, params, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                // 3.302到目标地址验证是否登陆成功
                                syncHttpClient.get("http://xuanke.tongji.edu.cn/pass.jsp?checkCode=" + result, new AsyncHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        // length 小于2000才是成功 否则密码错误(很tricky...)
                                        final int len = Jsoup.parse(new String(responseBody)).body().html().length();
                                        if (len < 2000) {
                                            Log.d(TAG, "Login successfully!");

                                            // 4.获取权限id
                                            // 用于获取GPA
                                            syncHttpClient.get(LOGIN_TREE, new AsyncHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                    Log.d(TAG, "Geting authority ID successfully!");
                                                    try {
                                                        String auth = new String(responseBody, "gb2312");
                                                        String jsonString = "";
                                                        Pattern pattern = Pattern.compile("\\{  ICO:\"null\", ID:\"\\d+\", DKFS:\"\\d+\", URL:\"/tj_xuankexjgl/score/query/student/cjcx.jsp\", MKPXH:\"\\d\", MKBH:\"((.*?))\", MKMC:\"成绩查询\", MKLX:\"\\d+\", SJMK_ID:\"\\d+\", QX_ID:\"\\d+\", QXMC:\"全部权限\", HELPURL:\"null\", MYXSJL:\"null\" \\}");
                                                        Matcher matcher = pattern.matcher(auth);
                                                        while (matcher.find()) {
                                                            jsonString = matcher.group(0);
                                                        }

                                                        pattern = Pattern.compile("ID:\"(.*?)\"");
                                                        matcher = pattern.matcher(jsonString);
                                                        int cnt = 0;
                                                        while (matcher.find()) {
                                                            if (cnt == 0) {
                                                                mkid = matcher.group(1);
                                                                cnt++;
                                                            } else if (cnt == 2) {
                                                                qxid = matcher.group(1);
                                                                break;
                                                            } else {
                                                                cnt++;
                                                            }
                                                        }


                                                        // 5.根据权限id拉出绩点表
                                                        GPA_QUERY += "qxid=" + qxid + "$mkid=" + mkid + "&qxid=" + qxid + "&HELP_URL=null&MYXSJL=null";
                                                        syncHttpClient.get(GPA_QUERY, new AsyncHttpResponseHandler() {
                                                            @Override
                                                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                                                Log.d(TAG, "Getting GPA table successfully!");
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
                                                                            semasterGPA = new SemasterGPA();
                                                                            semasterGPA.semaster_info = temp_data;
                                                                            gpa_table.semasterGPAs.add(semasterGPA);

                                                                            table_start = 1;
                                                                        } else if (table_start == 1) {
                                                                            table_start = 2;
                                                                        } else if (table_start == 2) {
                                                                            if (temp_data.startsWith("本学期平均绩点")) {
//                                                                                Log.d(TAG + " Semaster GPA", temp_data.split(":")[1]);
                                                                                semasterGPA.semaster_gpa = Float.parseFloat(temp_data.split(":")[1]);

                                                                                table_start = 0;
                                                                            } else {
                                                                                String[] tmp_course = temp_data.split(" ");
//                                                                                Log.d(TAG, "Course have " + tmp_course.length + " attributes.");

                                                                                if (tmp_course.length == 9) {
                                                                                    courseGPA = new CourseGPA();
                                                                                    courseGPA.course_id = tmp_course[0];
                                                                                    courseGPA.course_name = tmp_course[1];
                                                                                    courseGPA.evaluation = tmp_course[2];
                                                                                    courseGPA.grade = Float.parseFloat(tmp_course[3]);
                                                                                    courseGPA.gpa = Float.parseFloat(tmp_course[4]);
                                                                                    courseGPA.is_pass = tmp_course[5];
                                                                                    courseGPA.grade_feature = tmp_course[6];
                                                                                    courseGPA.major_info = tmp_course[7];
                                                                                    courseGPA.publish_time = tmp_course[8];
                                                                                    semasterGPA.courseGPAs.add(courseGPA);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                } catch (UnsupportedEncodingException e) {
                                                                    e.printStackTrace();
                                                                }

                                                            }

                                                            @Override
                                                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                                            }
                                                        });
                                                    } catch (UnsupportedEncodingException e) {
                                                        e.printStackTrace();
                                                    }
                                                }

                                                @Override
                                                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                                }
                                            });

                                        } else {
                                            Log.d(TAG, "自动识别验证码错误！");
                                            gpa_table = null;
                                        }

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                                    }
                                });

                            }
                        });
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });


                return gpa_table;
            }

            @Override
            protected void onPostExecute(StudentGPA studentGPA) {
                if (studentGPA != null) {
                    if (null != successCallback)
                        successCallback.onSuccess(studentGPA);
                } else {
                    if (null != failureCallback)
                        failureCallback.onFailure();
                }

            }
        }.execute();
    }

    private int getMax(double[] input) {
        double tmp = -1;
        int j = 0;
        for (int i = 0; i < 10; i++) {
            if (input[i] > tmp) {
                tmp = input[i];
                j = i;
            }
        }
        return j;
    }

    private void spiltCheckImage(Bitmap bitmap) {
        pieces.clear();
        for (int i = 0; i < 4; i++) {
            pieces.add(Bitmap.createBitmap(bitmap, 2 + i * 9, 3, 8, 12));
        }
    }

    public interface SuccessCallback {
        void onSuccess(StudentGPA studentGPA);
    }

    public interface FailureCallback {
        void onFailure();
    }
}
