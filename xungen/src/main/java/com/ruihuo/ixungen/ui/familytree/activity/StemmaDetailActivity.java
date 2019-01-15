package com.ruihuo.ixungen.ui.familytree.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.common.IntentSkip;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.common.PrivateUtils;
import com.ruihuo.ixungen.entity.PayResult;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.OrderSuccessInterface;
import com.ruihuo.ixungen.ui.familytree.BuyStemmaDialog;
import com.ruihuo.ixungen.ui.familytree.bean.PriceBean;
import com.ruihuo.ixungen.ui.familytree.bean.StemmaDetailBean;
import com.ruihuo.ixungen.ui.familytree.contract.StemmaDetailContract;
import com.ruihuo.ixungen.ui.familytree.presenter.StemmaDetailPresenter;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.PaymentUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StemmaDetailActivity extends AppCompatActivity implements StemmaDetailContract.View {
    StemmaDetailPresenter mPresenter = new StemmaDetailPresenter(this);
    private Context mContext;
    private ImageView mTitleBack;
    private TextView mText_title;
    private ImageView mImage_edit;
    private TextView mDelete;
    private LinearLayout mLl_notification;
    private TextView mNotification;
    private ImageView mBook;
    private TextView mName;
    private TextView mType;
    private TextView mPermission;
    private TextView mAdminor;
    private TextView mPhone;
    private TextView mCreatDate;
    private TextView mRegionName;
    private TextView mGenerations;
    private LinearLayout mLl_level;
    private TextView mLevels;
    private TextView mStemmaIntroduce;
    private TextView mTryRead;
    private TextView mBuy;
    private TextView mLookStemma;
    private String id;
    private BuyStemmaDialog mBuyStemmaDialog;
    List<PriceBean.DataBean> priceList = new ArrayList<>();
    private PaymentUtils paymentUtils;
    private int lookPrivate = 1;//0-没有购买；1-有权限；2-权限已经过期
    private String orderNo;//订单号
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case PaymentUtils.SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        isSuccess();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    private String name;
    private boolean flag;//false 家谱，true族谱
    private String stemmaId;
    private String familyId;
    private String createRid;//创建家谱的人的根号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stemma_detail);
        mContext = this;
        id = getIntent().getStringExtra("id");
        flag = getIntent().getBooleanExtra("flag", false);
        stemmaId = getIntent().getStringExtra("stemmaId");
        familyId = getIntent().getStringExtra("familyId");
        if (TextUtils.isEmpty(stemmaId)) flag = false;
        else flag = true;
        initView();
        addData();
        addListener();
        //注册一个动态广播
        registerBoradcastReceiver();
    }

    private void addListener() {
        mTitleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mBuy.setOnClickListener(BuyListener);
        mTryRead.setOnClickListener(TryReadListener);
        mLookStemma.setOnClickListener(LookStemmaListener);
        mLl_level.setOnClickListener(LlLevelListener);
        mImage_edit.setOnClickListener(EditeListener);
        mDelete.setOnClickListener(DeleteListener);
    }

    private void initView() {
        mTitleBack = (ImageView) findViewById(R.id.image_titlebar_back);
        mText_title = (TextView) findViewById(R.id.text_title);
        mImage_edit = (ImageView) findViewById(R.id.image_edit);
        mDelete = (TextView) findViewById(R.id.delete);
        mLl_notification = (LinearLayout) findViewById(R.id.ll_notification);
        mNotification = (TextView) findViewById(R.id.notification);
        mBook = (ImageView) findViewById(R.id.book);
        mName = (TextView) findViewById(R.id.name);
        mType = (TextView) findViewById(R.id.type);
        mPermission = (TextView) findViewById(R.id.permission);
        mAdminor = (TextView) findViewById(R.id.adminor);
        mPhone = (TextView) findViewById(R.id.phone);
        mCreatDate = (TextView) findViewById(R.id.creatDate);
        mRegionName = (TextView) findViewById(R.id.regionName);
        mGenerations = (TextView) findViewById(R.id.generations);
        mLl_level = (LinearLayout) findViewById(R.id.ll_level);
        mLevels = (TextView) findViewById(R.id.levels);
        mStemmaIntroduce = (TextView) findViewById(R.id.stemmaIntroduce);
        mTryRead = (TextView) findViewById(R.id.tryRead);
        mBuy = (TextView) findViewById(R.id.buy);
        mLookStemma = (TextView) findViewById(R.id.lookStemma);

        mBuyStemmaDialog = new BuyStemmaDialog(mContext, GetOrderSuccess);
        paymentUtils = new PaymentUtils(mContext, mHandler);
    }

    View.OnClickListener TryReadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IntentSkip intentSkip = new IntentSkip();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putInt("lookPrivate", lookPrivate);
            if (flag) {
                bundle.putString("stemmaId", stemmaId);
            } else {
                bundle.putString("familyId", familyId);
                bundle.putString("createRid", createRid);
            }
            intentSkip.skipTreeActivity(mContext, bundle);
        }
    };
    View.OnClickListener LookStemmaListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IntentSkip intentSkip = new IntentSkip();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putInt("lookPrivate", lookPrivate);
            if (flag) {
                bundle.putString("stemmaId", stemmaId);
            } else {
                bundle.putString("familyId", familyId);
                bundle.putString("createRid", createRid);
            }
            intentSkip.skipTreeActivity(mContext, bundle);
        }
    };
    View.OnClickListener LlLevelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            IntentSkip intentSkip = new IntentSkip();
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            bundle.putBoolean("treeOrbook", true);
            bundle.putInt("lookPrivate", lookPrivate);
            if (flag) {
                bundle.putString("stemmaId", stemmaId);
            } else {
                bundle.putString("familyId", familyId);
                bundle.putString("createRid", createRid);
            }
            intentSkip.skipTreeActivity(mContext, bundle);
        }
    };
    View.OnClickListener BuyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (priceList.size() > 0) {
                if (flag)
                    mBuyStemmaDialog.showDialog(stemmaId, name, priceList);
                else mBuyStemmaDialog.showDialog(familyId, name, priceList);
            }
        }
    };
    OrderSuccessInterface GetOrderSuccess = new OrderSuccessInterface() {
        @Override
        public void callBack(int payMethod, String money, String orderN) {
            orderNo = orderN;
            if (payMethod == 1) {
                paymentUtils.wxpay(money, orderNo);
            } else {
                paymentUtils.alipay(money, orderNo, name);
            }
        }
    };
    View.OnClickListener EditeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (dataBean == null) return;
            Intent intent = new Intent(mContext, EditeStemmaInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("dataBean", dataBean);
            intent.putExtras(bundle);
            startActivityForResult(intent, 6666);
        }
    };
    View.OnClickListener DeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
            hintDialogUtils.setMessage("您确定要删除该家谱？");
            hintDialogUtils.setTitle("温馨提示");
            hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
                @Override
                public void callBack(View view) {
                    Bundle parameters = new Bundle();
                    parameters.putString("token", XunGenApp.token);
                    parameters.putString("id", familyId);
                    mPresenter.deleteStemma(parameters, mContext);
                }
            });
        }
    };

    private void setView(StemmaDetailBean.DataBean dataBean) {
        if (dataBean != null) {
            createRid = dataBean.getRid();
            if (flag || !XunGenApp.rid.equals(createRid)) {
                mImage_edit.setVisibility(View.GONE);
                mDelete.setVisibility(View.GONE);
            } else {
                mImage_edit.setVisibility(View.VISIBLE);
                mDelete.setVisibility(View.VISIBLE);
            }
            name = dataBean.getName();
            String create_time = dataBean.getCreate_time();
            String generation = dataBean.getGeneration();
            String region = dataBean.getRegion();
            String summary = dataBean.getSummary();
            String phone = dataBean.getPhone();
            String truename = dataBean.getTruename();
            String nikename = dataBean.getNikename();
            String type;
            if (flag) {
                type = dataBean.getType();
            } else {
                //家谱详情显示
                mLookStemma.setVisibility(View.VISIBLE);
                type = "家谱";
                String privateX = dataBean.getPrivateX();
                PrivateUtils privateUtils = new PrivateUtils();
                mPermission.setText("权        限: " + privateUtils.getPrivateStr(privateX));
            }
            mName.setText(TextUtils.isEmpty(name) ? "--" : name);
            mType.setText("类        型: " + (TextUtils.isEmpty(type) ? "--" : type));
            if (TextUtils.isEmpty(truename)) {
                mAdminor.setText("管  理  人: " + (TextUtils.isEmpty(nikename) ? "--" : nikename));
            } else {
                mAdminor.setText("管  理  人: " + truename);
            }
            mPhone.setText("手  机  号: " + (TextUtils.isEmpty(phone) ? "--" : phone));
            mCreatDate.setText("创建日期: " + DateFormatUtils.longToDateM(create_time));
            mGenerations.setText(TextUtils.isEmpty(generation) ? "--" : generation);
            mStemmaIntroduce.setText(TextUtils.isEmpty(summary) ? "--" : summary);
            if (flag)//族谱存的是地区的数字。家谱直接上传的是汉字
                new NetWorkData().getAddress(mContext, region, new NetWorkData.AddressInterface() {
                    @Override
                    public void callBack(String result) {
                        mRegionName.setText("起  源  地: " + (TextUtils.isEmpty(result) ? "--" : result));
                    }
                });
            else mRegionName.setText("起  源  地: " + (TextUtils.isEmpty(region) ? "--" : region));
        }
    }

    private void addData() {
        if (flag) {
            Bundle parameters = new Bundle();
            parameters.putString("id", stemmaId);
            parameters.putString("url", Url.STEMMA_SYSTEM_DETAIL);
            parameters.putString("flagSelf", flag + "");
            mPresenter.getDetailData(parameters, mContext);

            Bundle parameters1 = new Bundle();
            parameters1.putString("token", XunGenApp.token);
            parameters1.putString("goodsId", stemmaId);
            mPresenter.getPrivateData(parameters1, mContext);
        } else {
            Bundle parameters = new Bundle();
            parameters.putString("id", familyId);
            parameters.putString("url", Url.STEMMA_APP_DETAIL);
            parameters.putString("flagSelf", flag + "");
            mPresenter.getDetailData(parameters, mContext);

           /* Bundle parameters1 = new Bundle();
            parameters1.putString("token", XunGenApp.token);
            parameters1.putString("goodsId", stemmaId);
            mPresenter.getPrivateData(parameters1, mContext);*/
        }
        mPresenter.getPriceData(new Bundle(), mContext);
    }

    private void isSuccess() {
        Bundle parameters = new Bundle();
        parameters.putString("orderNo", orderNo);
        parameters.putString("token", XunGenApp.token);
        mPresenter.getBuyData(parameters, mContext);
    }

    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantNum.PAYMENT_SUCEESS);
        registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ConstantNum.PAYMENT_SUCEESS)) {
                //微信支付成功后的回调
                int isSuccess = intent.getIntExtra("isSuccess", 0);//1成功，2没有成功
                //微信支付成功后的回调
                if (isSuccess == 1)
                    isSuccess();
                else Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private StemmaDetailBean.DataBean dataBean;

    @Override
    public void getDetailSuccess(String result) {
        Gson gson = GsonUtils.getGson();
        StemmaDetailBean stemmaDetailBean = gson.fromJson(result, StemmaDetailBean.class);
        dataBean = stemmaDetailBean.getData();
        setView(dataBean);
    }

    @Override
    public void getDetailError(String error) {

    }

    @Override
    public void deleteSuccess(String result) {
        Intent intent = new Intent();
        setResult(6626, intent);
        finish();
    }

    @Override
    public void deleteError(String error) {

    }

    @Override
    public void getBuySuccess(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            int data = jsonObject.getInt("data");
            HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
            hintDialogUtils.setTitle("提示");
            if (data == 1) {
                hintDialogUtils.setMessage("恭喜您！您已支付成功！");
                hintDialogUtils.setConfirm("查看订单", new DialogHintInterface() {
                    @Override
                    public void callBack(View view) {
                        Intent intent = new Intent(mContext, MyTreeActivity.class);
                        intent.putExtra("type", 2);
                        startActivity(intent);
                        finish();
                    }
                });
                hintDialogUtils.setCancel("继续预定", new DialogHintInterface() {
                    @Override
                    public void callBack(View view) {
                        finish();
                    }
                });
            } else {
                hintDialogUtils.setMessage("您是否已经支付?");
                hintDialogUtils.setCancel("未支付", new DialogHintInterface() {
                    @Override
                    public void callBack(View view) {

                    }
                });
                hintDialogUtils.setConfirm("已支付", new DialogHintInterface() {
                    @Override
                    public void callBack(View view) {
                        Intent intent = new Intent(mContext, MyTreeActivity.class);
                        intent.putExtra("type", 2);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getBuyError(String error) {

    }

    @Override
    public void getPrivateSuccess(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject data = jsonObject.getJSONObject("data");
            lookPrivate = data.getInt("flag");//0-没有购买；1-有权限；2-权限已经过期
            if (lookPrivate == 0 || lookPrivate == 2) {
                mPermission.setText("权        限: 无");
                mBuy.setVisibility(View.VISIBLE);
                mTryRead.setVisibility(View.VISIBLE);
                mLookStemma.setVisibility(View.GONE);
            } else {
                String consume_end_time = data.getString("consume_end_time");
                mPermission.setText("权        限: " + consume_end_time + "截止");
                mBuy.setVisibility(View.GONE);
                mTryRead.setVisibility(View.GONE);
                mLookStemma.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getPrivateError(String error) {

    }

    @Override
    public void getPriceSuccess(String result) {
        Gson gson = GsonUtils.getGson();
        PriceBean priceBean = gson.fromJson(result, PriceBean.class);
        priceList = priceBean.getData();
    }

    @Override
    public void getPriceError(String error) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 6666 && resultCode == 6667) {
            addData();
        } else if (requestCode == 221 && resultCode == 222) {
            Intent intent = new Intent();
            setResult(6626, intent);
            finish();
        }
    }
}
