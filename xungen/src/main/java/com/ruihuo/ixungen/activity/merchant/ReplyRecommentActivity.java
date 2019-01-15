package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import static com.ruihuo.ixungen.R.id.text_reply;

public class ReplyRecommentActivity extends BaseNoTitleActivity {
    private Context mContext;
    private ImageView mImage_titlebar_back;
    private TextView mText_reply;
    private EditText mReply;
    private String comment_rid;
    private String orderNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_recomment);
        mContext = this;
        comment_rid = getIntent().getStringExtra("comment_rid");
        orderNo = getIntent().getStringExtra("orderNo");
        initView();
        addListener();
    }

    private void initView() {
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mText_reply = (TextView) findViewById(text_reply);
        mReply = (EditText) findViewById(R.id.reply);
    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mText_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(comment_rid) || TextUtils.isEmpty(orderNo)) return;
                String content = mReply.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    Toast.makeText(mContext, "请输入回复内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                HttpInterface instance = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("orderNo", orderNo);
                params.putString("replyContent", content);
                params.putString("toRid", comment_rid);
                instance.post(Url.SHOP_REPLY, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        Intent intent=new Intent();
                        setResult(332,intent);
                        finish();
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });

            }
        });
    }
}
