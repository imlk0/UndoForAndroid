package top.imlk.undo.proxy;

import android.content.Context;
import android.os.Debug;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;

import top.imlk.undo.R;
import top.imlk.undo.holder.IMember;
import top.imlk.undo.holder.IResources;
import top.imlk.undo.undoUtil.IUndoManager;

/**
 * Created by imlk on 2018/3/19.
 */

//for android version under 6
public class IActionPopupWindowProxy implements View.OnClickListener {

    public TextView mUndoTextView;
    public TextView mRedoTextView;

    public EditText mEditText;

    public IActionPopupWindowProxy() {
    }

    public IActionPopupWindowProxy(EditText editText) {
        this.mEditText = editText;
    }

    public void initContentView(Object param_thisObject) throws IllegalAccessException, InvocationTargetException {

        LayoutInflater inflater = (LayoutInflater) mEditText.getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewGroup.LayoutParams layoutParams = ((TextView) IMember.FIELD.ActionPopupWindow_mPasteTextView_Field.get(param_thisObject)).getLayoutParams();

        int index = 0;

        mUndoTextView = (TextView) inflater.inflate((Integer) IMember.FIELD.POPUP_TEXT_LAYOUT_Field.get(param_thisObject), null);
        mUndoTextView.setLayoutParams(layoutParams);
        ((ViewGroup) IMember.FIELD.ActionPopupWindow_mContentView_Field.get(param_thisObject)).addView(mUndoTextView, index++);
        mUndoTextView.setText(IResources.resources.getString(R.string.undo));
        mUndoTextView.setOnClickListener(this);
        mUndoTextView.setId(R.id.menu_undo);


        mRedoTextView = (TextView) inflater.inflate((Integer) IMember.FIELD.POPUP_TEXT_LAYOUT_Field.get(param_thisObject), null);
        mRedoTextView.setLayoutParams(layoutParams);
        ((ViewGroup) IMember.FIELD.ActionPopupWindow_mContentView_Field.get(param_thisObject)).addView(mRedoTextView, index++);
        mRedoTextView.setText(IResources.resources.getString(R.string.redo));
        mRedoTextView.setOnClickListener(this);
        mRedoTextView.setId(R.id.menu_redo);

    }


    public void before_show(Object param_thisObject) throws InvocationTargetException, IllegalAccessException {

        Object editor = IMember.FIELD.ActionPopupWindow_this$0_Field.get(param_thisObject);
        IUndoManager iUndoManager = IUndoManager.getIUndoManager(this.mEditText);

        boolean canPaste = (boolean) IMember.METHOD.TextView_canPaste_Method.invoke(mEditText);
        boolean canSuggest = mEditText.isSuggestionsEnabled() && ((boolean) IMember.METHOD.Editor_isCursorInsideSuggestionSpan_Method.invoke(editor));
        boolean canUndo = iUndoManager.canPerformUndo();
        boolean canRedo = iUndoManager.canPerformRedo();

        ((TextView) IMember.FIELD.ActionPopupWindow_mPasteTextView_Field.get(param_thisObject)).setVisibility(canPaste ? View.VISIBLE : View.GONE);
        ((TextView) IMember.FIELD.ActionPopupWindow_mReplaceTextView_Field.get(param_thisObject)).setVisibility(canSuggest ? View.VISIBLE : View.GONE);

        mUndoTextView.setVisibility(canUndo ? View.VISIBLE : View.GONE);
        mRedoTextView.setVisibility(canRedo ? View.VISIBLE : View.GONE);

        if (!canPaste && !canSuggest && !canUndo && !canRedo) return;


//        IMember.METHOD.PinnedPopupWindow_show_Method.invoke(param_thisObject);//错误的表达


        Object positionListener = IMember.METHOD.Editor_getPositionListener_Method.invoke(editor);
        IMember.METHOD.PositionListener_addSubscriber_Method.invoke(positionListener, param_thisObject, false);

        IMember.METHOD.PinnedPopupWindow_computeLocalPosition_Method.invoke(param_thisObject);

        IMember.METHOD.PinnedPopupWindow_updatePosition_Method.invoke(param_thisObject,
                IMember.METHOD.PositionListener_getPositionX_Method.invoke(positionListener),
                IMember.METHOD.PositionListener_getPositionY_Method.invoke(positionListener));


/*
        getPositionListener().addSubscriber(this, false */
/* offset is fixed *//*
);

        computeLocalPosition();

        final PositionListener positionListener = getPositionListener();
        updatePosition(positionListener.getPositionX(), positionListener.getPositionY());

        super.show();
*/

    }


    @Override
    public void onClick(View v) {
//        Debug.waitForDebugger();
        switch (v.getId()) {
            case R.id.menu_undo:
                Log.e("IAMCallbackProxy", "performUndo");
                IUndoManager.getIUndoManager(this.mEditText).performUndo();
                break;
            case R.id.menu_redo:
                Log.e("IAMCallbackProxy", "performRedo");
                IUndoManager.getIUndoManager(this.mEditText).performRedo();
                break;
        }
    }
}