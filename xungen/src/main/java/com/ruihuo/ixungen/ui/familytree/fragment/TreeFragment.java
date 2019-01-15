package com.ruihuo.ixungen.ui.familytree.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.ui.familytree.TreeViewInterface;
import com.ruihuo.ixungen.ui.familytree.bean.ExpandBean;
import com.ruihuo.ixungen.ui.familytree.bean.Tree;
import com.ruihuo.ixungen.ui.familytree.bean.TreeBean;
import com.ruihuo.ixungen.ui.familytree.contract.TreeContract;
import com.ruihuo.ixungen.ui.familytree.presenter.TreePresenter;
import com.ruihuo.ixungen.ui.familytree.view.ClickTreeView;
import com.ruihuo.ixungen.ui.familytree.view.TreeView;
import com.ruihuo.ixungen.utils.GsonUtils;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/30
 * @describe May the Buddha bless bug-free!!!
 */
public class TreeFragment extends Fragment implements TreeContract.View {
    TreePresenter mPresenter = new TreePresenter(this);
    private View mInflate;
    private Context mContext;
    private TreeView mTreeView;
    // private String stemmaId;//族谱
    private String id;//家谱或者宗谱树形图如果是搜索用的id。
    private boolean flag;//false 家谱，true族谱
    private String createRid;
    private String stemmaId;
    private int lookPrivate;

    public void setData(boolean flag, String id, String stemmaId, String createRid, int lookPrivate) {
        this.flag = flag;
        this.id = id;
        this.stemmaId = stemmaId;
        this.createRid = createRid;
        this.lookPrivate = lookPrivate;
        // addData();
    }

    public void refreshData() {
        addData();
    }

    private ClickTreeView testView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.fragment_tree, null);
        mContext = getContext();
        initView();
        addListener();
        addData();
        return mInflate;
    }
    private void initView() {
        mTreeView = mInflate.findViewById(R.id.treeView);
    }

    private void addListener() {
        mTreeView.setListener(TreeViewListener);
    }

    private void addData() {
        Bundle parameters = new Bundle();
        /**
         *如果id不为空的话说明是从搜索人物页面跳转过来的。这个时候是以某个人为最上面的那个。
         * 否则是全本。
         */
        if (flag) {
            if (TextUtils.isEmpty(id))
                parameters.putString("stemmaId", stemmaId);
            else parameters.putString("id", id);
            parameters.putString("url", Url.TREE_URL + "getClanTreeOne");
        } else {
            if (TextUtils.isEmpty(id))
                parameters.putString("familyId", stemmaId);
            else parameters.putString("id", id);
            parameters.putString("url", Url.TREE_URL + "getClanTreeTwo");
        }
        parameters.putString("flagSelf", flag + "");
        mPresenter.getTreeData(parameters, mContext);
    }

    TreeViewInterface TreeViewListener = new TreeViewInterface() {
        @Override
        public void delete(String id) {//删除成员
            Bundle parameters = new Bundle();
            parameters.putString("id", id);
            parameters.putString("token", XunGenApp.token);
            mPresenter.getDeleteData(parameters, mContext);
        }

        @Override
        public void expand(Tree tree) {//展开
            String pid;
            if (tree.type == 1) {
                pid = tree.getId();
            } else {
                pid = tree.getPid();
            }
            Bundle parameters = new Bundle();
            parameters.putSerializable("tree", tree);
            parameters.putString("pid", pid);
            parameters.putString("type", tree.type + "");
            parameters.putBoolean("flag", flag);
            if (!flag) {//展开家谱
                parameters.putString("url", Url.EXPAND_URL + "expandSearchByPidTwo");
            } else {//展开族谱
                parameters.putString("url", Url.EXPAND_URL + "expandSearchByPid");
            }
            parameters.putString("flagSelf", flag + "");
            mPresenter.getExpandData(parameters, mContext);
        }
    };
    private Tree dataResurce;

    @Override
    public void getTreeSuccess(String result) {
        Gson gson = GsonUtils.getGson();
        TreeBean treeBean = gson.fromJson(result, TreeBean.class);
        dataResurce = treeBean.getData();
        if (dataResurce == null) {
            Intent intent = new Intent();
            ((Activity) mContext).setResult(222, intent);
            ((Activity) mContext).finish();
            return;
        }
        mTreeView.initData(dataResurce, createRid, flag, lookPrivate);
    }

    @Override
    public void getTreeError(String error) {

    }

    @Override
    public void getExpandSuccess(Bundle bundle) {
        String result = bundle.getString("result");
        String type = bundle.getString("type");
        Tree tree = (Tree) bundle.getSerializable("tree");//点击展开的节点
        String id = tree.getId();

        Gson gson = GsonUtils.getGson();

        if ("1".equals(type)) {//向下展开获取到的数据
            ExpandBean expandBean = gson.fromJson(result, ExpandBean.class);
            List<Tree> data = expandBean.getData();
            if (data != null && data.size() > 0) {
                tree.setFlag("0");
                tree.setChilds(data);
                mTreeView.initLocation(dataResurce);
                mTreeView.initData(dataResurce, createRid, flag, lookPrivate);
            }
        } else {//向上展开获取到的数据
            TreeBean treeBean = gson.fromJson(result, TreeBean.class);
            dataResurce = treeBean.getData();
            for (int i = 0; i < dataResurce.getChilds().size(); i++) {
                String id1 = dataResurce.getChilds().get(i).getId();
                if (!TextUtils.isEmpty(id) && id.equals(id1)) {
                    dataResurce.getChilds().get(i).setChilds(tree.getChilds());
                    dataResurce.getChilds().get(i).setFlag("0");
                    break;
                }
            }
            mTreeView.initLocation(dataResurce);
            mTreeView.initData(dataResurce, createRid, flag, lookPrivate);
        }
    }

    @Override
    public void getExpandError(String error) {

    }

    @Override
    public void getDeleteSuccess(String result) {
        mDeleteListener.callBack();
        addData();//刷新数据
    }

    @Override
    public void getDeleteError(String error) {

    }

    private com.ruihuo.ixungen.geninterface.CallBackInterface mDeleteListener;

    public void setDeleteListener(com.ruihuo.ixungen.geninterface.CallBackInterface mDeleteListener) {
        this.mDeleteListener = mDeleteListener;
    }
}
