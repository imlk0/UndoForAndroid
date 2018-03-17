package top.imlk.undo.staticValue;

import android.content.res.Resources;

/**
 * Created by imlk on 2018/3/15.
 */

public class IResources {
    private static Resources resources;

    public static void setResources(Resources resources) {
        IResources.resources = resources;
    }

    public static Resources getResources() {
        return resources;
    }
}
