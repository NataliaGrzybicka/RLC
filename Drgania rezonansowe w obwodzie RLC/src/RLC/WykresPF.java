package RLC;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class WykresPF extends JFrame 
{
	public WykresPF(Glowny glow)
	{
		this.glowny = glow;
		setSize(500, 400);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Wykres przesuniêcia fazowego");
		setLocationRelativeTo(glowny.menu.glowny);
		
		//***************************************MENU******************************************************
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menu = new JMenu("Pomiary");
		pomiary = new JMenuItem("Poka¿ pomiary");
		ActionListener pomiaryListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{

				PomiaryPF pomiaryPF = new PomiaryPF(WykresPF.this);
				pomiaryPF.setVisible(true);
				
			}	
		};
		pomiary.addActionListener(pomiaryListener);
		
		menu.add(pomiary);
		menuBar.add(menu);
		
	}
	
	JMenuBar menuBar;
	JMenu menu;
	JMenuItem pomiary;
	
	Glowny glowny;
}
