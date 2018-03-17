package top.imlk.undo.undoUtil;


import android.widget.EditText;

import top.imlk.undo.R;

public class IUndoOperation {
    private int start;

    public CharSequence getOldString() {
        return oldString;
    }

    public CharSequence getNewString() {
        return newString;
    }

    private CharSequence oldString;
    private CharSequence newString;

    public long time;

    public IUndoOperation nextUndo;
    public IUndoOperation nextRedo;

    public IUndoOperation() {
        time = System.currentTimeMillis();
    }

    public void undo(EditText editText) {
        editText.setTag(R.id.TOP_IMLK_UNDO_SHOULDDO_TAG, false);
        editText.setText(editText.getText().replace(start, start + newString.length(), oldString));
        editText.setTag(R.id.TOP_IMLK_UNDO_SHOULDDO_TAG, true);
        if (oldString.length() == 0) {
            editText.setSelection(start);
        } else {
            editText.setSelection(start, start + oldString.length());
        }
    }

    public void redo(EditText editText) {
        editText.setTag(R.id.TOP_IMLK_UNDO_SHOULDDO_TAG, false);
        editText.setText(editText.getText().replace(start, start + oldString.length(), newString));
        editText.setTag(R.id.TOP_IMLK_UNDO_SHOULDDO_TAG, true);
        if (newString.length() == 0) {
            editText.setSelection(start);
        } else {
            editText.setSelection(start, start + newString.length());
        }
    }


    public void setStart(int start) {
        this.start = start;
    }

    public void setOldString(CharSequence oldString) {
        this.oldString = oldString;
    }

    public void setNewString(CharSequence newString) {
        this.newString = newString;
    }
}
