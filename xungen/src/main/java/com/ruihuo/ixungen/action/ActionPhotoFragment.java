package com.ruihuo.ixungen.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.utils.dialog.PhotoDialogUtils;
import com.ydh.refresh_layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/5/26
 * @describe May the Buddha bless bug-free!!!
 */
public class ActionPhotoFragment extends Fragment {
    private View view;
    private GridView mGridView;
    private ImageView mNoData;
    private Context mContext;
    private String rid;
    private int limit = 10;
    private int page = 1;
    List<ActionPhotoFormBean.DataBean> mList = new ArrayList<>();
    private ActionPhotoFormAdapter mAdapter;

    public void setData(String rid) {
        this.rid = rid;
    }

    private int totalPage = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo, null);
        mContext = getActivity();
        initView();
        addData();
        mAdapter = new ActionPhotoFormAdapter(mList, mContext);
        mGridView.setAdapter(mAdapter);
        addListener();
        return view;
    }


    private void initView() {
        mGridView = (GridView) view.findViewById(R.id.gridview);
        mNoData = (ImageView) view.findViewById(R.id.no_data);
       /* mXuanfu = (ImageButton) view.findViewById(R.id.xuanfu);
        popupWindowView = new PopupWindowView(mContext);*/
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("rid", rid);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.ACTION_PHOTO_FORM_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                ActionPhotoFormBean actionPhotoFormBean = gson.fromJson(result, ActionPhotoFormBean.class);
                List<ActionPhotoFormBean.DataBean> data = actionPhotoFormBean.getData();
                mList.addAll(data);
                if (mList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                if (mList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
            }
        });
    }

    private void addDataRefresh(final SmartRefreshLayout mRefresh) {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("rid", rid);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.ACTION_PHOTO_FORM_URL, params, new JsonInterface() {

            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                ActionPhotoFormBean actionPhotoFormBean = gson.fromJson(result, ActionPhotoFormBean.class);
                List<ActionPhotoFormBean.DataBean> data = actionPhotoFormBean.getData();
                totalPage = actionPhotoFormBean.getTotalPage();
                mList.addAll(data);
                if (mList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                if (mList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
            }
        });
    }

    private void addListener() {

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ActionPhotoDetailActivity.class);
                intent.putExtra("photoId", mList.get(position).getId());
                intent.putExtra("rid", rid);
                intent.putExtra("photoName", mList.get(position).getAlbum_name());
                intent.putExtra("address", mList.get(position).getAddress());
                startActivityForResult(intent, 222);
            }
        });
    }

    public void setXuanfu(PopupWindowView popupWindowView, ImageButton mXuanfu) {
        popupWindowView.setOneText("添加相册", new PopupWindowView.PopupWindowInterface() {
            @Override
            public void callBack() {
                PhotoDialogUtils photoDialogUtils = new PhotoDialogUtils(mContext);
                photoDialogUtils.setConfirm(new DialogEditInterface() {
                    @Override
                    public void callBack(String message) {
                        mList.clear();
                        addData();
                    }
                });
            }
        });
        popupWindowView.setTwoText("删除相册", new PopupWindowView.PopupWindowInterface() {
            @Override
            public void callBack() {
                Intent intent = new Intent(mContext, ActionPhotoFormActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("rid", rid);
                intent.putExtras(bundle);
                startActivityForResult(intent, 221);
            }
        });
        popupWindowView.showUp(mXuanfu);
    }

    public void setDownRefresh(SmartRefreshLayout mRefresh) {
        mList.clear();
        addDataRefresh(mRefresh);
    }

    public void setUpRefresh(final SmartRefreshLayout mRefresh) {
        if (page < totalPage) {
            page++;
            addDataRefresh(mRefresh);
        } else {
            mRefresh.postDelayed(new Runnable() {

                @Override
                public void run() {
                    Toast.makeText(mContext, "已经是最后一页", Toast.LENGTH_SHORT).show();
                    mRefresh.finishLoadMore();
                }
            }, 1000);
        }
    }
}
