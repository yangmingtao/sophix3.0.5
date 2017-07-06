package yangmingtao.cc.hotfixapplication;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.taobao.sophix.SophixManager;

public class MainActivity extends AppCompatActivity implements HotFixApplication.BehaviourListener {

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HotFixApplication hotFixApplication = (HotFixApplication) getApplication();
        hotFixApplication.behaviourListener = this;
    }

    public void getPatch(View v) {
        SophixManager.getInstance().queryAndLoadNewPatch();
    }

    @Override
    public void handleBehaviour(final int code, final String msg,final int handlePatchVersion) {

        Log.i("woxx", "这里执行了");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                createDialog(code + ":::" + msg+":::"+handlePatchVersion);
            }
        });

    }
    public void createDialog(String msg) {
        Builder builder = new Builder(getApplicationContext());
        builder.setTitle("Title");
        builder.setMessage(msg);
        builder.setNegativeButton("取消", this.negativeClickListener);
        builder.setPositiveButton("确认", this.positiveClickListener);
        dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    DialogInterface.OnClickListener positiveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            restartApplication();
            SophixManager.getInstance().killProcessSafely();

        }
    };
    DialogInterface.OnClickListener negativeClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {

        }
    };

    private void restartApplication() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
