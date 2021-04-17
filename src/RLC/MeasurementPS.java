package RLC;

import javax.swing.*;

public class MeasurementPS extends JFrame 
{
	public MeasurementPS(GraphPS graph)
	{
		this.graphPS = graph;
		setSize(300, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Pomiary dla przesuniÍcia fazowego");
		setLocationRelativeTo(graphPS);
	}
	
	GraphPS graphPS;
}
