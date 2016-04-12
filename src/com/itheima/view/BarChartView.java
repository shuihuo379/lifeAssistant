package com.itheima.view;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.itheima.life.R;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

public class BarChartView{
	private XYMultipleSeriesRenderer renderer;
	private Context context;
	private int[] colors = new int[] { Color.BLUE, Color.CYAN};
	private List<double[]> valueList;
	
	public BarChartView(Context context,List<double[]> valueList){
		this.context = context;
		this.valueList = valueList;
		renderer = new XYMultipleSeriesRenderer();
	}
	
	public View getBarChartView(String[] xText,String[] titles,String yTitle,double yMax){
		ChartSettings(xText,yTitle,yMax);
//		renderer.getSeriesRendererAt(0).setDisplayChartValues(false); //设置柱子上是否显示数量值
		View view = ChartFactory.getBarChartView(context,getDataSet(titles), renderer, Type.DEFAULT); // Type.STACKED
		return view;
	}
	
	/**
	* 构造数据
	* @return
	*/
	public XYMultipleSeriesDataset getDataSet(String[] titles) {
		// 构造数据
		XYMultipleSeriesDataset barDataset = new XYMultipleSeriesDataset();
		for(int i=0;i<titles.length;i++){
			 XYSeries series = new XYSeries(titles[i]); //图例的标题
			 double [] yLable= valueList.get(i); //size=2
			 for(int j=0;j<yLable.length;j++){ 
				 series.add(j+1,yLable[j]);
			 }
			 barDataset.addSeries(series);
			 XYSeriesRenderer xyRenderer = new XYSeriesRenderer();
			 xyRenderer.setDisplayChartValues(false);   //设置柱子上是否显示数量值,默认是false
		     xyRenderer.setColor(colors[i]);  // 设置颜色
		     xyRenderer.setPointStyle(PointStyle.SQUARE); // 设置点的样式 
		     xyRenderer.setFillPoints(true);
		     renderer.addSeriesRenderer(xyRenderer);  // 将要绘制的点添加到坐标绘制中
		}
		return barDataset;
	}

	private void ChartSettings(String[] xText,String yTitle,double yMax) {
//		renderer.setChartTitle("个人收支表");  //设置柱图名称
		renderer.setAxesColor(Color.CYAN);  //设置 XY 轴颜色
		renderer.setLabelsColor(Color.BLACK);  // 设置轴标签颜色
		renderer.setXAxisMin(0);
		renderer.setXAxisMax(8.5);
		renderer.setYAxisMin(0.5);
		renderer.setYAxisMax(yMax);  // 设置X,Y轴的最小数字和最大数字
		renderer.setLabelsTextSize(30);  //设置轴标签字体大小
		renderer.setLegendTextSize(25); // 设置图例字体大小
		renderer.setMargins(new int[]{30, 60, 40, 10}); //上,左,下,右(控制你图的边距  实现跟图例的分离)
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setBarSpacing(0.8f);  // 柱子间宽度 
		renderer.setZoomButtonsVisible(false); 
		renderer.setAntialiasing(true);  // 消除锯齿
		renderer.setPanEnabled(false, false);   // 设置移动
		//renderer.setZoomEnabled(true,true);  // 设置放大  
		renderer.setZoomRate(1.5f);
		renderer.setXLabels(0); //设置X轴显示的刻度标签的个数
		renderer.setYLabels(6); //设置Y轴显示的刻度标签的个数
		renderer.setXLabelsPadding(230); //设置标签的间距
		renderer.setShowAxes(true); //设置是否需要显示坐标轴
		renderer.addXTextLabel(0, String.valueOf(0)); //设置显示X轴起始坐标为0
		renderer.setFitLegend(true);// 调整合适的位置
		renderer.setApplyBackgroundColor(true);
		renderer.setBackgroundColor(Color.WHITE);
		renderer.setMarginsColor(Color.WHITE);  //设置周边背景色为白色
		
		for(int i=0;i<xText.length;i++){
			renderer.addXTextLabel(i+1,xText[i]);  //替换X轴内容
		}
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
