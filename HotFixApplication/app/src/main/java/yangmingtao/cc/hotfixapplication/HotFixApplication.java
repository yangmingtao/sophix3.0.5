package yangmingtao.cc.hotfixapplication;

import android.app.Application;
import android.util.Log;

import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;

import yangmingtao.cc.hotfixapplication.utils.AppManager;

/**
 * Created by yangmingtao on 2017/7/5.
 */

public class HotFixApplication extends Application {
    public interface BehaviourListener{
         void handleBehaviour(int code,String msg,int handlePatchVersion);
    }
    private AppManager appManager;
    public  static BehaviourListener behaviourListener=null;
    @Override
    public void onCreate() {
        super.onCreate();
        appManager=AppManager.getAppManager(this);
        initHotfix();
        this.getPackageName();
    }

    /**
     * 初始化hotfix
     */
    private void initHotfix() {
        String appVersion;
        try {
            appVersion = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (Exception e) {
            appVersion = "1.0.0";
        }
        final String packgename=this.getPackageName();
        SophixManager.getInstance().setContext(this)
                .setAppVersion(appVersion)
                .setAesKey(null)
                //.setAesKey("0123456789123456")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        Log.i("woxx","code:"+code+"info:"+info+"packagename:"+packgename);
/*                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            if (behaviourListener!=null){
                                behaviourListener.handleBehaviour(code,info);
                            }
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后应用自杀
                            //如果应用不在前台启用，补丁生效完毕直接杀掉应用，否则弹出对话框提示用户bug更新完毕，需要用户选择是否重启应用
                            if (!appManager.isAppOnForeground()){
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }else{
                                if (behaviourListener!=null){
                                    behaviourListener.handleBehaviour(code,info);
                                }
                            }

                        } else if (code == PatchStatus.CODE_LOAD_FAIL) {
                            // 内部引擎异常, 推荐此时清空本地补丁, 防止失败补丁重复加载
                            SophixManager.getInstance().cleanPatches();
                        } else if (code ==PatchStatus.CODE_DOWNLOAD_SUCCESS){
                            //patch文件下载成功
                            if (behaviourListener!=null){
                                behaviourListener.handleBehaviour(code,info);
                            }
                        }
                        else {
                            // 其它错误信息, 查看PatchStatus类说明
                        }*/
                        if (behaviourListener!=null){
                            behaviourListener.handleBehaviour(code,info,handlePatchVersion);
                        }
                    }
                }).initialize();
    }
}
