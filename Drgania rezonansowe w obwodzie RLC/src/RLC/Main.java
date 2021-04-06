package RLC;

import javax.swing.SwingUtilities;

public class Main 
{

	public static void main(String[] args) 
	{
		SwingUtilities.invokeLater(new Runnable()
	{
		public void run() 
		{
			Menu menu = new Menu();
			menu.setLocationRelativeTo(null);	//okno wyskakuje na srodku
			menu.setVisible(true);
		}
	});
		
	}
	
}
