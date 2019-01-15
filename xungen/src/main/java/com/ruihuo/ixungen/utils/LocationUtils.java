package com.ruihuo.ixungen.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import java.util.List;

/**
 * Created by Administrator on 2017/1/9.
 */
public class LocationUtils {
    public static String getLocation(Context mContext) {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(mContext.LOCATION_SERVICE);//获取到位置服务
        //判断是否有可用的内容提供器
        String provider = judgeProvider(locationManager);
        if (provider != null) {//有位置提供器的情况
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return "0,0";
            }
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                //得到当前经纬度
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                return longitude + "," + latitude;
            }
        } else {//不存在位置提供器的情况
            //Toast.makeText(mContext,"无法获取当前位置",Toast.LENGTH_SHORT).show();
        }
        return "0,0";
    }

    /**
     * 判断是否有可用的内容提供器
     *
     * @return 不存在返回null
     */
    private static String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if (prodiverlist.contains(LocationManager.NETWORK_PROVIDER)) {
            return LocationManager.NETWORK_PROVIDER;
        } else if (prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        } else {
            return null;
        }

    }
}
