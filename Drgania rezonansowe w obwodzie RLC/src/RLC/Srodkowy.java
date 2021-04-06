package RLC;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Srodkowy extends JPanel 
{
	public Srodkowy(Glowny g)
	{	
		glowny=g;
		setBackground(Color.white);
		wykres = null;
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); 
		
		File inputFile = new File("Wykres.png");	//wykres pogladowy w przyszlych wersjach bedzie zmieniony przez symulacje

		try 
		{
			wykres = ImageIO.read(inputFile);
		} 
		catch(IOException ex) 
		{	
			System.out.println(ex.getMessage());
		}
		Image wykresZip = wykres.getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH);
		g.drawImage(wykresZip, 0, 0, this);
	}
	
	Glowny glowny;
	BufferedImage wykres;
}
