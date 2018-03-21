package top.imlk.undo.hooker;


import android.content.Context;
import android.content.res.XModuleResources;
import android.os.Build;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import top.imlk.undo.R;
import top.imlk.undo.proxy.IActionModeCallbackProxy;
import top.imlk.undo.holder.IResources;
import top.imlk.undo.listener.ITextChangedListener;
import top.imlk.undo.holder.IMember;
import top.imlk.undo.proxy.IActionPopupWindowProxy;


public class Injecter implements IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {

    public static String MODULE_PATH = null;


    @Override
    public void initZygote(IXposedHookZygoteInit.StartupParam startupParam) throws Throwable {
        MODULE_PATH = startupParam.modulePath;
    }

    @Override
    public void handleInitPackageResources(XC_InitPackageResources.InitPackageResourcesParam resparam) throws Throwable {
        IResources.resources = XModuleResources.createInstance(MODULE_PATH, resparam.res);

    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

//        for(Method method : View.class.getDeclaredMethods()){
//            XposedBridge.log(method.getReturnType().getName() + " " + method.getName() + "(");
//            for(Class clazz : method.getParameterTypes()){
//                XposedBridge.log(clazz.getName() + ", ");
//            }
//            XposedBridge.log(")");
//        }

        XposedBridge.hookAllConstructors(EditText.class, new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);

                //TODO change ID from 7f to 77
                if ((((EditText) param.thisObject).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG) == null)
                        || (!(((EditText) param.thisObject).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG) instanceof Boolean))
                        || (!((Boolean) ((EditText) param.thisObject).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG)).booleanValue())) {
                    ((EditText) param.thisObject).addTextChangedListener(new ITextChangedListener(((EditText) param.thisObject)));
                    ((EditText) param.thisObject).setTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG, true);

                }
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            XposedHelpers.findAndHookMethod(View.class, "startActionMode", ActionMode.Callback.class, int.class, new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);

//                Log.e("Injecter", "beforeHookedMethod");
                    if (param.thisObject instanceof EditText) {
                        if ((!(param.args[0] instanceof IActionModeCallbackProxy))
                                && (((EditText) param.thisObject).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG) != null)
                                && (((EditText) param.thisObject).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG) instanceof Boolean)
                                && ((Boolean) ((EditText) param.thisObject).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG)).booleanValue()) {
                            param.args[0] = new IActionModeCallbackProxy((ActionMode.Callback) param.args[0], ((EditText) param.thisObject));
                        }
                    }
                }
            });

        } else {
            //            "android.widget.TextView.Editor.ActionPopupWindow"
//                public EditText(Context context, AttributeSet attrs, int defStyle) {

//            final Class handleView_Class = XposedHelpers.findClass("android.widget.Editor.HandleView", lpparam.classLoader);
//            XposedHelpers.findAndHookMethod(handleView_Class, "showActionPopupWindow", int.class, new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    super.beforeHookedMethod(param);
//                    handleView_Class.getDeclaredField("mActionPopupWindow");
//
//
//
//                }
//            });

            IMember.CLASS.ActionPopupWindow_Class = XposedHelpers.findClass("android.widget.Editor.ActionPopupWindow", lpparam.classLoader);
            IMember.CLASS.Editor_Class = XposedHelpers.findClass("android.widget.Editor", lpparam.classLoader);
            IMember.CLASS.PinnedPopupWindow_Class = XposedHelpers.findClass("android.widget.Editor.PinnedPopupWindow", lpparam.classLoader);
            IMember.CLASS.PositionListener_Class = XposedHelpers.findClass("android.widget.Editor.PositionListener", lpparam.classLoader);
            IMember.CLASS.TextViewPositionListener_Class = XposedHelpers.findClass("android.widget.Editor.TextViewPositionListener", lpparam.classLoader);


            IMember.FIELD.POPUP_TEXT_LAYOUT_Field = IMember.CLASS.ActionPopupWindow_Class.getDeclaredField("POPUP_TEXT_LAYOUT");
            IMember.FIELD.POPUP_TEXT_LAYOUT_Field.setAccessible(true);
            IMember.FIELD.ActionPopupWindow_this$0_Field = IMember.CLASS.ActionPopupWindow_Class.getDeclaredField("this$0");
            IMember.FIELD.ActionPopupWindow_this$0_Field.setAccessible(true);
            IMember.FIELD.ActionPopupWindow_mPasteTextView_Field = IMember.CLASS.ActionPopupWindow_Class.getDeclaredField("mPasteTextView");
            IMember.FIELD.ActionPopupWindow_mPasteTextView_Field.setAccessible(true);
            IMember.FIELD.ActionPopupWindow_mReplaceTextView_Field = IMember.CLASS.ActionPopupWindow_Class.getDeclaredField("mReplaceTextView");
            IMember.FIELD.ActionPopupWindow_mReplaceTextView_Field.setAccessible(true);
            IMember.FIELD.ActionPopupWindow_mContentView_Field = IMember.CLASS.PinnedPopupWindow_Class.getDeclaredField("mContentView");
            IMember.FIELD.ActionPopupWindow_mContentView_Field.setAccessible(true);
            IMember.FIELD.Editor_mTextView_Field = IMember.CLASS.Editor_Class.getDeclaredField("mTextView");
            IMember.FIELD.Editor_mTextView_Field.setAccessible(true);
            IMember.METHOD.TextView_canPaste_Method = TextView.class.getDeclaredMethod("canPaste");
            IMember.METHOD.TextView_canPaste_Method.setAccessible(true);
            IMember.METHOD.Editor_isCursorInsideSuggestionSpan_Method = IMember.CLASS.Editor_Class.getDeclaredMethod("isCursorInsideSuggestionSpan");
            IMember.METHOD.Editor_isCursorInsideSuggestionSpan_Method.setAccessible(true);
            IMember.METHOD.Editor_getPositionListener_Method = IMember.CLASS.Editor_Class.getDeclaredMethod("getPositionListener");
            IMember.METHOD.Editor_getPositionListener_Method.setAccessible(true);
            IMember.METHOD.PositionListener_addSubscriber_Method = IMember.CLASS.PositionListener_Class.getDeclaredMethod("addSubscriber", IMember.CLASS.TextViewPositionListener_Class, boolean.class);
            IMember.METHOD.PositionListener_addSubscriber_Method.setAccessible(true);
            IMember.METHOD.PositionListener_getPositionX_Method = IMember.CLASS.PositionListener_Class.getDeclaredMethod("getPositionX");
            IMember.METHOD.PositionListener_getPositionX_Method.setAccessible(true);
            IMember.METHOD.PositionListener_getPositionY_Method = IMember.CLASS.PositionListener_Class.getDeclaredMethod("getPositionY");
            IMember.METHOD.PositionListener_getPositionY_Method.setAccessible(true);
            IMember.METHOD.PinnedPopupWindow_show_Method = IMember.CLASS.PinnedPopupWindow_Class.getDeclaredMethod("show");
            IMember.METHOD.PinnedPopupWindow_show_Method.setAccessible(true);
            IMember.METHOD.PinnedPopupWindow_computeLocalPosition_Method = IMember.CLASS.PinnedPopupWindow_Class.getDeclaredMethod("computeLocalPosition");
            IMember.METHOD.PinnedPopupWindow_computeLocalPosition_Method.setAccessible(true);
            IMember.METHOD.PinnedPopupWindow_updatePosition_Method = IMember.CLASS.PinnedPopupWindow_Class.getDeclaredMethod("updatePosition", int.class, int.class);
            IMember.METHOD.PinnedPopupWindow_updatePosition_Method.setAccessible(true);

            XposedHelpers.findAndHookMethod(IMember.CLASS.ActionPopupWindow_Class, "initContentView", new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    Log.e("Undo", "after initContentView");

//                    Debug.waitForDebugger();

                    Object editText = IMember.FIELD.Editor_mTextView_Field.get(IMember.FIELD.ActionPopupWindow_this$0_Field.get(param.thisObject));

                    if (editText instanceof EditText
                            && (((EditText) editText).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG) != null)
                            && ((((EditText) editText).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG) instanceof Boolean))
                            && ((Boolean) ((EditText) editText).getTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG)).booleanValue()) {

                        IActionPopupWindowProxy iActionPopupWindowProxy = new IActionPopupWindowProxy(((EditText) editText));
                        iActionPopupWindowProxy.initContentView(param.thisObject);

                        ((EditText) editText).setTag(R.id.TOP_IMLK_UNDO_IACTIONPOPUPWINDOWPROXY_TAG, iActionPopupWindowProxy);
                    } else {
                        Log.e("Undo", "un suit View");
                    }

                }
            });


            XposedHelpers.findAndHookMethod(IMember.CLASS.ActionPopupWindow_Class, "show", new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);

                    Log.e("Undo", "before show");


                    Object editText = IMember.FIELD.Editor_mTextView_Field.get(IMember.FIELD.ActionPopupWindow_this$0_Field.get(param.thisObject));


                    if (editText instanceof EditText
                            && ((EditText) editText).getTag(R.id.TOP_IMLK_UNDO_IACTIONPOPUPWINDOWPROXY_TAG) != null
                            && ((EditText) editText).getTag(R.id.TOP_IMLK_UNDO_IACTIONPOPUPWINDOWPROXY_TAG) instanceof IActionPopupWindowProxy) {

                        Log.e("Undo", "before show Proxy");
                        ((IActionPopupWindowProxy) ((EditText) editText).getTag(R.id.TOP_IMLK_UNDO_IACTIONPOPUPWINDOWPROXY_TAG)).show(param.thisObject);


                        param.setResult(null);
                    }
                }
            });


        }

//        XposedBridge.log("Down!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }
}
