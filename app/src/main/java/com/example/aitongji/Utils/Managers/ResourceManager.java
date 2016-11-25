package com.example.aitongji.Utils.Managers;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.aitongji.Model.CourseTableSubject;
import com.example.aitongji.Model.NewsTitleSubject;
import com.example.aitongji.Model.StudentGPASubject;
import com.example.aitongji.Model.UserInfo;
import com.example.aitongji.Utils.AndroidResource;
import com.example.aitongji.Utils.GPA.CourseGPA;
import com.example.aitongji.Utils.GPA.Semester;
import com.novemser.ocrtest.BP;

import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Novemser on 2016/11/14.
 */
public class ResourceManager {
    private static ResourceManager manager;
    private UserInfo userInfo;
    private Map<String, String> cookie; // 4m3的Cookie
    private ArrayList<Bitmap> pieces;
    private StudentGPASubject GPATable;
    private NewsTitleSubject newsTitleSubject;
    private CourseTableSubject courseTableSubject;

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


    public void clearInfos() {
        newsTitleSubject = new NewsTitleSubject();
    }

    private String ocrResult;
    private Semester semester;
    private CourseGPA courseGPA;
    private String cardRest;

    public String getCardRest() {
        return cardRest;
    }

    public void setCardRest(String cardRest) {
        this.cardRest = cardRest;
        ObserverManager.getInstance().notifyRowChanged(1);
    }


    public String getWeekStr() {
        return weekStr;
    }

    public void setWeekStr(String weekStr) {
        this.weekStr = weekStr;
        ObserverManager.getInstance().notifyRowChanged(1);
    }

    private String weekStr;


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

    public void setGPATable(StudentGPASubject GPATable) {
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

    public NewsTitleSubject getNewsTitleSubject() {
        if (null == newsTitleSubject)
            newsTitleSubject = new NewsTitleSubject();

        return newsTitleSubject;
    }

    public StudentGPASubject getGPATable() {
        if (null == GPATable)
            GPATable = new StudentGPASubject();

        return GPATable;
    }

    public CourseTableSubject getCourseTableSubject() {
        if (null == courseTableSubject)
            courseTableSubject = new CourseTableSubject();

        return courseTableSubject;
    }
}
