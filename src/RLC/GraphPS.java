package RLC;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class GraphPS extends JFrame implements Runnable
{
	public GraphPS(Master mast)
	{
		this.master = mast;
		setSize(500, 400);
		setMinimumSize(new Dimension(500, 400));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Wykres przesuni�cia fazowego");
		setLocationRelativeTo(master);
		
		//***************************************MENU******************************************************
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("Pomiary");
		measurements = new JMenuItem("Poka� pomiary");
		ActionListener measurementsListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				MeasurementPS measurementsPS = new MeasurementPS(GraphPS.this);
				measurementsPS.setVisible(true);
				
			}	
		};
		measurements.addActionListener(measurementsListener);
		
		menu.add(measurements);
		menuBar.add(menu);
		
		//***************************************WYKRES*****************************************************
		
		painted = false;
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
		panel.setSize(getPreferredSize());
		this.add(panel);
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paint(g);
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
				phi = -Math.atan((2*Math.PI*f*master.L-1/(2*Math.PI*f*master.C))/master.R);
				series.add(f,phi);			
			}
			
			else // rownolegle
			{
				phi = -Math.atan(master.R*(2*Math.PI*master.C*f-1/(2*Math.PI*f*master.L)));
				series.add(f,phi);
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
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem measurements;
	Master master;
	JFreeChart chart;
	ChartPanel panel;
	XYPlot plot;
	double f,phi;
	XYSeries series;
	XYSeriesCollection dataset;
	boolean painted;

	
}
