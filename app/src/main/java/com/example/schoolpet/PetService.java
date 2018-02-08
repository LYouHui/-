package com.example.schoolpet;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 尤辉 on 2017/3/23.
 */

public class PetService extends Service{
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    public static double Latitude;
    public static double Longitude;

    public void onCreate(){
        super.onCreate();
        Intent intent=new Intent(this,PetPage.class);
        PendingIntent pi=PendingIntent.getActivity(this,0,intent,0);
        Notification notification=new NotificationCompat.Builder(this).setContentTitle("校宠乐园").setWhen(System.currentTimeMillis()).setContentIntent(pi).build();
        startForeground(1,notification);
    }

    public int onStartCommand(Intent intent,int flags,int startId){
        new Thread(){
            public void run(){
                try{
                    Thread.sleep(500);//每5s一次
                }
                catch(Exception e){
                    e.printStackTrace();
                }
                //PetState mPetState=new PetState();
                //final int HUNGRY_STATE = 0;
                Calendar cal=Calendar.getInstance();
                int hour=cal.get(Calendar.HOUR_OF_DAY);
                int minute=cal.get(Calendar.MINUTE);
                int now=hour*60+minute;
                dataCallback.dataChanged(now);
                Intent intent1=new Intent("android.appwidget.action.APPWIDGET_UPDATE");

            }
        }.start();

        return super.onStartCommand(intent, flags, startId);
    }

    public void onDestroy(){
        super.onDestroy();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }


    public class MyBinder extends Binder {
        PetService getService() {
            return PetService.this;
        }
    }

    DataCallback dataCallback=null;

    public DataCallback getDataCallback() {
        return dataCallback;
    }
    public void setDataCallback(DataCallback dataCallback) {
        this.dataCallback = dataCallback;
    }
    // 通过回调机制，将Service内部的变化传递到外部
    public interface DataCallback {
        void dataChanged(int now);
    }

    private class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    Latitude=aMapLocation.getLatitude();//获取纬度
                    Longitude=aMapLocation.getLongitude();//获取经度
                    User bmobUser = BmobUser.getCurrentUser(User.class);

                    final User user = BmobUser.getCurrentUser(User.class);
                    BmobQuery<LocationDatebase> query = new BmobQuery<LocationDatebase>();
                    query.addWhereEqualTo("user", user);
                    query.findObjects(new FindListener<LocationDatebase>() {
                        @Override
                        public void done(List<LocationDatebase> object, BmobException e) {
                            if(e==null){
                                Log.i("bmob","成功");
                                LocationDatebase gameScore = new LocationDatebase();
                                gameScore.setLocation(Latitude,Longitude);
                                gameScore.setUser(user);
                                gameScore.update(object.get(0).getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if(e==null){
                                            Log.i("bmob","更新成功");
                                        }else{
                                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                                        }
                                    }
                                });
                            }else{
                                Log.i("bmob","失败："+e.getMessage());
                            }
                        }

                    });
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    }
    private void getLocation() {
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(false);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        mLocationOption.setMockEnable(false);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    };
}
