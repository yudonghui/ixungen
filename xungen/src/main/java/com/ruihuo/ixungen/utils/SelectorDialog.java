package com.ruihuo.ixungen.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.view.PickerView;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/7/18
 * @describe May the Buddha bless bug-free!!!
 */
public class SelectorDialog {
    private PopupWindow popupWindow;
    private Context mContext;
    private PickerView mPickerView;
    private String selectText;
    private DialogEditInterface listener;
    private TextView mCancel;
    private TextView mConfirm;

    public SelectorDialog(Context mContext) {
        this.mContext = mContext;
        final View popupWindowView = View.inflate(mContext, R.layout.popup_bottom, null);
        mPickerView = (PickerView) popupWindowView.findViewById(R.id.pickerView);
        mCancel = (TextView) popupWindowView.findViewById(R.id.cancel);
        mConfirm = (TextView) popupWindowView.findViewById(R.id.confirm);
        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        popupWindow.setOnDismissListener(new popupDismissListener());
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                listener.callBack(selectText);
            }
        });
    }

    public void initData(List<String> data) {
        mPickerView.setData(data);
        mPickerView.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectText = text;
                Log.e("选择了", text);
            }
        });
    }

    public void setSelector(int selector) {
        mPickerView.setSelected(selector);
    }

    public void show(int resId) {
        selectText = mPickerView.getText();//初始化selecttext的值
        backgroundAlpha(0.5f);
        popupWindow.showAtLocation(View.inflate(mContext, resId, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    class popupDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }

    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    public void setListener(DialogEditInterface listener) {
        this.listener = listener;
    }
}
