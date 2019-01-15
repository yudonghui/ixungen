package com.ruihuo.ixungen.activity.merchant;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.XunGenApp;
import com.ruihuo.ixungen.album.PickOrTakeImageActivity;
import com.ruihuo.ixungen.common.ConstantNum;
import com.ruihuo.ixungen.geninterface.CallBackPositionInterface;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.ItemClickListener;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;
import com.ruihuo.ixungen.utils.SDCardUtils;
import com.ruihuo.ixungen.utils.dialog.LoadingDialogUtils;
import com.ydh.refresh_layout.SmartRefreshLayout;
import com.ydh.refresh_layout.api.RefreshLayout;
import com.ydh.refresh_layout.listener.OnLoadMoreListener;
import com.ydh.refresh_layout.listener.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.app.Activity.RESULT_OK;
import static com.ruihuo.ixungen.utils.PhotoUtils.ACTIVITY_REQUEST_SELECT_PHOTO;
import static com.ruihuo.ixungen.utils.PhotoUtils.IMAGE_SAVE_DIR;
import static com.ruihuo.ixungen.utils.PhotoUtils.PERMISSIONS_REQUEST_PHOTO;
import static com.ruihuo.ixungen.utils.PhotoUtils.REQUEST_CODE_TAKING_PHOTO;

/**
 * @author yudonghui
 * @date 2017/8/3
 * @describe May the Buddha bless bug-free!!!
 */
public class PhotoFragment extends Fragment {
    private int mode;
    private View view;
    private Context mContext;
    private ImageView mNo_data;
    private SmartRefreshLayout mRefresh;
    private GridView mGridview;
    private TextView mCamera;
    private TextView mPhoto;
    private Button mCancel;
    private LinearLayout mLl_editor;
    private TextView mCancelDelete;
    private TextView mDelete;
    // private PopupWindow popupWindow;
    private View inflate;
    private Dialog mDialog;
    private Window window;
    private List<String> imgList = new ArrayList<>();
    private ShopsPhotoFormAdapter mAdapter;
    private HashMap<Integer, ShopsPhotoFormBean.DataBean> map = new HashMap<>();
    private String shopId;
    private String type;//1-餐饮；2-酒店
    private LoadingDialogUtils loadingDialogUtils;
    private List<ShopsPhotoFormBean.DataBean> dataList = new ArrayList<>();
    private ShopsPhotoFormBean.DataBean databean;
    private boolean isMerchant;//true 商家，false普通用户
    private CallBackPositionInterface mRefreshData;//添加照片刷新数据用。

    public void setData(int mode, String shopId, boolean isMerchant, CallBackPositionInterface mRefreshData) {
        this.mode = mode;
        this.shopId = shopId;
        this.isMerchant = isMerchant;
        this.mRefreshData = mRefreshData;
        if (mode == ConstantNum.FOOD_ALL || mode == ConstantNum.FOOD_CP || mode == ConstantNum.FOOD_HJ) {
            type = "1";
        } else if (mode == ConstantNum.TOUR_ALL || mode == ConstantNum.TOUR_FJ || mode == ConstantNum.TOUR_ZB)
            type = "4";
        else type = "2";
        dataList.clear();
        if (mode != ConstantNum.FOOD_ALL && mode != ConstantNum.HOTEL_ALL && mode != ConstantNum.TOUR_ALL && isMerchant)
            dataList.add(databean);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_photo_form, null);
        mContext = getContext();
        databean = new ShopsPhotoFormBean.DataBean();
        initView();
        dataList.clear();
        if (mode != ConstantNum.FOOD_ALL && mode != ConstantNum.HOTEL_ALL && mode != ConstantNum.TOUR_ALL && isMerchant) {
            dataList.add(databean);
            limit = 15;
        }
        mAdapter = new ShopsPhotoFormAdapter(dataList, mContext, mode, isMerchant);
        mGridview.setAdapter(mAdapter);
        addData();
        addListener();
        return view;
    }

    private void initView() {
        mRefresh = (SmartRefreshLayout) view.findViewById(R.id.refresh);
        mNo_data = (ImageView) view.findViewById(R.id.no_data);
        mGridview = (GridView) view.findViewById(R.id.gridview);
        mLl_editor = (LinearLayout) view.findViewById(R.id.ll_editor);
        mCancelDelete = (TextView) view.findViewById(R.id.cancel);
        mDelete = (TextView) view.findViewById(R.id.delete);

        inflate = View.inflate(mContext, R.layout.photo_popup, null);
        mCamera = (TextView) inflate.findViewById(R.id.buttonCamera);
        mPhoto = (TextView) inflate.findViewById(R.id.buttonPhoto_selector);
        mCancel = (Button) inflate.findViewById(R.id.buttoncancle);
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        window = mDialog.getWindow();
        mDialog.setContentView(inflate);
        File file = new File(IMAGE_SAVE_DIR);
        if (!file.exists()) {
            file.mkdirs();
        }
        mHttp = HttpUtilsManager.getInstance(mContext);
    }

    private boolean isEditor = true;

    private void addListener() {
        //拍照
        mCamera.setOnClickListener(CameraListener);
        //从相册中选取
        mPhoto.setOnClickListener(PhotoListener);
        //取消
        mCancel.setOnClickListener(CancelListener);

        mCancelDelete.setOnClickListener(CancelDeleteListener);
        mDelete.setOnClickListener(DeleteListener);
        mAdapter.setOnItemListener(new ItemClickListener() {
            @Override
            public void onLongClick(View v, int position) {
                if (!isMerchant) return;
                if (isEditor) {
                    isEditor = false;//编辑状态
                    map.clear();
                    mAdapter.setFlag(true);
                    mAdapter.notifyDataSetChanged();
                    mLl_editor.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onClick(View v, int position) {
                if (isEditor) {
                    if (position == 0 && mode != ConstantNum.FOOD_ALL && mode != ConstantNum.HOTEL_ALL && mode != ConstantNum.TOUR_ALL && isMerchant) {
                        mDialog.show();
                        window.setGravity(Gravity.BOTTOM);
                    } else {
                        Intent intent = new Intent(mContext, ShopGalleryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("dataList", (Serializable) dataList);
                        if (mode == ConstantNum.FOOD_ALL || mode == ConstantNum.HOTEL_ALL || mode == ConstantNum.TOUR_ALL)
                            bundle.putInt("position", position);
                        else bundle.putInt("position", position - 1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {
                    ImageView imageView = (ImageView) v.findViewById(R.id.check);
                    if (position != 0 || mode == ConstantNum.HOTEL_ALL || mode == ConstantNum.FOOD_ALL || mode == ConstantNum.TOUR_ALL || !isMerchant) {
                        if (map.containsKey(position)) {
                            imageView.setImageResource(R.drawable.unselect);
                            map.remove(position);
                        } else {
                            imageView.setImageResource(R.drawable.select);
                            map.put(position, dataList.get(position));
                        }
                    }
                }
            }
        });
        mRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                dataList.clear();
                page = 1;
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

    public void setEditor() {

    }

    private int limit = 16;//全部的时候限制是16偶数，否则15奇数。为了一页正好对齐
    private int page = 1;
    private int totalPage = 1;
    private HttpInterface mHttp;

    private void addData() {
        final LoadingDialogUtils loadingDialogUtils = new LoadingDialogUtils(mContext);
        Bundle params = new Bundle();
        params.putString("limit", limit + "");
        params.putString("page", page + "");
        params.putString("shopId", shopId);
        params.putString("type", type);//1-餐饮；2-酒店 ；4-旅游
        if (mode != ConstantNum.FOOD_ALL && mode != ConstantNum.HOTEL_ALL && mode != ConstantNum.TOUR_ALL)
            params.putString("albumId", mode + "");//相册编号，查全部相册时不传此参数
        mHttp.get(Url.SHOP_PHOTO_FORM, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
                Gson gson = GsonUtils.getGson();
                ShopsPhotoFormBean shopsPhotoFormBean = gson.fromJson(result, ShopsPhotoFormBean.class);
                totalPage = shopsPhotoFormBean.getTotalPage();
                dataList.addAll(shopsPhotoFormBean.getData());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
                mRefresh.finishRefresh();
                mRefresh.finishLoadMore();
            }
        });
    }

    public void refreshData() {
        dataList.clear();
        page = 1;
        if (mode != ConstantNum.FOOD_ALL && mode != ConstantNum.HOTEL_ALL && mode != ConstantNum.TOUR_ALL && isMerchant)
            dataList.add(databean);
        addData();
    }

    View.OnClickListener CancelDeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mLl_editor.setVisibility(View.GONE);
            isEditor = true;
            mAdapter.setFlag(false);
            mAdapter.notifyDataSetChanged();
        }
    };
    private StringBuilder ids;//要删除的图片编号
    View.OnClickListener DeleteListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (map.size() == 0) {
                Toast.makeText(mContext, "请选中要删除的图片", Toast.LENGTH_SHORT).show();
                return;
            }
            ids = new StringBuilder();
            boolean flag = true;
            for (Map.Entry<Integer, ShopsPhotoFormBean.DataBean> entry : map.entrySet()) {
                ShopsPhotoFormBean.DataBean value = entry.getValue();
                String id = value.getId();
                if (flag)
                    ids.append(id);
                else ids.append("," + id);
                flag = false;
            }
            Bundle params = new Bundle();
            params.putString("token", XunGenApp.token);
            params.putString("shopId", shopId);
            params.putString("ids", String.valueOf(ids));
            mHttp.post(Url.DELETE_SHOP_PHOTO, params, new JsonInterface() {
                @Override
                public void onSuccess(String result) {
                    mLl_editor.setVisibility(View.GONE);
                    isEditor = true;
                    mAdapter.setFlag(false);
                    map.clear();
                    dataList.clear();
                    if (mode != ConstantNum.FOOD_ALL && mode != ConstantNum.HOTEL_ALL && mode != ConstantNum.TOUR_ALL && isMerchant)
                        dataList.add(databean);
                    addData();
                    mRefreshData.callBack(mode);//删除成功后刷新别的栏目的数据
                }

                @Override
                public void onError(String message) {

                }
            });

        }
    };
    private String photoPath;
    //拍照
    View.OnClickListener CameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_PHOTO);
            } else {
                String mUUID = UUID.randomUUID().toString();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                photoPath = SDCardUtils.getStorageDirectory() + mUUID + ".jpg";
                File file = new File(photoPath);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //添加这一句表示对目标应用临时授权该Uri所代表的文件
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    //通过FileProvider创建一个content类型的Uri
                    Uri mUri = FileProvider.getUriForFile(mContext, "com.ruihuo.ixungen.FileProvider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                } else {
                    Uri mUri = Uri.fromFile(file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
                }
                startActivityForResult(intent, REQUEST_CODE_TAKING_PHOTO);
            }
        }
    };
    //从相册中选取
    View.OnClickListener PhotoListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
            Intent intent = new Intent(mContext, PickOrTakeImageActivity.class);
            startActivityForResult(intent, ACTIVITY_REQUEST_SELECT_PHOTO);
        }
    };
    //取消
    View.OnClickListener CancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mDialog.dismiss();
        }
    };

    /**
     * 上传图片，根据图片路径一个一个上传后得到 图片网上链接，拼接到一起，
     * 作为一个参数上传。
     */
    private void updateImg() {
        loadingDialogUtils = new LoadingDialogUtils(mContext);
        for (int i = 0; i < imgList.size(); i++) {
            getImgUrl(imgList.get(i));
        }
    }

    private int n = 0;
    private StringBuilder imgs = new StringBuilder();

    private void getImgUrl(String path) {
        File file = new File(path);
        Luban.get(mContext)
                .load(file)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        RequestParams entity = new RequestParams(Url.PHOTO_UPLOAD);
                        entity.addBodyParameter("token", XunGenApp.token);
                        entity.addBodyParameter("image", file);
                        x.http().post(entity, new Callback.CommonCallback<String>() {
                            @Override
                            public void onSuccess(String result) {
                                try {
                                    JSONObject jsonObject = new JSONObject(result);
                                    String fileUrl = jsonObject.getString("fid");
                                    if (n == 0) imgs.append(Url.PHOTO_URL + fileUrl);
                                    else imgs.append(";" + Url.PHOTO_URL + fileUrl);
                                    n++;
                                    if (n == imgList.size()) {
                                        addImage();//把拼接好的图片链接上传
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    loadingDialogUtils.setDimiss();
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                Log.e("返回错误", ex.getMessage());
                                loadingDialogUtils.setDimiss();
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {

                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable e) {
                        loadingDialogUtils.setDimiss();
                    }
                }).launch();

    }

    private void addImage() {
        Bundle params = new Bundle();
        params.putString("token", XunGenApp.token);
        params.putString("shopId", shopId);
        params.putString("albumId", mode + "");
        params.putString("imgs", String.valueOf(imgs));
        mHttp.post(Url.SHOP_ADDIMAGE, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                loadingDialogUtils.setDimiss();
                imgList.clear();//清空图片的路径集合。
                dataList.clear();
                if (mode != ConstantNum.FOOD_ALL && mode != ConstantNum.HOTEL_ALL && mode != ConstantNum.TOUR_ALL && isMerchant)
                    dataList.add(databean);
                addData();
                mRefreshData.callBack(mode);
            }

            @Override
            public void onError(String message) {
                loadingDialogUtils.setDimiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_TAKING_PHOTO:
                    //拍照的结果
                    imgList.add(photoPath);
                    updateImg();
                    break;
                case ACTIVITY_REQUEST_SELECT_PHOTO:
                    //相册选取的结果
                    List<String> pathList = (List<String>) data.getSerializableExtra("data");
                    imgList.addAll(pathList);
                    updateImg();
                    break;
            }

        }
    }
}
