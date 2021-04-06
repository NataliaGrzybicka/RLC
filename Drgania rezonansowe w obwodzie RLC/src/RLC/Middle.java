package RLC;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class Middle extends JPanel 
{
	public Middle(Master g)
	{	
		master=g;
		setBackground(Color.white);
		chart = null;
		
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g); 
		
		File inputFile = new File("Wykres.png");	//wykres pogladowy w przyszlych wersjach bedzie zmieniony przez symulacje

		try 
		{
			chart = ImageIO.read(inputFile);
		} 
		catch(IOException ex) 
		{	
			System.out.println(ex.getMessage());
		}
		Image chartZip = chart.getScaledInstance(getWidth(),getHeight(),Image.SCALE_SMOOTH);
		g.drawImage(chartZip, 0, 0, this);
	}
	
	Master master;
	BufferedImage chart;
}
