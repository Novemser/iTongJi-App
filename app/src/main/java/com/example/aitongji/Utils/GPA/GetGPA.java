package com.example.aitongji.Utils.GPA;

import android.content.Context;
import android.os.AsyncTask;

import com.example.aitongji.Model.StudentGPASubject;
import com.example.aitongji.Utils.Http.operation.request.BYCourseTableGetter;
import com.example.aitongji.Utils.Http.operation.request.BYGenericGetter;
import com.example.aitongji.Utils.Http.operation.request.CookieGetter;
import com.example.aitongji.Utils.Http.operation.request.GPAGetter;
import com.example.aitongji.Utils.Managers.ResourceManager;

/**
 * Created by Novemser on 3/7/2016.
 * 获取学生的GPA,成功返回一张GPA表，失败返回null
 */

public class GetGPA {
    public GetGPA(Context context, String username, String password, final SuccessCallback successCallback, final FailureCallback failureCallback) {

        new AsyncTask<Void, Void, StudentGPASubject>() {
            @Override
            protected StudentGPASubject doInBackground(Void... params) {
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
                CookieGetter cookieGetter = new CookieGetter();
                try {
                    cookieGetter.loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                BYGenericGetter getter = new BYCourseTableGetter();
                try {
                    getter.loadData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return ResourceManager.getInstance().getGPATable();
            }

            @Override
            protected void onPostExecute(StudentGPASubject studentGPASubject) {
                if (studentGPASubject != null) {
                    if (null != successCallback)
                        successCallback.onSuccess(studentGPASubject);
                } else {
                    if (null != failureCallback)
                        failureCallback.onFailure();
                }

            }
        }.execute();
    }

    public interface SuccessCallback {
        void onSuccess(StudentGPASubject studentGPASubject);
    }

    public interface FailureCallback {
        void onFailure();
    }
}
