package com.ruihuo.ixungen.ui.familytree.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.DialogHintInterface;
import com.ruihuo.ixungen.ui.familytree.CatalogInface;
import com.ruihuo.ixungen.ui.familytree.adapter.BookFragmentAdapter;
import com.ruihuo.ixungen.ui.familytree.bean.CatalagBean;
import com.ruihuo.ixungen.ui.familytree.contract.BookContract;
import com.ruihuo.ixungen.ui.familytree.presenter.BookPresenter;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.dialog.HintDialogUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yudonghui
 * @date 2017/10/30
 * @describe May the Buddha bless bug-free!!!
 */
public class BookFragment extends Fragment implements BookContract.View {
    BookPresenter mPresenter = new BookPresenter(this);
    private View mInflate;
    private Context mContext;
    private ViewPager mViewPager;
    private TextView mFirst;
    private ImageView mSub;
    private EditText mPageNum;
    private ImageView mAdd;
    private TextView mEnd;
    private LinearLayout mLlBottom;
    /*private TextView mBookCoverAddress;
    private TextView mBookCoverName;
    private TextView mBookCoverRemark;*/
    private String id;//家谱编号，或者宗谱编号
    private boolean flag;//false 家谱，true族谱
    List<Fragment> mViewList = new ArrayList<>();
    private LoadingDialogUtils mLoadingDialogUtils;

    private XSLYFragment mXslyFragment;//姓氏来源
    private JGJXFragment mJgjxFragment;//家规家训
    private JZMRFragment mJzmrFragment;//家族名人

    private String surnameIntroduce;
    private String familyInstruction;
    private String familyCelebrity;
    private int lookPrivate;
    private int catalogPage = 1;
    private int catalogLimit = 15;
    private int catalogTotalPage = 1;
    private Bundle parameters;
    private int vpPage = 0;
    private int currentItem;//viewpager的当前页面
    public static boolean updateData = false;

    public void setData(boolean flag, String id, int lookPrivate) {
        this.flag = flag;
        this.id = id;
        this.lookPrivate = lookPrivate;
        // addData();
    }

    public void RefreshData(int treeTitle, String content) {
        if (treeTitle == 1) mXslyFragment.setData(content);
        else if (treeTitle == 2) mJgjxFragment.setData(content);
        else if (treeTitle == 3) mJzmrFragment.setData(content);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_book, null);
        mContext = getContext();
        initView();
       /* mViewAdapter = new ViewAdapter(mViewList, mContext);
        mViewPager.setAdapter(mViewAdapter);*/
        // addData();
        bookFragmentAdapter = new BookFragmentAdapter(getFragmentManager(), mViewList);
        mViewPager.setAdapter(bookFragmentAdapter);
        addListener();
        return mInflate;
    }


    private void initView() {
        mViewPager = (ViewPager) mInflate.findViewById(R.id.viewpager);
        mFirst = (TextView) mInflate.findViewById(R.id.first);
        mSub = (ImageView) mInflate.findViewById(R.id.sub);
        mPageNum = (EditText) mInflate.findViewById(R.id.pageNum);
        mAdd = (ImageView) mInflate.findViewById(R.id.add);
        mEnd = (TextView) mInflate.findViewById(R.id.end);
        mLlBottom = (LinearLayout) mInflate.findViewById(R.id.ll_bottom);
        mXslyFragment = new XSLYFragment();
        mJgjxFragment = new JGJXFragment();
        mJzmrFragment = new JZMRFragment();

    }

   /* public void refreshData() {
        totalList.clear();
        pageId.clear();
        mViewList.clear();
        vpPage = 0;
        catalogPage = 1;
        catalogLimit = 15;
        catalogTotalPage = 1;
        parameters = new Bundle();
        BookNameFragment bookNameFragment = new BookNameFragment();
        bookNameFragment.setVpPage(vpPage);
        bookNameFragment.setData(flag, id, new BookNameFragment.CallBackListener() {
            @Override
            public void callBack(String surnameIntroduc, String familyInstructio, String familyCelebrit) {
                surnameIntroduce = surnameIntroduc;
                familyInstruction = familyInstructio;
                familyCelebrity = familyCelebrit;
                mXslyFragment.setData(surnameIntroduce);
                mJgjxFragment.setData(familyInstruction);
                mJzmrFragment.setData(familyCelebrity);
            }
        });
        mViewList.add(bookNameFragment);
        vpPage++;
        catalogueData();//目录数据
    }*/

    public void addData() {
        totalList.clear();
        pageId.clear();
        mViewList.clear();
        vpPage = 0;
        catalogPage = 1;
        catalogLimit = 15;
        catalogTotalPage = 1;
        //if (bookFragmentAdapter != null) bookFragmentAdapter.notifyDataSetChanged();
        parameters = new Bundle();
        mLoadingDialogUtils = new LoadingDialogUtils(mContext);
        BookNameFragment bookNameFragment = new BookNameFragment();
        bookNameFragment.setVpPage(vpPage);
        bookNameFragment.setData(flag, id, new BookNameFragment.CallBackListener() {
            @Override
            public void callBack(String surnameIntroduc, String familyInstructio, String familyCelebrit) {
                surnameIntroduce = surnameIntroduc;
                familyInstruction = familyInstructio;
                familyCelebrity = familyCelebrit;
                mXslyFragment.setData(surnameIntroduce);
                mJgjxFragment.setData(familyInstruction);
                mJzmrFragment.setData(familyCelebrity);
            }
        });
        mViewList.add(bookNameFragment);
        updateData = true;
        vpPage++;
        catalogueData();//目录数据
    }


    private void catalogueData() {
        parameters.clear();
        String url;
        if (flag) {
            parameters.putString("stemmaId", id);
            url = Url.STEMMA_CATALOGUE;
        } else {
            parameters.putString("id", id);
            url = Url.STEMMA_CATALOGUETWO;
        }
        parameters.putString("page", catalogPage + "");
        parameters.putString("limit", catalogLimit + "");
        parameters.putString("url", url);
        parameters.putString("flagSelf", flag + "");
        mPresenter.getCatalogueData(parameters, mContext);
    }

    private void addListener() {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("滑动到位置：", "" + position);
                currentItem = position;
                if (position >= vpPage) {
                    mLlBottom.setVisibility(View.VISIBLE);
                    mPageNum.setText((position - vpPage + 1) + "");
                } else mLlBottom.setVisibility(View.GONE);
                // if (flag) return;
                if (position == vpPage) {//姓氏来源
                    Intent intent = new Intent();
                    intent.setAction(ConstantNum.TREE_TITLE);
                    intent.putExtra("bookfragment", 1);
                    intent.putExtra("surnameIntroduce", surnameIntroduce);
                    mContext.sendBroadcast(intent);
                } else if (position == vpPage + 1) {
                    Intent intent = new Intent();
                    intent.setAction(ConstantNum.TREE_TITLE);
                    intent.putExtra("bookfragment", 2);
                    intent.putExtra("familyInstruction", familyInstruction);
                    mContext.sendBroadcast(intent);
                } else if (position == vpPage + 2) {
                    Intent intent = new Intent();
                    intent.setAction(ConstantNum.TREE_TITLE);
                    intent.putExtra("bookfragment", 3);
                    intent.putExtra("familyCelebrity", familyCelebrity);
                    mContext.sendBroadcast(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setAction(ConstantNum.TREE_TITLE);
                    intent.putExtra("bookfragment", 0);
                    mContext.sendBroadcast(intent);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        mViewPager.setOnTouchListener(VpOntouchListener);
        mPageNum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    notifyStartSearching(mPageNum.getText().toString());
                }
                return true;
            }
        });
        mPageNum.addTextChangedListener(new EditChangedListener());
        mFirst.setOnClickListener(TabListener);
        mSub.setOnClickListener(TabListener);
        mAdd.setOnClickListener(TabListener);
        mEnd.setOnClickListener(TabListener);
    }

    View.OnTouchListener VpOntouchListener = new View.OnTouchListener() {
        float startX;
        float startY;
        float endX;
        float endY;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    startY = event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    endX = event.getX();
                    endY = event.getY();
                    WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                    //获取屏幕的宽度
                    Point size = new Point();
                    windowManager.getDefaultDisplay().getSize(size);
                    int width = size.x;
                    //首先要确定的是，是否到了最后一页，然后判断是否向左滑动，并且滑动距离是否符合，我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                    if (currentItem == (mViewList.size() - 1) && startX - endX > 0 && startX - endX >= (width / 4)) {
                        if (lookPrivate == 1) {
                            Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
                        } else {
                            HintDialogUtils hintDialogUtils = new HintDialogUtils(mContext);
                            hintDialogUtils.setTitle("温馨提示");
                            hintDialogUtils.setMessage("仅可免费查看五代，是否返回购买？");
                            hintDialogUtils.setConfirm("返回购买", new DialogHintInterface() {
                                @Override
                                public void callBack(View view) {
                                    ((Activity) mContext).finish();
                                }
                            });
                        }
                    }
                    break;
            }
            return false;
        }
    };
    CatalogInface CatalogListener = new CatalogInface() {
        @Override
        public void callBack(CatalagBean.DataBean dataBean, int vpPag) {
            if (dataBean == null) return;
            int page = dataBean.getPage();
            mViewPager.setCurrentItem(vpPage + page - 1);
        }

        @Override
        public void callBackTop(int top) {
            mViewPager.setCurrentItem(vpPage + top - 1);
        }
    };
    View.OnClickListener TabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.first:
                    mViewPager.setCurrentItem(0);
                    break;
                case R.id.sub:
                    String subPage = mPageNum.getText().toString();
                    if (subPage != null && subPage.length() > 0) {
                        int pageNum = Integer.parseInt(subPage);
                        if (pageNum <= 1) {
                            mPageNum.setText(1 + "");
                        } else {
                            mPageNum.setText((pageNum - 1) + "");
                            mViewPager.setCurrentItem(vpPage + pageNum - 2);
                        }
                    }
                    break;
                case R.id.add:
                    String addPage = mPageNum.getText().toString();
                    if (addPage != null && addPage.length() > 0) {
                        int pageNum = Integer.parseInt(addPage);
                        mPageNum.setText((pageNum + 1) + "");
                        mViewPager.setCurrentItem(vpPage + pageNum);
                    }
                    break;
                case R.id.end:
                    mViewPager.setCurrentItem(mViewList.size() - 1);
                    break;
            }
        }
    };
    private List<CatalagBean.DataBean> totalList = new ArrayList<>();
    private List<List<CatalagBean.DataBean>> dataList = new ArrayList<>();
    private Map<String, List<CatalagBean.DataBean>> map = new HashMap();
    private Map<Integer, String> pageId = new HashMap<>();
    private boolean isData = true;//是否继续请求目录数据。当没有权限看，超过了五代的时候把这个状态改成false
    private List<CatalagBean.DataBean> currentList = new ArrayList<>();//临时用的。
    private BookFragmentAdapter bookFragmentAdapter;
    public static Map<Integer,List<List<CatalagBean.DataBean>>> catalogueMap=new HashMap();
    @Override
    public void getCatalogueSuccess(String result) {
        map.clear();
        dataList.clear();
        currentList.clear();
        Gson gson = GsonUtils.getGson();
        CatalagBean catalagBean = gson.fromJson(result, CatalagBean.class);
        catalogTotalPage = catalagBean.getTotalPage();
        List<CatalagBean.DataBean> data = catalagBean.getData();
        if (lookPrivate == 1) {
            totalList.addAll(data);
            currentList.addAll(data);
        } else {
            for (int i = 0; i < data.size(); i++) {
                CatalagBean.DataBean dataBean = data.get(i);
                String level_id = dataBean.getLevel_id();
                if (Integer.parseInt(level_id) < 6) {
                    totalList.add(dataBean);
                    currentList.add(dataBean);
                } else {
                    isData = false;
                    break;
                }
            }
        }
        if (totalList != null && totalList.size() > 0) {
            for (int i = 0; i < totalList.size(); i++) {
                CatalagBean.DataBean dataBean = totalList.get(i);
                dataBean.setPage((i + 8) / 2);
            }
            //用于排序
            ArrayList<String> orderList = new ArrayList<String>();
            for (int i = 0; i < currentList.size(); i++) {
                CatalagBean.DataBean dataBean = currentList.get(i);
                String level_id = dataBean.getLevel_id();
                List<CatalagBean.DataBean> dataList;
                if (lookPrivate == 1 || Integer.parseInt(level_id) < 6) {//有权限查看或者是五代以内，包括五代。
                    if (map.containsKey(level_id)) {
                        dataList = map.get(level_id);
                    } else {
                        dataList = new ArrayList<>();
                    }
                    dataList.add(dataBean);
                    map.put(level_id, dataList);
                    if (!orderList.contains(currentList.get(i).getLevel_id())) {
                        orderList.add(currentList.get(i).getLevel_id());
                    }
                } else {//无查看权限，并且超过了五代。这个时候数据不添加。
                    isData = false;
                    break;//跳出for循环
                }
            }
            for (int i = 0; i < orderList.size(); i++) {
                dataList.add(map.get(orderList.get(i)));
            }
            if (catalogPage == 1) {
                CatalogFragment catalogFragment = new CatalogFragment();
                catalogueMap.put(1,dataList);
                catalogFragment.setData(CatalogListener);
                catalogFragment.setVpPage(vpPage);
                mViewList.add(catalogFragment);
                updateData = true;
                vpPage++;
            } else {
                if (dataList.size() > 0) {
                    CatalogFragment2 catalogFragment2 = new CatalogFragment2();
                   catalogFragment2.setData( CatalogListener);
                    catalogueMap.put(catalogPage,dataList);
                    catalogFragment2.setVpPage(vpPage);
                    mViewList.add(catalogFragment2);
                    updateData = true;
                    vpPage++;
                }
            }
            if (catalogPage < catalogTotalPage && isData) {
                catalogPage++;
                catalogueData();
            } else {

                mViewList.add(mXslyFragment);
                updateData = true;
                mViewList.add(mJgjxFragment);
                updateData = true;
                mViewList.add(mJzmrFragment);
                updateData = true;
                mLoadingDialogUtils.setDimiss();
                pageId = new HashMap<>();
                for (int i = 0; i < totalList.size(); i++) {
                    CatalagBean.DataBean dataBean = totalList.get(i);
                    int page = dataBean.getPage();
                    String tree_id;
                    if (flag)
                        tree_id = dataBean.getTree_id();
                    else tree_id = dataBean.getId();
                    if (pageId.containsKey(page)) {
                        pageId.put(page, pageId.get(page) + "-" + tree_id);
                    } else {
                        pageId.put(page, tree_id);
                    }
                }
                CatalagBean.DataBean dataBean = totalList.get(totalList.size() - 1);
                int page = dataBean.getPage();
                XunGenApp.mBookMap.clear();
                for (int i = 4; i <= page; i++) {
                    BookDetailFragment bookDetailFragment = new BookDetailFragment();
                    bookDetailFragment.setId(i, pageId.get(i), flag);
                    //bookDetailFragment.setVpPage(vpPage);
                    mViewList.add(bookDetailFragment);
                    updateData = true;
                    //vpPage++;
                }
               /* bookFragmentAdapter = new BookFragmentAdapter(getFragmentManager(), mViewList);
                mViewPager.setAdapter(bookFragmentAdapter);*/
                bookFragmentAdapter.notifyDataSetChanged();
                mViewPager.setCurrentItem(0);

            }
        } else mLoadingDialogUtils.setDimiss();
    }

    @Override
    public void getCatalogueError(String error) {
        mLoadingDialogUtils.setDimiss();
    }

    @Override
    public void getBookDetailSuccess(String result) {

    }

    @Override
    public void getBookDetailError(String error) {

    }

    @Override
    public void getDataSuccess(String result) {

    }

    @Override
    public void getDataError(String error) {

    }

    /**
     * 通知监听者 进行搜索操作
     * 同时隐藏软键盘
     */
    private void notifyStartSearching(String text) {
        int num = Integer.parseInt(text);
        mViewPager.setCurrentItem(vpPage + num - 1);
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private class EditChangedListener implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            String string = editable.toString();
            if (string != null && string.length() > 0) {
                int i = Integer.parseInt(string);
                if (i > mViewList.size() - 1) {
                    mPageNum.setText((mViewList.size() - 1) + "");
                }
            }
        }
    }
}
