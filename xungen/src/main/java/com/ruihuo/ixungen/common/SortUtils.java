package com.ruihuo.ixungen.common;

import com.ruihuo.ixungen.utils.NumberFormatUtil;

/**
 * @author yudonghui
 * @date 2017/11/2
 * @describe May the Buddha bless bug-free!!!
 */
public class SortUtils {
    /**
     * @param sort 1排行老大 2排行老二...
     */
    public static String getSort(int sort, String sex) {
        if (sort == 1) {
            if ("1".equals(sex)) return "长子";
            else if ("2".equals(sex)) return "长女";
            else return "老大";
        } else if (sort == 2) {
            if ("1".equals(sex)) return "次子";
            else if ("2".equals(sex)) return "次女";
            else return "老二";
        } else {
            if ("1".equals(sex)) return NumberFormatUtil.formatInteger(sort) + "子";
            else if ("2".equals(sex)) return NumberFormatUtil.formatInteger(sort) + "女";
            else return "老" + NumberFormatUtil.formatInteger(sort);
        }
    }
}
