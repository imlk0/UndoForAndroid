package top.imlk.undo.undoUtil;


import android.util.Log;
import android.widget.EditText;

import top.imlk.undo.R;

public class IUndoOperation {
    public int start;

    public CharSequence oldString;
    public CharSequence newString;

    public long time;

    public IUndoOperation nextUndo;
    public IUndoOperation nextRedo;

    public IUndoOperation() {
        time = System.currentTimeMillis();
    }

    public void undo(EditText editText) {
//        editText.setTag(R.id.TOP_IMLK_UNDO_SHOULDDO_TAG, false);

        editText.setText(editText.getText().replace(start, start + newString.length(), oldString));
//        editText.setTag(R.id.TOP_IMLK_UNDO_SHOULDDO_TAG, true);
//        if (oldString.length() == 0) {
//            editText.setSelection(start);
//        } else {
//            editText.setSelection(start, start + oldString.length());
//        }
    }

    public void redo(EditText editText) {
//        editText.setTag(R.id.TOP_IMLK_UNDO_SHOULDDO_TAG, false);
        editText.setText(editText.getText().replace(start, start + oldString.length(), newString));
//        editText.setTag(R.id.TOP_IMLK_UNDO_SHOULDDO_TAG, true);
//        if (newString.length() == 0) {
//            editText.setSelection(start);
//        } else {
//            editText.setSelection(start, start + newString.length());
//        }
    }


}
