package com.ruihuo.ixungen.activity.useractivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.adapter.PhotoFormAdapter;
import com.ruihuo.ixungen.entity.PhotoFormBean;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.utils.dialog.PhotoDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ author yudonghui
 * @ date 2017/4/10
 * @ describe May the Buddha bless bug-free！！
 */
public class AgnationPhotoActivity extends AppCompatActivity {
    private SmartRefreshLayout mRefresh;
    private GridView mGridView;
    private ImageView mNoData;
    private Context mContext;
    private String presidentId;
    private String associationId;
    private TitleBar mTitleBar;
    private TextView mDeletePhoto;
    private PhotoFormAdapter mAdapter;
    private int limit = 20;
    private int page = 1;
    private int totalPage = 1;
    List<PhotoFormBean.DataBean> photoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agnation_photo);
        mContext = this;
        Intent intent = getIntent();
        presidentId = intent.getStringExtra("presidentId");
        associationId = intent.getStringExtra("associationId");
        initView();
        mAdapter = new PhotoFormAdapter(photoList, mContext, presidentId);
        mGridView.setAdapter(mAdapter);
        /*PhotoFormBean.DataBean dataBean = new PhotoFormBean.DataBean();
        photoList.add(dataBean);
        addData();*/
        addListener();
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.photo_titlebar);
        mTitleBar.setTitle("宗亲相册");
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
       /* */
        mGridView = (GridView) findViewById(R.id.gridview);
        mDeletePhoto = (TextView) findViewById(R.id.delete_photo);
        mNoData = (ImageView) findViewById(R.id.no_data);
    }

    private void addListener() {
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if ((XunGenApp.rid).equals(presidentId)) {
            mTitleBar.mTextRegister.setText("编辑");
            mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
            mTitleBar.mTextRegister.setOnClickListener(EditorListener);
        } else mTitleBar.mTextRegister.setVisibility(View.GONE);
        //删除相册
        mDeletePhoto.setOnClickListener(DeletePhotoListener);
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                photoList.clear();
                if ((XunGenApp.rid).equals(presidentId)) {
                    PhotoFormBean.DataBean dataBean = new PhotoFormBean.DataBean();
                    photoList.add(dataBean);
                }
                addData();
            }
        });
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 *isEditor true 右上角按钮显示是编辑
                 *         false 右上角按钮显示取消
                 *         当false的时候点击每个条目是选择状态，
                 *         当true 的时候是进行打开相册，看相册的具体内容
                 */
                if (isEditor) {
                    if ((XunGenApp.rid).equals(presidentId)) {
                        if (position == 0) {
                            PhotoDialogUtils photoDialogUtils = new PhotoDialogUtils(mContext);
                            photoDialogUtils.setConfirm(associationId, new DialogEditInterface() {
                                @Override
                                public void callBack(String message) {
                                    photoList.clear();
                                    PhotoFormBean.DataBean dataBean = new PhotoFormBean.DataBean();
                                    photoList.add(dataBean);
                                    addData();
                                }
                            });
                        } else {
                            Intent intent = new Intent(mContext, AgnationPhotoDetailActivity.class);
                            intent.putExtra("imageUrl", photoList.get(position).getImg_url());
                            intent.putExtra("presidentId", presidentId);
                            intent.putExtra("associationId", associationId);
                            intent.putExtra("photoId", photoList.get(position).getId());
                            intent.putExtra("photoName", photoList.get(position).getAlbum_name());
                            intent.putExtra("photoRemark", photoList.get(position).getRemark());
                            startActivityForResult(intent, 204);
                        }
                    } else {
                        Intent intent = new Intent(mContext, AgnationPhotoDetailActivity.class);
                        intent.putExtra("imageUrl", photoList.get(position).getImg_url());
                        intent.putExtra("presidentId", presidentId);
                        intent.putExtra("associationId", associationId);
                        intent.putExtra("photoId", photoList.get(position).getId());
                        intent.putExtra("photoName", photoList.get(position).getAlbum_name());
                        intent.putExtra("photoRemark", photoList.get(position).getRemark());
                        startActivityForResult(intent, 204);
                    }

                } else {
                    ImageView imageView = (ImageView) view.findViewById(R.id.check);
                    if (map.containsKey(position)) {
                        imageView.setImageResource(R.drawable.unselect);
                        map.remove(position);
                    } else {
                        imageView.setImageResource(R.drawable.select);
                        map.put(position, photoList.get(position).getId());
                    }

                }
            }
        });
    }

    private boolean isEditor = true;
    //存放点击选中的条目
    private HashMap<Integer, String> map = new HashMap<>();
    View.OnClickListener EditorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isEditor) {
                isEditor = false;
                map.clear();
                mTitleBar.mTextRegister.setText("取消");
                mDeletePhoto.setVisibility(View.VISIBLE);
                //true 显示编辑的小圆圈
                mAdapter.setFlag(true);
                mAdapter.notifyDataSetChanged();
            } else {
                isEditor = true;
                mTitleBar.mTextRegister.setText("编辑");
                mDeletePhoto.setVisibility(View.GONE);
                mAdapter.setFlag(false);
                mAdapter.notifyDataSetChanged();
            }
        }
    };
    View.OnClickListener DeletePhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (map.size() == 0) {
                ToastUtils.toast(mContext, "未选中删除项");
            } else {
                final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                StringBuilder id = new StringBuilder();
                int i = 0;
                //循环取出相册编号
                for (Map.Entry<Integer, String> entry : map.entrySet()) {
                    if (i == 0) {
                        id.append(entry.getValue());
                    } else id.append("," + entry.getValue());
                    i++;
                }
                HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                Bundle params = new Bundle();
                params.putString("token", XunGenApp.token);
                params.putString("associationId", associationId);
                params.putString("id", String.valueOf(id));
                mHttp.post(Url.DELETE_AGNATION_PHOTO_URL, params, new JsonInterface() {
                    @Override
                    public void onSuccess(String result) {
                        loadingDialogUtils.setDimiss();
                        //成功的时候更新数据源
                        mAdapter.setFlag(false);
                        mDeletePhoto.setVisibility(View.GONE);
                        mTitleBar.mTextRegister.setText("编辑");
                        isEditor = true;
                        photoList.clear();
                        if ((XunGenApp.rid).equals(presidentId)) {
                            PhotoFormBean.DataBean dataBean = new PhotoFormBean.DataBean();
                            photoList.add(dataBean);
                        }
                        addData();
                    }

                    @Override
                    public void onError(String message) {
                        loadingDialogUtils.setDimiss();
                    }
                });
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        photoList.clear();
        if ((XunGenApp.rid).equals(presidentId)) {
            PhotoFormBean.DataBean dataBean = new PhotoFormBean.DataBean();
            photoList.add(dataBean);
        }
        addData();
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("associationId", associationId);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.AGNATION_PHOTO_FORM_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishRefresh();
                loadingDialogUtils.setDimiss();
                Gson gson = GsonUtils.getGson();
                PhotoFormBean photoFormBean = gson.fromJson(result, PhotoFormBean.class);
                totalPage = photoFormBean.getTotalPage();
                photoList.addAll(photoFormBean.getData());
                mAdapter.notifyDataSetChanged();
                if (photoList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mNoData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 204 && resultCode == 304) {
            //编辑相册时候删除了相册，然后跳转到相册详情，然后再跳回到相册列表，重新刷新数据
            photoList.clear();

            PhotoFormBean.DataBean dataBean = new PhotoFormBean.DataBean();
            photoList.add(dataBean);

            addData();
        }
    }
}
