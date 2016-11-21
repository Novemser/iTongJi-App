package com.example.aitongji.Utils.Http.operation.request;

import com.example.aitongji.Utils.Managers.ObserverManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Novemser on 2016/11/19.
 */
public class BYTimeNotificationGetter extends BYGenericGetter {

    private String time_today;
    private String time_week;
    private ArrayList<String> info_title = new ArrayList<>();
    private ArrayList<String> info_time = new ArrayList<>();
    private ArrayList<String> info_id = new ArrayList<>();

    private void resetLists() {
        info_id.clear();
        info_time.clear();
        info_title.clear();
    }

    @Override
    public void loadData() throws Exception {
        // 匹配第几周
        Connection connect = Jsoup.connect(homeAction);
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            connect.cookie(entry.getKey(), entry.getValue());
        }

        Connection.Response response = connect.execute();
        Document doc = response.parse();
        Elements elements = doc.body().getElementsByClass("modulebody");
        Pattern pattern = Pattern.compile("第(.+?)周");
        Matcher matcher = pattern.matcher(elements.text());
        if (matcher.find()) {
            time_week = matcher.group(1);
        }

        // 匹配信息通知的id
        pattern = Pattern.compile("getNewNoticeInfo\\('(.+?)'\\);");
        matcher = pattern.matcher(response.body());
        while (matcher.find()) {
            info_id.add(matcher.group(1));
        }

        int cnt = 0;
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

        ObserverManager.getInstance().notifyRowChanged(1);
    }

    @Override
    public void refresh() throws Exception {
        resetLists();
        loadData();
    }

    /**
     * 半角转换为全角
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
