package com.ruihuo.ixungen.utils;


import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Pattern;

public class StringUtil {

    /**
     * @param strs   string array
     * @param target find target, not null
     * @return -1 if no found
     */
    public static int indexOf(String[] strs, String target) {
        int i = 0;
        for (String str : strs) {
            if (target.equals(str))
                return i;
            ++i;
        }

        return -1;
    }

    /**
     * check the string is null or just contains the empty character
     *
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }

    public static boolean isNullOr0Size(String str) {
        return str == null || str.length() == 0;
    }

    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        int arraySize = array.length;
        int bufSize =
                (arraySize == 0 ? 0
                        : ((array[0] == null ? 16 : array[0].toString().length()) + separator
                        .length()) * arraySize);
        StringBuilder buf = new StringBuilder(bufSize);
        for (int i = 0; i < arraySize; i++) {
            if (i > 0) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }

    public static String join(String separator, Object... args) {
        return join(args, separator);
    }

    public static String getMD5(String str) {
        return Hashing.md5().hashString(str, Charsets.UTF_8).toString().toUpperCase();
    }

    public static int nthOccurrence(String str, char c, int n) {
        int pos = str.indexOf(c);
        while (--n > 0 && pos != -1) {
            pos = str.indexOf(c, pos + 1);
        }
        return pos;
    }

    public static boolean isLetterOrDigitOrUnderScore(String str) {
        for (int i = 0, len = str.length(); i < len; ++i) {
            char c = str.charAt(i);
            if (!((c <= 'z' && c >= 'a') || (c <= 'Z' && c >= 'A') || (c <= '9' && c >= '0') || c == '_')) {
                return false;
            }
        }

        return true;
    }

    //将字符串中的小写字母换成大写
    public static String toUpperCase(String str) {
        StringBuffer sb = new StringBuffer();
        if (str != null) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (Character.isLowerCase(c)) {
                    sb.append(Character.toUpperCase(c));
                } else {
                    sb.append(c);
                }
            }
        }

        return sb.toString();
    }

    //判断是否为手机号   合法true   不合法flase
    public static boolean isLegalPhone(String phone) {
        if (TextUtils.isEmpty(phone)) return false;
        String reg = "^1[3-9][0-9]{9}$";
        return Pattern.matches(reg, phone);
    }

    public static String trim(String source) {
        if (source != null) {
            return source.trim();
        }
        return null;
    }

    public static boolean isBlank(String str) {
        return (str == null) || ("".equalsIgnoreCase(str.trim()));
    }

    /*
     * if str is null or "" return null;
     */
    public static String getConent(String str) {
        String result = null;
        if ((str == null) || ("".equalsIgnoreCase(str.trim()))) {

        } else {
            result = str.trim();
        }
        return result;
    }

    public static int getInt(String source) {
        int result = -1;
        if (source == null || "".equalsIgnoreCase(source.trim())) {
            return result;
        }

        try {
            result = Integer.valueOf(source.trim());
        } catch (Exception e) {
        }
        return result;
    }

    public static long getLong(String source) {
        long result = -1;
        if (source == null || "".equalsIgnoreCase(source.trim())) {
            return result;
        }

        try {
            result = Long.valueOf(source.trim());
        } catch (Exception e) {
        }
        return result;
    }

    public static String generateRandString(Integer numOfChars) {
        String randString = "";
        String charPool = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();

        if (numOfChars == 4 || numOfChars == null) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < 4; i++) {
                sb.append(charPool.charAt(random.nextInt(charPool.length())));
            }
            randString = sb.toString();
        } else {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < numOfChars; i++) {
                sb.append(charPool.charAt(random.nextInt(10)));
            }
            randString = sb.toString();
        }

        return randString;
    }

    public static String getCheckcode(int type) throws Exception {
        // phone checkcode
        if (type == 1) {
            String phonecheckCode = StringUtil.generateRandString(6);
            return phonecheckCode;
        }
        // webpage checkcode
        else if (type == 2) {
            String checkCode = StringUtil.generateRandString(4);
            return checkCode;
        }
        // email checkcode
        else if (type == 3) {

        }
        return null;
    }

    //判断字符串是否全为数字
    public static boolean intConvertable(String str) {
        int temp = 0;
        try {
            temp = Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean longConvertable(String str) {
        Long temp = 0L;
        try {
            temp = Long.parseLong(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String convertObjectToString(Object object) {
        String rtn = "";
        if (object instanceof Integer) {
            rtn = String.valueOf(((Integer) object).intValue());
        } else if (object instanceof String) {
            rtn = (String) object;
        } else if (object instanceof Double) {
            rtn = String.valueOf(((Double) object).doubleValue());
        } else if (object instanceof BigDecimal) {
            rtn = String.valueOf(((BigDecimal) object).doubleValue());
        } else if (object instanceof Float) {
            rtn = String.valueOf(((Float) object).floatValue());
        } else if (object instanceof Long) {
            rtn = String.valueOf(((Long) object).longValue());
        } else if (object instanceof Boolean) {
            rtn = String.valueOf(((Boolean) object).booleanValue());
        } else if (object instanceof Date) {
            rtn = object.toString();
        }
        return rtn;
    }

    //判断密码是否合法(6到24位)
    public static boolean isLegalPassword(String pwd) {
        return pwd != null && pwd.length() >= 6 && pwd.length() <= 24;
    }

    //通过出生日期获取年龄
    public static int getAgeByBirthday(String birthday) {
        int age = 0;
        if (!TextUtils.isEmpty(birthday)) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String current = simpleDateFormat.format(new Date());
            String[] curren = current.split("\\-");
            String[] birthda = birthday.split("\\-");
            int year = Integer.parseInt(curren[0]) - Integer.parseInt(birthda[0]);
            int month = Integer.parseInt(curren[1]) - Integer.parseInt(birthda[1]);
            int day = Integer.parseInt(curren[2]) - Integer.parseInt(birthda[2]);
            if (month < 0) age = year - 1;
            else if (month > 0) age = year;
            else {
                if (day < 0) age = year - 1;
                else age = year;
            }

        }
        return age;
    }

    //地址是汉字还是数字
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[\\,\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 参数：maxLines 要限制的最大行数
     * 参数：content  指TextView中要显示的内容
     */
    public static void setMaxEcplise(final TextView mTextView, final int maxLines, final String content) {
        mTextView.setText(content);
        if (mTextView.getLineCount() > maxLines) {

            int lineEndIndex = mTextView.getLayout().getLineEnd(maxLines - 1);
            //下面这句代码中：我在项目中用数字3发现效果不好，改成1了
            String text = content.subSequence(0, lineEndIndex - 3) + "...";
            mTextView.setText(text);
        }
    }

    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }


    }

    public static double getDecimal2(float f) {
        BigDecimal bg = new BigDecimal(f);
        double f1 = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return f1;
    }
}