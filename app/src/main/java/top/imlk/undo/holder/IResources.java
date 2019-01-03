package top.imlk.undo.holder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/**
 * Created by imlk on 2018/3/18.
 */

public class IResources {
    public static Resources resources;
    public static String MODULE_PATH;

    static boolean registed = false;

    public static synchronized void initResources(Context context) {
        try {

            if (MODULE_PATH == null) {// firsttime
                MODULE_PATH = context.getPackageManager().getApplicationInfo("top.imlk.undo", 0).sourceDir;
            }

            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, MODULE_PATH);
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());

            if (!registed) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.LOCALE_CHANGED");
                context.registerReceiver(new LanguageChangeListener(), intentFilter);
                registed = true;
            }

        } catch (Exception e) {
            XposedBridge.log("Undo:" + "initResources error");
            XposedBridge.log(e.toString());
        }
    }

    static class LanguageChangeListener extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            initResources(context);
        }
    }
}
