package RLC;

import javax.swing.JFrame;

public class Pomiary extends JFrame 
{
	public Pomiary(Glowny glow)
	{
		this.glowny = glow;
		setSize(300, 600);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle("Pomiary I(f)");
		setLocationRelativeTo(glowny.menu.glowny);
	}
	
	Glowny glowny;
}
