package com.example.aitongji.Service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.aitongji.Home.MainActivity;

import java.util.Timer;

public class CardRestNotice extends Service {
    static Timer timer = null;
    private String username = null;
    private String password = null;
    private int value;
    private static final String loginUrl_v2 = "http://tjis.tongji.edu.cn:58080/amserver/UI/Login";
    private static final String CARD_INFO = "http://urp.tongji.edu.cn/index.portal?.pn=p84_p468_p469";

    //清除通知
    public static void cleanAllNotification() {
        NotificationManager mn = (NotificationManager) MainActivity.getContext().getSystemService(NOTIFICATION_SERVICE);
        mn.cancelAll();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void onCreate() {
        Log.e("addNotification", "===========create=======");
    }

    //添加通知
    public static void addNotification(int delayTime, String tickerText, String contentTitle, String contentText) {
        Intent intent = new Intent(MainActivity.getContext(), CardRestNotice.class);
        intent.putExtra("delayTime", delayTime);
        intent.putExtra("tickerText", tickerText);
        intent.putExtra("contentTitle", contentTitle);
        intent.putExtra("contentText", contentText);
        MainActivity.getContext().startService(intent);
    }

    public CardRestNotice() {
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(final Intent intent, int flags, int startId) {
//        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
//        username = sharedPreferences.getString("username", "");
//        password = sharedPreferences.getString("password", "");
//        value = Integer.parseInt(sharedPreferences.getString("value", "10000"));
//
//        long period = 30 * 60 * 1000; //半小时一个周期
//        if (null == timer) {
//            timer = new Timer();
//        } else if (!sharedPreferences.getBoolean("FORCE_REFRESH", true)) {
//            return super.onStartCommand(intent, flags, startId);
//        }
//        sharedPreferences.edit().putBoolean("FORCE_REFRESH", false).apply();
//
//        timer.schedule(new TimerTask() {
//
//            @Override
//            public void run() {
//                if (username != null && !username.equals("")) {
//                    new InformationReq(username, password, new InformationReq.SuccessCallback() {
//                        @Override
//                        public void onSuccess(DataBundle dataBundle) {
//                            // 保存主要信息
//                            AndroidResource.dataBundle = dataBundle;
////                            SerializationUtil.saveObject(AndroidResource.getContext(), "dataBundle.dat", dataBundle);
////                            System.out.println("Card rest log:" + dataBundle.cardRest);
//                            // TODO  fix 1 只是简单的处理，网络层有点乱··
//                            if (dataBundle == null) {
//                                Log.e("Network","CardRestNotice access cardRest error!");
//                                return;
//                            }
//
//                            float card_rest = Float.parseFloat(ResourceManager.getInstance().getCardRest());
//                            if (card_rest < value) {
//                                NotificationManager mn = (NotificationManager) CardRestNotice.this.getSystemService(NOTIFICATION_SERVICE);
//                                mn.cancelAll();
//                                Notification.Builder builder = new Notification.Builder(CardRestNotice.this);
//                                builder.setSmallIcon(R.drawable.ic_stat_editor_attach_money);
//                                builder.setTicker("您的校园卡余额不足" + value + "元"); //测试通知栏标题
//                                builder.setContentText("请及时充值 :)"); //下拉通知啦内容
//                                builder.setContentTitle("余额:" + card_rest + "元");//下拉通知栏标题
//                                builder.setAutoCancel(true);
//                                builder.setDefaults(Notification.DEFAULT_ALL);
//                                Notification notification = builder.build();
//                                mn.notify((int) System.currentTimeMillis(), notification);
//                            }
//                        }
//                    }, new InformationReq.FailureCallback() {
//                        @Override
//                        public void onFailure() {
//
//                        }
//                    });
//                }
//
//            }
//        }, 0, period);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.e("addNotification", "===========destroy=======");
        super.onDestroy();
    }
}
