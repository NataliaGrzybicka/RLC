package RLC;

import java.awt.Dimension;

import javax.swing.JFrame;

public class Measurement extends JFrame 
{
	public Measurement(Master mas)
	{
		this.master = mas;
		setSize(300, 600);
		setMinimumSize(new Dimension(300, 600));
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Pomiary I(f)");
		setLocationRelativeTo(master.menu.master);
	}
	
	Master master;
}
