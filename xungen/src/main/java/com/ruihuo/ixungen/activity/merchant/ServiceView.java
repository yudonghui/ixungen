package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;

import java.util.HashMap;

/**
 * @author yudonghui
 * @date 2017/8/8
 * @describe May the Buddha bless bug-free!!!
 */
public class ServiceView extends LinearLayout {
    private Context mContext;
    private View inflate;
    private ImageView mWIFI;
    private ImageView mStopping_place;
    private ImageView mSofa;
    private ImageView mNon_smoking;
    private ImageView mRoom;
    private ImageView mChildren;
    private ImageView mActor;
    private ImageView mCard;
    private ImageView mBabyq;
    private TextView mThree;
    private TextView mFour;
    private TextView mFive;
    private TextView mSix;
    private TextView mSeven;
    private TextView mEight;
    private TextView mNine;
    private LinearLayout mLl_visiable;
    private View mView1;
    private View mView2;
    private View mView3;
    private View mView4;
    private View mView5;
    private View mView6;
    private View mView7;
    private View mView8;

    public ServiceView(Context context) {
        super(context);
        mContext = context;
        inflate = View.inflate(context, R.layout.service, this);
    }

    public ServiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflate = View.inflate(context, R.layout.service, this);
    }

    public ServiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflate = View.inflate(context, R.layout.service, this);
    }

    private TextView mOne;
    private TextView mTwo;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mWIFI = (ImageView) inflate.findViewById(R.id.WIFI);
        mStopping_place = (ImageView) inflate.findViewById(R.id.stopping_place);
        mSofa = (ImageView) inflate.findViewById(R.id.sofa);
        mNon_smoking = (ImageView) inflate.findViewById(R.id.non_smoking);
        mRoom = (ImageView) inflate.findViewById(R.id.room);
        mChildren = (ImageView) inflate.findViewById(R.id.children);
        mActor = (ImageView) inflate.findViewById(R.id.actor);
        mCard = (ImageView) inflate.findViewById(R.id.card);
        mBabyq = (ImageView) inflate.findViewById(R.id.babyq);
        mOne = (TextView) inflate.findViewById(R.id.one);
        mTwo = (TextView) inflate.findViewById(R.id.two);
        mThree = (TextView) inflate.findViewById(R.id.three);
        mFour = (TextView) inflate.findViewById(R.id.four);
        mFive = (TextView) inflate.findViewById(R.id.five);
        mSix = (TextView) inflate.findViewById(R.id.six);
        mSeven = (TextView) inflate.findViewById(R.id.seven);
        mEight = (TextView) inflate.findViewById(R.id.eight);
        mNine = (TextView) inflate.findViewById(R.id.nine);
        mLl_visiable = (LinearLayout) inflate.findViewById(R.id.ll_visiable);
        mView1 = inflate.findViewById(R.id.view1);
        mView2 = inflate.findViewById(R.id.view2);
        mView3 = inflate.findViewById(R.id.view3);
        mView4 = inflate.findViewById(R.id.view4);
        mView5 = inflate.findViewById(R.id.view5);
        mView6 = inflate.findViewById(R.id.view6);
        mView7 = inflate.findViewById(R.id.view7);
        mView8 = inflate.findViewById(R.id.view8);


        addListener();
    }

    private void addListener() {
        mWIFI.setOnClickListener(FacilityListener);
        mStopping_place.setOnClickListener(FacilityListener);
        mSofa.setOnClickListener(FacilityListener);
        mNon_smoking.setOnClickListener(FacilityListener);
        mRoom.setOnClickListener(FacilityListener);
        mChildren.setOnClickListener(FacilityListener);
        mActor.setOnClickListener(FacilityListener);
        mCard.setOnClickListener(FacilityListener);
        mBabyq.setOnClickListener(FacilityListener);
    }

    private boolean flag;

    public void setNoListener() {
        flag = true;
        mWIFI.setClickable(false);
        mStopping_place.setClickable(false);
        mSofa.setClickable(false);
        mNon_smoking.setClickable(false);
        mRoom.setClickable(false);
        mChildren.setClickable(false);
        mActor.setClickable(false);
        mCard.setClickable(false);
        mBabyq.setClickable(false);
        mView1.setVisibility(GONE);
        mView2.setVisibility(GONE);
        mView3.setVisibility(GONE);
        mView4.setVisibility(GONE);
        mView5.setVisibility(GONE);
        mView6.setVisibility(GONE);
        mView7.setVisibility(GONE);
        mView8.setVisibility(GONE);
        mWIFI.setImageResource(R.mipmap.wifi);
        mStopping_place.setImageResource(R.mipmap.tingchechang);
        if (type == 2) {
            mOne.setText(R.string.wifi);
            mTwo.setText(R.string.stopping_place);
            mThree.setText(R.string.internet);
            mFour.setText(R.string.dining_hall);
            mFive.setText(R.string.xingli);
            mSix.setText(R.string.zhuchefuwu);
            mSeven.setText(R.string.jiaoxingfuwu);
            mEight.setText(R.string.songcanfuwu);
            mNine.setText(R.string.xiyifuwu);
            mSofa.setImageResource(R.mipmap.internet);
            mNon_smoking.setImageResource(R.mipmap.canting);
            mRoom.setImageResource(R.mipmap.xingli);
            mChildren.setImageResource(R.mipmap.zuche);
            mActor.setImageResource(R.mipmap.jiaoxing);
            mCard.setImageResource(R.mipmap.songcan);
            mBabyq.setImageResource(R.mipmap.xiyi);
        } else {
            mOne.setText(R.string.wifi);
            mTwo.setText(R.string.stopping_place);
            mThree.setText(R.string.shafawei);
            mFour.setText(R.string.wuyanqu);
            mFive.setText(R.string.baojian);
            mSix.setText(R.string.ertongqu);
            mSeven.setText(R.string.biaoyan);
            mEight.setText(R.string.shuaka);
            mNine.setText(R.string.baobaoyi);
            mSofa.setImageResource(R.mipmap.shafa);
            mNon_smoking.setImageResource(R.mipmap.wuyan);
            mRoom.setImageResource(R.mipmap.baojian);
            mChildren.setImageResource(R.mipmap.ertongqu);
            mActor.setImageResource(R.mipmap.biaoyan);
            mCard.setImageResource(R.mipmap.shuaka);
            mBabyq.setImageResource(R.mipmap.baobaoyi);
        }

    }

    private SelectorService mListener;
    private int type;

    public void setListener(SelectorService mListener) {
        this.mListener = mListener;
    }

    public void setType(int type) {
        this.type = type;
        if (type == 2) {
            mOne.setText(R.string.wifi);
            mTwo.setText(R.string.stopping_place);
            mThree.setText(R.string.internet);
            mFour.setText(R.string.dining_hall);
            mFive.setText(R.string.xingli);
            mSix.setText(R.string.zhuchefuwu);
            mSeven.setText(R.string.jiaoxingfuwu);
            mEight.setText(R.string.songcanfuwu);
            mNine.setText(R.string.xiyifuwu);
        } else if (type == 1) {
            mOne.setText(R.string.wifi);
            mTwo.setText(R.string.stopping_place);
            mThree.setText(R.string.shafawei);
            mFour.setText(R.string.wuyanqu);
            mFive.setText(R.string.baojian);
            mSix.setText(R.string.ertongqu);
            mSeven.setText(R.string.biaoyan);
            mEight.setText(R.string.shuaka);
            mNine.setText(R.string.baobaoyi);
        } else {//旅游
            mLl_visiable.setVisibility(GONE);
            mOne.setText(R.string.dining_hall);
            mTwo.setText(R.string.stopping_place);
            mThree.setText(R.string.biaoyan);
        }
    }

    private HashMap<String, String> mapFactivity = new HashMap<>();
    View.OnClickListener FacilityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.WIFI:
                    if (type == 1 || type == 2) {
                        if (mapFactivity.containsKey("WIFI")) {
                            mapFactivity.remove("WIFI");
                            mWIFI.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("WIFI", "WIFI");
                            mWIFI.setImageResource(R.mipmap.green);
                        }
                    } else {
                        if (mapFactivity.containsKey("餐厅")) {
                            mapFactivity.remove("餐厅");
                            mWIFI.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("餐厅", "餐厅");
                            mWIFI.setImageResource(R.mipmap.green);
                        }
                    }

                    break;
                case R.id.stopping_place:
                    if (mapFactivity.containsKey("停车场")) {
                        mapFactivity.remove("停车场");
                        mStopping_place.setImageResource(R.mipmap.gray);
                    } else {
                        mapFactivity.put("停车场", "停车场");
                        mStopping_place.setImageResource(R.mipmap.green);
                    }
                    break;
                case R.id.sofa:
                    if (type == 1) {
                        if (mapFactivity.containsKey("沙发位")) {
                            mapFactivity.remove("沙发位");
                            mSofa.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("沙发位", "沙发位");
                            mSofa.setImageResource(R.mipmap.green);
                        }
                    } else if (type == 2) {
                        if (mapFactivity.containsKey("宽带")) {
                            mapFactivity.remove("宽带");
                            mSofa.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("宽带", "宽带");
                            mSofa.setImageResource(R.mipmap.green);
                        }
                    } else {
                        if (mapFactivity.containsKey("表演")) {
                            mapFactivity.remove("表演");
                            mSofa.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("表演", "表演");
                            mSofa.setImageResource(R.mipmap.green);
                        }
                    }

                    break;
                case R.id.non_smoking:
                    if (type == 1) {
                        if (mapFactivity.containsKey("无烟区")) {
                            mapFactivity.remove("无烟区");
                            mNon_smoking.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("无烟区", "无烟区");
                            mNon_smoking.setImageResource(R.mipmap.green);
                        }
                    } else {
                        if (mapFactivity.containsKey("餐厅")) {
                            mapFactivity.remove("餐厅");
                            mNon_smoking.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("餐厅", "餐厅");
                            mNon_smoking.setImageResource(R.mipmap.green);
                        }
                    }
                    break;
                case R.id.room:
                    if (type == 1) {
                        if (mapFactivity.containsKey("包间")) {
                            mapFactivity.remove("包间");
                            mRoom.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("包间", "包间");
                            mRoom.setImageResource(R.mipmap.green);
                        }
                    } else {
                        if (mapFactivity.containsKey("行李寄存")) {
                            mapFactivity.remove("行李寄存");
                            mRoom.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("行李寄存", "行李寄存");
                            mRoom.setImageResource(R.mipmap.green);
                        }
                    }

                    break;
                case R.id.children:
                    if (type == 1) {
                        if (mapFactivity.containsKey("儿童区")) {
                            mapFactivity.remove("儿童区");
                            mChildren.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("儿童区", "儿童区");
                            mChildren.setImageResource(R.mipmap.green);
                        }
                    } else {
                        if (mapFactivity.containsKey("租车服务")) {
                            mapFactivity.remove("租车服务");
                            mChildren.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("租车服务", "租车服务");
                            mChildren.setImageResource(R.mipmap.green);
                        }
                    }
                    break;
                case R.id.actor:
                    if (type == 1) {
                        if (mapFactivity.containsKey("表演")) {
                            mapFactivity.remove("表演");
                            mActor.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("表演", "表演");
                            mActor.setImageResource(R.mipmap.green);
                        }
                    } else {
                        if (mapFactivity.containsKey("叫醒服务")) {
                            mapFactivity.remove("叫醒服务");
                            mActor.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("叫醒服务", "叫醒服务");
                            mActor.setImageResource(R.mipmap.green);
                        }
                    }

                    break;
                case R.id.card:
                    if (type == 1) {
                        if (mapFactivity.containsKey("刷卡")) {
                            mapFactivity.remove("刷卡");
                            mCard.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("刷卡", "刷卡");
                            mCard.setImageResource(R.mipmap.green);
                        }
                    } else {
                        if (mapFactivity.containsKey("送餐服务")) {
                            mapFactivity.remove("送餐服务");
                            mCard.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("送餐服务", "送餐服务");
                            mCard.setImageResource(R.mipmap.green);
                        }
                    }

                    break;
                case R.id.babyq:
                    if (type == 1) {
                        if (mapFactivity.containsKey("宝宝骑")) {
                            mapFactivity.remove("宝宝骑");
                            mBabyq.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("宝宝骑", "宝宝骑");
                            mBabyq.setImageResource(R.mipmap.green);
                        }
                    } else {
                        if (mapFactivity.containsKey("洗衣服务")) {
                            mapFactivity.remove("洗衣服务");
                            mBabyq.setImageResource(R.mipmap.gray);
                        } else {
                            mapFactivity.put("洗衣服务", "洗衣服务");
                            mBabyq.setImageResource(R.mipmap.green);
                        }
                    }
                    break;
            }
            if (mListener != null)
                mListener.callBack(mapFactivity);
        }
    };

    public void setService(String service) {
        if (!TextUtils.isEmpty(service)) {
            mapFactivity.clear();
            String[] split = service.split("\\,");

            for (int i = 0; i < split.length; i++) {
                switch (split[i]) {
                    case "WIFI":
                        if (flag) mWIFI.setImageResource(R.mipmap.wifi);
                        else
                            mWIFI.setImageResource(R.mipmap.green);
                        mapFactivity.put("WIFI", "WIFI");
                        break;
                    case "停车场":
                        if (flag) mStopping_place.setImageResource(R.mipmap.tingchechang);
                        else
                            mStopping_place.setImageResource(R.mipmap.green);
                        mapFactivity.put("停车场", "停车场");
                        break;
                    case "沙发位":
                        if (flag) mSofa.setImageResource(R.mipmap.shafa);
                        else
                            mSofa.setImageResource(R.mipmap.green);
                        mapFactivity.put("沙发位", "沙发位");
                        break;
                    case "宽带":
                        if (flag) mSofa.setImageResource(R.mipmap.internet);
                        else
                            mSofa.setImageResource(R.mipmap.green);
                        mapFactivity.put("宽带", "宽带");
                        break;

                    case "无烟区":
                        if (flag) mNon_smoking.setImageResource(R.mipmap.wuyan);
                        else
                            mNon_smoking.setImageResource(R.mipmap.green);
                        mapFactivity.put("无烟区", "无烟区");
                        break;
                    case "餐厅":
                        if (type == 4) {
                            mWIFI.setImageResource(R.mipmap.green);
                            mapFactivity.put("餐厅", "餐厅");
                        } else {
                            if (flag) mNon_smoking.setImageResource(R.mipmap.canting);
                            else
                                mNon_smoking.setImageResource(R.mipmap.green);
                            mapFactivity.put("餐厅", "餐厅");
                        }
                        break;

                    case "包间":
                        if (flag) mRoom.setImageResource(R.mipmap.baojian);
                        else
                            mRoom.setImageResource(R.mipmap.green);
                        mapFactivity.put("包间", "包间");
                        break;
                    case "行李寄存":
                        if (flag) mRoom.setImageResource(R.mipmap.xingli);
                        else
                            mRoom.setImageResource(R.mipmap.green);
                        mapFactivity.put("行李寄存", "行李寄存");
                        break;

                    case "儿童区":
                        if (flag) mChildren.setImageResource(R.mipmap.ertongqu);
                        else
                            mChildren.setImageResource(R.mipmap.green);
                        mapFactivity.put("儿童区", "儿童区");
                        break;
                    case "租车服务":
                        if (flag) mChildren.setImageResource(R.mipmap.zuche);
                        else
                            mChildren.setImageResource(R.mipmap.green);
                        mapFactivity.put("租车服务", "租车服务");
                        break;

                    case "表演":
                        if (type == 4) {
                            mSofa.setImageResource(R.mipmap.green);
                            mapFactivity.put("表演", "表演");
                        } else {
                            if (flag) mActor.setImageResource(R.mipmap.biaoyan);
                            else
                                mActor.setImageResource(R.mipmap.green);
                            mapFactivity.put("表演", "表演");
                        }
                        break;
                    case "叫醒服务":
                        if (flag) mActor.setImageResource(R.mipmap.jiaoxing);
                        else
                            mActor.setImageResource(R.mipmap.green);
                        mapFactivity.put("叫醒服务", "叫醒服务");
                        break;

                    case "刷卡":
                        if (flag) mCard.setImageResource(R.mipmap.shuaka);
                        else
                            mCard.setImageResource(R.mipmap.green);
                        mapFactivity.put("刷卡", "刷卡");
                        break;
                    case "送餐服务":
                        if (flag) mCard.setImageResource(R.mipmap.songcan);
                        else
                            mCard.setImageResource(R.mipmap.green);
                        mapFactivity.put("送餐服务", "送餐服务");
                        break;

                    case "宝宝骑":
                        if (flag) mBabyq.setImageResource(R.mipmap.baobaoyi);
                        else
                            mBabyq.setImageResource(R.mipmap.green);
                        mapFactivity.put("宝宝骑", "宝宝骑");
                        break;
                    case "洗衣服务":
                        if (flag) mBabyq.setImageResource(R.mipmap.xiyi);
                        else
                            mBabyq.setImageResource(R.mipmap.green);
                        mapFactivity.put("洗衣服务", "洗衣服务");

                        break;
                }
            }

            if (mListener != null)
                mListener.callBack(mapFactivity);
        }

    }

    public interface SelectorService {
        void callBack(HashMap<String, String> mapFactivity);
    }
}
