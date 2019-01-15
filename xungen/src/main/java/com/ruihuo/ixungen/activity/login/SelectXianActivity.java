package com.ruihuo.ixungen.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.adapter.CityAdapter;
import com.ruihuo.ixungen.entity.CityModel;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;

import java.util.ArrayList;
import java.util.List;

public class SelectXianActivity extends AppCompatActivity {
    TitleBar mTitleBar;
    ListView mListView;
    private HttpInterface mHttp;
    List<CityModel.DataBean> data = new ArrayList<>();
    private String cityId = "1";
    private String cityName;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        Intent intent = getIntent();
        cityId = intent.getStringExtra("cityId");
        address = intent.getStringExtra("address");
        cityName = intent.getStringExtra("cityName");
        initView();
        addData();
        addListener();
    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_city);
        mTitleBar = (TitleBar) findViewById(R.id.titlebar_city);
        mTitleBar.setTitle(TextUtils.isEmpty(cityName) ? "选择城市" : cityName);
        mHttp = HttpUtilsManager.getInstance(this);
    }

    private void addData() {
        Bundle params = new Bundle();
        params.putString("act", "getlist");
        params.putString("id", cityId);
        mHttp.get(Url.CITY_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                CityModel cityModel = gson.fromJson(result, CityModel.class);
                if (cityModel.getCode() == 200) {
                    data = cityModel.getData();
                    CityAdapter mCityAdapter = new CityAdapter(data);
                    mListView.setAdapter(mCityAdapter);
                } else {
                    HintDialogUtils hintDialogUtils = new HintDialogUtils(SelectXianActivity.this);
                    hintDialogUtils.setMessage(cityModel.getMsg());
                }
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                // intent.putExtra("cityId", data.get(position).getId() + "");
                intent.putExtra("address", address + "," + data.get(position).getId());
                intent.putExtra("cityName", cityName + " " + data.get(position).getName());
                setResult(321, intent);
                finish();
                // startActivity(intent);
            }
        });
    }
}