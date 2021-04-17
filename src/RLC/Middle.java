package RLC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;

import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;


public class Middle extends JPanel 
{
	public Middle(Master m)
	{	
		master=m;
		setBackground(Color.white);
		setLayout(new GridLayout(1,1));
		setSize(getPreferredSize());
		
		XYSeries series = new XYSeries("");
		series.add(2,1);
		series.add(5,5);
		series.add(7,9);
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		chart = ChartFactory.createScatterPlot("", "Os X", "Os Y", dataset, PlotOrientation.VERTICAL, false, false, false);
		plot = (XYPlot) chart.getPlot();
		plot.setDomainGridlinePaint(Color.lightGray);
	    plot.setRangeGridlinePaint(Color.lightGray);
	    plot.setBackgroundPaint(Color.white);
	    plot.getRenderer().setSeriesPaint(0, new Color(204, 153, 255));
	 
		panel = new ChartPanel(chart);
		panel.setSize(new Dimension(getPreferredSize()));
		this.add(panel);
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		
		
		panel.setSize(new Dimension(getPreferredSize()));
		
	}
	
	Master master;
	JFreeChart chart;
	ChartPanel panel;
	XYPlot plot;
}
