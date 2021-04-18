package RLC;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.*;

public class Master extends JFrame 
{
	public Master(Menu men)
	{				
		exec = Executors.newFixedThreadPool(5);
		menu = men;
		middle = new Middle(this);	
		setSize(1280,700);
		setMinimumSize(new Dimension(1250,625));
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Drgania rezonansowe w uk³adzie RLC");
		setLocationRelativeTo(menu);
		choose = 1;	//domyslnie szeregowy
		on = false;
		
		//***********************************************MENU**************************************************************************
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		menuMenu = new JMenu("Plik");
		menuBar.add(menuMenu);
		menuBar.setBackground(new Color(223,223,223));
		
		fresh = new JMenuItem("Nowy");
		fresh.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		fresh.addActionListener(new ActionListener() 
		{
			
			@SuppressWarnings("deprecation")
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				threadI.stop();
				menu.master = new Master(menu); // przywrocenie ustawien poczatkowych obiektu
				Master.this.setVisible(false); // zamkniecie starego okna
				Master.this.graphPS.setVisible(false);
				menu.master.setVisible(true); // wyswietlenie nowego okna w miejsce starego
				
			}
		});
		menuMenu.add(fresh);
		
		open = new JMenuItem("Otwórz");
		open.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		open.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0); //do oprogramowania
			}
		});
		menuMenu.add(open);
		
		exit = new JMenuItem("Wyjœcie");
		exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.CTRL_MASK));
		exit.addActionListener(new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				System.exit(0); 
			}
		});
		menuMenu.add(exit);

		author = new JMenu("Autorzy");
		authorItem = new JMenuItem("Wyœwietl informacjê o autorach");
		ActionListener authorListener = new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				JOptionPane.showMessageDialog(Master.this, "Natalia Grzybicka\n305 014\nJakub W³odarczyk\n305 064","Autorzy",JOptionPane.INFORMATION_MESSAGE);
			}
		};
		authorItem.addActionListener(authorListener);
		author.add(authorItem);
		menuBar.add(author);
		
		//************************************************DOLNY*******************************************************************
				down = new JPanel();
				down.setBackground(new Color(204, 153, 255));
				
				bdGraphPS = new JButton("Wykres przesuniêcia fazowego");
				bdGraphPS.setBackground(new Color(223,223,223));
				graphPS = new GraphPS(Master.this);
				ActionListener graphPSListener = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						
						graphPS.setVisible(true);
						
					}	
				};
				bdGraphPS.addActionListener(graphPSListener);
				
				down.add(bdGraphPS);
		
		//****************************************PRAWY**************************************************************************
		
		right =  new JPanel();
		right.setBackground(new Color(204, 153, 255));
		lrScheme = new JLabel("Schemat uk³adu");
		lrScheme.setFont(lrScheme.getFont().deriveFont(Font.BOLD,20f)); //czcionka
		right.setLayout(new GridLayout(22,1)); 
		
		String[] circuitTypes = {"SZEREGOWY", "RÓWNOLEG£Y"}; //tablica elementow comboboxa
		cb1 = new JComboBox<String>(circuitTypes);
		cb1.setBackground(new Color(223,223,223));
		ItemListener cb1Listener = new ItemListener()
				{
					@Override
					public void itemStateChanged(ItemEvent e) 
					{
						
						if(e.getStateChange()==ItemEvent.SELECTED) 
						{
							String circuit = (String) e.getItem();
							  switch(circuit) 					//procedura zmiany obrazka
							  {
							  	  case "SZEREGOWY":			
							  	  {
							  		choose = 1;
							  	  	try 
							  	  	{					
							  	  		serial = ImageIO.read(new File("szer.png"));		//wczytanie obrazka
							  	  		Image serialZip = serial.getScaledInstance(165,185,Image.SCALE_SMOOTH); 	//skalowanie obrazka
							  	  		llPicture.setIcon(new ImageIcon(serialZip));		//wstawianie obrazka do labelki
							  	  		leftM.repaint();
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
							  		choose = 2;
							  		  try 
							  		  {
							  			  
							  			  paraller = ImageIO.read(new File("rown.png"));
							  			  Image parallerZip = paraller.getScaledInstance(165,185,Image.SCALE_SMOOTH);						  
							  			  llPicture.setIcon(new ImageIcon(parallerZip));
							  			  leftM.repaint();
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
		
		lrCircuit = new JLabel("Parametry uk³adu");
		lrCircuit.setFont(lrCircuit.getFont().deriveFont(Font.BOLD,20f));
		lrR = new JLabel("Rezystancja R ["+(char)(0x03A9)+ "]");	// znak zapisany w unicode
		Double[] resistors = {100.0, 200.0, 300.0, 400.0, 500.0, 600.0, 700.0, 800.0, 900.0, 1000.0};
		cb2 = new JComboBox<Double>(resistors);
		cb2.setBackground(new Color(223,223,223));
		cb2.setEditable(true);		//mozliwosc edytowania 
		cb2.setSelectedIndex(0);	//domyslny wybor 1. elementu
		R = resistors[0];
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
							JOptionPane.showMessageDialog(Master.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
							cb2.setSelectedIndex(0);		//powrot do wartosci domyslnej
						}
						catch(ArithmeticException ex)
						{
							JOptionPane.showMessageDialog(Master.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
							cb2.setSelectedIndex(0);
						} 
					}
				};
		cb2.addActionListener(cb2Listener);
		
		lrL = new JLabel("Indukcyjnoœæ L [nH]");
		Double[] inductance = {10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0};
		cb3 = new JComboBox<Double>(inductance);
		cb3.setBackground(new Color(223,223,223));
		cb3.setEditable(true);
		cb3.setSelectedIndex(0);
		L = inductance[0]*Math.pow(10, -9);
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
								llResultf.setText("f="+ round(fRez/1000, 2) + "kHz");
								leftM.repaint();
							}
							catch(ArithmeticException ex)
							{
								JOptionPane.showMessageDialog(Master.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
								cb3.setSelectedIndex(0);
							}
							catch(NumberFormatException exe)
							{
								JOptionPane.showMessageDialog(Master.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
								cb3.setSelectedIndex(0);
							}
					}
				};
		cb3.addActionListener(cb3Listener);
		
		lrC = new JLabel("Pojemnoœæ C ["+(char)(181) + "F]");
		Double[] capacity = {10.0, 20.0, 30.0, 40.0, 50.0, 60.0, 70.0, 80.0, 90.0, 100.0};
		cb4 = new JComboBox<Double>(capacity);
		cb4.setBackground(new Color(223,223,223));
		cb4.setEditable(true);
		cb4.setSelectedIndex(0);
		C = capacity[0]*Math.pow(10, -6);
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
							llResultf.setText("f="+ round(fRez/1000, 2) + "kHz");
							leftM.repaint();
							}
							catch(ArithmeticException ex)
							{
								JOptionPane.showMessageDialog(Master.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
								cb4.setSelectedIndex(0);
							}
							catch(NumberFormatException exe)
							{
								JOptionPane.showMessageDialog(Master.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
								cb4.setSelectedIndex(0);
							}
					}
				};
		cb4.addActionListener(cb4Listener);
		
		lrGenerator = new JLabel("Parametry generatora");
		lrGenerator.setFont(lrGenerator.getFont().deriveFont(Font.BOLD,20f));
		
		lrfMin =new JLabel("f_min [kHz]");
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
							JOptionPane.showMessageDialog(Master.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
							cb5.setSelectedIndex(0);
						}
						catch(ArithmeticException ex)
						{
							JOptionPane.showMessageDialog(Master.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
							cb5.setSelectedIndex(0);						
						} 
					}
				};
		cb5.addActionListener(cb5Listener);
		
		lrfDelta =new JLabel("f_Delta [kHz]");
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
					JOptionPane.showMessageDialog(Master.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb6.setSelectedIndex(0);
				}
				catch(ArithmeticException ex)
				{
					JOptionPane.showMessageDialog(Master.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb6.setSelectedIndex(0);						
				} 
			}
		};
cb6.addActionListener(cb6Listener);
		
		lrfMax =new JLabel("f_max [kHz]");
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
					JOptionPane.showMessageDialog(Master.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb7.setSelectedIndex(0);
				}
				catch(ArithmeticException ex)
				{
					JOptionPane.showMessageDialog(Master.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb7.setSelectedIndex(0);						
				} 
			}
		};
cb7.addActionListener(cb7Listener);
		
		lrAmplitude =new JLabel("Amplituda [V]");
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
					JOptionPane.showMessageDialog(Master.this, "Wprowadzona wartoœæ nie jest liczb¹!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb8.setSelectedIndex(0);
				}
				catch(ArithmeticException ex)
				{
					JOptionPane.showMessageDialog(Master.this, "Dopuszczalne s¹ wy³¹cznie wartoœci wiêksze od 0!\nLiczby zmiennoprzecinkowe wpisujemy z '.'","Niedozwolona wartoœæ",JOptionPane.ERROR_MESSAGE);
					cb8.setSelectedIndex(0);						
				} 
			}
		};
				
cb8.addActionListener(cb8Listener);
		
		threadI = new Thread(middle);
		threadPhi = new Thread(graphPS);
		brONOFF = new JButton("ON/OFF");
		runnable = false;
		ActionListener onOffListener = new ActionListener()
		{
			
			@SuppressWarnings({ "removal", "deprecation" })
			@Override
			public void actionPerformed(ActionEvent arg0) //wyswietlenie okieniek dialogowych w przypadku nie optymalnych parametrow
			{
															
				
				if(on == false) //wylaczony
				{
						
					on = true;
					if(middle.painted==true) //narysowano w pelni wykres
					{
						JOptionPane.showMessageDialog(Master.this, "Wykres zosta³ narysowany. W celu uzyskania nowego wykresu, wybierz Plik - Nowy", "Wykres narysowany",JOptionPane.INFORMATION_MESSAGE);
					}
					if(!runnable)
						{
						if(fRez<fMin)
						{
							JOptionPane.showMessageDialog(Master.this, "Czêstotliwoœæ minimalna jest za du¿a, aby na wykresie zosta³a pokazana czêstotliwoœæ rezonansowa!", "Na wykresie nie pojawi siê czêstotliwoœæ rezonansowa!",JOptionPane.INFORMATION_MESSAGE);
						}
						
						else if(fRez>fMax)
						{
							JOptionPane.showMessageDialog(Master.this, "Czêstotliwoœæ maksymalna jest za ma³a, aby na wykresie zosta³a pokazana czêstotliwoœæ rezonansowa!", "Na wykresie nie pojawi siê czêstotliwoœæ rezonansowa!",JOptionPane.INFORMATION_MESSAGE);
						}
						threadI.start();
						threadPhi.start();
						}
					else 
						{
						threadI.resume();
						threadPhi.resume();
						}
					
					
					runnable = true;
					
				}
				else //wlaczony
				{
					on = false;	
					threadI.suspend();
					threadPhi.suspend();
				}
				
				
			}	
		};
		brONOFF.addActionListener(onOffListener);
		//exec.execute(middle);
		brONOFF.setBackground(new Color(223,223,223));
		
		pNull1 = new JPanel();
		pNull1.setBackground(new Color(204, 153, 255));
		
		pNull2 = new JPanel();
		pNull2.setBackground(new Color(204, 153, 255));
		
		pNull3 = new JPanel();
		pNull3.setBackground(new Color(204, 153, 255));
		
		right.add(lrScheme);
		right.add(cb1);
		right.add(pNull1);
		right.add(lrCircuit);
		right.add(lrR);
		right.add(cb2);
		right.add(lrL);
		right.add(cb3);
		right.add(lrC);
		right.add(cb4);
		right.add(pNull2);
		right.add(lrGenerator);
		right.add(lrfMin);
		right.add(cb5);
		right.add(lrfDelta);
		right.add(cb6);
		right.add(lrfMax);
		right.add(cb7);
		right.add(lrAmplitude);
		right.add(cb8);
		right.add(pNull3);
		right.add(brONOFF);
		
		//****************************************************LEWY*********************************************************************
		
				left = new JPanel();
				left.setBackground(new Color(204, 153, 255));
				
				leftT = new JPanel();
				leftT.setBackground(new Color(204, 153, 255));
				leftD = new JPanel();
				leftD.setBackground(new Color(204, 153, 255));
				leftM = new JPanel();
				leftM.setBackground(new Color(204, 153, 255));
				
				left.setPreferredSize(new Dimension(200, 720));
				left.setLayout(new GridLayout(3,1));
						
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
						Master.this.setVisible(false);
						menu.setVisible(true);
					}	
				};
				blMenu.addActionListener(menuListener);
				
				blInstruction = new JButton("INSTRUKCJA");
				blInstruction.setMnemonic(KeyEvent.VK_I);
				blInstruction.setToolTipText("Naciœnij ALT + I");
				blInstruction.setPreferredSize(new Dimension(120, 30));
				blInstruction.setBackground(new Color(223,223,223));
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
				blInstruction.addActionListener(instructionListener);
				
				blMeasurements = new JButton("POMIARY");
				blMeasurements.setMnemonic(KeyEvent.VK_P);
				blMeasurements.setToolTipText("Naciœnij ALT + P");
				blMeasurements.setPreferredSize(new Dimension(120, 30));
				blMeasurements.setBackground(new Color(223,223,223));
				ActionListener measurementsListener = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent arg0) 
					{
						Measurement measurements = new Measurement(Master.this);
						measurements.setVisible(true);
						
					}	
				};
				blMeasurements.addActionListener(measurementsListener);
				
				fRez = 1/(2*Math.PI*Math.sqrt(L*C));		//domyslna czestotliwosc rezonansowa
				llResonant = new JLabel("Czêstotliwoœæ rezonansowa");
				llResonant.setFont(lrScheme.getFont().deriveFont(Font.BOLD,15f));
				llResultf = new JLabel("f="+ round(fRez/1000, 2) + "kHz");
				llResultf.setFont(lrScheme.getFont().deriveFont(Font.BOLD,20f));
				
				//*****************************ZDJECIE UKLADU **************************************************
				
					try 
					{
						serial = ImageIO.read(new File("szer.png"));
						Image serialZip = serial.getScaledInstance(165,185,Image.SCALE_SMOOTH);
						llPicture = new JLabel(new ImageIcon(serialZip));
						repaint();
					} 
					catch (IOException e1) 
					{
						System.out.println("Nie widze obrazka");
						e1.printStackTrace();
					}
				
				//*********************************************************************************	
				
				leftT.add(blMenu);
				leftT.add(blInstruction);
				leftT.add(blMeasurements);
				
				leftM.add(llPicture);
				
				leftD.add(llResonant, BorderLayout.PAGE_END);
				leftD.add(llResultf, BorderLayout.PAGE_END);
				
				left.add(leftT);
				left.add(leftM);
				left.add(leftD);
		
		//*****************************************GORNY**************************************************************************
		
		top = new JPanel();
		top.setBackground(new Color(204, 153, 255));
		title = new JLabel("Wykres zale¿noœci modu³u natê¿enia pr¹du p³yn¹cego w obwodzie w funkcji czêstotliwoœci");
		title.setFont(title.getFont().deriveFont(Font.BOLD,28f));
		top.add(title);
		
		

		//*********************************************SRODKOWY**********************************************************************
		
		add(left, BorderLayout.LINE_START);
		add(right, BorderLayout.LINE_END);
		add(top, BorderLayout.PAGE_START);
		add(down, BorderLayout.PAGE_END);		
		add(middle, BorderLayout.CENTER);
	}
	
	public static double round(double value, int places) //https://www.baeldung.com/java-round-decimal-number
	{
	    if (places < 0) throw new IllegalArgumentException();	//nie w³asciwa ilosc cyfr po przecinku

	    BigDecimal bd = new BigDecimal(Double.toString(value));	//zrzucenie wartosci na stringa 
	    bd = bd.setScale(places, RoundingMode.HALF_UP);		//wyskalowanie stringa
	    return bd.doubleValue();			//zwrocenie wartosci double przeskalowanego stringa
	}
	
	
	JPanel left, right, top, down, leftT, leftD, leftM, pNull1, pNull2, pNull3;
	JLabel title, llResonant, llResultf, lrScheme, lrCircuit, lrGenerator, lrR, lrL, lrC, lrfMin, lrfDelta, lrfMax, lrAmplitude, llPicture;
	JMenuBar menuBar;
	JMenu menuMenu, author;
	JMenuItem fresh, open, exit, authorItem;
	JButton blMenu, blInstruction, blMeasurements, bdGraphPS, brONOFF;
	JComboBox<String>cb1;
	JComboBox<Double> cb2, cb3, cb4, cb5, cb6, cb7, cb8;
	int choose;
	Double R, L, C, fMin, fDelta, fMax, A, fRez;
	BufferedImage serial, paraller;
	GraphPS graphPS;
	Middle middle;
	Menu menu;
	boolean on, runnable;
	ExecutorService exec;
	Thread threadI, threadPhi;
}
