package com.example.aitongji.Utils.GPA;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Looper;
import android.util.Log;

import com.example.aitongji.Utils.Http.GPAGetter;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.example.aitongji.Utils.Managers.ResourceManager;
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
    private String username;
    private String password;
    private BP bp;
    private Bitmap bitmap;
    private String TAG = "GAP fetch";
    private StudentGPA gpa_table;
    private Semester semester;
    private CourseGPA courseGPA;
    private Context context;

    public GetGPA(Context context, String username, String password, final SuccessCallback successCallback, final FailureCallback failureCallback) {

        new AsyncTask<Void, Void, StudentGPA>() {
            @Override
            protected StudentGPA doInBackground(Void... params) {
                int count = 0;
                GPAGetter gpaGetter = new GPAGetter();
                try {
                    gpaGetter.loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 最多自动验证10次
                while (count < 10 && (null == ResourceManager.getInstance().getGPATable())) {
                    try {
                        gpaGetter.loadData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    count++;
                }

                return ResourceManager.getInstance().getGPATable();
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

    public interface SuccessCallback {
        void onSuccess(StudentGPA studentGPA);
    }

    public interface FailureCallback {
        void onFailure();
    }
}
