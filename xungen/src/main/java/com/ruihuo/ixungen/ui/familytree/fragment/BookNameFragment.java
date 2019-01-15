package com.ruihuo.ixungen.ui.familytree.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.Url;
import com.ruihuo.ixungen.common.NetWorkData;
import com.ruihuo.ixungen.geninterface.HttpInterface;
import com.ruihuo.ixungen.geninterface.JsonInterface;
import com.ruihuo.ixungen.ui.familytree.bean.StemmaDetailBean;
import com.ruihuo.ixungen.utils.GsonUtils;
import com.ruihuo.ixungen.utils.HttpUtilsManager;

/**
 * @author yudonghui
 * @date 2017/11/2
 * @describe May the Buddha bless bug-free!!!
 */
public class BookNameFragment extends Fragment {
    private View mInflate;
    private Context mContext;
    private TextView mBookCoverAddress;
    private TextView mBookCoverName;
    private TextView mBookCoverRemark;
    private int vpPage;
    private String stemmaId;
    private boolean flag;
    private CallBackListener mListener;

    public void setVpPage(int vpPage) {
        this.vpPage = vpPage;
    }

    public void setData(boolean flag, String stemmaId, CallBackListener mListener) {
        this.flag = flag;
        this.stemmaId = stemmaId;
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mInflate = inflater.inflate(R.layout.book_name, null);
        mContext = getContext();
        initView();
        addData();
        return mInflate;
    }

    private void initView() {
        mBookCoverAddress = (TextView) mInflate.findViewById(R.id.address);
        mBookCoverName = (TextView) mInflate.findViewById(R.id.stemmaName);
        mBookCoverRemark = (TextView) mInflate.findViewById(R.id.remark);
    }

    private void addData() {
        HttpInterface mHttp = HttpUtilsManager.getInstance(mContext);
        Bundle params = new Bundle();
        String url;
        if (flag)
            url = Url.STEMMA_SYSTEM_DETAIL;
        else url = Url.STEMMA_APP_DETAIL;
        params.putString("id", stemmaId);
        params.putString("flagSelf", flag + "");
        mHttp.getRSA(url, params, new JsonInterface() {
            @Override
            public void onSuccess(String result) {
                Gson gson = GsonUtils.getGson();
                StemmaDetailBean stemmaDetailBean = gson.fromJson(result, StemmaDetailBean.class);
                StemmaDetailBean.DataBean dataBean = stemmaDetailBean.getData();
                String name = dataBean.getName();
                String region = dataBean.getRegion();
                String surname_introduce = dataBean.getSurname_introduce();
                String family_instruction = dataBean.getFamily_instruction();
                String family_celebrity = dataBean.getFamily_celebrity();
                mListener.callBack(surname_introduce, family_instruction, family_celebrity);
                setTextSize(name);
                mBookCoverName.setText(TextUtils.isEmpty(name) ? "--" : name);
                if (flag)
                    new NetWorkData().getAddress(mContext, region, new NetWorkData.AddressInterface() {
                        @Override
                        public void callBack(String result) {
                            mBookCoverAddress.setText(TextUtils.isEmpty(result) ? "--" : result);
                        }
                    });
                else mBookCoverAddress.setText(TextUtils.isEmpty(region) ? "--" : region);
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void setTextSize(String name) {
        if (name == null) return;
        switch (name.length()) {
            case 1:
                mBookCoverName.setTextSize(34);
            case 2:
                mBookCoverName.setTextSize(34);
            case 3:
                mBookCoverName.setTextSize(34);
            case 4:
                mBookCoverName.setTextSize(34);
            case 5:
                mBookCoverName.setTextSize(23);
                break;
            case 6:
                mBookCoverName.setTextSize(20);
                break;
            case 7:
                mBookCoverName.setTextSize(18);
                break;
            case 8:
                mBookCoverName.setTextSize(16);
                break;
            case 9:
                mBookCoverName.setTextSize(14);
                break;
            case 10:
                mBookCoverName.setTextSize(12);
                break;
            case 11:
                mBookCoverName.setTextSize(12);
                break;
            default:
                mBookCoverName.setTextSize(12);
                break;
        }
    }

    public interface CallBackListener {
        void callBack(String surnameIntroduce, String familyInstruction, String familyCelebrity);
    }
}
