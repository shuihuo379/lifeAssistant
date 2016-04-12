package com.itheima.view;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.itheima.life.R;

/**
 * 折线图
 * @author zhangming
 * @date 2016/04/10
 */
public class PolygonalLineView {
	private Context context;
	private XYMultipleSeriesRenderer renderer;
	
	public PolygonalLineView(Context context) {
		this.context = context; 
		renderer = new XYMultipleSeriesRenderer();
	}
	
	public View getPolygonalLineView(String[]xText,double[]y1,double yMax){
		PolygonalChartSetting(xText,yMax);
		View view = ChartFactory.getLineChartView(context,getDataSet(y1),renderer);
		return view;
	}
	
	private void PolygonalChartSetting(String[]xText,double yMax) {
		renderer.setChartTitleTextSize(0);  //设置图表标题字体大小,设置0是把标题隐藏掉 
		renderer.setAxesColor(Color.CYAN); // 设置 XY 轴颜色
		renderer.setLabelsColor(Color.BLACK); // 设置轴标签颜色
		renderer.setLabelsTextSize(30); // 设置轴标签字体大小
		renderer.setLegendTextSize(25); // 设置图例字体大小
		renderer.setMargins(new int[] {30, 60, 40, 10 }); // 上,左,下,右(控制你图的边距,实现跟图例的分离)
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.WHITE);  //设置中间背景色 
		renderer.setMarginsColor(Color.WHITE); // 设置周边背景色为白色
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setXAxisMin(0.25f);
		renderer.setXAxisMax(7.5);
		renderer.setYAxisMin(0);
		renderer.setYAxisMax(yMax); 
		renderer.setXLabels(0); // 设置X轴显示的刻度标签的个数
		renderer.setYLabels(6); // 设置Y轴显示的刻度标签的个数
		renderer.setPointSize(10f);  //设置点的大小
		
		for(int i=0;i<xText.length;i++){
			renderer.addXTextLabel(i+1,xText[i]);
		}
	}

	/**
	* 构造数据
	* @return
	*/
	private XYMultipleSeriesDataset getDataSet(double[]y1) {
		// 构造数据
		XYMultipleSeriesDataset barDataset = new XYMultipleSeriesDataset();
		XYSeries series = new XYSeries("温度"); //图例的标题
		for(int i=0;i<y1.length;i++){
			series.add(i+1,y1[i]);
		}
		barDataset.addSeries(series);
		
		XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
        xyRenderer.setColor(Color.rgb(85,90,205)); //设置颜色
        xyRenderer.setPointStyle(PointStyle.CIRCLE); //设置点的样式
        xyRenderer.setFillPoints(true); //设置图上的点为实心
        xyRenderer.setLineWidth(3f);
        renderer.addSeriesRenderer(xyRenderer); //将要绘制的点添加到坐标绘制中
		
		return barDataset;
	}
	
	/**
	 * 获取Y轴的标题,实质是一个TextView控件
	 * @param context
	 * @param yTitle
	 * @return
	 */
	public TextView getYTitleView(Context context,String yTitle) {
		TextView tv = new TextView(context);
		
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		params.setMargins((int)context.getResources().getDimension(R.dimen.dp30),(int)context.getResources().getDimension(R.dimen.dp10),0,0);
		tv.setLayoutParams(params);
		tv.setText(yTitle);
		tv.setTextSize(15);
		tv.setTextColor(Color.rgb(85,90,205));
		
		return tv;
	}
}
