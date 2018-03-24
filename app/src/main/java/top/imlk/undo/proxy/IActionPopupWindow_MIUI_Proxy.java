package top.imlk.undo.proxy;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

import top.imlk.undo.R;
import top.imlk.undo.holder.IMember;
import top.imlk.undo.holder.IResources;
import top.imlk.undo.holder.Iid;
import top.imlk.undo.hooker.Injecter;
import top.imlk.undo.type.OsType;
import top.imlk.undo.undoUtil.IUndoManager;


//for miui

public class IActionPopupWindow_MIUI_Proxy extends IActionPopupWindowProxy implements View.OnClickListener {

    public IActionPopupWindow_MIUI_Proxy(EditText editText) {
        this.mEditText = editText;
    }

    public boolean initContentView(Object param_thisObject) throws IllegalAccessException, InvocationTargetException {

        //获取装盛这些view的容器
        ViewGroup viewGroup = getViewGroup(param_thisObject);

        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            if (viewGroup.getChildAt(i).getId() == Iid.menu_undo) {
                mUndoTextView = ((TextView) viewGroup.getChildAt(i));
            }

            if (viewGroup.getChildAt(i).getId() == Iid.menu_redo) {
                mRedoTextView = ((TextView) viewGroup.getChildAt(i));
            }
        }

//            XposedBridge.log(viewGroup.getChildCount() + "");
//            XposedBridge.log(new Throwable());


        boolean result = false;
        int index = findInsertIndex(viewGroup);

        if (mUndoTextView == null) {

            mUndoTextView = ((TextView) IMember.METHOD.ActionPopupWindow_newTextView_Method.invoke(param_thisObject));
            viewGroup.addView(mUndoTextView, index++);
            mUndoTextView.setText(IResources.resources.getString(R.string.undo));
            mUndoTextView.setOnClickListener(this);
            mUndoTextView.setId(Iid.menu_undo);

            result = true;
        }

        if (mRedoTextView == null) {
            mRedoTextView = ((TextView) IMember.METHOD.ActionPopupWindow_newTextView_Method.invoke(param_thisObject));
            viewGroup.addView(mRedoTextView, index++);
            mRedoTextView.setText(IResources.resources.getString(R.string.redo));
            mRedoTextView.setOnClickListener(this);
            mRedoTextView.setId(Iid.menu_redo);

            result = true;
        }


        return result;
    }


    public void before_show(Object param_thisObject) throws InvocationTargetException, IllegalAccessException {

        IUndoManager iUndoManager = IUndoManager.getIUndoManager(this.mEditText);
        boolean canUndo = iUndoManager.canPerformUndo();
        boolean canRedo = iUndoManager.canPerformRedo();
        mUndoTextView.setVisibility(canUndo ? View.VISIBLE : View.GONE);
        mRedoTextView.setVisibility(canRedo ? View.VISIBLE : View.GONE);

    }


    private static int findInsertIndex(ViewGroup viewGroup) {
        int i;
        for (i = 0; i < viewGroup.getChildCount(); ++i) {
            if (viewGroup.getChildAt(i) instanceof TextView) {
                return i;
            }
        }
        return i;
    }

    public static ViewGroup getViewGroup(Object param_thisObject) throws IllegalAccessException {

        if (Injecter.osType == OsType.MIUI) {
            return ((ViewGroup) IMember.FIELD.ActionPinnedPopupWindow_mMainPanel_Field.get(param_thisObject));
        } else {

            return ((ViewGroup) IMember.FIELD.FloatPanelView_mContent_Field.get(IMember.FIELD.ActionPinnedPopupWindow_mPanel_Field.get(param_thisObject)));
        }

    }

    public static int getVisibleChildrenCount(Object param_thisObject) throws IllegalAccessException {

        ViewGroup viewGroup = getViewGroup(param_thisObject);

        int count = 0;

        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            if (viewGroup.getChildAt(i).getVisibility() == View.VISIBLE) {
                count++;
            }
        }

        return count;
    }


}