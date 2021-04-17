package RLC;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class GraphPS extends JFrame 
{
	public GraphPS(Master mast)
	{
		this.master = mast;
		setSize(500, 400);
		setMinimumSize(new Dimension(500, 400));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Wykres przesuniêcia fazowego");
		setLocationRelativeTo(master);
		
		//***************************************MENU******************************************************
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("Pomiary");
		measurements = new JMenuItem("Poka¿ pomiary");
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
		
	}
	
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem measurements;
	
	Master master;
}
