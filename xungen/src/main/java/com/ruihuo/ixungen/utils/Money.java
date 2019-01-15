package com.ruihuo.ixungen.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author yudonghui
 * @date 2017/11/30
 * @describe May the Buddha bless bug-free!!!
 */
public class Money {
    public static void setPricePoint(final EditText editText) {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    int num = countInnerStr(s.toString(), ".");
                    if (num > 1) {
                        int length = s.length();
                        s = s.toString().subSequence(0, length - 1);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }
                if (s.toString().startsWith("0") && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }
        });
    }

    public static int countInnerStr(final String str, final String patternStr) {//判断一个字符串中有多少个指定的字符

        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            char string = str.charAt(i);
            if (".".equals(String.valueOf(string))) {
                count++;
            }
        }

        return count;

    }
}
