package RLC;


import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Glowny extends JFrame 
{
	public Glowny(Menu men)
	{
		menu = men;
		setSize(1280,720);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Drgania rezonansowe w uk³adzie RLC");
		setLocationRelativeTo(menu);
		wybor = 1;	//domyslnie szeregowy
		//***********************************************MENU**************************************************************************
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuMenu = new JMenu("Plik");
		menuBar.add(menuMenu);
		menuBar.setBackground(new Color(223,223,223));
		
		nowy = new JMenuItem("Nowy");
		nowy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		nowy.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0); //do oprogramowania
			}
		});
		menuMenu.add(nowy);
		
		otworz = new JMenuItem("Otwórz");
		otworz.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		otworz.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0); //do oprogramowania
			}
		});
		menuMenu.add(otworz);
		
		wyjscie = new JMenuItem("Wyjœcie");
		wyjscie.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, ActionEvent.CTRL_MASK));
		wyjscie.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0); 
			}
		});
		menuMenu.add(wyjscie);

		author = new JMenu("Autorzy");
		authorItem = new JMenuItem("Wyœwietl informacjê o autorach");
		ActionListener authorListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(Glowny.this, "Natalia Grzybicka\n305 014\nJakub W³odarczyk\n305 064","Autorzy",JOptionPane.INFORMATION_MESSAGE);
			}
		};
		authorItem.addActionListener(authorListener);
		author.add(authorItem);
		menuBar.add(author);
		
		//****************************************PRAWY**************************************************************************
		
		prawy =  new JPanel();
		prawy.setBackground(new Color(204, 153, 255));
		lpSchemat = new JLabel("Schemat uk³adu");
		lpSchemat.setFont(lpSchemat.getFont().deriveFont(Font.BOLD,20f)); //czcionka
		prawy.setLayout(new GridLayout(22,1)); 
		
		String[] typyUkladow = {"SZEREGOWY", "RÓWNOLEG£Y"}; //tablica elementow comboboxa
		cb1 = new JComboBox<String>(typyUkladow);
		cb1.setBackground(new Color(223,223,223));
		ItemListener cb1Listener = new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent e) 
					{
						
						if(e.getStateChange()==ItemEvent.SELECTED) 
						{
							String uklad = (String) e.getItem();
							  switch(uklad) 					//procedura zmiany obrazka
							  {
							  	  case "SZEREGOWY":			
							  	  {
							  	  	wybor = 1;
							  	  	try 
							  	  	{					
							  	  		szeregowy = ImageIO.read(new File("szer.png"));		//wczytanie obrazka
							  	  		Image szeregowyZip = szeregowy.getScaledInstance(165,185,Image.SCALE_SMOOTH); 	//skalowanie obrazka
							  	  		llObrazek.setIcon(new ImageIcon(szeregowyZip));		//wstawianie obrazka do labelki
							  	  		lewyS.repaint();
							  	  	} 
							  	  	catch (IOException e1) 
							  	  	{
							  	  		System.out.println("Nie widze obrazka");
							  	  		e1.printStackTrace();
							  	  	}
							  	  	break;
							  	  	}
							  		  
							  	  case "RÓWNOLEG£Y":
							  	  {
							  		  wybor = 2;
							  		  try 
							  		  {
							  			  
							  			  rownolegly = ImageIO.read(new File("rown.png"));
							  			  Image rownoleglyZip = rownolegly.getScaledInstance(165,185,Image.SCALE_SMOOTH);						  
							  			  llObrazek.setIcon(new ImageIcon(rownoleglyZip));
							  			  lewyS.repaint();
							  		  } 
							  		  catch (IOException e1) 
							  		  {
							  			  System.out.println("Nie widze obrazka");
							  			  e1.printStackTrace();
							  		  }
							  		  break;
							  	  }
							  		  
							  }
						}
					}
				};
		cb1.addItemListener(cb1Listener);
		
		//**********************************************************************************************
		
		lpUkladu = new JLabel("Parametry uk³adu");
		lpUkladu.setFont(lpUkladu.getFont().deriveFont(Font.BOLD,20f));
		lpR = new JLabel("Rezystancja R ["+(char)(0x03A9)+ "]");	// znak zapisany w unicode
		Double[] oporniki = {100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1000.0};
		cb2 = new JComboBox<Double>(oporniki);
		cb2.setBackground(new Color(223,223,223));
		cb2.setEditable(true);		//mozliwosc edytowania 
		cb2.setSelectedIndex(0);	//domyslny wybor 1. elementu
		R = oporniki[0];
		ActionListener cb2Listener = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{						
						try
						{
							if((cb2.getSelectedItem().getClass()==R.getClass()))	//jezeli wpisana wartosc jest takiego samego typu co R
							R = (Double) cb2.getSelectedItem();						//odbywa siê rzutowanie wpisanego elementu na double i przypisanie na R 
							else throw new NumberFormatException();					//niezgodnosc typów
							if(R<=0) throw new ArithmeticException();				//wyjatek dla R niedodatniego
						}
						catch(NumberFormatException exe)
						{
							JOptionPane.showMessageDialog(Glowny.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
							cb2.setSelectedIndex(0);		//powrot do wartosci domyslnej
						}
						catch(ArithmeticException ex)
						{
							JOptionPane.showMessageDialog(Glowny.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
							cb2.setSelectedIndex(0);
						} 
					}
				};
		cb2.addActionListener(cb2Listener);
		
		lpL = new JLabel("Indukcyjnoœæ L [nH]");
		Double[] indukcyjnosc = {10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0};
		cb3 = new JComboBox<Double>(indukcyjnosc);
		cb3.setBackground(new Color(223,223,223));
		cb3.setEditable(true);
		cb3.setSelectedIndex(0);
		L = indukcyjnosc[0]*Math.pow(10, -9);
		ActionListener cb3Listener = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e) 
					{
							try
							{
								if((cb3.getSelectedItem().getClass()==L.getClass()))
									L = (Double) cb3.getSelectedItem();
								else throw new NumberFormatException();
								if(L<=0) throw new ArithmeticException();
								
								L*=Math.pow(10, -9);
								fRez = 1/(2*Math.PI*Math.sqrt(L*C));		//obliczenie czestotliwosci rezonansowej
								llWynikf.setText("f="+ round(fRez/1000, 2) + "kHz");
								lewyS.repaint();
							}
							catch(ArithmeticException ex)
							{
								JOptionPane.showMessageDialog(Glowny.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
								cb3.setSelectedIndex(0);
							}
							catch(NumberFormatException exe)
							{
								JOptionPane.showMessageDialog(Glowny.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
								cb3.setSelectedIndex(0);
							}
					}
				};
		cb3.addActionListener(cb3Listener);
		
		lpC = new JLabel("Pojemnoœæ C ["+(char)(181) + "F]");
		Double[] pojemnosc = {10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0};
		cb4 = new JComboBox<Double>(pojemnosc);
		cb4.setBackground(new Color(223,223,223));
		cb4.setEditable(true);
		cb4.setSelectedIndex(0);
		C = pojemnosc[0]*Math.pow(10, -6);
		ActionListener cb4Listener = new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e) 
					{
						
							try
							{
							if((cb4.getSelectedItem().getClass()==C.getClass()))
							C = (Double) cb4.getSelectedItem();
							else throw new NumberFormatException();
							if(C<=0) throw new ArithmeticException();
							C*=Math.pow(10, -6);
							fRez = 1/(2*Math.PI*Math.sqrt(L*C));
							llWynikf.setText("f="+ round(fRez/1000, 2) + "kHz");
							lewyS.repaint();
							}
							catch(ArithmeticException ex)
							{
								JOptionPane.showMessageDialog(Glowny.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
								cb4.setSelectedIndex(0);
							}
							catch(NumberFormatException exe)
							{
								JOptionPane.showMessageDialog(Glowny.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
								cb4.setSelectedIndex(0);
							}
					}
				};
		cb4.addActionListener(cb4Listener);
		
		lpGeneratora = new JLabel("Parametry generatora");
		lpGeneratora.setFont(lpGeneratora.getFont().deriveFont(Font.BOLD,20f));
		
		lpfMin =new JLabel("f_min [kHz]");
		Double[] fMinTab = {10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0};
		cb5 = new JComboBox<Double>(fMinTab);
		cb5.setBackground(new Color(223,223,223));
		cb5.setEditable(true);
		cb5.setSelectedIndex(0);
		fMin = fMinTab[0];
		ActionListener cb5Listener = new ActionListener()
				{

					@Override
					public void actionPerformed(ActionEvent e) 
					{
						try
						{
						if((cb5.getSelectedItem().getClass()==fMin.getClass()))
						fMin = (Double) cb5.getSelectedItem()*1000;
						else throw new NumberFormatException();
						if(fMin<=0) throw new ArithmeticException();
						
						
						}
						catch(NumberFormatException exe)
						{
							JOptionPane.showMessageDialog(Glowny.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
							cb5.setSelectedIndex(0);
						}
						catch(ArithmeticException ex)
						{
							JOptionPane.showMessageDialog(Glowny.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
							cb5.setSelectedIndex(0);						
						} 
					}
				};
		cb5.addActionListener(cb5Listener);
		
		lpfDelta =new JLabel("f_Delta [kHz]");
		Double[] fDeltaTab = {5.0, 10.0, 15.0, 20.0, 25.0, 30.0, 35.0, 40.0, 45.0, 50.0};
		cb6 = new JComboBox<Double>(fDeltaTab);
		cb6.setBackground(new Color(223,223,223));
		cb6.setEditable(true);
		cb6.setSelectedIndex(0);
		fDelta = fDeltaTab[0];
		ActionListener cb6Listener = new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
				if((cb6.getSelectedItem().getClass()==fDelta.getClass()))
				fDelta = (Double) cb6.getSelectedItem()*1000;
				else throw new NumberFormatException();
				if(fDelta<=0) throw new ArithmeticException();
				
				
				}
				catch(NumberFormatException exe)
				{
					JOptionPane.showMessageDialog(Glowny.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb6.setSelectedIndex(0);
				}
				catch(ArithmeticException ex)
				{
					JOptionPane.showMessageDialog(Glowny.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb6.setSelectedIndex(0);						
				} 
			}
		};
cb6.addActionListener(cb6Listener);
		
		lpfMax =new JLabel("f_max [kHz]");
		Double[] fMaxTab = {60.0, 120.0, 180.0, 240.0, 300.0, 360.0, 420.0, 480.0, 540.0, 600.0};
		cb7 = new JComboBox<Double>(fMaxTab);
		cb7.setBackground(new Color(223,223,223));
		cb7.setEditable(true);
		cb7.setSelectedIndex(0);
		fMax = fMaxTab[0];
		ActionListener cb7Listener = new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
					if((cb7.getSelectedItem().getClass()==fMax.getClass()))
						fMax = (Double) cb7.getSelectedItem()*1000;
					else throw new NumberFormatException();
					if(fMax<=0) throw new ArithmeticException();
				}
				catch(NumberFormatException exe)
				{
					JOptionPane.showMessageDialog(Glowny.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb7.setSelectedIndex(0);
				}
				catch(ArithmeticException ex)
				{
					JOptionPane.showMessageDialog(Glowny.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb7.setSelectedIndex(0);						
				} 
			}
		};
cb7.addActionListener(cb7Listener);
		
		lpAmplituda =new JLabel("Amplituda [V]");
		Double[] ATab = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
		cb8 = new JComboBox<Double>(ATab);
		cb8.setBackground(new Color(223,223,223));
		cb8.setEditable(true);
		cb8.setSelectedIndex(0);
		A = ATab[0];
		ActionListener cb8Listener = new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e) 
			{
				try
				{
				if((cb8.getSelectedItem().getClass()==A.getClass()))
				A = (Double) cb8.getSelectedItem()*1000;
				else throw new NumberFormatException();
				if(A<=0) throw new ArithmeticException();
				
				
				}
				catch(NumberFormatException exe)
				{
					JOptionPane.showMessageDialog(Glowny.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb8.setSelectedIndex(0);
				}
				catch(ArithmeticException ex)
				{
					JOptionPane.showMessageDialog(Glowny.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb8.setSelectedIndex(0);						
				} 
			}
		};
cb8.addActionListener(cb8Listener);
		
		bpONOFF = new JButton("ON/OFF");
		ActionListener onOffListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) //wyswietlenie okieniek dialogowych w przypadku nie optymalnych parametrow
			{
				if(fRez<fMin)
				{
					JOptionPane.showMessageDialog(Glowny.this, "Czêstotliwoœæ minimalna jest za du¿a, aby na wykresie zosta³a pokazana czêstotliwoœæ rezonansowa!", "Na wykresie nie pojawi siê czêstotliwoœæ rezonansowa!",JOptionPane.INFORMATION_MESSAGE);
				}
				
				if(fRez>fMax)
				{
					JOptionPane.showMessageDialog(Glowny.this, "Czêstotliwoœæ maksymalna jest za ma³a, aby na wykresie zosta³a pokazana czêstotliwoœæ rezonansowa!", "Na wykresie nie pojawi siê czêstotliwoœæ rezonansowa!",JOptionPane.INFORMATION_MESSAGE);
				}
				
			}	
		};
		bpONOFF.addActionListener(onOffListener);
		bpONOFF.setBackground(new Color(223,223,223));
		
		pNull1 = new JPanel();
		pNull1.setBackground(new Color(204, 153, 255));
		
		pNull2 = new JPanel();
		pNull2.setBackground(new Color(204, 153, 255));
		
		pNull3 = new JPanel();
		pNull3.setBackground(new Color(204, 153, 255));
		
		prawy.add(lpSchemat);
		prawy.add(cb1);
		prawy.add(pNull1);
		prawy.add(lpUkladu);
		prawy.add(lpR);
		prawy.add(cb2);
		prawy.add(lpL);
		prawy.add(cb3);
		prawy.add(lpC);
		prawy.add(cb4);
		prawy.add(pNull2);
		prawy.add(lpGeneratora);
		prawy.add(lpfMin);
		prawy.add(cb5);
		prawy.add(lpfDelta);
		prawy.add(cb6);
		prawy.add(lpfMax);
		prawy.add(cb7);
		prawy.add(lpAmplituda);
		prawy.add(cb8);
		prawy.add(pNull3);
		prawy.add(bpONOFF);
		
		//****************************************************LEWY*********************************************************************
		
				lewy = new JPanel();
				lewy.setBackground(new Color(204, 153, 255));
				
				lewyG = new JPanel();
				lewyG.setBackground(new Color(204, 153, 255));
				lewyD = new JPanel();
				lewyD.setBackground(new Color(204, 153, 255));
				lewyS = new JPanel();
				lewyS.setBackground(new Color(204, 153, 255));
				
				lewy.setPreferredSize(new Dimension(200, 720));
				lewy.setLayout(new GridLayout(3,1));
						
				blMenu = new JButton("MENU");
				blMenu.setMnemonic(KeyEvent.VK_M);	//obluga przycisku klawiatura (skrot klawiszowy)
				blMenu.setToolTipText("Naciœnij ALT + M");	//okienko z podpowiedzia
				blMenu.setPreferredSize(new Dimension(120, 30));
				blMenu.setBackground(new Color(223,223,223));
				ActionListener menuListener = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						Glowny.this.setVisible(false);
						menu.setVisible(true);
					}	
				};
				blMenu.addActionListener(menuListener);
				
				blInstrukcja = new JButton("INSTRUKCJA");
				blInstrukcja.setMnemonic(KeyEvent.VK_I);
				blInstrukcja.setToolTipText("Naciœnij ALT + I");
				blInstrukcja.setPreferredSize(new Dimension(120, 30));
				blInstrukcja.setBackground(new Color(223,223,223));
				ActionListener instructionListener = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{

						Desktop d = java.awt.Desktop.getDesktop();
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
				blInstrukcja.addActionListener(instructionListener);
				
				blPomiary = new JButton("POMIARY");
				blPomiary.setMnemonic(KeyEvent.VK_P);
				blPomiary.setToolTipText("Naciœnij ALT + P");
				blPomiary.setPreferredSize(new Dimension(120, 30));
				blPomiary.setBackground(new Color(223,223,223));
				ActionListener pomiaryListener = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						Pomiary pomiary = new Pomiary(Glowny.this);
						pomiary.setVisible(true);
						
					}	
				};
				blPomiary.addActionListener(pomiaryListener);
				
				fRez = 1/(2*Math.PI*Math.sqrt(L*C));		//domyslna czestotliwosc rezonansowa
				llRezonansowa = new JLabel("Czêstotliwoœæ rezonansowa");
				llRezonansowa.setFont(lpSchemat.getFont().deriveFont(Font.BOLD,15f));
				llWynikf = new JLabel("f="+ round(fRez/1000, 2) + "kHz");
				llWynikf.setFont(lpSchemat.getFont().deriveFont(Font.BOLD,20f));
				
				//*****************************ZDJECIE UKLADU **************************************************
				
					try 
					{
						szeregowy = ImageIO.read(new File("szer.png"));
						Image szeregowyZip = szeregowy.getScaledInstance(165,185,Image.SCALE_SMOOTH);
						llObrazek = new JLabel(new ImageIcon(szeregowyZip));
						repaint();
					} 
					catch (IOException e1) 
					{
						System.out.println("Nie widze obrazka");
						e1.printStackTrace();
					}
				
				//*********************************************************************************	
				
				lewyG.add(blMenu);
				lewyG.add(blInstrukcja);
				lewyG.add(blPomiary);
				
				lewyS.add(llObrazek);
				
				lewyD.add(llRezonansowa, BorderLayout.PAGE_END);
				lewyD.add(llWynikf, BorderLayout.PAGE_END);
				
				lewy.add(lewyG);
				lewy.add(lewyS);
				lewy.add(lewyD);
		
		//*****************************************GORNY**************************************************************************
		
		gorny = new JPanel();
		gorny.setBackground(new Color(204, 153, 255));
		tytul = new JLabel("Wykres zale¿noœci modu³u natê¿enia pr¹du p³yn¹cego w obwodzie w funkcji czêstotliwoœci");
		tytul.setFont(tytul.getFont().deriveFont(Font.BOLD,28f));
		gorny.add(tytul);
		
		//************************************************DOLNY*******************************************************************
		dolny = new JPanel();
		dolny.setBackground(new Color(204, 153, 255));
		
		bdWykresPF = new JButton("Wykres przesuniêcia fazowego");
		bdWykresPF.setBackground(new Color(223,223,223));
		ActionListener wykresPFListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0) 
			{
				wykresPF = new WykresPF(Glowny.this);
				wykresPF.setVisible(true);
				
			}	
		};
		bdWykresPF.addActionListener(wykresPFListener);
		
		dolny.add(bdWykresPF);

		//*********************************************SRODKOWY**********************************************************************
		srodkowy = new Srodkowy(this);
		
		add(lewy, BorderLayout.LINE_START);
		add(prawy, BorderLayout.LINE_END);
		add(gorny, BorderLayout.PAGE_START);
		add(dolny, BorderLayout.PAGE_END);
		add(srodkowy, BorderLayout.CENTER);
	}
	
	public static double round(double value, int places) 
	{
	    if (places < 0) throw new IllegalArgumentException();	//nie w³asciwa ilosc cyfr po przecinku

	    BigDecimal bd = new BigDecimal(Double.toString(value));	//zrzucenie wartosci na stringa 
	    bd = bd.setScale(places, RoundingMode.HALF_UP);		//wyskalowanie stringa
	    return bd.doubleValue();			//zwrocenie wartosci double przeskalowanego stringa
	}
	
	JPanel lewy, prawy, gorny, dolny, lewyG, lewyD, lewyS, pNull1, pNull2, pNull3;
	JLabel tytul, llRezonansowa, llWynikf, lpSchemat, lpUkladu, lpGeneratora, lpR, lpL, lpC, lpfMin, lpfDelta, lpfMax, lpAmplituda, llObrazek;
	JMenuBar menuBar;
	JMenu menuMenu, author;
	JMenuItem nowy, otworz, wyjscie, authorItem;
	JButton blMenu, blInstrukcja, blPomiary, bdWykresPF, bpONOFF;
	JComboBox<String>cb1;
	JComboBox<Double> cb2, cb3, cb4, cb5, cb6, cb7, cb8;
	int wybor;
	Double R, L, C, fMin, fDelta, fMax, A, fRez;
	BufferedImage szeregowy, rownolegly;
	WykresPF wykresPF;
	Srodkowy srodkowy;
	Menu menu;
	
}
