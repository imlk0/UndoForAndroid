package top.imlk.undo.holder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by imlk on 2018/3/19.
 */

public class IMember {
    public static class CLASS {

        public static Class Editor_Class;
        public static Class ActionPopupWindow_Class;
        public static Class PinnedPopupWindow_Class;
        public static Class PositionListener_Class;
        public static Class TextViewPositionListener_Class;

    }

    public static class FIELD {

        public static Field POPUP_TEXT_LAYOUT_Field;
        public static Field ActionPopupWindow_this$0_Field;
        public static Field ActionPopupWindow_mPasteTextView_Field;
        public static Field ActionPopupWindow_mReplaceTextView_Field;
        public static Field ActionPopupWindow_mContentView_Field;
        public static Field Editor_mTextView_Field;
    }

    public static class METHOD {

        public static Method TextView_canPaste_Method;
        public static Method Editor_isCursorInsideSuggestionSpan_Method;
        public static Method Editor_getPositionListener_Method;
        public static Method PositionListener_addSubscriber_Method;
        public static Method PositionListener_getPositionX_Method;
        public static Method PositionListener_getPositionY_Method;
        public static Method PinnedPopupWindow_show_Method;
        public static Method PinnedPopupWindow_computeLocalPosition_Method;
        public static Method PinnedPopupWindow_updatePosition_Method;

    }
}
