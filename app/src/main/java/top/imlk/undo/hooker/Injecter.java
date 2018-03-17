package top.imlk.undo.hooker;


import android.content.Context;
import android.content.res.XModuleResources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.EditText;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import top.imlk.undo.R;
import top.imlk.undo.callback.IActionModeCallbackProxy;
import top.imlk.undo.listener.ITextChangedListener;
import top.imlk.undo.staticValue.IResources;

public class Injecter implements IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {

    public static String MODULE_PATH = null;


    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }


    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        IResources.setResources(XModuleResources.createInstance(MODULE_PATH, resparam.res));
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        XposedHelpers.findAndHookConstructor(EditText.class, Context.class, AttributeSet.class, int.class, int.class, new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                if ((((EditText) param.thisObject).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG) == null) || (!(((EditText) param.thisObject).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG) instanceof Boolean)) || (!((Boolean) ((EditText) param.thisObject).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG)).booleanValue())) {
                    ((EditText) param.thisObject).addTextChangedListener(new ITextChangedListener(((EditText) param.thisObject)));
                }
            }
        });

        XposedHelpers.findAndHookMethod(View.class, "startActionMode", ActionMode.Callback.class, int.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);

                Log.e("Injecter", "beforeHookedMethod");
                if (param.thisObject instanceof EditText) {
                    if ((!(param.args[0] instanceof IActionModeCallbackProxy)) && ((Boolean) ((EditText) param.thisObject).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG)).booleanValue()) {
                        param.args[0] = new IActionModeCallbackProxy((ActionMode.Callback) param.args[0], ((EditText) param.thisObject));
                    }
                }
            }
        });


//        XposedBridge.log("Down!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }
}
