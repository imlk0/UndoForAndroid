package top.imlk.undo.holder;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.lang.reflect.Method;

/**
 * Created by imlk on 2018/3/18.
 */

public class IResources {
    public static Resources resources;
    public static String MODULE_PATH;

    public static void initResources(Context context) {

        try {
            MODULE_PATH = context.getPackageManager().getApplicationInfo("top.imlk.undo", 0).sourceDir;
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, MODULE_PATH);
            resources = new Resources(assetManager, context.getResources().getDisplayMetrics(), context.getResources().getConfiguration());
        } catch (Exception e) {

        }
    }
}
