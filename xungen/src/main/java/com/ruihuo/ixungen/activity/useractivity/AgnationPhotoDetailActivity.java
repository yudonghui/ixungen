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
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.activity.GalleryActivity;
import com.ruihuo.ixungen.adapter.PhotoFormDetailAdapter;
import com.ruihuo.ixungen.entity.PhotoDetailBean;
import com.ruihuo.ixungen.geninterface.CallBackInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.AgnationPhotoUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ruihuo.ixungen.view.TitleBar;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ author yudonghui
 * @ date 2017/4/11
 * @ describe May the Buddha bless bug-free！！
 */
public class AgnationPhotoDetailActivity extends AppCompatActivity {
    private TitleBar mTitleBar;
    private ImageView mNoData;
    private SmartRefreshLayout mRefresh;
    private GridView mGridview;
    private TextView mDeletePhoto;
    private Context mContext;
    //private String imageUrl;
    private String presidentId;
    private String associationId;
    private PhotoFormDetailAdapter mAdapter;
    private List<PhotoDetailBean.DataBean> photoList = new ArrayList<>();
    private String photoId;
    private String photoName;
    private String photoRemark;
    private PhotoUtils photoUtils;
    private int limit = 30;
    private int page = 1;
    private int totalPage = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agnation_photo_detail);
        mContext = this;
        Intent intent = getIntent();
        //imageUrl = intent.getStringExtra("imageUrl");
        presidentId = intent.getStringExtra("presidentId");
        associationId = intent.getStringExtra("associationId");
        photoId = intent.getStringExtra("photoId");
        photoName = intent.getStringExtra("photoName");
        photoRemark = intent.getStringExtra("photoRemark");

        initView();
        addData();
        mAdapter = new PhotoFormDetailAdapter(photoList, mContext, presidentId);
        mAdapter.setFlag(false);
        mGridview.setAdapter(mAdapter);
        addListener();
    }

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("albumId", photoId);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.AGNATION_PHOTO_DETAIL_URL, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                PhotoDetailBean photoDetailBean = gson.fromJson(result, PhotoDetailBean.class);
                photoList.addAll(photoDetailBean.getData());
                mAdapter.notifyDataSetChanged();
                if (photoList.size() == 0) mNoData.setVisibility(View.VISIBLE);
                else mNoData.setVisibility(View.GONE);
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
            }
        });
    }

    private void initView() {
        mTitleBar = (TitleBar) findViewById(R.id.Photo_detail_titlebar);
        mTitleBar.setTitle("宗亲相册");
        mNoData = (ImageView) findViewById(R.id.no_data);
        mRefresh = (SmartRefreshLayout) findViewById(R.id.refresh);
        mGridview = (GridView) findViewById(R.id.gridview);

        mDeletePhoto = (TextView) findViewById(R.id.delete_photo);
        photoUtils = new PhotoUtils(mContext);
        if ((XunGenApp.rid).equals(presidentId)) {
            mTitleBar.mTextRegister.setText("编辑");
            mTitleBar.mTextRegister.setVisibility(View.VISIBLE);
            //会长有添加图片的权限
            PhotoDetailBean.DataBean dataBean = new PhotoDetailBean.DataBean();
            photoList.add(dataBean);
        } else {
            mTitleBar.mTextRegister.setVisibility(View.GONE);
        }
    }

    private boolean isEditor = true;
    //存放点击选中的条目
    private HashMap<Integer, PhotoDetailBean.DataBean> map = new HashMap<>();

    private void addListener() {
        photoUtils.setCallBack(new CallBackInterface() {
            @Override
            public void callBack() {
                //添加图片成功的回调
                // ToastUtils.toast(mContext, "添加图片成功，请返回查看");
                photoList.clear();
                page = 1;
                if ((XunGenApp.rid).equals(presidentId)) {
                    //会长有添加图片的权限
                    PhotoDetailBean.DataBean dataBean = new PhotoDetailBean.DataBean();
                    photoList.add(dataBean);
                }
                addData();
            }
        });
        mTitleBar.mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mTitleBar.mTextRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEditor) {
                    AgnationPhotoUtils agnationPhotoUtils = new AgnationPhotoUtils(mContext);
                    agnationPhotoUtils.setDeletePhoto(new CallBackInterface() {
                        @Override
                        public void callBack() {
                            isEditor = false;
                            map.clear();
                            mTitleBar.mTextRegister.setText("取消");
                            mDeletePhoto.setVisibility(View.VISIBLE);
                            mAdapter.setFlag(true);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    agnationPhotoUtils.setEditorPhoto(new CallBackInterface() {
                        @Override
                        public void callBack() {
                            Intent intent = new Intent(mContext, EditorPhotoInfoActivity.class);
                            intent.putExtra("associationId", associationId);
                            intent.putExtra("photoId", photoId);
                            intent.putExtra("photoName", photoName);
                            intent.putExtra("photoRemark", photoRemark);
                            startActivityForResult(intent, 203);
                        }
                    });
                } else {
                    isEditor = true;
                    mTitleBar.mTextRegister.setText("编辑");
                    mDeletePhoto.setVisibility(View.GONE);
                    mAdapter.setFlag(false);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEditor) {
                    if ((XunGenApp.rid).equals(presidentId) && position == 0) {
                        //添加相片
                        //1是头像，需要裁剪。2是原图
                        photoUtils.setMode(2);
                        photoUtils.showDialog();
                    } else {
                        Intent intent = new Intent(mContext, GalleryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("photoList", (Serializable) photoList);
                        bundle.putInt("position", position - 1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {
                    ImageView imageView = (ImageView) view.findViewById(R.id.check);
                    if (map.containsKey(position)) {
                        imageView.setImageResource(R.drawable.unselect);
                        map.remove(position);
                    } else {
                        imageView.setImageResource(R.drawable.select);
                        map.put(position, photoList.get(position));
                    }
                }
            }
        });
        mDeletePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map.size() == 0) {
                    ToastUtils.toast(mContext, "未选中删除项");
                } else {
                    final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
                    StringBuilder ids = new StringBuilder();
                    int i = 0;
                    //循环取出相册编号
                    for (Map.Entry<Integer, PhotoDetailBean.DataBean> entry : map.entrySet()) {
                        if (i == 0) {
                            ids.append(entry.getValue().getId());
                        } else ids.append("," + entry.getValue().getId());
                        i++;
                    }
                    HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
                    Bundle params = new Bundle();
                    params.putString("token", XunGenApp.token);
                    params.putString("albumId", photoId);
                    params.putString("ids", String.valueOf(ids));
                    mHttp.post(Url.DELETE_AGNATION_PHOTO_DETAIL_URL, params, new JsonInterface() {
                        @Override
                        public void onSuccess(String result) {
                            loadingDialogUtils.setDimiss();
                            //成功的时候更新数据源
                            mAdapter.setFlag(false);
                            mDeletePhoto.setVisibility(View.GONE);
                            mTitleBar.mTextRegister.setText("编辑");
                            isEditor = true;
                            //List<String> mList=new ArrayList<>();
                            for (Map.Entry<Integer, PhotoDetailBean.DataBean> entry : map.entrySet()) {
                                photoList.remove(entry.getValue());
                            }
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onError(String message) {
                            loadingDialogUtils.setDimiss();
                        }
                    });
                }
            }
        });
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                photoList.clear();
                addData();
            }
        });

        mRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (page < totalPage) {
                    page++;
                    addData();
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

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 203 && resultCode == 303) {
            //在编辑相册信息界面，删除相册。成功后。
            Intent intent = new Intent();
            setResult(304, intent);
            finish();
        } else if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoUtils.REQUEST_CODE_TAKING_PHOTO:
                    //拍照的结果
                    List<String> pathList1 = new ArrayList<>();
                    pathList1.add(PhotoUtils.photoPath);
                    photoUtils.uploadPhoto(pathList1, photoId);
                    break;
                case PhotoUtils.ACTIVITY_REQUEST_SELECT_PHOTO:
                    //相册选取的结果
                    List<String> pathList = (List<String>) data.getSerializableExtra("data");
                    photoUtils.uploadPhoto(pathList, photoId);
                    break;
            }

        }
    }
}
