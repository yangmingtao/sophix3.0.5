package yangmingtao.cc.hotfixapplication.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by yangmingtao on 2017/7/5.
 */

public class AppManager {
    private static AppManager appManager;
    private Context context;
    private AppManager(Context context){
        this.context=context;
    }
    public static AppManager getAppManager(Context context) {
        if (appManager==null){
            synchronized (AppManager.class){
                appManager=new AppManager(context);
                return appManager;
            }
        }
        return appManager;
    }

    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public boolean isAppOnForeground() {
        ActivityManager activityManager = (ActivityManager)context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getApplicationContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
}