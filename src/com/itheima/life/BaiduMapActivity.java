package com.itheima.life;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.itheima.net.NetWorkHelper;
import com.itheima.util.BaiduMapUtil;
import com.itheima.util.T;

/**
 * 关于百度地图的模块
 * @author zhangming
 */
public class BaiduMapActivity extends BaseActivity implements View.OnClickListener{
	private Context context;
	private EditText et_content;
	private TextView tv_search,tv_back;
	
    protected BaiduMap baiduMap;
    protected MapView mMapView;
    protected LocationClient mLocationClient;
    protected MyBDLocationListener myBDLocationListener;
    protected Overlay markOverlay; //标志物图层
    protected Overlay popOverlay;  //信息框图层
    
    protected static int defaultLevel = 15;
    protected static int scanTime = 5000;
    
    @Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_back:
			finish();
			break;
		case R.id.tv_search:
			 String address = et_content.getText().toString().trim();
             addressToGeoPoint(address);
			break;
		}
	}
    
    protected void addressToGeoPoint(String address) {
        GeoCoder mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new MyGeoCoderListener());
        mSearch.geocode(new GeoCodeOption().city(address).address(address));
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidumap_activity);
		init();
		initBaiduMapView();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		initNetManager();
	}
	
	private void init(){
		context = this;
		tv_back = (TextView)findViewById(R.id.tv_back);
		et_content = (EditText)findViewById(R.id.et_content);
		tv_search = (TextView)findViewById(R.id.tv_search);
		
		tv_back.setOnClickListener(this);
		tv_search.setOnClickListener(this);
	}
	
	private void initBaiduMapView(){
		mMapView = (MapView) findViewById(R.id.bmapView);  	// 获取地图控件引用
	    baiduMap = mMapView.getMap();  //管理具体的某一个MapView对象,缩放,旋转,平移
        /**
         * 百度地图加载完毕后回调此方法(传参入口)
         */
        baiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                registerMyLocation();
            }
        });
        BaiduMapUtil.hiddenBaiduLogo(mMapView);  //隐藏百度广告图标
        mMapView.showZoomControls(false);
        mMapView.showScaleControl(true);  //默认是true,显示标尺
	}
	
	private void registerMyLocation(){
		myBDLocationListener = new MyBDLocationListener();
        mLocationClient = new LocationClient(context);
        mLocationClient.registerLocationListener(myBDLocationListener);
        baiduMap.setMyLocationEnabled(true);// 打开定位图层

        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
        option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
        option.setScanSpan(scanTime);  //设置扫描定位时间
        option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
        option.setNeedDeviceDirect(true);
        option.setOpenGps(true);  //设置打开GPS
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        baiduMap.getUiSettings().setCompassEnabled(false);  //不显示指南针
        MyLocationConfiguration configuration = new MyLocationConfiguration(
                MyLocationConfiguration.LocationMode.FOLLOWING, true,
                BitmapDescriptorFactory.fromResource(R.drawable.shop));
        baiduMap.setMyLocationConfigeration(configuration);// 设置定位显示的模式
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(baiduMap.getMapStatus().zoom));  //定位后更新缩放级别
	}
	
	 /**
     * 通过经纬度显示标注在地图上
     * param mLatlng
     */
    private void showAddressInMap(LatLng mLatlng,String address){
        if(markOverlay!=null){
            markOverlay.remove();
        }
        if(popOverlay!=null){
            popOverlay.remove();
        }
        if(!NetWorkHelper.isNetworkAvailable(context)) { //无网络不显示
            return;
        }
        markOverlay = BaiduMapUtil.drawMarker(baiduMap,mLatlng,BitmapDescriptorFactory.fromResource(R.drawable.shop),BaiduMapUtil.markZIndex);
        popOverlay = BaiduMapUtil.drawPopWindow(baiduMap,context,mLatlng,address,BaiduMapUtil.popZIndex);
        BaiduMapUtil.zoomByOneCenterPoint(baiduMap,mLatlng,defaultLevel);
    }
    
    @Override
    protected void operateMethod() {
    	Log.i("test","覆写父类方法,执行定位操作...");
    	BaiduMapUtil.locate(baiduMap); 
    }
	
	@Override
    protected void onResume() {
        super.onResume();
        // 在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        destroyNetManager();
        BaiduMapUtil.closeLocationClient(baiduMap,mLocationClient);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myBDLocationListener!=null){
            mLocationClient.unRegisterLocationListener(myBDLocationListener);
            myBDLocationListener = null;
        }
        if(markOverlay!=null){  //每次退出时销毁地点标记
            markOverlay = null;
        }
        if(popOverlay!=null){
            popOverlay = null;
        }
        mLocationClient = null;
        mMapView.onDestroy();
    }
    
    /**
     * 定位监听类
     */
    class MyBDLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation result) {
            if (result != null) {
                final double latitude = result.getLatitude();
                final double longitude = result.getLongitude();
                final LatLng latLng = new LatLng(latitude, longitude);
                String address = result.getAddrStr();

                if(markOverlay!=null){
                    View pop = BaiduMapUtil.initPop(context,null,false);
                    TextView tv = (TextView) pop.findViewById(R.id.title);
                    tv.setText(address);
                }else{
                    if(!NetWorkHelper.isNetworkAvailable(context)) {  //无网络不显示
                        return;
                    }
                    markOverlay = BaiduMapUtil.drawMarker(baiduMap,latLng, BitmapDescriptorFactory.fromResource(R.drawable.shop),BaiduMapUtil.markZIndex);
                    popOverlay = BaiduMapUtil.drawPopWindow(baiduMap,context,latLng,address,BaiduMapUtil.popZIndex);
                    BaiduMapUtil.zoomByOneCenterPoint(baiduMap,latLng,defaultLevel);
                }
            }
        }
    }
    
    class MyGeoCoderListener implements OnGetGeoCoderResultListener {
        @Override
        public void onGetGeoCodeResult(GeoCodeResult result) {
            if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                T.show(context,getString(R.string.no_result_tips));  //没有检索到结果
                return;
            }
            LatLng mLatlng = result.getLocation();
            String address = result.getAddress();
            showAddressInMap(mLatlng,address);  //根据经纬度设置图标在地图上
        }

        @Override
        public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {

        }
    }
}
