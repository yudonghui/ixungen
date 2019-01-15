package com.ruihuo.ixungen.ui.familytree.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.ui.familytree.fragment.BookFragment;
import com.ruihuo.ixungen.ui.familytree.fragment.TreeFragment;

public class TreeActivity extends AppCompatActivity {


    private Context mContext;
    private String id;
    private boolean flag;//false 家谱，true族谱
    private ImageView mImage_titlebar_back;
    private TextView mTree_title;
    private TextView mBook_title;
    private ImageView mMore;
    private FrameLayout mFl_clan;
    private TreeFragment mTreeFragment;
    private BookFragment mBookFragment;
    private FragmentManager mFragmentManager;
    private String stemmaId;
    private boolean treeOrbook;//false 显示树形图，true显示书形图
    private String familyId;
    private int lookPrivate;//0-没有购买；1-有权限；2-权限已经过期

    /*
    * 成员id用于查询树形图。这个id是树形图的上面那个人的id
    * 编号id是当前这个谱的唯一标识。用于查简介等信息。
    * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree);
        mContext = this;
        Intent intent = getIntent();
        flag = intent.getBooleanExtra("flag", false);
        id = intent.getStringExtra("id");//宗谱中是成员的id，家谱中是家谱编号
        stemmaId = intent.getStringExtra("stemmaId");//宗谱编号
        familyId = intent.getStringExtra("familyId");//家谱的成员id
        treeOrbook = intent.getBooleanExtra("treeOrbook", false);
        createRid = intent.getStringExtra("createRid");
        lookPrivate = intent.getIntExtra("lookPrivate", 0);
        initView();
        initFragment();
        addListener();
        //注册一个动态广播
        registerBoradcastReceiver();
    }

    private void initView() {
        mImage_titlebar_back = (ImageView) findViewById(R.id.image_titlebar_back);
        mTree_title = (TextView) findViewById(R.id.tree_title);
        mBook_title = (TextView) findViewById(R.id.book_title);
        mMore = (ImageView) findViewById(R.id.more);
        mFl_clan = (FrameLayout) findViewById(R.id.fl_clan);
    }

    private void initFragment() {
        mTreeFragment = new TreeFragment();
        mBookFragment = new BookFragment();
        if (flag) {
            mTreeFragment.setData(flag, id, stemmaId, createRid, lookPrivate);
            mBookFragment.setData(flag, stemmaId, lookPrivate);
            mTreeFragment.setDeleteListener(CallBackListener);
        } else {
            mTreeFragment.setData(flag, id, familyId, createRid, lookPrivate);
            mBookFragment.setData(flag, familyId, lookPrivate);
            mTreeFragment.setDeleteListener(CallBackListener);
        }
        mFragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        if (treeOrbook) {
            fragmentTransaction.add(R.id.fl_clan, mTreeFragment).add(R.id.fl_clan, mBookFragment)
                    .show(mBookFragment).hide(mTreeFragment);
            mTree_title.setBackgroundResource(R.drawable.shape_left_raduis_browns);
            mTree_title.setTextColor(getResources().getColor(R.color.white));
            mBook_title.setBackgroundResource(R.drawable.shape_right_raduis_white);
            mBook_title.setTextColor(getResources().getColor(R.color.brown_bg));
            if (isFirst) {
                mBookFragment.addData();
                isFirst = false;
            }
        } else {
            fragmentTransaction.add(R.id.fl_clan, mTreeFragment).add(R.id.fl_clan, mBookFragment)
                    .show(mTreeFragment).hide(mBookFragment);
        }
        fragmentTransaction.commit();
    }

    private void addListener() {
        mImage_titlebar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTree_title.setOnClickListener(TabListener);
        mBook_title.setOnClickListener(TabListener);
        if (treeOrbook) {
            mMore.setOnClickListener(SearchBookListener);
        } else
            mMore.setOnClickListener(SearchTreeListener);
    }

    View.OnClickListener MoreListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    private boolean isFirst = true;
    View.OnClickListener TabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            switch (v.getId()) {
                case R.id.tree_title:
                    mTree_title.setBackgroundResource(R.drawable.shape_left_raduis_white);
                    mTree_title.setTextColor(getResources().getColor(R.color.brown_bg));
                    mBook_title.setBackgroundResource(R.drawable.shape_right_raduis_browns);
                    mBook_title.setTextColor(getResources().getColor(R.color.white));
                    fragmentTransaction.show(mTreeFragment).hide(mBookFragment);
                    mMore.setImageResource(0);
                    mMore.setOnClickListener(SearchTreeListener);
                    break;
                case R.id.book_title:
                    mTree_title.setBackgroundResource(R.drawable.shape_left_raduis_browns);
                    mTree_title.setTextColor(getResources().getColor(R.color.white));
                    mBook_title.setBackgroundResource(R.drawable.shape_right_raduis_white);
                    mBook_title.setTextColor(getResources().getColor(R.color.brown_bg));
                    fragmentTransaction.show(mBookFragment).hide(mTreeFragment);
                    if (isFirst) {
                        mBookFragment.addData();
                        isFirst = false;
                    }
                    setTitle();
                    break;
            }
            fragmentTransaction.commit();

        }
    };

    private void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConstantNum.TREE_TITLE);
        registerReceiver(br, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(br);
    }

    private int treeTitle;//1编辑姓氏来源，2编辑家规家训，3编辑姓氏名人
    private String surnameIntroduce;
    private String familyInstruction;
    private String familyCelebrity;
    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(ConstantNum.TREE_TITLE)) {
                treeTitle = intent.getIntExtra("bookfragment", 0);
                surnameIntroduce = intent.getStringExtra("surnameIntroduce");
                familyInstruction = intent.getStringExtra("familyInstruction");
                familyCelebrity = intent.getStringExtra("familyCelebrity");
                setTitle();
            }
        }
    };
    private String createRid;//创建家谱的人的rid

    private void setTitle() {
        if (flag || !XunGenApp.rid.equals(createRid)) {
            mMore.setImageResource(0);
            mMore.setOnClickListener(SearchBookListener);
        } else {
            switch (treeTitle) {
                case 0://搜索书形家谱
                    mMore.setImageResource(0);
                    mMore.setOnClickListener(SearchBookListener);
                    break;
                case 1://编辑姓氏来源
                    mMore.setImageResource(R.mipmap.icon_edit);
                    mMore.setOnClickListener(EditeXslyListener);
                    break;
                case 2://编辑家规家训
                    mMore.setImageResource(R.mipmap.icon_edit);
                    mMore.setOnClickListener(EditeJgjxListener);
                    break;
                case 3://编辑姓氏名人
                    mMore.setImageResource(R.mipmap.icon_edit);
                    mMore.setOnClickListener(EditeJzmrListener);
                    break;
            }
        }
    }

    View.OnClickListener SearchTreeListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //Toast.makeText(mContext, "搜索树形图", Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener SearchBookListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Toast.makeText(mContext, "搜索书形图", Toast.LENGTH_SHORT).show();
        }
    };
    View.OnClickListener EditeXslyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Toast.makeText(mContext, "编辑姓氏来源", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, EditorStemmaActivity.class);
            intent.putExtra("treeTitle", treeTitle);
            intent.putExtra("familyId", familyId);
            intent.putExtra("content", surnameIntroduce);
            startActivityForResult(intent, 8881);
        }
    };
    View.OnClickListener EditeJgjxListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Toast.makeText(mContext, "编辑家规家训", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, EditorStemmaActivity.class);
            intent.putExtra("treeTitle", treeTitle);
            intent.putExtra("familyId", familyId);
            intent.putExtra("content", familyInstruction);
            startActivityForResult(intent, 8881);
        }
    };
    View.OnClickListener EditeJzmrListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //   Toast.makeText(mContext, "编辑家族名人", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(mContext, EditorStemmaActivity.class);
            intent.putExtra("treeTitle", treeTitle);
            intent.putExtra("familyId", familyId);
            intent.putExtra("content", familyCelebrity);
            startActivityForResult(intent, 8881);
        }
    };
    com.ruihuo.ixungen.geninterface.CallBackInterface CallBackListener = new com.ruihuo.ixungen.geninterface.CallBackInterface() {
        @Override
        public void callBack() {//树形图删除的时候回调
            if (!isFirst){
                isFirst=true;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 111 && resultCode == 222) {//添加亲属
            mTreeFragment.refreshData();//刷新数据
            if (!isFirst){
                isFirst=true;
            }

        } else if (requestCode == 112 && resultCode == 223) {//修改信息（编辑）
            mTreeFragment.refreshData();//刷新数据
            if (!isFirst)
                isFirst=true;
        } else if (requestCode == 8881 && resultCode == 9991) {
            if (data != null) {
                String content = data.getStringExtra("content");
                if (treeTitle == 1) {
                    surnameIntroduce = content;
                } else if (treeTitle == 2) familyInstruction = content;
                else if (treeTitle == 3) familyCelebrity = content;
                isFirst=true;
                mBookFragment.RefreshData(treeTitle, content);
            }
        }
    }
}
