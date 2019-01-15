package com.ruihuo.ixungen.activity.merchant;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.utils.PhotoUtils;
import com.ruihuo.ixungen.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomPhotoActivity extends AppCompatActivity {
    private Context mContext;
    private ImageView mImageBack;
    private TextView mText_title;
    private TextView mEdit;
    private ImageView mNo_data;
    private GridView mGridview;
    private List<String> imgList;
    private PhotoAdapter mAdapter;
    private PhotoUtils photoUtils;
    private HashMap<Integer, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shops_photo);
        mContext = this;
        imgList = (List<String>) getIntent().getSerializableExtra("imgList");
        initView();
        addListener();
        addData();
    }

    private void initView() {
        mImageBack = (ImageView) findViewById(R.id.image_titlebar_back);
        mText_title = (TextView) findViewById(R.id.text_title);
        mEdit = (TextView) findViewById(R.id.edit);
        mNo_data = (ImageView) findViewById(R.id.no_data);
        mGridview = (GridView) findViewById(R.id.gridview);
        photoUtils = new PhotoUtils(mContext);
    }

    private void addData() {
        for (int i = 0; i < imgList.size(); i++) {
            Log.e("图片链接", i + "个" + imgList.get(i));
        }
        if (imgList == null || imgList.size() == 0) {
            mNo_data.setVisibility(View.VISIBLE);
        } else {
            mNo_data.setVisibility(View.GONE);
            mAdapter = new PhotoAdapter(imgList, mContext);
            mGridview.setAdapter(mAdapter);
        }
    }

    private boolean isEditor = true;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("imgList", (Serializable) imgList);
            intent.putExtras(bundle);
            setResult(611, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void addListener() {
        mImageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("imgList", (Serializable) imgList);
                intent.putExtras(bundle);
                setResult(611, intent);
                finish();
            }
        });
        mEdit.setOnClickListener(EditorListener);
        mGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (isEditor) {
                    if (position == 0) {
                        //1是头像，需要裁剪。2是原图
                        photoUtils.setMode(2);
                        photoUtils.showDialog();
                    } else {
                        Intent intent = new Intent(mContext, ShopGalleryActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("imgList", (Serializable) imgList);
                        bundle.putInt("position", position-1);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                } else {
                    ImageView imageView = (ImageView) view.findViewById(R.id.check);
                    if (position != 0) {
                        if (map.containsKey(position)) {
                            imageView.setImageResource(R.drawable.unselect);
                            map.remove(position);
                        } else {
                            imageView.setImageResource(R.drawable.select);
                            map.put(position, imgList.get(position));
                        }
                    }
                }
            }
        });
    }

    View.OnClickListener EditorListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isEditor) {
                isEditor = false;
                map.clear();
                mEdit.setText("删除");
                mAdapter.setFlag(true);
                mAdapter.notifyDataSetChanged();
            } else {
                if (map.size() == 0) {
                    ToastUtils.toast(mContext, "未选中删除项");
                } else {
                    for (Map.Entry<Integer, String> entry : map.entrySet()) {
                        String value = entry.getValue();
                        Log.e("删除", value);
                        for (int i = 0; i < imgList.size(); i++) {
                            if (value.equals(imgList.get(i))) {
                                imgList.remove(i);
                            }
                        }
                    }
                    isEditor = true;
                    mEdit.setText("编辑");
                    mAdapter.setFlag(false);
                    mAdapter.notifyDataSetChanged();
                }

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoUtils.REQUEST_CODE_TAKING_PHOTO:
                    //拍照的结果
                    List<String> pathList1 = new ArrayList<>();
                    pathList1.add(PhotoUtils.photoPath);
                    imgList.addAll(pathList1);
                    mAdapter.notifyDataSetChanged();
                    break;
                case PhotoUtils.ACTIVITY_REQUEST_SELECT_PHOTO:
                    //相册选取的结果
                    List<String> pathList = (List<String>) data.getSerializableExtra("data");
                    imgList.addAll(pathList);
                    mAdapter.notifyDataSetChanged();
                    break;
            }

        }
    }
}
