package RLC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.swing.JPanel;

import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.*;


public class Middle extends JPanel implements Runnable
{
	public Middle(Master m)
	{	
		master=m;
		setBackground(Color.white);
		setLayout(new GridLayout(1,1));
		setSize(getPreferredSize());
		painted = false;
		
		//******************************************WYKRES***********************************************
		
		series = new XYSeries("");
		dataset = new XYSeriesCollection();
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
	
	@Override
	public void run() 
	{
		f=master.fMin;
		while(f<=master.fMax)
		{	
			if(master.choose == 1) 	//szeregowy
			{			
				I = Math.pow(master.A, 2)/Math.sqrt(Math.pow(master.R, 2)+Math.pow(2*Math.PI*2*f*master.L-1/(2*Math.PI*f*master.C),2));
				series.add(f,I);			
			}
			
			else // rownolegle
			{
				I = master.A*Math.sqrt(Math.pow(1/master.R,2)+Math.pow(2*Math.PI*f*master.C-1/(2*Math.PI*master.L*f), 2));
				series.add(f,I);
			}
			f+=master.fDelta;
			
			try 
			{			
				Thread.sleep(500);
			} 
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
		}
		master.on = false;
		painted = true;
	}

	Master master;
	JFreeChart chart;
	ChartPanel panel;
	XYPlot plot;
	double f,I;
	XYSeries series;
	XYSeriesCollection dataset;
	boolean painted;
}
