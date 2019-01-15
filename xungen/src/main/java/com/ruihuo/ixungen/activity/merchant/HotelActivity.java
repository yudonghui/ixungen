package com.ruihuo.ixungen.activity.merchant;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.calendarselector.CalendarDialog;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.view.ForumScrollView;
import com.ruihuo.ixungen.view.MyGridView;
import com.ruihuo.ixungen.view.MyListView;
import com.ruihuo.ixungen.view.TitleBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HotelActivity extends AppCompatActivity {
    private String shopId;
    private Context mContext;
    private CalendarDialog mCalendarDialog;
    private ForumScrollView mScrollView;
    private ImageView mAdd_photo;
    private TextView mPhotos;
    private TextView mName;
    private TextView mCommentNum;
    private TextView mAddress;
    private ImageView mPhone;
    private TextView mCheck_in;//入住
    private TextView mCheck_out;//离店
    private TextView mForm_text;
    private LinearLayout mLl_check_in_out;
    private TextView mDays;
    private MyListView mListView;
    private TextView mMore;
    private LinearLayout mLl_comment;
    private MyListView mListViewComment;
    private LinearLayout mLl_shops_info;
    // private com.ruihuo.ixungen.activity.merchant.ServiceView mServiceView;
    private MyGridView mGridView;
    private TextView mIntime;//酒店规定的每天入住时间
    private TextView mOuttime;//酒店规定的每天退房时间
    private LinearLayout mLl_nearby;
    private MyListView mListViewNearby;
    private ImageView mImage_titlebar_back;
    private TitleBar mTitleBar;
    private LinearLayout mRl_title;
    private LinearLayout mLl_tuangou;
    private MyListView mListViewTuangou;
    private LinearLayout mLl_hotel_yuding;
    // private LinearLayout mLl_tuijian;
    //private RecyclerView mRecyclerView;
    private LinearLayout mLl_inTime;
    private LinearLayout mLl_outTime;
    private TextView mOpeningTime;
    private TextView mService;
    private TextView mNearBy_text;

    private String inRoomTime;
    private String outRoomTime;
    private StarView mStarView;
    private HttpInterface mHttp;
    private GoodsDetailDialog mGoodsDetailDialog;
    private String logo;//店铺头像链接
    private String shop_name;//店铺名称
    private List<CommentBaseBean> commentList = new ArrayList<>();
    private List<NearbyShopBaseBean> nearbyShopList = new ArrayList<>();
    private String region;//省市县（区）
    private String address;//具体地址
    private RecommendFoodAdapter mFoodAdapter;
    private int limit = 5;
    private int page = 1;
    private int totalPage = 1;
    private int type = 2;
    private List<ShopsFormBean.DataBean> dataList = new ArrayList<>();
    private GoodsFormAdapter mHotelAdapter;
    private String mobile;
    // private HashMap<String, String> mapFactivity = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        mContext = this;
        shopId = getIntent().getStringExtra("shopId");
        type = getIntent().getIntExtra("type", 0);
        initView();
        mHotelAdapter = new GoodsFormAdapter(dataList, mContext, ReserveListener);
        mListView.setAdapter(mHotelAdapter);
        mFoodAdapter = new RecommendFoodAdapter(dataList, mContext);
        mListViewTuangou.setAdapter(mFoodAdapter);
        addListener();
        addData();
    }

    private void initView() {
        mScrollView = (ForumScrollView) findViewById(R.id.scrollView);
        mAdd_photo = (ImageView) findViewById(R.id.add_photo);
        mPhotos = (TextView) findViewById(R.id.photos);
        mStarView = (StarView) findViewById(R.id.starView);
        mName = (TextView) findViewById(R.id.name);
        mService = (TextView) findViewById(R.id.service);
        mNearBy_text = (TextView) findViewById(R.id.nearby_text);

        mLl_tuangou = (LinearLayout) findViewById(R.id.ll_tuangou);
        mListViewTuangou = (MyListView) findViewById(R.id.listViewTuangou);
        mLl_hotel_yuding = (LinearLayout) findViewById(R.id.ll_hotel_yuding);
        //   mLl_tuijian = (LinearLayout) findViewById(R.id.ll_tuijian);
        //  mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mLl_inTime = (LinearLayout) findViewById(R.id.ll_inTime);
        mLl_outTime = (LinearLayout) findViewById(R.id.ll_outTime);
        mOpeningTime = (TextView) findViewById(R.id.opening_time);
        //LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        //   linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        //  mRecyclerView.setLayoutManager(linearLayoutManager);

        mCommentNum = (TextView) findViewById(R.id.commentNum);
        mAddress = (TextView) findViewById(R.id.address);
        mPhone = (ImageView) findViewById(R.id.phone);
        mForm_text = (TextView) findViewById(R.id.form_text);
        mLl_check_in_out = (LinearLayout) findViewById(R.id.ll_check_in_out);
        mCheck_in = (TextView) findViewById(R.id.check_in);
        mDays = (TextView) findViewById(R.id.days);
        mCheck_out = (TextView) findViewById(R.id.check_out);
        mListView = (MyListView) findViewById(R.id.listView);
        mMore = (TextView) findViewById(R.id.more);
        mLl_comment = (LinearLayout) findViewById(R.id.ll_comment);
        mListViewComment = (MyListView) findViewById(R.id.listViewComment);
        mLl_shops_info = (LinearLayout) findViewById(R.id.ll_shops_info);
        //  mServiceView = (com.ruihuo.ixungen.activity.merchant.ServiceView) findViewById(R.id.serviceView);
        mGridView = (MyGridView) findViewById(R.id.gridView);
        mIntime = (TextView) findViewById(R.id.intime);
        mOuttime = (TextView) findViewById(R.id.outtime);
        mLl_nearby = (LinearLayout) findViewById(R.id.ll_nearby);
        mListViewNearby = (MyListView) findViewById(R.id.listViewNearby);
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mRl_title = (LinearLayout) findViewById(R.id.rl_title);
        mTitleBar = (TitleBar) findViewById(R.id.titlBar);
        mTitleBar.setTitle("");
        mTitleBar.mImageBack.setVisibility(View.GONE);

        mHttp = HttpUtilsManager.getInstance(mContext);
        mCalendarDialog = new CalendarDialog(mContext, CalendarSelector);
        inRoomTime = DateFormatUtils.longToDate(System.currentTimeMillis());
        outRoomTime = DateFormatUtils.longToDate(System.currentTimeMillis() + 86400000);
        mCheck_in.setText(inRoomTime);
        mCheck_out.setText(outRoomTime);
        mGoodsDetailDialog = new GoodsDetailDialog(mContext);

        int displayWidth = DisplayUtilX.getDisplayWidth();
        FrameLayout.LayoutParams photoLayout = new FrameLayout.LayoutParams(displayWidth, displayWidth / 2);
        mAdd_photo.setLayoutParams(photoLayout);

        if (type == 1) {//餐厅
            mLl_tuangou.setVisibility(View.VISIBLE);
            //  mLl_tuijian.setVisibility(View.VISIBLE);
            mLl_hotel_yuding.setVisibility(View.GONE);
            mLl_inTime.setVisibility(View.GONE);
            mLl_outTime.setVisibility(View.GONE);
            mOpeningTime.setVisibility(View.VISIBLE);
        } else if (type == 2) {//酒店
            mLl_tuangou.setVisibility(View.GONE);
            //    mLl_tuijian.setVisibility(View.GONE);
            mLl_hotel_yuding.setVisibility(View.VISIBLE);
            mLl_inTime.setVisibility(View.VISIBLE);
            mLl_outTime.setVisibility(View.VISIBLE);
            mOpeningTime.setVisibility(View.GONE);
        } else {
            mLl_tuangou.setVisibility(View.GONE);
            //    mLl_tuijian.setVisibility(View.GONE);
            mLl_hotel_yuding.setVisibility(View.VISIBLE);
            mLl_inTime.setVisibility(View.GONE);
            mLl_outTime.setVisibility(View.GONE);
            mLl_check_in_out.setVisibility(View.GONE);
            mOpeningTime.setVisibility(View.VISIBLE);
            mForm_text.setText("商品预订");
            mNearBy_text.setText("附近推荐");
            mService.setText("景点信息");
        }
    }

    private void addListener() {
        mRl_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mScrollView.setOnScrollistener(ScrollListener);//滑动监听
        mLl_check_in_out.setOnClickListener(CheckInOutListener);
        mPhone.setOnClickListener(TelListener);//联系商户
        mLl_comment.setOnClickListener(CommentListener);//用户评价
        mLl_shops_info.setOnClickListener(ShopsInfoListener);//酒店信息
        mMore.setOnClickListener(MoreListener);//查看更多房间信息
 /*       mServiceView.setNoListener();
        mServiceView.setType(type);*/
        mLl_nearby.setOnClickListener(NearByListener);
        mListViewNearby.setOnItemClickListener(NearByItemListener);
        //mLl_tuijian.setOnClickListener(TuijianListener);
        mAdd_photo.setOnClickListener(LookPhotoListener);//点击商品图片
        mListViewTuangou.setOnItemClickListener(FoodItemListener);
        //  mListViewComment.setOnItemClickListener(CommentItemListener);
    }

    private void addData() {
        getData();//获取酒店商品列表信息
        Bundle params = new Bundle();
        params.putString("shopId", shopId);
        mHttp.get(Url.SHOPS_HOME, params, new JsonInterface() {

            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                ShopsInfoBean shopsInfoBean = gson.fromJson(result, ShopsInfoBean.class);
                ShopsInfoBean.DataBean data = shopsInfoBean.getData();
                ShopsInfoBean.DataBean.ShopInfoBean shop_info = data.getShop_info();

                commentList.clear();
                commentList.addAll(data.getComment());
                CommentAdapter commentAdapter = new CommentAdapter(commentList, mContext, 1);
                mListViewComment.setAdapter(commentAdapter);

                nearbyShopList.clear();
                nearbyShopList.addAll(data.getNearby_shop());
                NearbyShopAdatper nearbyShopAdatper = new NearbyShopAdatper(nearbyShopList, mContext, 1);
                mListViewNearby.setAdapter(nearbyShopAdatper);

                String shop_card_a = shop_info.getShop_card_a();
                shop_name = shop_info.getShop_name();//店名
                region = shop_info.getRegion();//地址
                String shop_time = shop_info.getShop_time();//店铺营业时间
                address = shop_info.getAddress();//具体地址
                String consumption_per_person = shop_info.getConsumption_per_person();//人均消费
                mobile = shop_info.getMobile();//商家电话
                String level = shop_info.getLevel();//星级
                String check_in_time = shop_info.getCheck_in_time();
                String check_out_time = shop_info.getCheck_out_time();
                String service = shop_info.getService();
                String total_comment = shop_info.getTotal_comment();
                String total_img = shop_info.getTotal_img();
                logo = shop_info.getLogo();
                mPhotos.setText(TextUtils.isEmpty(total_img) ? "0" : total_img);
                mCommentNum.setText(TextUtils.isEmpty(total_comment) ? "0" : total_comment + "人");
                mName.setText(TextUtils.isEmpty(shop_name) ? "--" : shop_name);
                if (!TextUtils.isEmpty(region) && !TextUtils.isEmpty(address))
                    mAddress.setText(region + "  " + address);
                mOpeningTime.setText("店铺营业时间: " + (TextUtils.isEmpty(shop_time) ? "--" : shop_time));
                mTitleBar.setTitle(TextUtils.isEmpty(shop_name) ? "--" : shop_name);

                mStarView.setLevel(level);
                mIntime.setText(TextUtils.isEmpty(check_in_time) ? "--" : check_in_time);
                mOuttime.setText(TextUtils.isEmpty(check_out_time) ? "--" : check_out_time);
                //mServiceView.setService(service);
                if (!TextUtils.isEmpty(service)) {
                    String[] split = service.split("\\,");
                    ServiceAdapter serviceAdapter = new ServiceAdapter(split);
                    mGridView.setAdapter(serviceAdapter);
                }
                if (!TextUtils.isEmpty(shop_card_a)) {
                    Picasso.with(mContext)
                            .load(shop_card_a)
                            .config(Bitmap.Config.RGB_565)
                            .placeholder(R.mipmap.default_long)
                            .error(R.mipmap.default_long)
                            .into(mAdd_photo);
                }

            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void getData() {//获取商品列表信息
        final Bundle params = new Bundle();
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        params.putString("shopId", shopId);
        params.putString("type", type + "");
        mHttp.get(Url.SHOPS_FORM, params, new JsonInterface() {

            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                ShopsFormBean shopsFormBean = gson.fromJson(result, ShopsFormBean.class);
                totalPage = shopsFormBean.getTotalPage();
                if (page < totalPage) mMore.setVisibility(View.VISIBLE);
                else mMore.setVisibility(View.GONE);
                dataList.addAll(shopsFormBean.getData());
                if (type == 1) {
                    mFoodAdapter.notifyDataSetChanged();
                } else {
                    mHotelAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onError(String message) {
            }
        });
    }

    DialogEditInterface CalendarSelector = new DialogEditInterface() {
        @Override
        public void callBack(String message) {
            String[] split = message.split("\\*");
            inRoomTime = split[0];
            outRoomTime = split[1];
            mCheck_in.setText(inRoomTime);
            mCheck_out.setText(outRoomTime);
            mDays.setText(split[2] + "晚");
        }
    };
    AdapterView.OnItemClickListener FoodItemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(mContext, GoodsDetailActivity.class);
            ShopsFormBean.DataBean dataBean = dataList.get(position);
            Bundle bundle = new Bundle();
            bundle.putSerializable("dataBean", dataBean);
            bundle.putString("address", address);
            bundle.putString("mobile", mobile);
            bundle.putString("shopName", shop_name);
            bundle.putString("region", region);
            bundle.putString("shopId", shopId);
            bundle.putString("logo", logo);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };
    View.OnClickListener LookPhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ShopsPhotoActivity.class);
            intent.putExtra("shopId", shopId);
            intent.putExtra("type", type);//1-餐饮；2-酒店
            intent.putExtra("mode", false);
            startActivity(intent);
        }
    };
    View.OnClickListener NearByListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(region)) return;
            Intent intent = new Intent(mContext, NearByShopActivity.class);
            intent.putExtra("region", region);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    };
    AdapterView.OnItemClickListener NearByItemListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            NearbyShopBaseBean nearbyShopBaseBean = nearbyShopList.get(position);
            int type = Integer.parseInt(nearbyShopBaseBean.getType());
            String shopId = nearbyShopBaseBean.getId();
            Intent intent = new Intent(mContext, HotelActivity.class);
            intent.putExtra("type", type);
            intent.putExtra("shopId", shopId);
            startActivity(intent);
        }
    };
    View.OnClickListener MoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (page < totalPage) {
                page++;
                getData();
            } else {
                Toast.makeText(mContext, "抱歉，没有更多信息了！", Toast.LENGTH_SHORT).show();
            }
        }
    };
    //点击预定按钮
    CallBackPositionInterface ReserveListener = new CallBackPositionInterface() {
        @Override
        public void callBack(int position) {
            ShopsFormBean.DataBean dataBean = dataList.get(position);
            String amount = dataBean.getAmount();
            if (TextUtils.isEmpty(amount) || Float.parseFloat(amount) < 1) {
                Toast.makeText(mContext, "已售完", Toast.LENGTH_SHORT).show();
                return;
            }
            if (type==2){//酒店
                long in = DateFormatUtils.StringToLong(inRoomTime);
                long out = DateFormatUtils.StringToLong(outRoomTime);
                if (out <= in) {
                    Toast.makeText(mContext, "请选择正确的入住和离店时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                int days = (int) ((out - in) / 86400000);

                //这里还差cost总金额。amount总数量
                OrderData orderData = new OrderData();
                orderData.setType(type);
                orderData.setDays(days);
                orderData.setGoodsId(dataBean.getId());
                orderData.setShopId(shopId);
                orderData.setCover(dataBean.getCover());
                orderData.setIsCancel(dataBean.getIs_cancel());
                orderData.setLogo(logo);
                orderData.setShopName(shop_name);
                orderData.setConsumeStartTime(inRoomTime);
                orderData.setConsumeEndTime(outRoomTime);
                orderData.setAllAmount(amount);//房间额总数量，不是预订的数量
                orderData.setName(dataBean.getName());
                orderData.setPrice(dataBean.getPrice());
                orderData.setInfo(dataBean.getInfo());
                orderData.setClassify(dataBean.getClassify());
                orderData.setService(dataBean.getService());
                mGoodsDetailDialog.showDialog(dataBean, orderData, 1);
            }else {//旅游
                Intent intent = new Intent(mContext, GoodsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("dataBean", dataBean);
                bundle.putString("address", address);
                bundle.putString("mobile", mobile);
                bundle.putString("shopName", shop_name);
                bundle.putString("region", region);
                bundle.putString("shopId", shopId);
                bundle.putString("logo", logo);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }
    };
    View.OnClickListener ShopsInfoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    View.OnClickListener CommentListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ShopsCommentActivity.class);
            intent.putExtra("shopId", shopId);
            startActivity(intent);
        }
    };
    View.OnClickListener TelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (TextUtils.isEmpty(mobile)) return;
            HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
            hintDialogUtils.setMessage("您将拨打" + mobile);
            hintDialogUtils.setConfirm("确定", new DialogHintInterface() {
                @Override
                public void callBack(View view) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }
            });
        }
    };
    ForumScrollView.OnScrollistener ScrollListener = new ForumScrollView.OnScrollistener() {
        @Override
        public void onScroll(int startY, int endY) {
            //获取到状态栏的高度
            int statusBarHeight = DisplayUtilX.getStatusBarHeight();
            int titleBarHeight = mTitleBar.getLayoutParams().height;
            //获取背景高度
            int backHeight = mAdd_photo.getLayoutParams().height;
            int totalHeight = backHeight;
            if (endY >= totalHeight) {
                mTitleBar.setAlpha(1);
                mRl_title.setVisibility(View.GONE);
            } else {
                float alpha = (float) endY / totalHeight;
                mTitleBar.setAlpha(alpha);

                if (endY < totalHeight / 2) {
                    mRl_title.setVisibility(View.VISIBLE);
                    mTitleBar.mImageBack.setVisibility(View.GONE);
                } else {
                    mRl_title.setVisibility(View.GONE);
                    mTitleBar.mImageBack.setVisibility(View.VISIBLE);

                }
            }
        }
    };
    View.OnClickListener CheckInOutListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCalendarDialog.showDialog();
        }
    };
}
