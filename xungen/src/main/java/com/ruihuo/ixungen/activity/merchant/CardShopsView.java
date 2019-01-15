package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.squareup.picasso.Picasso;

import static com.ruihuo.ixungen.R.id.cover;

/**
 * @author yudonghui
 * @date 2017/8/17
 * @describe May the Buddha bless bug-free!!!
 */
public class CardShopsView extends LinearLayout {
    private Context mContext;
    private View inflate;
    private ImageView mCover;
    private TextView mGoodsName;
    private TextView mService;
    private TextView mPrice;
    private int resize;

    public CardShopsView(Context context) {
        super(context);
        mContext = context;
        inflate = View.inflate(context, R.layout.card_shops, this);
    }

    public CardShopsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        inflate = View.inflate(context, R.layout.card_shops, this);
    }

    public CardShopsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        inflate = View.inflate(context, R.layout.card_shops, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCover = (ImageView) findViewById(cover);
        mGoodsName = (TextView) findViewById(R.id.goodsName);
        mService = (TextView) findViewById(R.id.service);
        mPrice = (TextView) findViewById(R.id.price);
    }

    public void setData(OrderBaseBean dataBean) {
        if (dataBean == null) return;
        int displayWidth = DisplayUtilX.getDisplayWidth();
        resize = (int) (displayWidth / 3.5);

        String type = dataBean.getType();
        String cover = dataBean.getCover();
        String goods_name = dataBean.getGoods_name();
        String is_cancel = dataBean.getIs_cancel();
        String consume_start_time = dataBean.getConsume_start_time();
        String consume_end_time = dataBean.getConsume_end_time();
        String price = dataBean.getPrice();//单价

        mPrice.setText("￥" + (TextUtils.isEmpty(price) ? "" : price));
        mGoodsName.setText(TextUtils.isEmpty(goods_name) ? "--" : goods_name);
        String remark = "";
        if ("1".equals(type)) {//餐厅
            remark = "1".equals(is_cancel) ? "可取消" : "不可取消";
        } else {
            if (TextUtils.isEmpty(consume_end_time) || TextUtils.isEmpty(consume_start_time))
                return;
            long in = DateFormatUtils.StringToLong(consume_start_time);
            long out = DateFormatUtils.StringToLong(consume_end_time);
            int days = (int) ((out - in) / 86400000);
            String start = DateFormatUtils.longToDate(in);
            String end = DateFormatUtils.longToDate(out);
            remark = start + "至" + end + "  共" + days + "晚";
        }
        mService.setText(remark);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(resize, resize * 3 / 4);
        mCover.setLayoutParams(layoutParams);
        if (!TextUtils.isEmpty(cover)) {
            String[] split = cover.split("\\;");
            Picasso.with(mContext)
                    .load(split[0])
                    .config(Bitmap.Config.RGB_565)
                    .placeholder(R.mipmap.default_long)
                    .error(R.mipmap.default_long)
                    .into(mCover);
        }
    }
}
