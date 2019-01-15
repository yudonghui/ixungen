package com.ruihuo.ixungen.ui.familytree.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.ui.familytree.ArabicToChineseUtils;
import com.ruihuo.ixungen.ui.familytree.bean.CatalagBean;

import java.util.List;

/**
 * @author yudonghui
 * @date 2017/10/31
 * @describe May the Buddha bless bug-free!!!
 */
public class CatalagAdapter extends BaseExpandableListAdapter {
    List<List<CatalagBean.DataBean>> dataList;
    private Context mContext;
    private LayoutInflater mInflater;
    private CatalogInterface mCallBack;

    public CatalagAdapter(List<List<CatalagBean.DataBean>> dataList, Context mContext, CatalogInterface mCallBack) {
        this.dataList = dataList;
        this.mCallBack = mCallBack;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getGroupCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return dataList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return dataList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return dataList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder mGroupHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_catalag, null);
            mGroupHolder = new GroupHolder();
            mGroupHolder.mTextView = (TextView) convertView.findViewById(R.id.level);
            convertView.setTag(mGroupHolder);
        } else {
            mGroupHolder = (GroupHolder) convertView.getTag();
        }
        String level_id = dataList.get(groupPosition).get(0).getLevel_id();
        if (!TextUtils.isEmpty(level_id))
            mGroupHolder.mTextView.setText("第" + ArabicToChineseUtils.foematInteger(Integer.parseInt(level_id)) + "世");
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_catalog_child, null);
            mViewHolder = new ViewHolder();
            mViewHolder.mName = (TextView) convertView.findViewById(R.id.name);
            mViewHolder.mPageNum = (TextView) convertView.findViewById(R.id.pageNum);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        CatalagBean.DataBean dataBean = dataList.get(groupPosition).get(childPosition);
        String name = dataBean.getName();
        int page = dataBean.getPage();
        mViewHolder.mName.setText(TextUtils.isEmpty(name) ? "--" : name);
        mViewHolder.mPageNum.setText("" + page);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallBack.callBack(groupPosition, childPosition);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    class ViewHolder {
        TextView mName;
        TextView mPageNum;
    }

    class GroupHolder {
        TextView mTextView;
    }

    public interface CatalogInterface {
        void callBack(int groupPosition, int childPosition);
    }
}
