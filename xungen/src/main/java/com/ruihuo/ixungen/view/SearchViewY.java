package com.ruihuo.ixungen.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;

/**
 * @author yudonghui
 * @date 2017/4/14
 * @describe May the Buddha bless bug-free!!!
 */
public class SearchViewY extends LinearLayout implements View.OnClickListener {
    private LinearLayout mSearchContainer;
    //输入框
    private EditText etInput;
    //删除键
    private ImageView ivDelete;
    //取消按钮
    private TextView btnBack;

    private Context mContext;
    private SearchViewListener mListener;

    public void setSearchViewListener(SearchViewListener listener) {
        mListener = listener;
    }

    public SearchViewY(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.search_layout, this);
        initViews();
    }

    private void initViews() {
        mSearchContainer = findViewById(R.id.search_container);
        etInput = (EditText) findViewById(R.id.search_et_input);
        ivDelete = (ImageView) findViewById(R.id.search_iv_delete);
        btnBack = (TextView) findViewById(R.id.search_btn_back);
        ivDelete.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        etInput.addTextChangedListener(new EditChangedListener());
        etInput.setOnClickListener(this);
        etInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    notifyStartSearching(etInput.getText().toString());
                }
                return true;
            }
        });
    }

    /**
     * 通知监听者 进行搜索操作
     * 同时隐藏软键盘
     */
    private void notifyStartSearching(String text) {
        if (mListener != null) {
            mListener.onSearch(text);
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setHint(String hint) {
        etInput.setHint(hint);
    }

    public void setText(String text) {
        etInput.setText(TextUtils.isEmpty(text) ? "" : text);
    }

    public void setBackGround(int resId) {
        mSearchContainer.setBackgroundColor(ContextCompat.getColor(mContext, resId));
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (!"".equals(charSequence.toString())) {
                ivDelete.setVisibility(VISIBLE);
            } else {
                ivDelete.setVisibility(GONE);
            }


        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mListener != null) {
                mListener.onChange(editable.toString());
            }

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.search_iv_delete:
                etInput.setText("");
                ivDelete.setVisibility(GONE);
                mListener.onDelete();
                break;
            case R.id.search_btn_back:
                //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                //如果输入法在窗口上已经显示，则隐藏，反之则显示
                //imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                //imm.showSoftInput(view,InputMethodManager.SHOW_FORCED);//强制显示软键盘
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
                etInput.setText("");
                ivDelete.setVisibility(GONE);
                mListener.onCancel();
                break;
        }
    }

    /**
     * search view回调方法
     */
    public interface SearchViewListener {
        //x标记的按钮，清除文字，同时恢复原来数据
        void onDelete();

        //开始搜索
        void onSearch(String text);

        //取消
        void onCancel();

        void onChange(String text);
    }
}
