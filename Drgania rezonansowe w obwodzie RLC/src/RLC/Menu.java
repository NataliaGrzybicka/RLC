package RLC;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

import javax.swing.*;

public class Menu extends JFrame 
{
	public Menu()
	{
		setSize(280, 500);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(new FlowLayout());
		setBackground(new Color(204, 153, 255));
		setTitle("Obw�d RLC");
		setResizable(false);		//staly rozmiar okna
		
		//*********************************MENUBAR**************************
		
		menuBar = new JMenuBar();
		menuBar.setBackground(new Color(223,223,223));
		setJMenuBar(menuBar);
		
		author = new JMenu("Autorzy");
		authorItem = new JMenuItem("Wy�wietl informacj� o autorach");
		ActionListener authorListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(Menu.this, "Natalia Grzybicka\n305 014\nJakub W�odarczyk\n305 064","Autorzy",JOptionPane.INFORMATION_MESSAGE);
			}
		};
		authorItem.addActionListener(authorListener);
		author.add(authorItem);
		menuBar.add(author);

		//*******************************************************************
		
		label = new JLabel("DRGANIA REZONANSOWE W UK�ADZIE RLC");
		label.setFont(label.getFont().deriveFont(Font.BOLD,12f)); //ustawienia czcionki (pogrubienie)
		this.getContentPane().setBackground(new Color(204, 153, 255));
		label.setPreferredSize(new Dimension(260, 75));
		
		ukladRLC = new JButton("UK�AD RLC");
		ukladRLC.setBackground(new Color(223,223,223));
		ukladRLC.setPreferredSize(new Dimension(150,50));
		
		instrukcja = new JButton("INSTRUKCJA");
		instrukcja.setBackground(new Color(223,223,223));
		instrukcja.setPreferredSize(new Dimension(150,50));
		ActionListener instructionListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{

				Desktop d = java.awt.Desktop.getDesktop();	//Otwieranie pdf
				try 
				{
					d.open(new java.io.File("INSTRUKCJA_PL.PDF"));
				} 
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}	
		};
		instrukcja.addActionListener(instructionListener);
		
		wyjscie = new JButton("WYJ�CIE");
		wyjscie.setBackground(new Color(223,223,223));
		wyjscie.setPreferredSize(new Dimension(150,50));
		ActionListener exitListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				System.exit(0);	
			}	
		};
		wyjscie.addActionListener(exitListener);
		
		glowny = new Glowny(this);	//wywolanie konstruktora klasy Glowny 
		
		ActionListener glownyListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{ 
				glowny.setVisible(true);	
				Menu.this.setVisible(false);
			}	
		};
		ukladRLC.addActionListener(glownyListener);
		
		lNull1 = new JLabel();
		lNull1.setPreferredSize(new Dimension(this.getWidth(), 25));
		
		lNull2 = new JLabel();
		lNull2.setPreferredSize(new Dimension(this.getWidth(), 50));
		
		lNull3 = new JLabel();
		lNull3.setPreferredSize(new Dimension(this.getWidth(), 50));
		
		this.add(label);
		this.add(lNull1);
		this.add(ukladRLC);
		this.add(lNull2);
		this.add(instrukcja);
		this.add(lNull3);
		this.add(wyjscie);
	}
	
	JButton ukladRLC, instrukcja, wyjscie;
	JLabel label, lNull1, lNull2, lNull3;
	Glowny glowny;
	
	JMenuBar menuBar;
	JMenu author;
	JMenuItem authorItem;
}
