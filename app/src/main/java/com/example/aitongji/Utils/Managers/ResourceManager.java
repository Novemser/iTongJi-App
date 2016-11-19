package com.example.aitongji.Utils.Managers;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;

import com.example.aitongji.Model.UserInfo;
import com.example.aitongji.Utils.AndroidResource;
import com.example.aitongji.Utils.GPA.CourseGPA;
import com.example.aitongji.Utils.GPA.Semester;
import com.example.aitongji.Utils.GPA.StudentGPA;
import com.novemser.ocrtest.BP;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Novemser on 2016/11/14.
 */
public class ResourceManager {
    private static ResourceManager manager;
    private StudentGPA GPATable;
    private UserInfo userInfo;
    private Map<String, String> cookie; // 4m3的Cookie
    private ArrayList<Bitmap> pieces;
    private String ocrResult;
    private Semester semester;
    private CourseGPA courseGPA;


    public BP getBpNN() {
        return bp;
    }

    private BP bp;

    public String getOcrResult() {
        return ocrResult;
    }

    public void setOcrResult(String ocrResult) {
        this.ocrResult = ocrResult;
    }

    public Bitmap getCheckCodeBm() {
        return checkCodeBm;
    }

    public void setCheckCodeBm(Bitmap checkCodeBm) {
        this.checkCodeBm = checkCodeBm;
    }

    private Bitmap checkCodeBm;

    public ArrayList<Bitmap> getPieces() {
        return pieces;
    }

    public void setPieces(ArrayList<Bitmap> pieces) {
        this.pieces = pieces;
    }

    public StudentGPA getGPATable() {
        return GPATable;
    }

    public void setGPATable(StudentGPA GPATable) {
        this.GPATable = GPATable;
    }

    public String getUserName() {
        return userInfo.getUsername();
    }

    public String getUserPwd() {
        return userInfo.getPassword();
    }

    public void setUserName(String name) {
        userInfo.setUsername(name);
    }

    public void setUserPwd(String pwd) {
        userInfo.setPassword(pwd);
    }

    public Map<String, String> getCookie() {
        return cookie;
    }

    public void setCookie(Map<String, String> cookie) {
        this.cookie = cookie;
    }

    public static ResourceManager getInstance() {

        if (null == manager) {
            synchronized (new Object()) {
                if (null == manager)
                    manager = new ResourceManager();
            }
        }

        return manager;
    }

    private ResourceManager() {
        GPATable = new StudentGPA();
        pieces = new ArrayList<>();
        userInfo = new UserInfo();

        // 从Assets文件夹读取BP神经网络
        try {
            ObjectInputStream ois = new ObjectInputStream(getApplicationContext().getAssets().open("bp.dat"));
            bp = (BP) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Context getApplicationContext() {
        return AndroidResource.getContext();
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public CourseGPA getCourseGPA() {
        return courseGPA;
    }

    public void setCourseGPA(CourseGPA courseGPA) {
        this.courseGPA = courseGPA;
    }
}
