package top.imlk.undo.undoUtil;


import android.widget.EditText;

import java.util.ArrayList;

import top.imlk.undo.R;


public class IUndoManager {

    private ArrayList<IUndoOperation> undoOperations = new ArrayList<>();
    private ArrayList<IUndoOperation> redoOperations = new ArrayList<>();

    private final static String IUNDOMANAGER_TAG = "TOP_IMLK_UNDO_IUNDOMANAGER_TAG";

    private EditText mEditText;

    public static IUndoManager getIUndoManager(EditText editText) {
        if (editText.getTag(R.id.TOP_IMLK_UNDO_IUNDOMANAGER_TAG) == null || (!(editText.getTag(R.id.TOP_IMLK_UNDO_IUNDOMANAGER_TAG) instanceof IUndoManager))) {
            editText.setTag(R.id.TOP_IMLK_UNDO_IUNDOMANAGER_TAG, new IUndoManager(editText));
        }
        return (IUndoManager) editText.getTag(R.id.TOP_IMLK_UNDO_IUNDOMANAGER_TAG);
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
            while (iUndoOperation != null) {
                undoOperations.remove(undoOperations.size() - 1);
                iUndoOperation.undo(this.mEditText);
                redoOperations.add(iUndoOperation);
                iUndoOperation = iUndoOperation.nextUndo;
            }
        }
    }

    public void performRedo() {
        if (canPerformRedo()) {
            IUndoOperation iUndoOperation = redoOperations.get(redoOperations.size() - 1);
            while (iUndoOperation != null) {
                redoOperations.remove(redoOperations.size() - 1);
                iUndoOperation.redo(this.mEditText);
                undoOperations.add(iUndoOperation);
                iUndoOperation = iUndoOperation.nextRedo;
            }
        }
    }

    public void addIUndoOperation(IUndoOperation iUndoOperation) {
        undoOperations.add(iUndoOperation);
    }

    public void cleanAllRedo() {
        redoOperations.clear();
    }

}
