package com.ruihuo.ixungen.ui.familytree.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.common.SortUtils;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.familytree.ArabicToChineseUtils;
import com.ruihuo.ixungen.ui.familytree.bean.TreeDetailSystemBean;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PicassoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/11/2
 * @describe May the Buddha bless bug-free!!!
 */
public class BookDetailFragment extends Fragment {
    private View mInflate;
    private Context mContext;
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
    private String treeId;
    private String treeId1;
    private String treeId2;
    private boolean flag;//false 家谱，true族谱
    private int page;
    private int vpPage;

    public void setVpPage(int vpPage) {
        this.vpPage = vpPage;
    }

    public void setId(int page, String treeId, boolean flag) {
        this.treeId = treeId;
        this.flag = flag;
        this.page = page;
        String[] split = treeId.split("\\-");
        if (split.length == 1) treeId1 = treeId;
        else if (split.length == 2) {
            treeId1 = split[0];
            treeId2 = split[1];
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.book_box_view, null);
        mContext = getContext();
        initView();
        addData();
        return mInflate;
    }

    private TreeDetailSystemBean.DataBean dataBeen1;
    private TreeDetailSystemBean.DataBean dataBeen2;
    private List<TreeDetailSystemBean.DataBean> dataBeanList = new ArrayList<>();

    private void addData() {
        if (XunGenApp.mBookMap.containsKey(page)) {
            List<TreeDetailSystemBean.DataBean> dataBeenList = XunGenApp.mBookMap.get(page);
            if (dataBeenList == null) return;
            if (dataBeenList.size() == 1) {
                TreeDetailSystemBean.DataBean dataBeen = dataBeenList.get(0);
                setView1(dataBeen);
            } else if (dataBeenList.size() == 2) {
                TreeDetailSystemBean.DataBean dataBeen1 = dataBeenList.get(0);
                String level_id1 = dataBeen1.getLevel_id();

                TreeDetailSystemBean.DataBean dataBeen2 = dataBeenList.get(1);
                String level_id2 = dataBeen2.getLevel_id();
                if (Integer.parseInt(level_id1) > Integer.parseInt(level_id2)) {//防止出现同一页上面的世代比下面的大。
                    setView1(dataBeen2);
                    setView2(dataBeen1);
                } else {
                    setView1(dataBeen1);
                    setView2(dataBeen2);
                }

            }
        } else {
            dataBeanList.clear();
            HttpInterface instance = HttpUtilsManager.getInstance(mContext);
            if (!TextUtils.isEmpty(treeId1)) {
                Bundle params = new Bundle();
                String url;
                if (flag) {
                    params.putString("id", treeId1);
                    params.putString("token", XunGenApp.token);
                    url = Url.STEMMA_MEMEBER_SYSTME_INFO;
                } else {
                    params.putString("id", treeId1);
                    url = Url.STEMMA_MEMEBER_APP_INFO;
                }
                params.putString("flagSelf", flag + "");
                instance.getRSA(url, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        Gson gson = GsonUtils.getGson();
                        TreeDetailSystemBean treeDetailSystemBean = gson.fromJson(result, TreeDetailSystemBean.class);
                        dataBeen1 = treeDetailSystemBean.getData();
                        dataBeanList.add(dataBeen1);
                        XunGenApp.mBookMap.put(page, dataBeanList);
                        addData2();
                    }

                    @Override
                    public void onError(String message) {
                        addData2();
                    }
                });
            } else addData2();
        }
    }

    private void addData2() {

        HttpInterface instance = HttpUtilsManager.getInstance(mContext);
        if (!TextUtils.isEmpty(treeId2)) {
            Bundle params2 = new Bundle();
            String url2;
            if (flag) {
                params2.putString("id", treeId2);
                params2.putString("token", XunGenApp.token);
                url2 = Url.STEMMA_MEMEBER_SYSTME_INFO;
            } else {
                params2.putString("id", treeId2);
                url2 = Url.STEMMA_MEMEBER_APP_INFO;
            }
            params2.putString("flagSelf", flag + "");
            instance.getRSA(url2, params2, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = GsonUtils.getGson();
                    TreeDetailSystemBean treeDetailSystemBean = gson.fromJson(result, TreeDetailSystemBean.class);
                    dataBeen2 = treeDetailSystemBean.getData();
                    dataBeanList.add(dataBeen2);
                    XunGenApp.mBookMap.put(page, dataBeanList);
                    setview();
                }

                @Override
                public void onError(String message) {
                    setview();
                }
            });
        } else {
            setview();
        }
    }

    private void setview() {
        if (dataBeen1 != null && dataBeen2 != null) {
            String level_id1 = dataBeen1.getLevel_id();
            String level_id2 = dataBeen2.getLevel_id();
            if (Integer.parseInt(level_id1) > Integer.parseInt(level_id2)) {//防止出现同一页上面的世代比下面的大。
                setView1(dataBeen2);
                setView2(dataBeen1);
            } else {
                setView1(dataBeen1);
                setView2(dataBeen2);
            }
        } else if (dataBeen1 == null && dataBeen2 != null) {
            setView1(dataBeen2);
        } else if (dataBeen1 != null && dataBeen2 == null) {
            setView1(dataBeen1);
        } else {//全为空

        }
    }

    private void setView1(TreeDetailSystemBean.DataBean data) {
        if (data == null) return;
        int total = data.getTotal();
        String level_id = data.getLevel_id();
        String generation = data.getGeneration();
        String name = data.getName();
        String sex = data.getSex();
        int sort = data.getSort();
        String father = data.getFather();
        String mother = data.getMother();
        String text = data.getText();
        String avatar_url = data.getAvatar_url();
        String birth;
        String death = "";
        if (flag) {
            String year = data.getBirthday_year();
            String month = data.getBirthday_month();
            String day = data.getBirthday_day();
            birth = (TextUtils.isEmpty(year) ? "-" : year) + (TextUtils.isEmpty(month) ? "-" : month) + (TextUtils.isEmpty(day) ? "-" : day);
        } else {
            birth = data.getBirth();
            death = data.getDeath();
        }
        List<TreeDetailSystemBean.DataBean.ChildsBean> childs = data.getChilds();
        if (!TextUtils.isEmpty(level_id))
            mGeneration1.setText(ArabicToChineseUtils.foematInteger(Integer.parseInt(level_id)) + "世/");
        mClan1.setText(TextUtils.isEmpty(generation) ? "" : generation);
        mSelf_name1.setText(TextUtils.isEmpty(name) ? "--" : name);
        mSelf_birthday1.setText(TextUtils.isEmpty(birth) ? "--" : birth);
        mSelf_dateDate1.setText(TextUtils.isEmpty(death) ? "--" : death);
        mSelf_rank1.setText(SortUtils.getSort(sort, sex));
        if (!TextUtils.isEmpty(avatar_url)) {
            mSelf_avatar1.setScaleType(ImageView.ScaleType.CENTER_CROP);
            PicassoUtils.setImgView(avatar_url, R.mipmap.default_header_clan, mContext, mSelf_avatar1);
        }
        TreeDetailSystemBean.DataBean.SpouseInfoBean spouse_info = data.getSpouse_info();
        if (spouse_info != null) {
            String spouseName = spouse_info.getName();
            int spouseSort = spouse_info.getSort();
            String spouseAvatar = spouse_info.getAvatar_url();
            String spouseBirth;
            String spouseDeath = "";
            if (flag) {
                String yearSpouse = spouse_info.getBirthday_year();
                String monthSpouse = spouse_info.getBirthday_month();
                String daySpouse = spouse_info.getBirthday_day();
                spouseBirth = (TextUtils.isEmpty(yearSpouse) ? "-" : yearSpouse) + "-" + (TextUtils.isEmpty(monthSpouse) ? "-" : monthSpouse) + "-" + (TextUtils.isEmpty(daySpouse) ? "-" : daySpouse);

            } else {
                spouseBirth = spouse_info.getBirth();
                spouseDeath = spouse_info.getDeath();
            }
            mSpouse_name1.setText(TextUtils.isEmpty(spouseName) ? "--" : spouseName);
            mSpouse_birthday1.setText(TextUtils.isEmpty(spouseBirth) ? "--" : spouseBirth);
            mSpouse_dateDate1.setText(TextUtils.isEmpty(spouseDeath) ? "--" : spouseDeath);
            String spouseSex;
            if ("1".equals(sex))
                spouseSex = "2";
            else if ("2".equals(sex))
                spouseSex = "1";
            else spouseSex = "0";
            mSpouse_rank1.setText(SortUtils.getSort(spouseSort, spouseSex));
            if (!TextUtils.isEmpty(spouseAvatar)) {
                mSpouse_avatar1.setScaleType(ImageView.ScaleType.CENTER_CROP);
                PicassoUtils.setImgView(spouseAvatar, R.mipmap.default_header_clan, mContext, mSpouse_avatar1);
            }
        }
        mFather1.setText(TextUtils.isEmpty(father) ? "--" : father);
        mMother1.setText(TextUtils.isEmpty(mother) ? "--" : mother);
        mBrother1.setText(total + "人\n兄弟姐妹");
        if (!TextUtils.isEmpty(text)) {
            // String replace = text.replace("<p>", " ").replace("</p>", " ").replace("<br/>", "\n").replace("&nbsp", " ");
            mIntroduce1.setText(Html.fromHtml(text));
        } else mIntroduce1.setText("暂无简介信息");
        if (childs != null && childs.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < childs.size(); i++) {
                TreeDetailSystemBean.DataBean.ChildsBean childsBean = childs.get(i);
                String sex1 = childsBean.getSex();
                String childName = childsBean.getName();
                if (i == 0) {
                    stringBuilder.append(TextUtils.isEmpty(childName) ? "未详" : childName + getSex(sex1));
                } else {
                    stringBuilder.append("," + (TextUtils.isEmpty(childName) ? "未详" : childName) + getSex(sex1));
                }
            }
            mChildren1.setText(stringBuilder);
        }

    }

    private String getSex(String sex) {
        switch (sex) {
            case "1":
                return "(男)";
            case "2":
                return "(女)";
            default:
                return "(未知)";
        }
    }

    private void setView2(TreeDetailSystemBean.DataBean data) {
        if (data == null) return;
        int total = data.getTotal();
        String level_id = data.getLevel_id();
        String generation = data.getGeneration();
        String name = data.getName();
        int sort = data.getSort();
        String sex = data.getSex();
        String father = data.getFather();
        String mother = data.getMother();
        String text = data.getText();
        String avatar_url = data.getAvatar_url();
        String birth;
        String death = "";
        if (flag) {
            String year = data.getBirthday_year();
            String month = data.getBirthday_month();
            String day = data.getBirthday_day();
            birth = (TextUtils.isEmpty(year) ? "-" : year) + (TextUtils.isEmpty(month) ? "-" : month) + (TextUtils.isEmpty(day) ? "-" : day);
        } else {
            birth = data.getBirth();
            death = data.getDeath();
        }
        List<TreeDetailSystemBean.DataBean.ChildsBean> childs = data.getChilds();
        if (!TextUtils.isEmpty(level_id))
            mGeneration2.setText(ArabicToChineseUtils.foematInteger(Integer.parseInt(level_id)) + "世/");
        mClan2.setText(TextUtils.isEmpty(generation) ? "" : generation);
        mSelf_name2.setText(TextUtils.isEmpty(name) ? "--" : name);
        mSelf_birthday2.setText(TextUtils.isEmpty(birth) ? "--" : birth);
        mSelf_dateDate2.setText(TextUtils.isEmpty(death) ? "--" : death);
        mSelf_rank2.setText(SortUtils.getSort(sort, sex));
        if (!TextUtils.isEmpty(avatar_url)) {
            mSelf_avatar2.setScaleType(ImageView.ScaleType.CENTER_CROP);
            PicassoUtils.setImgView(avatar_url, R.mipmap.default_header_clan, mContext, mSelf_avatar2);
        }
        TreeDetailSystemBean.DataBean.SpouseInfoBean spouse_info = data.getSpouse_info();
        if (spouse_info != null) {
            String spouseName = spouse_info.getName();
            int spouseSort = spouse_info.getSort();
            String spouseAvatar = spouse_info.getAvatar_url();
            String spouseBirth;
            String spouseDeath = "";
            if (flag) {
                String yearSpouse = spouse_info.getBirthday_year();
                String monthSpouse = spouse_info.getBirthday_month();
                String daySpouse = spouse_info.getBirthday_day();
                spouseBirth = (TextUtils.isEmpty(yearSpouse) ? "-" : yearSpouse) + "-" + (TextUtils.isEmpty(monthSpouse) ? "-" : monthSpouse) + "-" + (TextUtils.isEmpty(daySpouse) ? "-" : daySpouse);

            } else {
                spouseBirth = spouse_info.getBirth();
                spouseDeath = spouse_info.getDeath();
            }
            mSpouse_name2.setText(TextUtils.isEmpty(spouseName) ? "--" : spouseName);
            mSpouse_birthday2.setText(TextUtils.isEmpty(spouseBirth) ? "--" : spouseBirth);
            mSpouse_dateDate2.setText(TextUtils.isEmpty(spouseDeath) ? "--" : spouseDeath);
            String spouseSex;
            if ("1".equals(sex))
                spouseSex = "2";
            else if ("2".equals(sex))
                spouseSex = "1";
            else spouseSex = "0";
            mSpouse_rank1.setText(SortUtils.getSort(spouseSort, spouseSex));
            mSpouse_rank2.setText(SortUtils.getSort(spouseSort, spouseSex));
            if (!TextUtils.isEmpty(spouseAvatar)) {
                mSpouse_avatar2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                PicassoUtils.setImgView(spouseAvatar, R.mipmap.default_header_clan, mContext, mSpouse_avatar2);

            }
        }
        mFather2.setText(TextUtils.isEmpty(father) ? "--" : father);
        mMother2.setText(TextUtils.isEmpty(mother) ? "--" : mother);
        mBrother2.setText(total + "人\n兄弟姐妹");
        if (!TextUtils.isEmpty(text)) {
            //String replace = text.replace("<p>", " ").replace("</p>", " ").replace("<br/>", "\n").replace("&nbsp", " ");
            mIntroduce2.setText(Html.fromHtml(text));
        } else mIntroduce2.setText("暂无简介信息");
        if (childs != null && childs.size() > 0) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < childs.size(); i++) {
                TreeDetailSystemBean.DataBean.ChildsBean childsBean = childs.get(i);
                String sex1 = childsBean.getSex();
                String childName = childsBean.getName();
                if (i == 0) {
                    stringBuilder.append(TextUtils.isEmpty(childName) ? "未详" : childName + getSex(sex1));
                } else {
                    stringBuilder.append("," + (TextUtils.isEmpty(childName) ? "未详" : childName) + getSex(sex1));
                }
            }
            mChildren2.setText(stringBuilder);
        }

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
}
