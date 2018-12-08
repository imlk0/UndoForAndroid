package top.imlk.undo.undoUtil;


import android.widget.EditText;

import java.util.ArrayList;

import top.imlk.undo.holder.ITag;


public class IUndoManager {

    private ArrayList<IUndoOperation> undoOperations = new ArrayList<>();
    private ArrayList<IUndoOperation> redoOperations = new ArrayList<>();

//    private final static String IUNDOMANAGER_TAG = "TOP_IMLK_UNDO_IUNDOMANAGER_TAG";

    private EditText mEditText;

    public static IUndoManager getIUndoManager(EditText editText) {
        if (editText.getTag(ITag.TOP_IMLK_UNDO_IUNDOMANAGER_TAG) == null || (!(editText.getTag(ITag.TOP_IMLK_UNDO_IUNDOMANAGER_TAG) instanceof IUndoManager))) {
            editText.setTag(ITag.TOP_IMLK_UNDO_IUNDOMANAGER_TAG, new IUndoManager(editText));
        }

        return (IUndoManager) editText.getTag(ITag.TOP_IMLK_UNDO_IUNDOMANAGER_TAG);
    }

    public IUndoManager(EditText mEditText) {
        this.mEditText = mEditText;
    }

    public boolean canPerformUndo() {
        return !undoOperations.isEmpty();
    }

    public boolean canPerformRedo() {
        return !redoOperations.isEmpty();
    }

    public void performUndo() {
        if (canPerformUndo()) {
            IUndoOperation iUndoOperation = undoOperations.get(undoOperations.size() - 1);

            mEditText.setTag(ITag.TOP_IMLK_UNDO_SHOULDDO_TAG, false);
            while (iUndoOperation != null && canPerformUndo()) {
                undoOperations.remove(undoOperations.size() - 1);
                redoOperations.add(iUndoOperation);
                iUndoOperation.undo(this.mEditText);
                iUndoOperation = iUndoOperation.nextUndo;
            }

            //取出最后一个iUndoOperation
            iUndoOperation = redoOperations.get(redoOperations.size() - 1);

            this.mEditText.setSelection(iUndoOperation.start + iUndoOperation.oldString.length());

            mEditText.setTag(ITag.TOP_IMLK_UNDO_SHOULDDO_TAG, true);
        }
    }

    public void performRedo() {
        if (canPerformRedo()) {
            IUndoOperation iUndoOperation = redoOperations.get(redoOperations.size() - 1);

            mEditText.setTag(ITag.TOP_IMLK_UNDO_SHOULDDO_TAG, false);
            while (iUndoOperation != null && canPerformRedo()) {
                redoOperations.remove(redoOperations.size() - 1);
                undoOperations.add(iUndoOperation);
                iUndoOperation.redo(this.mEditText);
                iUndoOperation = iUndoOperation.nextRedo;
            }


            //取出最后一个iUndoOperation
            iUndoOperation = undoOperations.get(undoOperations.size() - 1);
            this.mEditText.setSelection(iUndoOperation.start + iUndoOperation.newString.length());

            mEditText.setTag(ITag.TOP_IMLK_UNDO_SHOULDDO_TAG, true);

        }
    }

    public void addIUndoOperation(IUndoOperation iUndoOperation) {
        undoOperations.add(iUndoOperation);
    }

    public void cleanAllRedo() {
        redoOperations.clear();
    }

}
