package top.imlk.undo.listener;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import top.imlk.undo.holder.ITag;
import top.imlk.undo.undoUtil.IUndoManager;
import top.imlk.undo.undoUtil.IUndoOperation;

public class ITextChangedListener implements TextWatcher {

    private EditText mEditText;

    private IUndoOperation newIUndoOperation;
    private IUndoOperation oldIUndoOperation;


    public ITextChangedListener(EditText mEditText) {
        this.mEditText = mEditText;
    }


    private boolean shouldDo() {
        Object o = mEditText.getTag(ITag.TOP_IMLK_UNDO_SHOULDDO_TAG);
        if (o == null) {
            mEditText.setTag(ITag.TOP_IMLK_UNDO_SHOULDDO_TAG, true);
            return true;
        }

        return (o instanceof Boolean) && (((Boolean) o).booleanValue());
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        if (shouldDo()) {
            newIUndoOperation = new IUndoOperation();

//            Log.e("Changed", "" + newIUndoOperation.time);


            newIUndoOperation.start = start;
            newIUndoOperation.oldString = s.subSequence(start, start + count);
        } else {
            oldIUndoOperation = null;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (shouldDo()) {
            newIUndoOperation.newString = s.subSequence(start, start + count);

            if (!(newIUndoOperation.newString == null || newIUndoOperation.oldString == null || (newIUndoOperation.newString.length() == 0 && newIUndoOperation.oldString.length() == 0))) {

                if (oldIUndoOperation != null && newIUndoOperation.time - oldIUndoOperation.time < 360) {
                    oldIUndoOperation.nextRedo = newIUndoOperation;
                    newIUndoOperation.nextUndo = oldIUndoOperation;
                }

                IUndoManager.getIUndoManager(this.mEditText).addIUndoOperation(newIUndoOperation);
                IUndoManager.getIUndoManager(this.mEditText).cleanAllRedo();
            }

            oldIUndoOperation = newIUndoOperation;
            newIUndoOperation = null;
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
