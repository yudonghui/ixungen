package com.ruihuo.ixungen.activity.merchant;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.BaseNoTitleActivity;
import com.ruihuo.ixungen.activity.ShopsRequestActivity;
import com.ruihuo.ixungen.entity.ShopsDetailBean;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;

import org.json.JSONException;
import org.json.JSONObject;

public class ProcessActivity extends BaseNoTitleActivity {
    private TextView mNext;
    private ImageView imageView;
    private TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process);
        initView();
        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(ProcessActivity.this);
                HttpInterface mHttp = HttpUtilsManager.getInstance(ProcessActivity.this);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                mHttp.get(Url.SHOPS_DETAIL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            JSONObject data = jsonObject.getJSONObject("data");
                            //有店铺可能正在审核中
                            Gson gson = GsonUtils.getGson();
                            ShopsDetailBean shopsDetailBean = gson.fromJson(result, ShopsDetailBean.class);
                            Intent intent = new Intent(ProcessActivity.this, CheckInfoActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("shopsDetailBean", shopsDetailBean);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } catch (JSONException e) {
                            //没有店铺
                            Intent intent = new Intent(ProcessActivity.this, ShopsRequestActivity.class);
                            startActivity(intent);
                            finish();
                        }

                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            }
        });
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.titlBar);
        mTitleBar.setTitle("入驻说明");
        mNext = (TextView) findViewById(R.id.next);
        imageView = (ImageView) findViewById(R.id.imageview);
        int displayWidth = DisplayUtilX.getDisplayWidth();
        LinearLayout.LayoutParams layoutParams =
                new LinearLayout.LayoutParams(displayWidth, (int) (displayWidth * 3));
        imageView.setLayoutParams(layoutParams);
    }
}
