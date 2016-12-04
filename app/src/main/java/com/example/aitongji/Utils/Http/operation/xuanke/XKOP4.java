package com.example.aitongji.Utils.Http.operation.xuanke;

import android.util.Log;

import com.example.aitongji.Utils.Http.callback.FailCallBack;
import com.example.aitongji.Utils.Http.callback.Operation;
import com.example.aitongji.Utils.Http.callback.SuccessCallBack;
import com.example.aitongji.Utils.Managers.NetWorkManager;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Novemser on 2016/11/19.
 */
public class XKOP4 extends XuankeOperation {
    private final static String LOGIN_TREE = "http://xuanke.tongji.edu.cn/tj_login/loginTree.jsp";
    private String GPA_QUERY = "http://xuanke.tongji.edu.cn/tj_login/redirect.jsp?link=/tj_xuankexjgl/score/query/student/cjcx.jsp?";
    private String qxid;
    private String mkid;

    public XKOP4(List<Operation> operations, SuccessCallBack successCallBack, FailCallBack failCallBack) {
        super(operations, successCallBack, failCallBack);
    }

    @Override
    public void perform() {
        // 4.获取权限id
        // 用于获取GPA
        NetWorkManager.getInstance().getXuanKeHttpClient().
                get(LOGIN_TREE, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d(getClass().getName(), "Getting authority ID successfully!");
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

                            // 根据权限id拉出绩点表
                            GPA_QUERY += "qxid=" + qxid + "$mkid=" + mkid + "&qxid=" + qxid + "&HELP_URL=null&MYXSJL=null";
                            setNextUrl(GPA_QUERY);
                            stepToNext();

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
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
