package top.imlk.undo.holder;

import top.imlk.undo.R;

/**
 * Created by imlk on 2018/3/21.
 */

//修改id防止冲突

public class ITag {
    public static final int TOP_IMLK_UNDO_INJECTED_TAG = (R.id.TOP_IMLK_UNDO_INJECTED_TAG & 0x00FFFFFF) | 0x77000000;
    public static final int TOP_IMLK_UNDO_IACTIONPOPUPWINDOWPROXY_TAG = (R.id.TOP_IMLK_UNDO_IACTIONPOPUPWINDOWPROXY_TAG & 0x00FFFFFF) | 0x77000000;
    public static final int TOP_IMLK_UNDO_SHOULDDO_TAG = (R.id.TOP_IMLK_UNDO_SHOULDDO_TAG & 0x00FFFFFF) | 0x77000000;
    public static final int TOP_IMLK_UNDO_IUNDOMANAGER_TAG = (R.id.TOP_IMLK_UNDO_IUNDOMANAGER_TAG & 0x00FFFFFF) | 0x77000000;

}
