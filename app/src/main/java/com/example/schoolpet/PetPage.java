package com.example.schoolpet;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.fence.GeoFence;
import com.amap.api.fence.GeoFenceClient;
import com.amap.api.fence.GeoFenceListener;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.DPoint;
import com.amap.api.location.LocationManagerBase;
import com.amap.api.maps.model.Text;
import com.getbase.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

import java.util.List;

import static cn.bmob.v3.Bmob.getApplicationContext;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_IN;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_OUT;
import static com.amap.api.fence.GeoFenceClient.GEOFENCE_STAYED;

public class PetPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public ImageView mImageView_health;
    public ImageView mImageView_mood;
    public ImageView mImageView_pet;
    private IntentFilter intentFilter;
    private NetworkChangeReceiver networkChangeRecevier;
    public ImageView testImageView;
    public Context context;
    public TextView textView_state;
    public TextView textView_health;
    public TextView textView_mood;
    public FloatingActionButton floatingActionButton1;
    public FloatingActionButton floatingActionButton2;
    public static Chronometer chronometer;
    //public TextView textView_showAddress;
    public TextView textView_showlocation;

    /*//声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new MyAMapLocationListener();
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;*/

    //定义接收广播的action字符串，地理围栏
    public static final String GEOFENCE_BROADCAST_ACTION = "com.location.apis.geofencedemo.broadcast";

    protected static final int HUNGRY_STATE = 0;
    PetState myPetState = new PetState(this);
    private PetService.MyBinder binder = null;
    public static double Latitude;
    public static double Longitude;
    public static String userLocation=new String();
    public static int anim_number=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_pet_page);
        //隐藏标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        //检查网络
        intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        networkChangeRecevier = new NetworkChangeReceiver();
        registerReceiver(networkChangeRecevier, intentFilter);
        //绑定服务
        Intent intent = new Intent(this, PetService.class);
        startService(intent);
        Intent bindIntent = new Intent(this, PetService.class);
        MyServiceConn myServiceConn = new MyServiceConn();
        bindService(bindIntent, myServiceConn, BIND_AUTO_CREATE);

        //设置界面以及宠物相关属性
        setUI();
        //getLocation();
        createGeoFence();
        getPetInformation();
        //setPetState();
        //myPetState.updateState(UserLocation.PLAYGROUND, PetAction.COMMON_STATE);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkMessage();
    }

    private void setPetState() {
        if(userLocation.equals("教室")){
            myPetState.updateState(UserLocation.CLASSROOM, PetAction.STUDY_STATE);
            //textView_state.setText("状态：学习" );
            anim_number=1;
        }
        if(userLocation.equals("食堂")){
            myPetState.updateState(UserLocation.REFECTORY,PetAction.EAT_STATE);
            anim_number=2;
            //textView_state.setText("状态：吃饭" );
            Pet.isEvenEating=true;
        }
        if(userLocation.equals("操场")||userLocation.equals("体育馆")){
            myPetState.updateState(UserLocation.REFECTORY,PetAction.SPORT_STATE);
            //textView_state.setText("状态：运动" );
            anim_number=3;
        }
        if(userLocation.equals("宿舍")){
            myPetState.updateState(UserLocation.REFECTORY,PetAction.SPORT_STATE);
            //myPetState.updateState(UserLocation.DORMITORY,PetAction.REST_STATE);
            //textView_state.setText("状态：碎觉" );
            anim_number=4;
        }
    }


    /*private void getLocation() {
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
        mLocationOption.setInterval(2000);
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
    private String getMyAddress(String address){
        return address;
    }

    private class MyAMapLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    Latitude=aMapLocation.getLatitude();//获取纬度
                    Longitude=aMapLocation.getLongitude();//获取经度

                    String number=Double.toString(Latitude);
                    userLocation=aMapLocation.getAddress();
                    textView_showAddress.setText(userLocation);
                    textView_showlocation.setText(number);
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
    }*/

    private void checkMessage() {
        final BmobRealTimeData realTimeData = new BmobRealTimeData();
        realTimeData.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject arg0) {
                User bmobUser = BmobUser.getCurrentUser(User.class);
                BmobQuery<MessageDatebase> query = new BmobQuery<MessageDatebase>();
                query.addWhereEqualTo("getter", bmobUser);
                query.setLimit(50);
                query.findObjects(new FindListener<MessageDatebase>() {
                    @Override
                    public void done(List<MessageDatebase> object, BmobException e) {
                        if(e==null){
                            for (final MessageDatebase messageDatebase : object) {
                                if(messageDatebase.getMessageType()==0) {//处理好友请求
                                    BmobQuery<User> query = new BmobQuery<User>();
                                    query.getObject(messageDatebase.getSender().getObjectId(), new QueryListener<User>() {
                                        @Override
                                        public void done(User object, BmobException e) {
                                            if (e == null) {
                                                String name = new String(object.gettureName());
                                                String department = new String(object.getdepartment());
                                                messageHandling0("以下用户想添加您为好友", "来自" + department + "的" + name, messageDatebase);
                                            } else {
                                                Toast.makeText(PetPage.this, "获取对方信息失败，请重试", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                if(messageDatebase.getMessageType()==1){//处理借书请求
                                    BmobQuery<User> query = new BmobQuery<User>();
                                    query.getObject(messageDatebase.getSender().getObjectId(), new QueryListener<User>() {
                                        @Override
                                        public void done(User object, BmobException e) {
                                            if (e == null) {
                                                String name = new String(object.gettureName());
                                                String department = new String(object.getdepartment());
                                                //还要查借的是哪本书
                                                OldBookDatebase oldbook=messageDatebase.getOldBookDatebase();
                                                messageHandling1("有用户向您借书", "来自" + department + "的" + name + "希望借您的" + oldbook.getBookName(), messageDatebase);
                                            } else {
                                                Toast.makeText(PetPage.this, "获取对方信息失败，请重试", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                if(messageDatebase.getMessageType()==-1){
                                    BmobQuery<User> query = new BmobQuery<User>();
                                    query.getObject(messageDatebase.getSender().getObjectId(), new QueryListener<User>() {
                                        @Override
                                        public void done(User object, BmobException e) {
                                            if (e == null) {
                                                String name = new String(object.gettureName());
                                                messageHandling_1("来自" + name + "的消息", "该用户拒绝了您的请求，您要再试一次吗", messageDatebase);
                                            } else {
                                                Toast.makeText(PetPage.this, "获取对方信息失败，请重试", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                                if(messageDatebase.getMessageType()<=6&&messageDatebase.getMessageType()>=2){
                                    BmobQuery<User> query = new BmobQuery<User>();
                                    query.getObject(messageDatebase.getSender().getObjectId(), new QueryListener<User>() {
                                        @Override
                                        public void done(User object, BmobException e) {
                                            if (e == null) {
                                                String name = new String(object.gettureName());
                                                String string=new String();
                                                if(messageDatebase.getMessageType()==2)
                                                    string="吃饭";
                                                if(messageDatebase.getMessageType()==3)
                                                    string="学习";
                                                if(messageDatebase.getMessageType()==4)
                                                    string="运动";
                                                if(messageDatebase.getMessageType()==5)
                                                    string="玩游戏";
                                                if(messageDatebase.getMessageType()==6)
                                                    string="出去浪";
                                                messageHandling2to6("来自"+name+"的消息","该用户想约您"
                                                        +messageDatebase.getTime()+"在"+messageDatebase.getPlace()+string,messageDatebase);
                                            } else {
                                                Toast.makeText(PetPage.this, "获取对方信息失败，请重试", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }else{
                        }
                    }
                });
            }
            @Override
            public void onConnectCompleted(Exception ex) {
                String tableName=new String("MessageDatebase");
                if(realTimeData.isConnected()){
                    // 监听表更新
                    realTimeData.subTableUpdate(tableName);
                }
            }
        });
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(anim_number==1)
            mImageView_pet.setBackgroundResource(R.drawable.anim_study);
        else if(anim_number==2)
            mImageView_pet.setBackgroundResource(R.drawable.anim_eating);
        else if(anim_number==3)
            mImageView_pet.setBackgroundResource(R.drawable.anim_sport);
        else if(anim_number==4)
            mImageView_pet.setBackgroundResource(R.drawable.anim_sleep);
        else {
            //Toast.makeText(context, "未知位置！！", Toast.LENGTH_LONG).show();
            mImageView_pet.setBackgroundResource(R.drawable.anim_study);
        }
        AnimationDrawable anim = (AnimationDrawable) mImageView_pet.getBackground();
        anim.start();

    }

    public void onStart() {
        super.onStart();
        //textView_showlocation.setText(anim_number);
        //getSharedPreferences();
        //UserLocation myLocation=getLocation();等地图解决了要换了
        //myPetState.updateState(UserLocation.DORMITORY, PetAction.COMMON_STATE);
    }

    public void onPause() {
        super.onPause();
        //setSharedPreferences();
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeRecevier);
        setSharedPreferences();
    }

    public void onRestart() {
        super.onRestart();
        getPetInformation();
        //UserLocation myLocation=getLocation();等地图解决了要换了
        //myPetState.updateState(UserLocation.CLASSROOM, PetAction.STUDY_STATE);
    }

    class NetworkChangeReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService((Context.CONNECTIVITY_SERVICE));
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()) {

            } else {
                Toast.makeText(context, "您好像进入了没有网络的原始世界", Toast.LENGTH_LONG).show();
            }
        }
    }

    class MyServiceConn implements ServiceConnection {
        //服务被绑定成功之后执行
        public void onServiceConnected(ComponentName name, IBinder service) {
            // IBinder service为onBind方法返回的Service实例
            binder = (PetService.MyBinder) service;
            binder.getService().setDataCallback(new PetService.DataCallback() {
                //执行回调函数
                public void dataChanged(int now) {
                    final int start1 = 9 * 60;
                    final int end1 = 10 * 60;//饿早餐
                    final int start2 = 12 * 60;
                    final int end2 = 13 * 60;//饿午饭
                    final int start3 = 18 * 60;
                    final int end3 = 19 * 60;//饿晚饭
                    if (Pet.isEvenEating == false && ((now >= start1 && now <= end1) || (now >= start2 && now <= end2) || (now >= start3 && now <= end3))) {
                        //if(now>=start1&&now<=end1)
                        //	Pet.habit+=1;
                        Pet.isEvenHungry = true;
                        Message m = myHandler1.obtainMessage();
                        m.what = HUNGRY_STATE;
                        myHandler1.sendMessage(m);
                    }
                    if (now < 5 && now > 0) {//过了0点部分状态还原
                        Pet.isEvenEating = false;
                        Pet.isEvenHungry = true;
                    }

                    //getLocation();

                    Message m = myHandler2.obtainMessage();
                    m.what = HUNGRY_STATE;
                    myHandler2.sendMessage(m);
                }
            });
        }
        Handler myHandler1 = new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == HUNGRY_STATE) {
                    myPetState.updateState(UserLocation.EVERYWHERE, PetAction.HUNGRY_STATE);
                }
            }
        };
        Handler myHandler2 = new Handler() {
            public void handleMessage(Message msg) {
                //getLocation();
            }
        };

        // 服务奔溃或者被杀掉执行
        public void onServiceDisconnected(ComponentName name) {
            binder = null;
        }
    }

    public void setSharedPreferences() {
        User bmobUser = BmobUser.getCurrentUser(User.class);
        Integer myhealth = Pet.getHealth();
        Integer myMood = Pet.getMood();
        User newUser = new User();
        newUser.setHealth(myhealth);
        newUser.setMood(myMood);
        newUser.setIsEvenEating(Pet.isEvenHungry);
        newUser.setIsEvenHungry(Pet.isEvenEating);
        newUser.update(bmobUser.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                } else {
                    Toast toast = Toast.makeText(PetPage.this, "上传宠物数据失败", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 100);
                    toast.show();
                }
            }
        });
    }

    public void getPetInformation() {
        User bmobUser = BmobUser.getCurrentUser(User.class);
        if (bmobUser != null) {
            // 允许用户使用应用
            BmobQuery<User> query = new BmobQuery<User>();
            query.addWhereEqualTo("username", bmobUser.getUsername());
            query.findObjects(new FindListener<User>() {
                @Override
                public void done(List<User> object, BmobException e) {
                    if (e == null) {
                        Pet myPet = new Pet(object.get(0).getHealth(), object.get(0).getMood());
                        myPet.setIsEvenHungry(object.get(0).getIsEvenHungry());
                        myPet.setIsEvenEating(object.get(0).getIsEvenEating());
                        showHealth(myPet);
                        showMood(myPet);
                    } else {
                        createDialog("发生错误，请联系客服", e.getMessage() + e.getErrorCode());
                    }
                }
            });
        } else {
            //缓存用户对象为空时， 可打开用户注册界面…
            Toast toast = Toast.makeText(PetPage.this, "从云端获取用户信息失败，请联系客服!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 100);
            toast.show();
        }

    }

    private void createDialog(String title, String msg) {
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setMessage(msg);
        b.setTitle(title);
        b.setNegativeButton("重试", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        b.create().show();
    }

    private void setUI() {
        Toast toast = Toast.makeText(PetPage.this, "欢迎来到校宠乐园", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, 100);
        toast.show();

        mImageView_pet = (ImageView) findViewById(R.id.imageView_pet);
        mImageView_health = (ImageView) findViewById(R.id.pet_page_imageView_blood);
        mImageView_mood = (ImageView) findViewById(R.id.pet_page_imageView_showMood);
        testImageView = (ImageView) findViewById(R.id.animationTV);
        chronometer=(Chronometer)findViewById(R.id.pet_page_chronometer);
        textView_state=(TextView)findViewById(R.id.petpage_textView_showState);
        textView_health=(TextView)findViewById(R.id.petPage_textView_showHealth);
        textView_mood=(TextView)findViewById(R.id.petPage_textView_showMood);
        //floatingActionButton1=(FloatingActionButton)findViewById(R.id.action_a);
        //floatingActionButton2=(FloatingActionButton)findViewById(R.id.action_b);
        //textView_showAddress=(TextView)findViewById(R.id.petPage_textView_showAddress);
        textView_showlocation=(TextView)findViewById(R.id.petPage_textView_showlocation);

        //textView_state.setText("状态：运动" );
    }

    public void showHealth(Pet myPet) {
        int number = myPet.getHealth();
        String health_state = new String();
            if (number > 80)
                health_state="健康";
            if (number <= 80 && number > 60)
                health_state="亚健康";
            if (number <= 60 && number > 40)
                health_state="生病";
            if (number <= 40 && number > 10)
                health_state="生病";
            if (number <= 10)
                health_state="身死";

        textView_health.setText(number + "/100" + health_state);
    }

    public void showMood(Pet myPet) {
        int number = myPet.getMood();
        String mood_state=new String();
        if (number > 80)
            mood_state="高兴";
        if (number <= 80 && number > 60)
            mood_state="正常";
        if (number <= 60 && number > 40)
            mood_state="郁闷";
        if (number <= 40 && number > 20)
            mood_state="伤心";
        if (number <= 20)
            mood_state="心死";

        textView_mood.setText(number + "/100" + mood_state);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pet_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.menu_PetPage) {
            // Handle the camera action
        } else if (id == R.id.menu_FriendList) {
            Intent intent = new Intent(PetPage.this, FriendList.class);
            startActivity(intent);
        } else if (id == R.id.menu_OldBookMarKet) {
            Intent intent = new Intent(PetPage.this, OldBookMarket.class);
            startActivity(intent);
        } else if (id == R.id.menu_neihbor) {
            Intent intent = new Intent(PetPage.this, LocationShow.class);
            startActivity(intent);
        } else if (id == R.id.menu_AddFriend) {
            Intent intent = new Intent(PetPage.this, FriendAdd.class);
            startActivity(intent);
        } else if (id == R.id.menu_LoginOrNot) {
            BmobUser bmobUser = BmobUser.getCurrentUser();
            if (bmobUser != null) {
                // 允许用户使用应用
                Intent intent = new Intent(PetPage.this, UserInfo.class);
                startActivity(intent);
            } else {
                //缓存用户对象为空时， 可打开用户注册界面…
                Intent intent = new Intent(PetPage.this, RegisterOrLogin.class);
                startActivity(intent);
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void messageHandling0(String title, String msg, final MessageDatebase messageDatebase) {
        final String friendId=new String(messageDatebase.getSender().getObjectId());
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setMessage(msg);
        b.setTitle(title);
        b.setPositiveButton("接受", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                //先查新好友的信息，再往双方的好友数据库添加对方
                BmobQuery<User> query = new BmobQuery<User>();
                query.getObject(friendId, new QueryListener<User>() {
                    @Override
                    public void done(final User friend, BmobException e) {
                        if(e==null){
                            //先将本用户添加为对方的好友
                            //首先查对方的好友数据库的ID
                            final User bmobUser = BmobUser.getCurrentUser(User.class);
                            BmobQuery<FriendDatebase> query = new BmobQuery<FriendDatebase>();
                            query.addWhereEqualTo("myUser", friend);
                            query.findObjects(new FindListener<FriendDatebase>() {
                                @Override
                                public void done(List<FriendDatebase> object,BmobException e) {
                                    if(e==null){
                                        FriendDatebase strangerFriendDatabase=new FriendDatebase();
                                        strangerFriendDatabase.setObjectId(object.get(0).getObjectId());//这步完成，strangerFriendDatabase即是本地用户的好友数据库

                                        //添加好友
                                        BmobRelation relation = object.get(0).getFriend();
                                        relation.add(bmobUser);
                                        strangerFriendDatabase.setFriend(relation);
                                        strangerFriendDatabase.update(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if(e==null){
                                                }else{
                                                    Toast.makeText(PetPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else{
                                        createDialog("失败",e.getErrorCode()+e.getMessage());
                                    }
                                }
                            });

                            //继续添加对方为本用户的好友。
                            // 首先查出本用户的好友数据库的id
                            BmobQuery<FriendDatebase> query1 = new BmobQuery<FriendDatebase>();
                            query1.addWhereEqualTo("myUser", bmobUser);
                            query1.findObjects(new FindListener<FriendDatebase>() {
                                @Override
                                public void done(List<FriendDatebase> object,BmobException e) {
                                    if(e==null){
                                        FriendDatebase newFriend=new FriendDatebase();
                                        newFriend.setObjectId(object.get(0).getObjectId());//这步完成，newFriend即是本地用户的好友数据库
                                        //添加好友
                                        BmobRelation relation1 = object.get(0).getFriend();
                                        relation1.add(friend);
                                        newFriend.setFriend(relation1);
                                        newFriend.update(new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {
                                                if(e==null){
                                                }else{
                                                    Toast.makeText(PetPage.this,  e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }else{
                                        createDialog("失败",e.getErrorCode()+e.getMessage());
                                    }
                                }
                            });

                            MessageDatebase oldMessage = new MessageDatebase();
                            oldMessage.setObjectId(messageDatebase.getObjectId());
                            oldMessage.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                    }else{
                                        createDialog("删除该好友请求失败",e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                        }else{
                            createDialog("添加好友失败",e.getErrorCode()+e.getMessage());
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        b.setNegativeButton("拒绝",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MessageDatebase oldMessage = new MessageDatebase();
                oldMessage.setObjectId(messageDatebase.getObjectId());
                oldMessage.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            MessageDatebase gameScore = new MessageDatebase(-1,messageDatebase.getGetter(),messageDatebase.getSender());
                            gameScore.save(new SaveListener<String>() {
                                @Override
                                public void done(String objectId, BmobException e) {
                                    if(e==null){

                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                        }else{
                            createDialog("删除该好友请求失败",e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
        b.show();
    }

    private void messageHandling1(String title, String msg, final MessageDatebase messageDatebase) {
        final String friendId=new String(messageDatebase.getSender().getObjectId());
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setMessage(msg);
        b.setTitle(title);
        b.setPositiveButton("接受", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                MessageDatebase oldMessage = new MessageDatebase();
                oldMessage.setObjectId(messageDatebase.getObjectId());
                oldMessage.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            OldBookDatebase oldBookMessage = new OldBookDatebase();
                            oldBookMessage.setObjectId(messageDatebase.getOldBookDatebase().getObjectId());
                            oldBookMessage.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){
                                    }else{
                                        createDialog("删除该旧书信息失败",e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                        }else{
                            createDialog("删除该借书请求失败",e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

                User gameScore = new User();
                gameScore.setMood(messageDatebase.getSender().getMood()+10);
                gameScore.update(messageDatebase.getSender().getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","更新成功");
                        }else{
                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

                User gameScore1 = new User();
                gameScore1.setMood(messageDatebase.getGetter().getMood()+10);
                gameScore1.update(messageDatebase.getGetter().getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","更新成功");
                        }else{
                            Log.i("bmob","更新失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });

                dialog.dismiss();
            }
        });
        b.setNegativeButton("拒绝",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MessageDatebase oldMessage = new MessageDatebase();
                oldMessage.setObjectId(messageDatebase.getObjectId());
                oldMessage.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            MessageDatebase gameScore = new MessageDatebase(-1,messageDatebase.getGetter(),messageDatebase.getSender());
                            gameScore.save(new SaveListener<String>() {
                                @Override
                                public void done(String objectId, BmobException e) {
                                    if(e==null){

                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                        }else{
                            createDialog("删除该借书请求失败",e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
        b.show();
    }

    private void messageHandling2to6(String title, String msg, final MessageDatebase messageDatebase) {
        final String friendId=new String(messageDatebase.getSender().getObjectId());
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setMessage(msg);
        b.setTitle(title);
        b.setPositiveButton("接受", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                MessageDatebase oldMessage = new MessageDatebase();
                oldMessage.setObjectId(messageDatebase.getObjectId());
                oldMessage.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            OldBookDatebase oldBookMessage = new OldBookDatebase();
                            oldBookMessage.setObjectId(messageDatebase.getOldBookDatebase().getObjectId());
                            oldBookMessage.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if(e==null){

                                    }else{
                                        createDialog("删除该信息失败",e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                        }else{

                        }
                    }
                });
                dialog.dismiss();
            }
        });
        b.setNegativeButton("拒绝",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MessageDatebase oldMessage = new MessageDatebase();
                oldMessage.setObjectId(messageDatebase.getObjectId());
                oldMessage.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            MessageDatebase gameScore = new MessageDatebase(-1,messageDatebase.getGetter(),messageDatebase.getSender());
                            gameScore.save(new SaveListener<String>() {
                                @Override
                                public void done(String objectId, BmobException e) {
                                    if(e==null){

                                    }else{
                                        Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                                    }
                                }
                            });
                        }else{
                            createDialog("删除该请求失败",e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        });
        b.show();
    }

    private void messageHandling_1(String title, String msg, final MessageDatebase messageDatebase){
        AlertDialog.Builder b=new AlertDialog.Builder(this);
        b.setMessage(msg);
        b.setTitle(title);
        b.setNegativeButton("好的", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog,int which){
                MessageDatebase gameScore = new MessageDatebase();
                gameScore.setObjectId(messageDatebase.getObjectId());
                gameScore.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            Log.i("bmob","成功");
                        }else{
                            Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
                dialog.dismiss();
            }
        });
        b.create().show();
    }

    private void createGeoFence(){
        createGeoFence1(31.889722F,118.818333F,"教室",300F);
        createGeoFence1(31.885555F,118.821388F,"食堂",100F);
        createGeoFence1(31.888611F,118.821388F,"体育馆",100F);
        createGeoFence1(31.889722F,118.818333F,"操场",100F);
        createGeoFence1(31.884444F,118.821666F,"宿舍",300F);
    }
    private void createGeoFence1(Float latitude1, Float longitude1, final String where,float radiu){
        //实例化地理围栏客户端
        GeoFenceClient mGeoFenceClient = new GeoFenceClient(getApplicationContext());
        mGeoFenceClient.setActivateAction(GEOFENCE_IN|GEOFENCE_OUT|GEOFENCE_STAYED);
        //创建一个中心点坐标
        DPoint centerPoint = new DPoint();
        //设置中心点纬度
        centerPoint.setLatitude(latitude1);
        //设置中心点经度
        centerPoint.setLongitude(longitude1);
        mGeoFenceClient.addGeoFence (centerPoint,radiu,where);
        //创建回调监听
        GeoFenceListener fenceListenter = new GeoFenceListener() {
            @Override
            public void onGeoFenceCreateFinished(List<GeoFence> geoFenceList, int errorCode,String string) {
                if(errorCode == GeoFence.ADDGEOFENCE_SUCCESS){//判断围栏是否创建成功
                    //geoFenceList就是已经添加的围栏列表，可据此查看创建的围栏
                } else {
                    //geoFenceList就是已经添加的围栏列表
                    createDialog("添加围栏失败!!",where);
                }
            }
        };
        //设置回调监听
        mGeoFenceClient.setGeoFenceListener(fenceListenter);
        //创建并设置PendingIntent
        mGeoFenceClient.createPendingIntent(GEOFENCE_BROADCAST_ACTION);
        BroadcastReceiver mGeoFenceReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(GEOFENCE_BROADCAST_ACTION)) {
                    //解析广播内容
                    //获取Bundle
                    Bundle bundle = intent.getExtras();
                    //获取围栏行为：
                    int status = bundle.getInt(GeoFence.BUNDLE_KEY_FENCESTATUS);
                    //获取自定义的围栏标识：
                    String customId = bundle.getString(GeoFence.BUNDLE_KEY_CUSTOMID);
                    //获取围栏ID:
                    String fenceId = bundle.getString(GeoFence.BUNDLE_KEY_FENCEID);
                    //获取当前有触发的围栏对象：
                    GeoFence fence = bundle.getParcelable(GeoFence.BUNDLE_KEY_FENCE);
                    if(status==GEOFENCE_IN||status==GEOFENCE_STAYED) {
                        userLocation = where;
                        setPetState();
                    }
                }
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(GEOFENCE_BROADCAST_ACTION);
        registerReceiver(mGeoFenceReceiver, filter);
    }
}
