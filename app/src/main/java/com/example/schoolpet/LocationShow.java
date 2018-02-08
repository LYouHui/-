package com.example.schoolpet;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.MapView;

import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

public class LocationShow extends AppCompatActivity {
    MapView mMapView = null;
    AMap aMap = null;

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private PetService.MyBinder binder = null;
    public static double Latitude;
    public static double Longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        //绑定服务
        Intent intent = new Intent(this, PetService.class);
        startService(intent);
        Intent bindIntent = new Intent(this, PetService.class);
        MyServiceConn myServiceConn = new MyServiceConn();
        bindService(bindIntent, myServiceConn, BIND_AUTO_CREATE);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图

        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象

        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(17);//设置希望展示的地图缩放级别
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(5000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        //aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.showIndoorMap(true);  //true：显示室内地图；false：不显示；


        UiSettings mUiSettings;//定义一个UiSettings对象
        mUiSettings = aMap.getUiSettings();//实例化UiSettings类对象

        mUiSettings.setZoomControlsEnabled(true); //放大缩小。
        mUiSettings.setCompassEnabled(true);  //指南针。
        mUiSettings.setScaleControlsEnabled(true);//比例尺。

        initFriendLocation();
        checkLocation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    public void initFriendLocation(){
        aMap.clear();
        User user = BmobUser.getCurrentUser(User.class);

        BmobQuery<FriendDatebase> query = new BmobQuery<FriendDatebase>();
        query.addWhereEqualTo("myUser", user);
        query.findObjects(new FindListener<FriendDatebase>() {
            @Override
            public void done(List<FriendDatebase> object,BmobException e) {
                if(e==null){
                    String myFriendDatebase=object.get(0).getObjectId();

                    BmobQuery<User> query = new BmobQuery<User>();
                    FriendDatebase post = new FriendDatebase();
                    post.setObjectId(myFriendDatebase);
                    query.addWhereRelatedTo("friend", new BmobPointer(post));
                    query.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> object,BmobException e) {
                            if(e==null){
                                for(final User friend : object) {
                                    BmobQuery<LocationDatebase> query = new BmobQuery<LocationDatebase>();
                                    query.addWhereEqualTo("user", friend);
                                    query.findObjects(new FindListener<LocationDatebase>() {
                                        @Override
                                        public void done(List<LocationDatebase> object, BmobException e) {
                                            if (e == null) {
                                                double latitude=object.get(0).getLatitude();
                                                double longitude=object.get(0).getLongitude();
                                                LatLng latLng = new LatLng(latitude,longitude);
                                                Marker marker = aMap.addMarker(new MarkerOptions().position(latLng).title(friend.gettureName()));
                                            } else {
                                                Log.i("bmob", "失败：" + e.getMessage());
                                            }
                                        }
                                    });
                                }
                            }else{
                                Log.i("bmob","失败："+e.getMessage());
                            }
                        }
                    });
                }else{
                    Log.i("bmob","失败："+e.getMessage());
                }
            }
        });
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


    private void checkLocation() {
        final BmobRealTimeData realTimeData = new BmobRealTimeData();
        realTimeData.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject arg0) {
                initFriendLocation();
            }
            @Override
            public void onConnectCompleted(Exception ex) {
                String tableName=new String("LocationDatebase");
                if(realTimeData.isConnected()){
                    // 监听表更新
                    realTimeData.subTableUpdate(tableName);
                }
            }
        });
    }

    class MyServiceConn implements ServiceConnection {
        //服务被绑定成功之后执行
        public void onServiceConnected(ComponentName name, IBinder service) {
            // IBinder service为onBind方法返回的Service实例
            binder = (PetService.MyBinder) service;
            binder.getService().setDataCallback(new PetService.DataCallback() {
                //执行回调函数
                public void dataChanged(int now) {
                    Message m = myHandler2.obtainMessage();
                    myHandler2.sendMessage(m);
                }
            });
        }
        Handler myHandler2 = new Handler() {
            public void handleMessage(Message msg) {
                getLocation();
            }
        };
        // 服务奔溃或者被杀掉执行
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    }
}

