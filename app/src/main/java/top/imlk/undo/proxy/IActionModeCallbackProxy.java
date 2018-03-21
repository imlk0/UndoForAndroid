package top.imlk.undo.proxy;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import top.imlk.undo.R;

import top.imlk.undo.holder.IResources;
import top.imlk.undo.undoUtil.IUndoManager;

/**
 * Created by imlk on 2018/3/15.
 */

public class IActionModeCallbackProxy implements ActionMode.Callback {

    private ActionMode.Callback mCallback;
    private EditText mEditText;

    public IActionModeCallbackProxy(ActionMode.Callback mCallback, EditText mEditText) {
        this.mCallback = mCallback;
        this.mEditText = mEditText;
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {

        IUndoManager iUndoManager = IUndoManager.getIUndoManager(this.mEditText);

        boolean b1 = mCallback.onCreateActionMode(mode, menu);
        boolean b2, b3;

        //由于第三方rom的修改，这里取消了取用系统字符串的做法
        menu.add(Menu.NONE, R.id.menu_undo, 2, IResources.resources.getString(R.string.undo))
//                mEditText.getResources().getSystem().getIdentifier("undo", "string", "android"))
                .setAlphabeticShortcut('z')
                .setEnabled(b2 = iUndoManager.canPerformUndo())
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        menu.add(Menu.NONE, R.id.menu_redo, 3, IResources.resources.getString(R.string.redo))
//                mEditText.getResources().getSystem().getIdentifier("redo", "string", "android"))
                .setEnabled(b3 = iUndoManager.canPerformRedo())
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return b1 || b2 || b3;
        //返回true表示弹出ActionMode

//
//        XmlResourceParser parser = null;
//        try {
//            parser = IResources.getResources().getLayout(R.menu.action_mode_menu);
//            AttributeSet attrs = Xml.asAttributeSet(parser);
//
////               private void parseMenu(XmlPullParser parser, AttributeSet attrs, Menu menu)
//            Method method = MenuInflater.class.getDeclaredMethod("parseMenu", XmlPullParser.class, AttributeSet.class, Menu.class);
//            method.setAccessible(true);
//            method.invoke(mode.getMenuInflater(), parser, attrs, menu);
//
//            if (!IUndoManager.getIUndoManager(this.mEditText).canPerformUndo()) {
//                menu.removeItem(R.id.menu_undo);
//            }
//            if (!IUndoManager.getIUndoManager(this.mEditText).canPerformRedo()) {
//                menu.removeItem(R.id.menu_redo);
//            }
//
//
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } finally {
//            if (parser != null) parser.close();
//        }

    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return mCallback.onPrepareActionMode(mode, menu);
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_undo:
                Log.e("IAMCallbackProxy", "performUndo");
                IUndoManager.getIUndoManager(this.mEditText).performUndo();
                return true;

            case R.id.menu_redo:
                Log.e("IAMCallbackProxy", "performRedo");
                IUndoManager.getIUndoManager(this.mEditText).performRedo();
                return true;
        }
        return mCallback.onActionItemClicked(mode, item);
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mCallback.onDestroyActionMode(mode);
    }
}
