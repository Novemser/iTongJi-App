package com.example.aitongji.Utils.GPA;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.aitongji.Model.StudentGPASubject;
import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Http.operation.request.GPAGetter;
import com.example.aitongji.Utils.Managers.ResourceManager;

/**
 * Created by Novemser on 3/7/2016.
 * 获取学生的GPA,成功返回一张GPA表，失败返回null
 */

public class GetGPA {
    public GetGPA(Context context, String username, String password, final SuccessCallback successCallback, final FailureCallback failureCallback) {

        new AsyncTask<Void, Void, StudentGPASubject>() {
            final SuccessCallBack successCallBack = new SuccessCallBack() {
                @Override
                public void onSuccess(Object data) {
                    Log.e(getClass().getName(), "加载GPA成功！");

                }
            };

            final FailCallBack failCallBack = new FailCallBack() {
                @Override
                public void onFailure(Object data) {
                    Log.e(getClass().getName(), "加载GPA失败！");

                }
            };

            @Override
            protected StudentGPASubject doInBackground(Void... params) {
                int count = 0;
                GPAGetter gpaGetter = new GPAGetter();
                try {
                    gpaGetter.loadData(successCallBack, failCallBack);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 最多自动验证10次
                while (count < 10 && (null == ResourceManager.getInstance().getGPATable())) {
                    try {
                        gpaGetter.loadData(successCallBack, failCallBack);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    count++;
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
