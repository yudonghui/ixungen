package com.ruihuo.ixungen.action;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.DisplayUtilX;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.ToastUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.ListItemDecoration;
import com.ydh.refresh_layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ruihuo.ixungen.R.id.recyclerView;

/**
 * @author yudonghui
 * @date 2017/5/26
 * @describe May the Buddha bless bug-free!!!
 */
public class VideoFragment extends Fragment {
    private View view;
    private Context mContext;
    private ImageView mNoData;
    private SwipeMenuRecyclerView mRecyclerView;

    private ListViewSlideAdapter mAdapter;
    List<VideoFormBean.DataBean> mList = new ArrayList<>();

    private CallBackPositionInterface mCallBack;
    private int totalPage = 1;
    private int limit = 10;
    private int page = 1;
    private boolean haveEnoughSpace = false;
    private String rid;
    private static final int REQUEST_CAMERA = 600;
    private static final int REQUEST_READE = 601;//请求读取存储卡的权限申请。
    public static final int REQUEST_FILE = 6001;//打开系统文件选取视频的请求码；

    public void setData(CallBackPositionInterface mCallBack, String rid) {
        this.mCallBack = mCallBack;
        this.rid = rid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video, null);
        mContext = getActivity();
        long freeSpace = DisplayUtilX.getFreeSpace();
        haveEnoughSpace = !(freeSpace < 5242880);//TODO 检测剩余空间，限制是5M
        initView();
        addData();
        addListener();
        return view;
    }

    private void addData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("rid", rid);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.ACTION_VIDEO_FORM, params, new JsonInterface() {

            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                VideoFormBean videoFormBean = gson.fromJson(result, VideoFormBean.class);
                totalPage = videoFormBean.getTotalPage();
                mList.addAll(videoFormBean.getData());
                mAdapter.notifyDataSetChanged();
                if (mList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
            }

            @Override
            public void onError(String message) {
                if (mList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
            }
        });
    }

    private void addDataRefresh(final SmartRefreshLayout mRefresh) {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        params.putString("rid", rid);
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        mHttp.get(Url.ACTION_VIDEO_FORM, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                mRefresh.finishLoadMore();
                mRefresh.finishRefresh();
                Gson gson = GsonUtils.getGson();
                VideoFormBean videoFormBean = gson.fromJson(result, VideoFormBean.class);
                totalPage = videoFormBean.getTotalPage();
                mList.addAll(videoFormBean.getData());
                mAdapter.notifyDataSetChanged();
                if (mList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
            }

            @Override
            public void onError(String message) {
                mRefresh.finishLoadMore();
                mRefresh.finishRefresh();
                if (mList.size() == 0) {
                    mNoData.setVisibility(View.VISIBLE);
                } else mNoData.setVisibility(View.GONE);
            }
        });
    }

    private void initView() {
        mNoData = (ImageView) view.findViewById(R.id.no_data);
        mRecyclerView = (SwipeMenuRecyclerView) view.findViewById(recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new ListItemDecoration(ContextCompat.getColor(mContext, R.color.gray_box)));
        mRecyclerView.setSwipeItemClickListener(mItemClickListener); // Item点击。
        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        mAdapter = new ListViewSlideAdapter(mList, mContext, type);
        mRecyclerView.setAdapter(mAdapter);
        /*mXuanfu = (ImageButton) view.findViewById(R.id.xuanfu);
        popupWindowView = new PopupWindowView(mContext);*/
    }

    //存放点击选中的条目
    private HashMap<Integer, VideoFormBean.DataBean> map = new HashMap<>();

    private void addListener() {

    }

    private int type = 1;

    public void refreshData() {
        mList.clear();
        page = 1;
        addData();
    }

    public void setDownRefresh(SmartRefreshLayout mRefresh) {
        mList.clear();
        page = 1;
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

    private boolean isCheck = false;

    public void cancelData() {
        isCheck = false;
        mAdapter.setFlag(false);
        mAdapter.notifyDataSetChanged();
    }

    public void deleteData() {

        if (map.size() == 0) {
            ToastUtils.toast(mContext, "未选中删除项");
            isCheck = true;
            mCallBack.callBack(5);
        } else {
            isCheck = false;
            final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
            StringBuilder ids = new StringBuilder();
            int i = 0;
            //循环取出相册编号
            for (Map.Entry<Integer, VideoFormBean.DataBean> entry : map.entrySet()) {
                if (i == 0) {
                    ids.append(entry.getValue().getId());
                } else ids.append("," + entry.getValue().getId());
                i++;
            }
            HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("id", String.valueOf(ids));
            mHttp.post(Url.DELETE_VIDEO, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    loadingDialogUtils.setDimiss();
                    //成功的时候更新数据源
                    mAdapter.setFlag(false);
                    //List<String> mList=new ArrayList<>();
                    for (Map.Entry<Integer, VideoFormBean.DataBean> entry : map.entrySet()) {
                        mList.remove(entry.getValue());
                    }
                    map.clear();
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(String message) {
                    loadingDialogUtils.setDimiss();
                    mAdapter.setFlag(false);
                    mAdapter.notifyDataSetChanged();
                }
            });
        }
    }


    /**
     * Item点击监听。
     */
    private SwipeItemClickListener mItemClickListener = new SwipeItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            if (isCheck) {
                ImageView imageView = (ImageView) itemView.findViewById(R.id.flag);
                if (map.containsKey(position)) {
                    imageView.setImageResource(R.drawable.unselect);
                    map.remove(position);
                } else {
                    imageView.setImageResource(R.drawable.select);
                    map.put(position, mList.get(position));
                }
            } else {
                Intent intent = new Intent(mContext, VideoDetailActivity.class);
                intent.putExtra("videoId", mList.get(position).getId());
                intent.putExtra("avatar", mList.get(position).getAvatar());
                intent.putExtra("imgBg", mList.get(position).getImg());
                intent.putExtra("nikename", mList.get(position).getNikename());
                startActivity(intent);
            }
        }
    };
    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int adapterPosition = menuBridge.getAdapterPosition(); // RecyclerView的Item的position。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                map.put(adapterPosition, mList.get(adapterPosition));
                deleteData();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                //Toast.makeText(mContext, "list第" + adapterPosition + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT).show();
            }
        }
    };

    SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int i) {
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext)
                    .setBackgroundColor(Color.rgb(0xff, 0x2b, 0x30))
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(200)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
        }
    };

    public void setXuanfu(PopupWindowView popupWindowView, final ImageButton mXuanfu) {
        popupWindowView.setOneText("录制视频", new PopupWindowView.PopupWindowInterface() {
            @Override
            public void callBack() {
                if (haveEnoughSpace) {
                    checkCameraPermission();
                } else {
                    Toast.makeText(mContext, "剩余空间不够充足，请清理一下再试一次", Toast.LENGTH_SHORT).show();
                }
            }
        });
        popupWindowView.setTwoText("上传视频", new PopupWindowView.PopupWindowInterface() {
            @Override
            public void callBack() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READE);
                    } else {
                       /* Intent intent = new Intent();
                        intent.setType("video*//*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);*/
                        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        ((Activity) mContext).startActivityForResult(intent, REQUEST_FILE);
                    }
                } else {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    ((Activity) mContext).startActivityForResult(intent, REQUEST_FILE);
                }
            }
        });
        popupWindowView.setThreeText("批量删除", new PopupWindowView.PopupWindowInterface() {
            @Override
            public void callBack() {
                isCheck = true;
                mAdapter.setFlag(true);
                mAdapter.notifyDataSetChanged();
                mCallBack.callBack(5);
            }
        });
        popupWindowView.showUp(mXuanfu);
    }


    //视频录制需要的权限(相机，录音，外部存储)
    private String[] VIDEO_PERMISSION = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private List<String> NO_VIDEO_PERMISSION = new ArrayList<String>();

    /**
     * 检测摄像头权限，具备相关权限才能继续
     */
    private void checkCameraPermission() {
        NO_VIDEO_PERMISSION.clear();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < VIDEO_PERMISSION.length; i++) {
                if (ActivityCompat.checkSelfPermission(mContext, VIDEO_PERMISSION[i]) != PackageManager.PERMISSION_GRANTED) {
                    NO_VIDEO_PERMISSION.add(VIDEO_PERMISSION[i]);
                }
            }
            if (NO_VIDEO_PERMISSION.size() == 0) {
                Intent intent = new Intent(mContext, RecordVideoActivity.class);
                ((Activity) mContext).startActivityForResult(intent, 220);
            } else {
                VideoFragment.this.requestPermissions(NO_VIDEO_PERMISSION.toArray(new String[NO_VIDEO_PERMISSION.size()]), REQUEST_CAMERA);
            }
        } else {
            Intent intent = new Intent(mContext, RecordVideoActivity.class);
            ((Activity) mContext).startActivityForResult(intent, 220);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_CAMERA) {
            boolean flag = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    flag = true;
                } else {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Intent intent = new Intent(mContext, RecordVideoActivity.class);
                ((Activity) mContext).startActivityForResult(intent, 220);
            } else {
                Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_READE) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            ((Activity) mContext).startActivityForResult(intent, REQUEST_FILE);
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
