package top.imlk.undo.proxy;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

import top.imlk.undo.R;
import top.imlk.undo.holder.IMember;
import top.imlk.undo.holder.IResources;
import top.imlk.undo.undoUtil.IUndoManager;

/**
 * Created by imlk on 2018/3/19.
 */

//for miui

public class IActionPopupWindow_MIUI_Proxy extends IActionPopupWindowProxy implements View.OnClickListener {

    public IActionPopupWindow_MIUI_Proxy(EditText editText) {
        this.mEditText = editText;
    }

    public void initContentView(Object param_thisObject) throws IllegalAccessException, InvocationTargetException {


        LinearLayout mMainPanel = ((LinearLayout) IMember.FIELD.ActionPinnedPopupWindow_mMainPanel_Field.get(param_thisObject));


        int count = 0;
        for (int i = 0; i < mMainPanel.getChildCount(); ++i) {
            if (mMainPanel.getChildAt(i) == mUndoTextView || mMainPanel.getChildAt(i) == mUndoTextView) {
                count++;
            }
        }

        if (count == 0) {
            int index = findInsertIndex(mMainPanel);
            mUndoTextView = ((TextView) IMember.METHOD.ActionPopupWindow_newTextView_Method.invoke(param_thisObject));
            mMainPanel.addView(mUndoTextView, index++);
            mUndoTextView.setText(IResources.resources.getString(R.string.undo));
            mUndoTextView.setOnClickListener(this);
            mUndoTextView.setId(R.id.menu_undo);

            mRedoTextView = ((TextView) IMember.METHOD.ActionPopupWindow_newTextView_Method.invoke(param_thisObject));
            mMainPanel.addView(mRedoTextView, index++);
            mRedoTextView.setText(IResources.resources.getString(R.string.redo));
            mRedoTextView.setOnClickListener(this);
            mRedoTextView.setId(R.id.menu_redo);
        }


    }


    public void before_show(Object param_thisObject) throws InvocationTargetException, IllegalAccessException {

        IUndoManager iUndoManager = IUndoManager.getIUndoManager(this.mEditText);
        boolean canUndo = iUndoManager.canPerformUndo();
        boolean canRedo = iUndoManager.canPerformRedo();
        mUndoTextView.setVisibility(canUndo ? View.VISIBLE : View.GONE);
        mRedoTextView.setVisibility(canRedo ? View.VISIBLE : View.GONE);

    }

    public void after_show(Object param_thisObject) throws InvocationTargetException, IllegalAccessException {

        IMember.METHOD.ActionPinnedPopupWindow_setMainPanelAsContent_Method.invoke(param_thisObject);

        Object editor = IMember.FIELD.ActionPopupWindow_this$0_Field.get(param_thisObject);
        Object positionListener = IMember.METHOD.Editor_getPositionListener_Method.invoke(editor);
        IMember.METHOD.PositionListener_addSubscriber_Method.invoke(positionListener, param_thisObject, false);

        IMember.METHOD.PinnedPopupWindow_computeLocalPosition_Method.invoke(param_thisObject);

        IMember.METHOD.PinnedPopupWindow_updatePosition_Method.invoke(param_thisObject,
                IMember.METHOD.PositionListener_getPositionX_Method.invoke(positionListener),
                IMember.METHOD.PositionListener_getPositionY_Method.invoke(positionListener));
    }


    private int findInsertIndex(LinearLayout linearLayout) {
        int i;
        for (i = 0; i < linearLayout.getChildCount(); ++i) {
            if (linearLayout.getChildAt(i) instanceof TextView) {
                return i;
            }
        }
        return i;
    }


}