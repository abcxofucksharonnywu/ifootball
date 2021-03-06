package com.abcxo.android.ifootball.utils;

import com.abcxo.android.ifootball.Application;
import com.abcxo.android.ifootball.R;
import com.abcxo.android.ifootball.models.User;
import com.abcxo.android.ifootball.restfuls.RestfulError;
import com.abcxo.android.ifootball.restfuls.UserRestful;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;


/**
 * Created by shadow on 15/11/24.
 */
public class LocationUtils {
    private static LocationClient locationClient;

    public static void saveLocation() {
        if (locationClient == null) {
            locationClient = new LocationClient(Application.INSTANCE);
        } else {
            locationClient.stop();
        }

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (locationClient != null) {
                    if (UserRestful.INSTANCE.isLogin()
                            && location != null
                            && (location.getLocType() == BDLocation.TypeCacheLocation
                            || location.getLocType() == BDLocation.TypeNetWorkLocation
                            || location.getLocType() == BDLocation.TypeOffLineLocation)) {
                        User user = UserRestful.INSTANCE.me();
                        user.lat = location.getLatitude();
                        user.lon = location.getLongitude();
                        UserRestful.INSTANCE.edit(user, new UserRestful.OnUserRestfulGet() {
                            @Override
                            public void onSuccess(User user) {

                            }

                            @Override
                            public void onError(RestfulError error) {

                            }

                            @Override
                            public void onFinish() {

                            }
                        });

                    }
                    locationClient.stop();
                    locationClient = null;
                }

            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("gcj02");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setEnableSimulateGps(true);
        locationClient.setLocOption(option);
        locationClient.start();
    }


    public static void getLocation(final LocationListener locationListener) {
        if (locationClient == null) {
            locationClient = new LocationClient(Application.INSTANCE);
        } else {
            locationClient.stop();
        }

        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                if (locationClient != null) {
                    if (UserRestful.INSTANCE.isLogin()
                            && location != null
                            && (location.getLocType() == BDLocation.TypeCacheLocation
                            || location.getLocType() == BDLocation.TypeNetWorkLocation
                            || location.getLocType() == BDLocation.TypeOffLineLocation)) {

                        locationListener.onSuccess(location);
                    } else {
                        locationListener.onError(ViewUtils.getString(R.string.error_location));
                    }
                    locationListener.onFinish();
                    locationClient.stop();
                    locationClient = null;
                }

            }
        });
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        option.setIsNeedAddress(true);
        option.setIsNeedLocationDescribe(true);
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setEnableSimulateGps(true);
        locationClient.setLocOption(option);
        locationClient.start();
    }


    public interface LocationListener {
        void onSuccess(BDLocation location);

        void onError(String msg);

        void onFinish();
    }
}


