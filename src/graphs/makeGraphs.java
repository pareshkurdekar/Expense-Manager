package graphs;


import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.util.HashMap;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler.ChartTheme;

public class makeGraphs {


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Testing");

	    PieChart chart = new PieChartBuilder().width(1600).height(1200).title("My Pie Chart").theme(ChartTheme.GGPlot2).build();
	    
    //Color[] sliceColors = new Color[] { new Color(24, 168, 124), new Color(224, 68, 14), new Color(230, 105, 62), new Color(236, 143, 110), new Color(243, 180, 159), new Color(246, 199, 182) };

	  // chart.getStyler().setSeriesColors(sliceColors);	
	 /*   chart.getStyler().setLabelsFontColor(new Color(255,25,0));
	   chart.getStyler().setAnnotationLineColor(new Color(255,200,20));
	    chart.getStyler().setAnnotationTextFontColor(new Color(255,0,0));
	    chart.getStyler().setAnnotationTextPanelFontColor(new Color(255,255,0));
	    chart.getStyler().setToolTipBackgroundColor(new Color(0,0,0));*/
	    chart.getStyler().setLabelsDistance(1.15);
	    chart.getStyler().setLegendVisible(true);
//	    chart.getStyler().setPlotContentSize(.7);
//	    chart.getStyler().setStartAngleInDegrees(90);
	 //   chart.getStyler().setLabelsVisible(true);	  
	    chart.getStyler().setLabelsVisible(true);
	//	chart.getStyler().setSumVisible(true);
		///chart.getStyler().setForceAllLabelsVisible(true);   
	   // chart.getStyler().setForceAllLabelsVisible(true);
	//    chart.getStyler().setHas
	/*   chart.getStyler().setAnnotationTextFont(Font.decode("S"));
	   chart.getStyler().setLabelsFont(Font.decode("S"));
	   chart.getStyler().setLabelsVisible(true);
	   chart.getStyler().setSumVisible(true);
	    chart.getStyler().setLabelsDistance(1.15);
	  chart.getStyler().setPlotBorderVisible(true);
	  chart.getStyler().setChartTitleBoxVisible(true);
	  */ 
	// chart.getStyler().setSeriesLines(new BasicStroke(12,23,3));

	    // Series
	    chart.addSeries("hghg", 67);

	    chart.addSeries("Prague", 2);
	    chart.addSeries("Dresdensdxhhh",4);
	    chart.addSeries("Munich", 34);
	    
	    chart.addSeries("Hamburg", 222);
	  //  chart.addSeries("Berlin", 29);
	//   chart.addSeries("Berlisdsdn", 99);
	    
	    // Show it
	    new SwingWrapper(chart).displayChart();
	/*	try {
			BitmapEncoder.saveBitmapWithDPI(chart,"D:\\PEM - Documents\\Graphs\\sample", BitmapFormat.PNG, 300);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
*/
	}

	public static PieChart create(HashMap<String, Double> chartValues, String title) {
		// TODO Auto-generated method stub
		
	/*	for(String s: chartValues.keySet())
		{
			System.out.println(s + "  " + chartValues.get(s));
		}
		*/
	    PieChart chart = new PieChartBuilder().width(2000).height(1200).title(title.toUpperCase()).theme(ChartTheme.GGPlot2).build();
	    chart.getStyler().setLabelsDistance(1.15);
	    chart.getStyler().setLegendVisible(true);
	    chart.getStyler().setPlotContentSize(.7);
	    chart.getStyler().setStartAngleInDegrees(90);
	    chart.getStyler().setLabelsVisible(true);	  
	    chart.getStyler().setLabelsVisible(true);
		chart.getStyler().setSumVisible(true);
		chart.getStyler().setForceAllLabelsVisible(true);   
		   
	    for(String key: chartValues.keySet())
	    {
	    	chart.addSeries(key, chartValues.get(key)).setLabel("Rs. "+String.valueOf(chartValues.get(key)));
	    }
	    
	  //  new SwingWrapper(chart).displayChart();
	    
	    return chart;
	    
	   
	     
	    
	}

	public static void save(PieChart chart, String graphFilePath, String month, String year, String title) {
		// TODO Auto-generated method stub
		
		 try {
				BitmapEncoder.saveBitmapWithDPI(chart,graphFilePath +"\\"+ title + "_"+year+"MTH"+month, BitmapFormat.PNG, 300);
				System.out.println("Graph  Saved Succesfully!");
		 
		 } catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	}

}
