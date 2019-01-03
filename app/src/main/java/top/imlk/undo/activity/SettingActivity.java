package top.imlk.undo.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.widget.Toast;

import com.danielstone.materialaboutlibrary.ConvenienceBuilder;
import com.danielstone.materialaboutlibrary.MaterialAboutActivity;
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem;
import com.danielstone.materialaboutlibrary.items.MaterialAboutItemOnClickAction;
import com.danielstone.materialaboutlibrary.items.MaterialAboutTitleItem;
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard;
import com.danielstone.materialaboutlibrary.model.MaterialAboutList;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater2;

import java.io.File;
import java.io.InputStream;

import top.imlk.undo.BuildConfig;
import top.imlk.undo.R;

public class SettingActivity extends MaterialAboutActivity {


    @NonNull
    @Override
    protected MaterialAboutList getMaterialAboutList(@NonNull final Context context) {
        int icoColor = ContextCompat.getColor(context, R.color.ico);
        int icoSizeDp = 18;
        return new MaterialAboutList.Builder()
                .addCard(new MaterialAboutCard.Builder()
                        .addItem(
                                new MaterialAboutTitleItem.Builder()
                                        .icon(R.mipmap.ic_launcher)
                                        .text(R.string.about_title_undo)
                                        .desc(R.string.about_title_desc)
                                        .build()
                        )
                        .addItem(
                                ConvenienceBuilder.createVersionActionItem(
                                        context,
                                        new IconicsDrawable(context, CommunityMaterial.Icon2.cmd_information_outline).color(icoColor).sizeDp(icoSizeDp),
                                        getString(R.string.version),
                                        true
                                ).setOnClickAction(() -> {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://repo.xposed.info/module/top.imlk.undo")));
                                })
                        )
                        .addItem(
                                new MaterialAboutActionItem.Builder()
                                        .icon(new IconicsDrawable(context, CommunityMaterial.Icon.cmd_book).color(icoColor).sizeDp(icoSizeDp))
                                        .text(R.string.license)
                                        .subText(R.string.subtext_license)
                                        .setOnClickAction(() -> {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/KB5201314/UndoForAndroid/blob/master/LICENSE")));
                                        })
                                        .build()
                        )
                        .build()
                )
                .addCard(new MaterialAboutCard.Builder()
                        .title(R.string.settings)
                        .addItem(new MaterialAboutActionItem.Builder()
                                .icon(new IconicsDrawable(context, CommunityMaterial.Icon.cmd_file_hidden).color(icoColor).sizeDp(icoSizeDp))
                                .text(R.string.hide_or_show_ico)
                                .setOnClickAction(new MaterialAboutItemOnClickAction() {
                                    @Override
                                    public void onClick() {
                                        getPackageManager().setComponentEnabledSetting(getAliseComponentName(), isIcoShownOnLauncher() ? PackageManager.COMPONENT_ENABLED_STATE_DISABLED : PackageManager.COMPONENT_ENABLED_STATE_DEFAULT, PackageManager.DONT_KILL_APP);
                                        Toast.makeText(context, isIcoShownOnLauncher() ? getString(R.string.ico_show) : getString(R.string.ico_hide), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .build()
                        )
                        .build()
                )

                .addCard(new MaterialAboutCard.Builder()
                        .title(R.string.donate)
                        .addItem(new MaterialAboutActionItem.Builder()
                                .text(R.string.alipay)
                                .icon(new IconicsDrawable(context).icon(CommunityMaterial.Icon2.cmd_pine_tree).color(icoColor).sizeDp(icoSizeDp))
                                .setOnClickAction(this::alipaypay)
                                .build()
                        )
                        .addItem(new MaterialAboutActionItem.Builder()
                                .text(R.string.wechat)
                                .icon(new IconicsDrawable(context).icon(CommunityMaterial.Icon2.cmd_wechat).color(icoColor).sizeDp(icoSizeDp))
                                .setOnClickAction(this::wechatpay)
                                .build()
                        )
                        .addItem(new MaterialAboutActionItem.Builder()
                                .text(R.string.paypal)
                                .icon(new IconicsDrawable(context).icon(CommunityMaterial.Icon2.cmd_paypal).color(icoColor).sizeDp(icoSizeDp))
                                .setOnClickAction(this::paypalpay)
                                .build()
                        )
                        .build()

                )
                .addCard(new MaterialAboutCard.Builder()
                        .title(R.string.developers)
                        .addItem(new MaterialAboutActionItem.Builder()
                                .text("imlk")
                                .subText(R.string.developer)
                                .icon(new IconicsDrawable(context).icon(CommunityMaterial.Icon.cmd_code_braces).color(icoColor).sizeDp(icoSizeDp))
                                .setOnClickAction(() -> {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.coolapk.com/u/477017")));
                                })
                                .build()
                        )
                        .addItem(new MaterialAboutActionItem.Builder()
                                .text("pandecheng")
                                .subText(R.string.ico_designer)
                                .icon(new IconicsDrawable(context).icon(CommunityMaterial.Icon2.cmd_palette).color(icoColor).sizeDp(icoSizeDp))
                                .setOnClickAction(() -> {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.coolapk.com/u/531994")));
                                })
                                .build()
                        )
                        .build()
                )
                .addCard(new MaterialAboutCard.Builder()
                        .title(R.string.links)
                        .addItem(new MaterialAboutActionItem.Builder()
                                .text(R.string.coolapk)
                                .icon(new IconicsDrawable(context).icon(CommunityMaterial.Icon.cmd_android_head).color(icoColor).sizeDp(icoSizeDp))
                                .setOnClickAction(() -> {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.coolapk.com/apk/top.imlk.undo")));
                                })
                                .build()
                        )
                        .addItem(new MaterialAboutActionItem.Builder()
                                .text(R.string.qq_group)
                                .icon(new IconicsDrawable(context).icon(CommunityMaterial.Icon2.cmd_qqchat).color(icoColor).sizeDp(icoSizeDp))
                                .setOnClickAction(() -> {
                                    Intent intent = new Intent();
                                    intent.setData(Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3Dt3KJwqRiDDvuu8hCIY7Ku0cEZIXkRCKE"));
                                    try {
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Toast.makeText(SettingActivity.this, R.string.error_wake_up_qq, Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .build()
                        )
                        .addItem(new MaterialAboutActionItem.Builder()
                                .text(R.string.github)
                                .icon(new IconicsDrawable(context).icon(CommunityMaterial.Icon.cmd_github_circle).color(icoColor).sizeDp(icoSizeDp))
                                .setOnClickAction(() -> {
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/KB5201314/UndoForAndroid")));
                                })
                                .build()
                        )
                        .build()
                )
                .build();

    }

    private void wechatpay() {

        if (!checkPermissions()) {
            return;
        }

        InputStream weixinQrIs = getResources().openRawResource(R.raw.donate_wechat);
        String qrPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + BuildConfig.APPLICATION_ID + File.separator +
                "imlk_weixin.png";
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
        WeiXinDonate.donateViaWeiXin(this, qrPath);

        Toast.makeText(SettingActivity.this, R.string.donate_wechat_thx, Toast.LENGTH_SHORT).show();

    }

    private void alipaypay() {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this);
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(this, "FKX093049UCVM4EEN8WV84");
        }
        Toast.makeText(SettingActivity.this, R.string.donate_alipay_thx, Toast.LENGTH_SHORT).show();
    }

    private void paypalpay() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.paypal.me/imlk77")));
        Toast.makeText(this, R.string.donate_paypal_thx, Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    protected CharSequence getActivityTitle() {
        return getString(R.string.app_name);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory2(getLayoutInflater(), new IconicsLayoutInflater2(getDelegate()));
        super.onCreate(savedInstanceState);

    }

    public boolean isIcoShownOnLauncher() {
        return getPackageManager().getComponentEnabledSetting(getAliseComponentName()) == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
    }


    private ComponentName getAliseComponentName() {
        return new ComponentName(SettingActivity.this, "top.imlk.undo.hooker.Injecter");
    }

    private boolean checkPermissions() {

        int permission_0 = ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permission_1 = ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int permission_2 = ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS);

        if (permission_0 != PackageManager.PERMISSION_GRANTED || permission_1 != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(SettingActivity.this, R.string.need_permission, Toast.LENGTH_LONG).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS}, 1);
                return false;
            }
        }
        return true;
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


    @Override
    protected void onDestroy() {

        //删除付款码

        String qrPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + BuildConfig.APPLICATION_ID + File.separator +
                "imlk_weixin.png";

        File qrFile = new File(qrPath);
        File parentFile = qrFile.getParentFile();

        if (qrFile.exists()) {
            qrFile.delete();
            parentFile.delete();
        }

        System.exit(0);
        super.onDestroy();
    }

}

