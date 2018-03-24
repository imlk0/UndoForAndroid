package top.imlk.undo.hooker;


import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.ActionMode;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.reflect.Method;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import top.imlk.undo.holder.ITag;
import top.imlk.undo.proxy.IActionModeCallbackProxy;
import top.imlk.undo.holder.IResources;
import top.imlk.undo.listener.ITextChangedListener;
import top.imlk.undo.holder.IMember;
import top.imlk.undo.proxy.IActionPopupWindowProxy;
import top.imlk.undo.proxy.IActionPopupWindow_MIUI_Proxy;
import top.imlk.undo.type.OsType;


public class Injecter implements IXposedHookLoadPackage {

    public static OsType osType = checkOsType();

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

                //初始化resources
                if (IResources.resources == null) {
                    IResources.initResources(((Context) param.args[0]));
                }

                //TODO change ID from 7f to 77
                if ((((EditText) param.thisObject).getTag(ITag.TOP_IMLK_UNDO_INJECTED_TAG) == null)
                        || (!(((EditText) param.thisObject).getTag(ITag.TOP_IMLK_UNDO_INJECTED_TAG) instanceof Boolean))
                        || (!((Boolean) ((EditText) param.thisObject).getTag(ITag.TOP_IMLK_UNDO_INJECTED_TAG)).booleanValue())) {
                    ((EditText) param.thisObject).addTextChangedListener(new ITextChangedListener(((EditText) param.thisObject)));
                    ((EditText) param.thisObject).setTag(ITag.TOP_IMLK_UNDO_INJECTED_TAG, true);

                }
            }
        });


        if (osType == OsType.AOSP) {

            XposedHelpers.findAndHookMethod(View.class, "startActionMode", ActionMode.Callback.class, int.class, new XC_MethodHook() {

                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);

//                Log.e("Injecter", "beforeHookedMethod");
                    if (param.thisObject instanceof EditText) {
                        if ((!(param.args[0] instanceof IActionModeCallbackProxy))
                                && (((EditText) param.thisObject).getTag(ITag.TOP_IMLK_UNDO_INJECTED_TAG) != null)
                                && (((EditText) param.thisObject).getTag(ITag.TOP_IMLK_UNDO_INJECTED_TAG) instanceof Boolean)
                                && ((Boolean) ((EditText) param.thisObject).getTag(ITag.TOP_IMLK_UNDO_INJECTED_TAG)).booleanValue()) {
                            param.args[0] = new IActionModeCallbackProxy((ActionMode.Callback) param.args[0], ((EditText) param.thisObject));
                        }
                    }
                }
            });

        } else {


//            final Class handleView_Class = XposedHelpers.findClass("android.widget.Editor.HandleView", lpparam.classLoader);
//            XposedHelpers.findAndHookMethod(handleView_Class, "showActionPopupWindow", int.class, new XC_MethodHook() {
//                @Override
//                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                    super.beforeHookedMethod(param);
//                    handleView_Class.getDeclaredField("mActionPopupWindow");
//
//                }
//            });

            IMember.CLASS.ActionPopupWindow_Class = XposedHelpers.findClass("android.widget.Editor.ActionPopupWindow", lpparam.classLoader);
            IMember.CLASS.Editor_Class = XposedHelpers.findClass("android.widget.Editor", lpparam.classLoader);
            IMember.CLASS.PinnedPopupWindow_Class = XposedHelpers.findClass("android.widget.Editor.PinnedPopupWindow", lpparam.classLoader);
            IMember.CLASS.PositionListener_Class = XposedHelpers.findClass("android.widget.Editor.PositionListener", lpparam.classLoader);
            IMember.CLASS.TextViewPositionListener_Class = XposedHelpers.findClass("android.widget.Editor.TextViewPositionListener", lpparam.classLoader);


            IMember.FIELD.ActionPopupWindow_this$0_Field = IMember.CLASS.ActionPopupWindow_Class.getDeclaredField("this$0");
            IMember.FIELD.ActionPopupWindow_this$0_Field.setAccessible(true);
            IMember.FIELD.Editor_mTextView_Field = IMember.CLASS.Editor_Class.getDeclaredField("mTextView");
            IMember.FIELD.Editor_mTextView_Field.setAccessible(true);


            IMember.METHOD.TextView_canPaste_Method = TextView.class.getDeclaredMethod("canPaste");
            IMember.METHOD.TextView_canPaste_Method.setAccessible(true);
            IMember.METHOD.PinnedPopupWindow_show_Method = IMember.CLASS.PinnedPopupWindow_Class.getDeclaredMethod("show");
            IMember.METHOD.PinnedPopupWindow_show_Method.setAccessible(true);
            IMember.METHOD.Editor_getPositionListener_Method = IMember.CLASS.Editor_Class.getDeclaredMethod("getPositionListener");
            IMember.METHOD.Editor_getPositionListener_Method.setAccessible(true);
            IMember.METHOD.PinnedPopupWindow_computeLocalPosition_Method = IMember.CLASS.PinnedPopupWindow_Class.getDeclaredMethod("computeLocalPosition");
            IMember.METHOD.PinnedPopupWindow_computeLocalPosition_Method.setAccessible(true);

            IMember.METHOD.PositionListener_getPositionX_Method = IMember.CLASS.PositionListener_Class.getDeclaredMethod("getPositionX");
            IMember.METHOD.PositionListener_getPositionX_Method.setAccessible(true);

            IMember.METHOD.PositionListener_getPositionY_Method = IMember.CLASS.PositionListener_Class.getDeclaredMethod("getPositionY");
            IMember.METHOD.PositionListener_getPositionY_Method.setAccessible(true);

            IMember.METHOD.PinnedPopupWindow_updatePosition_Method = IMember.CLASS.PinnedPopupWindow_Class.getDeclaredMethod("updatePosition", int.class, int.class);
            IMember.METHOD.PinnedPopupWindow_updatePosition_Method.setAccessible(true);
            IMember.METHOD.PositionListener_addSubscriber_Method = IMember.CLASS.PositionListener_Class.getDeclaredMethod("addSubscriber", IMember.CLASS.TextViewPositionListener_Class, boolean.class);
            IMember.METHOD.PositionListener_addSubscriber_Method.setAccessible(true);


            if (osType == OsType.MIUI) {
                IMember.CLASS.ActionPinnedPopupWindow_Class = XposedHelpers.findClass("android.widget.Editor.ActionPinnedPopupWindow", lpparam.classLoader);

//                XposedBridge.log(IMember.CLASS.ActionPinnedPopupWindow_Class.getResource("").getPath());
//
//                XposedBridge.log("Undo start");
//                for(Field field:IMember.CLASS.ActionPinnedPopupWindow_Class.getDeclaredFields()){
//                    XposedBridge.log(field.getType().getName() + " " +  field.getName());
//                }
//                XposedBridge.log("Undo end");


                try {
                    IMember.FIELD.ActionPinnedPopupWindow_mMainPanel_Field = IMember.CLASS.ActionPinnedPopupWindow_Class.getDeclaredField("mMainPanel");
                    IMember.FIELD.ActionPinnedPopupWindow_mMainPanel_Field.setAccessible(true);
                    IMember.METHOD.ActionPinnedPopupWindow_setMainPanelAsContent_Method = IMember.CLASS.ActionPinnedPopupWindow_Class.getDeclaredMethod("setMainPanelAsContent");
                    IMember.METHOD.ActionPinnedPopupWindow_setMainPanelAsContent_Method.setAccessible(true);

                } catch (NoSuchFieldException e) {
                    Log.e("Undo", "isOldMIUI");
                    osType = OsType.MIUI_OLD;

                    IMember.CLASS.FloatPanelView_Class = XposedHelpers.findClass("android.widget.FloatPanelView", lpparam.classLoader);
                    IMember.FIELD.FloatPanelView_mContent_Field = IMember.CLASS.FloatPanelView_Class.getDeclaredField("mContent");
                    IMember.FIELD.FloatPanelView_mContent_Field.setAccessible(true);

                    IMember.FIELD.ActionPinnedPopupWindow_mPanel_Field = IMember.CLASS.ActionPinnedPopupWindow_Class.getDeclaredField("mPanel");
                    IMember.FIELD.ActionPinnedPopupWindow_mPanel_Field.setAccessible(true);

                }

                IMember.METHOD.ActionPopupWindow_newTextView_Method = IMember.CLASS.ActionPopupWindow_Class.getDeclaredMethod("newTextView");
                IMember.METHOD.ActionPopupWindow_newTextView_Method.setAccessible(true);

            } else {
                IMember.FIELD.POPUP_TEXT_LAYOUT_Field = IMember.CLASS.ActionPopupWindow_Class.getDeclaredField("POPUP_TEXT_LAYOUT");
                IMember.FIELD.POPUP_TEXT_LAYOUT_Field.setAccessible(true);
                IMember.FIELD.ActionPopupWindow_mPasteTextView_Field = IMember.CLASS.ActionPopupWindow_Class.getDeclaredField("mPasteTextView");
                IMember.FIELD.ActionPopupWindow_mPasteTextView_Field.setAccessible(true);
                IMember.FIELD.ActionPopupWindow_mReplaceTextView_Field = IMember.CLASS.ActionPopupWindow_Class.getDeclaredField("mReplaceTextView");
                IMember.FIELD.ActionPopupWindow_mReplaceTextView_Field.setAccessible(true);
                IMember.FIELD.ActionPopupWindow_mContentView_Field = IMember.CLASS.PinnedPopupWindow_Class.getDeclaredField("mContentView");
                IMember.FIELD.ActionPopupWindow_mContentView_Field.setAccessible(true);

                IMember.METHOD.Editor_isCursorInsideSuggestionSpan_Method = IMember.CLASS.Editor_Class.getDeclaredMethod("isCursorInsideSuggestionSpan");
                IMember.METHOD.Editor_isCursorInsideSuggestionSpan_Method.setAccessible(true);

            }


            XposedHelpers.findAndHookMethod(IMember.CLASS.ActionPopupWindow_Class, "initContentView", new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);


                    Log.e("Undo", "after initContentView");

//                    Debug.waitForDebugger();

                    Object editText = IMember.FIELD.Editor_mTextView_Field.get(IMember.FIELD.ActionPopupWindow_this$0_Field.get(param.thisObject));

                    if (editText instanceof EditText
                            && (((EditText) editText).getTag(ITag.TOP_IMLK_UNDO_INJECTED_TAG) != null)
                            && ((((EditText) editText).getTag(ITag.TOP_IMLK_UNDO_INJECTED_TAG) instanceof Boolean))
                            && ((Boolean) ((EditText) editText).getTag(ITag.TOP_IMLK_UNDO_INJECTED_TAG)).booleanValue()) {


                        IActionPopupWindowProxy iActionPopupWindowProxy = (osType == OsType.MIUI || osType == OsType.MIUI_OLD) ?
                                new IActionPopupWindow_MIUI_Proxy(((EditText) editText))
                                : new IActionPopupWindowProxy(((EditText) editText));


                        if (iActionPopupWindowProxy.initContentView(param.thisObject)) {
                            ((EditText) editText).setTag(ITag.TOP_IMLK_UNDO_IACTIONPOPUPWINDOWPROXY_TAG, iActionPopupWindowProxy);
                        }

                    } else {
                        Log.e("Undo", "un suit View");
                    }

                }
            });


            XposedHelpers.findAndHookMethod(IMember.CLASS.ActionPopupWindow_Class, "show", new XC_MethodHook() {

                private boolean in = false;

                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);

                    if (in) {
                        return;
                    }

                    in = true;

                    XposedBridge.log("Undo" + "before show");


                    Object editText = IMember.FIELD.Editor_mTextView_Field.get(IMember.FIELD.ActionPopupWindow_this$0_Field.get(param.thisObject));

                    FrameLayout frameLayout;


                    if (editText instanceof EditText
                            && ((EditText) editText).getTag(ITag.TOP_IMLK_UNDO_IACTIONPOPUPWINDOWPROXY_TAG) != null
                            && ((EditText) editText).getTag(ITag.TOP_IMLK_UNDO_IACTIONPOPUPWINDOWPROXY_TAG) instanceof IActionPopupWindowProxy) {

                        XposedBridge.log("Undo" + "before show Proxy");

                        ((IActionPopupWindowProxy) ((EditText) editText).getTag(ITag.TOP_IMLK_UNDO_IACTIONPOPUPWINDOWPROXY_TAG)).before_show(param.thisObject);
                        if (osType == OsType.AOSP_UNDER_M) {//原生系统
                            in = false;
                            param.setResult(null);
                        }
                    }
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);

                    if (!in) {
                        return;
                    }
                    in = false;

                    if (osType == OsType.MIUI || osType == OsType.MIUI_OLD) {
                        //MIUI
                        if (IActionPopupWindow_MIUI_Proxy.getVisibleChildrenCount(param.thisObject) != 0) {


                            Log.e("Undo", "show PopupWindow");


                            Object editText = IMember.FIELD.Editor_mTextView_Field.get(IMember.FIELD.ActionPopupWindow_this$0_Field.get(param.thisObject));


                            if (osType == OsType.MIUI) {

                                //        public void show() {
                                //            setMainPanelAsContent();
                                //            super.show();
                                //        }
                                IMember.METHOD.ActionPinnedPopupWindow_setMainPanelAsContent_Method.invoke(param.thisObject);
                            }

                            ((IActionPopupWindow_MIUI_Proxy) ((EditText) editText).getTag(ITag.TOP_IMLK_UNDO_IACTIONPOPUPWINDOWPROXY_TAG)).super_show(param.thisObject);

                        }

                    }
                }
            });


        }

//        XposedBridge.log("Down!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

    }

    public static OsType checkOsType() {
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class, String.class);

            Log.e("Undo", "android.os.SystemProperties:" + get.invoke(c, "ro.miui.ui.version.name", "unknown"));

            return (get.invoke(c, "ro.miui.ui.version.name", "unknown")) != "unknown" ?
                    OsType.MIUI
                    : Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                    OsType.AOSP : OsType.AOSP_UNDER_M;
        } catch (Exception e) {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ?
                    OsType.AOSP
                    : OsType.AOSP_UNDER_M;
        }
    }
}
