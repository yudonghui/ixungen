package com.ruihuo.ixungen.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Sdcard操作有关的方法
 */
public class SDCardUtils {


	//判断SD卡是否挂载
	public static boolean  isSDCardMounted(){
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	//获取SD卡目录
	public static String getSDCardPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	//获取SD空间大小  (字节)
	public static long getSDCardSize(){
		StatFs statFs = new StatFs(getSDCardPath());
		long count = statFs.getBlockCount();
		long size = statFs.getBlockSize();
		return count*size;
	}
	/**
	 * 获取储存Image的目录
	 * @return
	 */
	/**
	 * 保存Image的目录名
	 */
	/**
	 * sd卡的根目录
	 */
	private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
	/**
	 * 手机的缓存根目录
	 */
	private static String mDataRootPath = null;
	private final static String FOLDER_NAME = "/ccclubsdk";
	public static String getStorageDirectory(){
		String  dir= Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ?
				mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
		File file=new File(dir);
		if (!file.exists()){
			file.mkdir();
		}
		return dir;
	}
	// 获取SD卡剩余空间 可用空间
	public static long getSDCardFreeSize(){
		StatFs statFs = new StatFs(getSDCardPath());
		long count = statFs.getAvailableBlocks();
		long size = statFs.getBlockSize();
		return count*size;
	}

	//保存数据到指定目录
	/**
	 * @param data 需要保存的数据
	 * @param dir  目录
	 * @param filename  保存的文件名
	 *
	 * \storage\sdcard\test\123.jpg
	 * \storage\sdcard 	    sdcard的目录
	 * \test  	   			dir
	 * \123.jpg        		filename
	 */
	public static void saveFileToSDCard(byte[] data, String dir,String filename){
		if(isSDCardMounted()){  //存在sdcard才保存数据
			if( getSDCardFreeSize() >= data.length){  //可用空间大于需要保存数据的大小
				File f = new File(getSDCardPath()+File.separator+dir);
				if(!f.exists()){
					f.mkdirs();
				}
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(new File(f,filename));
					fos.write(data, 0, data.length);
				} catch (Exception e) {
					e.printStackTrace();
				} finally{
					if(fos != null){
						try {
							fos.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	//从sdcard上读取数据
	//filepath --->  test\123.jpg
	public static byte[] readFileFromSDCard(String filepath){
		if(isSDCardMounted()){ //sdcard存在
			File f = new File(getSDCardPath()+File.separator+filepath);
			if(f.exists()){
				FileInputStream fis = null;
				try {
					fis = new FileInputStream(f);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					byte[] buffer = new byte[1024];
					int len = 0 ;
					while((len = fis.read(buffer)) != -1){
						baos.write(buffer, 0, len);
					}
					return baos.toByteArray();
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					if(fis !=  null){
						try {
							fis.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return null;
	}
	/**
	 * 获取SD卡路径（无添加  -> / -> File.separator）
	 * @return
	 */
	public static String getSDCartPath(){
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}
	/*boolean  isSDCardMounted()判断SD卡是否挂载
	String getSDCardPath()  获取SD卡目录
	long getSDCardSize()   获取SD空间大小
	long getSDCardFreeSize()  获取SD卡剩余空间
	saveFileToSDCard(byte[] data, String dir,String filename)    保存数据到指定目录
	readFileFromSDCard(String filepath)   从SD卡获取数据*/
}
