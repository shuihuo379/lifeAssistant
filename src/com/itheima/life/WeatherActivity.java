package com.itheima.life;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.itheima.view.BarChartView;
import com.itheima.view.PolygonalLineView;

/**
 * 天气模块,界面使用折线图显示平均气温，用柱状图显示最高气温和最低气温
 * @author zhangming
 */
public class WeatherActivity extends BaseActivity{
	private FrameLayout ll_polview,ll_barView;
	
	/**折线图数据**/
	private double[] y1 = new double[] {35,36,32,29,28,27,30};
	private String[] xText = new String[]{"05-01","05-02","05-03","05-04","05-05","05-06","05-07"};
	
	/**柱状图数据**/
	private double[] d1 = new double[] {25,27,23,21,23,24,20};
	private double[] d2 = new double[] {31,32,30,29,27,29,30};
	private String[] titles = new String[] { "低温", "高温" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_activity);
		initView();
	}
	
	private void initView(){
		ll_polview = (FrameLayout)findViewById(R.id.ll_polview);
		ll_barView = (FrameLayout)findViewById(R.id.ll_barView);
		
		PolygonalLineView polyLineView = new PolygonalLineView(this);
		ll_polview.addView(polyLineView.getPolygonalLineView(xText, y1, 45),0);
		ll_polview.addView(polyLineView.getYTitleView(this,"温度"),1);
		
		List<double[]> valueList = new ArrayList<double[]>();
		valueList.add(d1);
		valueList.add(d2);
		
		BarChartView barChatView = new BarChartView(this, valueList);
		ll_barView.addView(barChatView.getBarChartView(xText, titles, "温度", 45),0);
		ll_barView.addView(barChatView.getYTitleView(this,"温度"),1);
	}
}
