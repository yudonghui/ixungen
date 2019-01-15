package com.ruihuo.ixungen.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.action.ActionUserInfoActivity;
import com.ruihuo.ixungen.action.CommentActivity;
import com.ruihuo.ixungen.action.DynamicDetailActivity;
import com.ruihuo.ixungen.activity.merchant.GridImageAdapter;
import com.ruihuo.ixungen.entity.FriendStateBean;
import com.ruihuo.ixungen.geninterface.CallBackInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.familytree.activity.StemmaDetailActivity;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;
import com.ruihuo.ixungen.utils.dialog.LookPrivateDialog;
import com.ruihuo.ixungen.view.CircleImageView;
import com.ruihuo.ixungen.view.MyGridView;

import java.util.HashMap;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/8
 * @describe May the Buddha bless bug-free!!!
 */
public class FriendStateAdapter extends BaseAdapter {
    List<FriendStateBean.DataBean> friendList;
    Context mContext;
    private int type;//0-查看所有动态，1-获取宗亲会动态；2-获取自己发表的动态，
    private CallBackInterface mCallBack;
    private String rid;

    public FriendStateAdapter(List<FriendStateBean.DataBean> friendList, Context mContext, int type) {
        this.friendList = friendList;
        this.mContext = mContext;
        this.type = type;
    }

    public void setRid(String rid) {
        this.rid = rid;
    }

    public void setDelete(CallBackInterface mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public int getCount() {
        map.clear();
        for (int i = 0; i < friendList.size(); i++) {
            map.put(i, friendList.get(i));
        }
        return friendList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private HashMap<Integer, FriendStateBean.DataBean> map = new HashMap<>();

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_friend_state, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mAvatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.mDate = (TextView) convertView.findViewById(R.id.date);
            mViewHolder.mContent = (TextView) convertView.findViewById(R.id.content);
            mViewHolder.mGridView = (MyGridView) convertView.findViewById(R.id.gridView);
            mViewHolder.mLookNum = (TextView) convertView.findViewById(R.id.lookNum);
            mViewHolder.mLl_comment = (LinearLayout) convertView.findViewById(R.id.ll_comment);
            mViewHolder.mComment_img = (ImageView) convertView.findViewById(R.id.comment_img);
            mViewHolder.mComment_text = (TextView) convertView.findViewById(R.id.comment_text);
            mViewHolder.mLl_praise = (LinearLayout) convertView.findViewById(R.id.ll_praise);
            mViewHolder.mPraise_img = (ImageView) convertView.findViewById(R.id.praise_img);
            mViewHolder.mPraise_text = (TextView) convertView.findViewById(R.id.praise_text);
            mViewHolder.mXiaLa = (ImageView) convertView.findViewById(R.id.xiala);
            convertView.setTag(mViewHolder);
        } else mViewHolder = (ViewHolder) convertView.getTag();

        final FriendStateBean.DataBean dataBean = map.get(position);
        String avatar = dataBean.getAvatar();
        String nikename = dataBean.getNikename();
        final String ridP = dataBean.getRid();//发表者根号
        final String pid = dataBean.getId();
        String create_time = dataBean.getCreate_time();
        String content = dataBean.getContent();
        String count = dataBean.getCount();
        String img = dataBean.getImg();
        String total_reply = dataBean.getTotal_reply();//回复数
        final String total_like = dataBean.getTotal_like();//点赞数
        final String family_id = dataBean.getFamily_id();
        String family_name = dataBean.getFamily_name();
        final String is_like = dataBean.getIs_like();//1是点赞过了。0是没有点赞
        if (type == 2) {
            if (this.rid != null && this.rid.equals(XunGenApp.rid)) {//自己发表的动态
                mViewHolder.mXiaLa.setVisibility(View.VISIBLE);
            } else mViewHolder.mXiaLa.setVisibility(View.GONE);
        } else {
            mViewHolder.mXiaLa.setVisibility(View.GONE);
        }
        if (TextUtils.isEmpty(total_reply) || "0".equals(total_reply)) {
            mViewHolder.mComment_text.setText("评论");
        } else mViewHolder.mComment_text.setText(total_reply);
        if (TextUtils.isEmpty(total_like) || "0".equals(total_like)) {
            mViewHolder.mPraise_text.setText("点赞");
        } else mViewHolder.mPraise_text.setText(total_like);
        mViewHolder.mLl_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "评论", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, CommentActivity.class);
                intent.putExtra("pid", pid);
                mContext.startActivity(intent);
            }
        });
        if ("1".equals(is_like)) {
            mViewHolder.mPraise_img.setImageResource(R.mipmap.praise);
            mViewHolder.mPraise_text.setTextColor(mContext.getResources().getColor(R.color.green_txt));
        } else {
            mViewHolder.mPraise_img.setImageResource(R.mipmap.praise_icon);
            mViewHolder.mPraise_text.setTextColor(mContext.getResources().getColor(R.color.gray_txt));
        }
        if (!"2".equals(type)) {
            mViewHolder.mAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActionUserInfoActivity.class);
                    intent.putExtra("rid", ridP);
                    mContext.startActivity(intent);
                }
            });
        }
        mViewHolder.mLl_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("topicId", pid);
                mHttp.post(Url.PRAISE, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {

                        FriendStateBean.DataBean dataBean1 = map.get(position);
                        if ("1".equals(is_like)) {
                            dataBean1.setIs_like("0");
                            if (!TextUtils.isEmpty(total_like))//取消。则点赞数减少1
                                dataBean1.setTotal_like((Integer.parseInt(total_like) - 1) + "");
                        } else {
                            dataBean1.setIs_like("1");
                            if (!TextUtils.isEmpty(total_like))//
                                dataBean1.setTotal_like((Integer.parseInt(total_like) + 1) + "");
                        }
                        map.put(position, dataBean1);
                        notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String message) {

                    }
                });
            }
        });
        mViewHolder.mName.setText(TextUtils.isEmpty(nikename) ? "" : nikename);
        if (!TextUtils.isEmpty(content))
            setMaxEcplise(mViewHolder.mContent, 2, content);//只显示两行，多的省略号
        else mViewHolder.mContent.setText("");
        PicassoUtils.setImgView(avatar, mContext, R.mipmap.default_header, mViewHolder.mAvatar);
        mViewHolder.mDate.setText(DateFormatUtils.longToDateM(create_time));
        mViewHolder.mLookNum.setText(TextUtils.isEmpty(count) ? "0" : count);
        if (!TextUtils.isEmpty(img)) {
            String[] urlArray = img.split("\\;");
            GridImageAdapter gridImageAdapter = new GridImageAdapter(urlArray, 3, mContext);
            mViewHolder.mGridView.setAdapter(gridImageAdapter);
        } else {
            String[] urlArray = {};
            GridImageAdapter gridImageAdapter = new GridImageAdapter(urlArray, 3, mContext);
            mViewHolder.mGridView.setAdapter(gridImageAdapter);
        }
        mViewHolder.mXiaLa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LookPrivateDialog(mContext, pid, new CallBackInterface() {
                    @Override
                    public void callBack() {
                        mCallBack.callBack();
                    }
                }).showDialog();
            }
        });
        if (TextUtils.isEmpty(family_id) || "0".equals(family_id)) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DynamicDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("type", type);
                    bundle.putSerializable("dataBean", dataBean);
                    intent.putExtras(bundle);
                    ((Activity) mContext).startActivityForResult(intent, 221);

                }
            });
        } else {
            if (!TextUtils.isEmpty(content) && !TextUtils.isEmpty(family_name)) {
                int index = content.indexOf(family_name);
                SpannableString spannableString = new SpannableString(content);
                spannableString.setSpan(new ForegroundColorSpan(Color.BLUE), index, index + family_name.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                mViewHolder.mContent.setText(spannableString);
            }
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, StemmaDetailActivity.class);
                    intent.putExtra("familyId", family_id);
                    intent.putExtra("flag", false);
                    ((Activity) mContext).startActivityForResult(intent, 221);
                }
            });
        }
        return convertView;
    }

    class ViewHolder {
        private CircleImageView mAvatar;
        private TextView mName;
        private TextView mDate;
        private TextView mContent;
        private MyGridView mGridView;
        private TextView mLookNum;
        private LinearLayout mLl_comment;
        private ImageView mComment_img;
        private TextView mComment_text;
        private LinearLayout mLl_praise;
        private ImageView mPraise_img;
        private TextView mPraise_text;
        private ImageView mXiaLa;
    }


    /**
     * 参数：maxLines 要限制的最大行数
     * 参数：content  指TextView中要显示的内容
     */
    public void setMaxEcplise(final TextView mTextView, final int maxLines, final String content) {
        mTextView.setText(content);
        if (mTextView.getLineCount() > maxLines) {
            int lineEndIndex = mTextView.getLayout().getLineEnd(maxLines - 1);
            //下面这句代码中：我在项目中用数字3发现效果不好，改成1了
            String text = content.subSequence(0, lineEndIndex - 1) + "...";
            mTextView.setText(text);
        }
    }
}
