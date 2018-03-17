package top.imlk.undo.listener;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import top.imlk.undo.R;
import top.imlk.undo.undoUtil.IUndoManager;
import top.imlk.undo.undoUtil.IUndoOperation;

public class ITextChangedListener implements TextWatcher {

    private EditText mEditText;

    private IUndoOperation newIUndoOperation;
    private IUndoOperation oldIUndoOperation;


    public ITextChangedListener(EditText mEditText) {
        this.mEditText = mEditText;
        this.mEditText.setTag(R.id.TOP_IMLK_UNDO_INJECTED_TAG, true);
    }


    private boolean shouldDo() {
        Object o = mEditText.getTag(R.id.TOP_IMLK_UNDO_SHOULDDO_TAG);
        if (o == null) {
            mEditText.setTag(R.id.TOP_IMLK_UNDO_SHOULDDO_TAG, true);
            return true;
        }

        return (o instanceof Boolean) && (((Boolean) o).booleanValue());
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


        if (shouldDo()) {
            newIUndoOperation = new IUndoOperation();

            Log.e("Changed", "" + newIUndoOperation.time);

            if (oldIUndoOperation != null && newIUndoOperation.time - oldIUndoOperation.time < 360) {
                oldIUndoOperation.nextRedo = newIUndoOperation;
                newIUndoOperation.nextUndo = oldIUndoOperation;
            }

            newIUndoOperation.setStart(start);
            newIUndoOperation.setOldString(s.subSequence(start, start + count));
        } else {
            oldIUndoOperation = null;
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (shouldDo()) {
            newIUndoOperation.setNewString(s.subSequence(start, start + count));

            if (!(newIUndoOperation.getNewString() == null || newIUndoOperation.getOldString() == null || (newIUndoOperation.getNewString().length() == 0 && newIUndoOperation.getOldString().length() == 0))) {
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
