package RLC;

import javax.swing.*;

public class PomiaryPF extends JFrame 
{
	public PomiaryPF(WykresPF wykres)
	{
		this.wykresPF = wykres;
		setSize(300, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Pomiary dla przesuniêcia fazowego");
		setLocationRelativeTo(wykresPF.glowny.wykresPF);
	}
	
	WykresPF wykresPF;
}
