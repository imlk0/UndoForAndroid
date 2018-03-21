package top.imlk.undo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;
import java.io.InputStream;

import top.imlk.undo.R;

public class SettingActivity extends Activity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        checkPermissions();

        try {
            ((TextView) findViewById(R.id.tv_version)).setText(getApplication().getPackageManager().getPackageInfo(getPackageName(), 0).versionName + "(" + getApplication().getPackageManager().getPackageInfo(getPackageName(), 0).versionCode + ")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ((TextView) findViewById(R.id.tv_title_donate)).setText(randomString());
//        ((TextView) findViewById(R.id.tv_setting_show_ico)).setText(icoStatus());

        findViewById(R.id.tv_version).setOnClickListener(this);
        findViewById(R.id.tv_author_imlk).setOnClickListener(this);
        findViewById(R.id.tv_author_pdc).setOnClickListener(this);
        findViewById(R.id.tv_donate_wechat).setOnClickListener(this);
        findViewById(R.id.tv_donate_alipay).setOnClickListener(this);
//        findViewById(R.id.tv_setting_show_ico).setOnClickListener(this);
        findViewById(R.id.tv_link_github).setOnClickListener(this);
        findViewById(R.id.tv_link_github_license).setOnClickListener(this);


    }

    public String randomString() {
        String[] strings = {"我女票很漂亮，能捐赠我吗(*/ω＼*)",
                "我手机很旧了，能支持我换新吗( •̀ ω •́ )✧",
                "施主，能给点钱吗(。_。)",
                "苦逼开发者求喂养(ง •_•)ง",
                "我没有钱钱( $ _ $ )",
                "我想吃零食(。・∀・)ノ",
                "给咸鱼一点动力吧( •̀ ω •́ )y",
                "拯救秃头程序员〒▽〒"};

        return strings[((int) (strings.length * Math.random())) % strings.length];
    }

//    public String icoStatus() {
//        if (getPackageManager().getComponentEnabledSetting(getComponentName()) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
//            return "点击在启动器中隐藏应用图标";
//        } else {
//            return "点击在启动器中显示应用图标";
//        }
//    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_version:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coolapk.com/apk/top.imlk.undo")));
                break;
            case R.id.tv_author_imlk:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.coolapk.com/u/477017")));
                break;
            case R.id.tv_author_pdc:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.coolapk.com/u/531994")));
                break;
            case R.id.tv_donate_wechat:

                InputStream weixinQrIs = getResources().openRawResource(R.raw.donate_wechat);
                String qrPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + "Undo" + File.separator +
                        "imlk_weixin.png";
                WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
                WeiXinDonate.donateViaWeiXin(this, qrPath);

                Toast.makeText(SettingActivity.this, "请从相册选择收款码，蟹蟹，你的鼓励是我的最大动力", Toast.LENGTH_SHORT);

                break;
            case R.id.tv_donate_alipay:
                boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this);
                if (hasInstalledAlipayClient) {
                    AlipayDonate.startAlipayClient(this, "FKX093049UCVM4EEN8WV84");
                }
                Toast.makeText(SettingActivity.this, "蟹蟹，你的鼓励是我的最大动力", Toast.LENGTH_SHORT);
                break;

//            case R.id.tv_setting_show_ico:
//                if (getPackageManager().getComponentEnabledSetting(getComponentName()) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT) {
//                    getPackageManager().setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DISABLED_USER, PackageManager.DONT_KILL_APP);
//                } else {
//                    getPackageManager().setComponentEnabledSetting(getComponentName(), PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
//                }
//                break;
            case R.id.tv_link_github:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/KB5201314/UndoForAndroid")));
                break;
            case R.id.tv_link_github_license:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/KB5201314/UndoForAndroid/blob/master/LICENSE")));
                break;
        }
    }


    private void checkPermissions() {


        int permission_0 = ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission_1 = ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission_2 = ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);

        if (permission_0 != PackageManager.PERMISSION_GRANTED || permission_1 != PackageManager.PERMISSION_GRANTED || permission_2 != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(SettingActivity.this, "需要读写文件权限", Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 1);
            }
//                getPermissionByUser();
        }
    }
//
//    private void getPermissionByUser() {
//
//        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//        Uri uri = Uri.fromParts("package", getPackageName(), null);
//        intent.setData(uri);
//        startActivity(intent);
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
}
