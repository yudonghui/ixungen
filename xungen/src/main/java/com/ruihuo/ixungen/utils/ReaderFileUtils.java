package com.ruihuo.ixungen.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/10/21 0021.
 */
public class ReaderFileUtils {
	//读取assets中的json文件。参数2为文件名 带后缀。例如“lottery.json”
    public static String ReadTxtFile(Context mContext,String fileName) {

        String content = ""; //文件内容字符串
        try {
            InputStream instream = mContext.getAssets().open(fileName);
            //InputStream instream = new FileInputStream(file);
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    content += line + "\n";
                }
                instream.close();
            }
        } catch (IOException e) {
            Log.d("TestFile", e.getMessage());
        }
        return content;
    }
}
