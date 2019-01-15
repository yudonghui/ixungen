package com.ruihuo.ixungen.ui.familytree.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.ui.familytree.bean.TreeDetailSystemBean;

/**
 * @author yudonghui
 * @date 2017/10/30
 * @describe May the Buddha bless bug-free!!!
 */
public class BookBoxView extends LinearLayout {
    private Context mContext;
    private View mInflate;
    private TextView mShixi1;
    private TextView mGeneration1;
    private TextView mClan1;
    private TextView mBrother1;
    private TextView mSelf_name1;
    private TextView mSelf_birthday1;
    private TextView mSelf_dateDate1;
    private TextView mSelf_rank1;
    private ImageView mSelf_avatar1;
    private TextView mSpouse_name1;
    private TextView mSpouse_birthday1;
    private TextView mSpouse_dateDate1;
    private TextView mSpouse_rank1;
    private ImageView mSpouse_avatar1;
    private TextView mFather1;
    private TextView mMother1;
    private TextView mChildren1;
    private TextView mIntroduce1;
    private TextView mShixi2;
    private TextView mGeneration2;
    private TextView mClan2;
    private TextView mBrother2;
    private TextView mSelf_name2;
    private TextView mSelf_birthday2;
    private TextView mSelf_dateDate2;
    private TextView mSelf_rank2;
    private ImageView mSelf_avatar2;
    private TextView mSpouse_name2;
    private TextView mSpouse_birthday2;
    private TextView mSpouse_dateDate2;
    private TextView mSpouse_rank2;
    private ImageView mSpouse_avatar2;
    private TextView mFather2;
    private TextView mMother2;
    private TextView mChildren2;
    private TextView mIntroduce2;

    public BookBoxView(Context context) {
        super(context);
        mContext = context;
        mInflate = View.inflate(context, R.layout.book_box_view, this);
        initView();
    }

    public BookBoxView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mInflate = View.inflate(context, R.layout.book_box_view, this);
        initView();
    }
    private void initView() {
        mShixi1 = (TextView) mInflate.findViewById(R.id.shixi1);
        mGeneration1 = (TextView) mInflate.findViewById(R.id.generation1);
        mClan1 = (TextView) mInflate.findViewById(R.id.clan1);
        mBrother1 = (TextView) mInflate.findViewById(R.id.brother1);
        mSelf_name1 = (TextView) mInflate.findViewById(R.id.self_name1);
        mSelf_birthday1 = (TextView) mInflate.findViewById(R.id.self_birthday1);
        mSelf_dateDate1 = (TextView) mInflate.findViewById(R.id.self_dateDate1);
        mSelf_rank1 = (TextView) mInflate.findViewById(R.id.self_rank1);
        mSelf_avatar1 = (ImageView) mInflate.findViewById(R.id.self_avatar1);
        mSpouse_name1 = (TextView) mInflate.findViewById(R.id.spouse_name1);
        mSpouse_birthday1 = (TextView) mInflate.findViewById(R.id.spouse_birthday1);
        mSpouse_dateDate1 = (TextView) mInflate.findViewById(R.id.spouse_dateDate1);
        mSpouse_rank1 = (TextView) mInflate.findViewById(R.id.spouse_rank1);
        mSpouse_avatar1 = (ImageView) mInflate.findViewById(R.id.spouse_avatar1);
        mFather1 = (TextView) mInflate.findViewById(R.id.father1);
        mMother1 = (TextView) mInflate.findViewById(R.id.mother1);
        mChildren1 = (TextView) mInflate.findViewById(R.id.children1);
        mIntroduce1 = (TextView) mInflate.findViewById(R.id.introduce1);
        mShixi2 = (TextView) mInflate.findViewById(R.id.shixi2);
        mGeneration2 = (TextView) mInflate.findViewById(R.id.generation2);
        mClan2 = (TextView) mInflate.findViewById(R.id.clan2);
        mBrother2 = (TextView) mInflate.findViewById(R.id.brother2);
        mSelf_name2 = (TextView) mInflate.findViewById(R.id.self_name2);
        mSelf_birthday2 = (TextView) mInflate.findViewById(R.id.self_birthday2);
        mSelf_dateDate2 = (TextView) mInflate.findViewById(R.id.self_dateDate2);
        mSelf_rank2 = (TextView) mInflate.findViewById(R.id.self_rank2);
        mSelf_avatar2 = (ImageView) mInflate.findViewById(R.id.self_avatar2);
        mSpouse_name2 = (TextView) mInflate.findViewById(R.id.spouse_name2);
        mSpouse_birthday2 = (TextView) mInflate.findViewById(R.id.spouse_birthday2);
        mSpouse_dateDate2 = (TextView) mInflate.findViewById(R.id.spouse_dateDate2);
        mSpouse_rank2 = (TextView) mInflate.findViewById(R.id.spouse_rank2);
        mSpouse_avatar2 = (ImageView) mInflate.findViewById(R.id.spouse_avatar2);
        mFather2 = (TextView) mInflate.findViewById(R.id.father2);
        mMother2 = (TextView) mInflate.findViewById(R.id.mother2);
        mChildren2 = (TextView) mInflate.findViewById(R.id.children2);
        mIntroduce2 = (TextView) mInflate.findViewById(R.id.introduce2);
    }

    public void setView1(TreeDetailSystemBean.DataBean data) {

    }
    public void setView2(TreeDetailSystemBean.DataBean data) {

    }
}
