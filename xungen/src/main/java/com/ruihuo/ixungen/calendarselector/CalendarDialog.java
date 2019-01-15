package com.ruihuo.ixungen.calendarselector;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ruihuo.ixungen.R;
import com.ruihuo.ixungen.calendars.DatePickerController;
import com.ruihuo.ixungen.calendars.DayPickerView;
import com.ruihuo.ixungen.calendars.SimpleMonthAdapter;
import com.ruihuo.ixungen.geninterface.DialogEditInterface;
import com.ruihuo.ixungen.utils.DateFormatUtils;
import com.ruihuo.ixungen.utils.DisplayUtilX;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yudonghui
 * @date 2017/8/7
 * @describe May the Buddha bless bug-free!!!
 */
public class CalendarDialog {
    private final Window window;
    private Context mContext;
    private View inflate;
    private DayPickerView mDayPV;
    private TextView mCancel;
    private TextView mTitle;
    private Dialog mDialog;
    private TextView mConfirm;
    private List<SimpleMonthAdapter.CalendarDay> days = new ArrayList<>();

    public CalendarDialog(final Context mContext, final DialogEditInterface mStringInterface) {
        this.mContext = mContext;
        //有关相机调用
        inflate = View.inflate(mContext, R.layout.calendar_selector, null);
        mDayPV = (DayPickerView) inflate.findViewById(R.id.dpv_calendar);
        mCancel = (TextView) inflate.findViewById(R.id.cancel);
        mTitle = (TextView) inflate.findViewById(R.id.title);
        mConfirm = (TextView) inflate.findViewById(R.id.confirm);
        mDialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        window = mDialog.getWindow();
        mDialog.setContentView(inflate);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        final DayPickerView.DataModel dataModel = new DayPickerView.DataModel();
        String s = DateFormatUtils.longToDate(System.currentTimeMillis());
        String[] split = s.split("\\-");
        dataModel.yearStart = Integer.parseInt(split[0]);
        dataModel.monthStart = Integer.parseInt(split[1]) - 1;
        dataModel.monthCount = 6;
        dataModel.leastDaysNum = 2;//至少选择几天
        dataModel.mostDaysNum = 100;//最多显示几天
        dataModel.defTag = "";
        mDayPV.setParameter(dataModel, new DatePickerController() {
            @Override
            public void onDayOfMonthSelected(SimpleMonthAdapter.CalendarDay calendarDay) {
                Log.e("入住日期", calendarDay.toString());
                mConfirm.setVisibility(View.GONE);
            }

            @Override
            public void onDateRangeSelected(List<SimpleMonthAdapter.CalendarDay> selectedDays) {
                days.clear();
                days.addAll(selectedDays);
                mConfirm.setVisibility(View.VISIBLE);
            }

            @Override
            public void alertSelectedFail(FailEven even) {
            }
        });
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (days.size() > 1) {
                    SimpleMonthAdapter.CalendarDay start = days.get(0);
                    int year = start.year;
                    int month = start.month + 1;
                    int day = start.day;
                    SimpleMonthAdapter.CalendarDay end = days.get(days.size() - 1);
                    int yearend = end.year;
                    int monthend = end.month + 1;
                    int dayend = end.day;
                    mStringInterface.callBack(year + "-" + month + "-" + day +
                            "*" + yearend + "-" + monthend + "-" + dayend + "*" + (days.size() - 1));
                    mDialog.dismiss();
                }
            }
        });
    }

    public void showDialog() {
        mDialog.show();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        //layoutParams.width = window.getWindowManager().getDefaultDisplay().getWidth();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = DisplayUtilX.getDisplayHeight() * 3 / 4;
        window.setAttributes(layoutParams);
        window.setGravity(Gravity.BOTTOM);
    }
}
