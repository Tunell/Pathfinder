import graphs.*;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class GUI extends JFrame{
	private static final long serialVersionUID = -6852549253484688456L;
	Background display;
	MouseList mouseListener = new MouseList();
	SelectionList selectionListener = new SelectionList();
	JFileChooser fc;
	private CityPost cp1 = null, cp2 = null;
	Map<CityPost, Node>	cities = new HashMap<CityPost, Node>();
	ListGraph<Node> g = new ListGraph<Node>();
	
	JButton findButton;
	JButton showEdgeButton;
	JButton newNodeButton;
	JButton newEdgeButton;
	JButton changeEdgeButton;
	
	GUI(){
		//bygger upp layouten på fönstret	
		super("PathFinder");
		JMenuBar menubar = new JMenuBar();
		JMenu filemenu = new JMenu("Arkiv");
		menubar.add(filemenu);
		filemenu.add(new JSeparator());
		JMenuItem fileItem1 = new JMenuItem("Ny");
		filemenu.add(fileItem1);
		fc = new JFileChooser(".");
		fileItem1.addActionListener(new newBGList());
		JMenuItem fileItem2 = new JMenuItem("Avsluta");
		filemenu.add(fileItem2);
		fileItem2.addActionListener(new avslutaList());
		
		JMenu operatMenu = new JMenu("Operationer");
		menubar.add(operatMenu);
		operatMenu.add(new JSeparator());
		JMenuItem operatItem1 = new JMenuItem("Hitta väg");
		operatMenu.add(operatItem1);
		operatItem1.addActionListener(new findList());
		JMenuItem operatItem2 = new JMenuItem("Visa förbindelse");
		operatMenu.add(operatItem2);
		operatItem2.addActionListener(new showEdgeList());
		JMenuItem operatItem3 = new JMenuItem("Ny plats");
		operatMenu.add(operatItem3);
		operatItem3.addActionListener(new newNodeList());
		JMenuItem operatItem4 = new JMenuItem("Ny förbindelse");
		operatMenu.add(operatItem4);
		operatItem4.addActionListener(new newEdgeList());
		JMenuItem operatItem5 = new JMenuItem("Ändra förbindelse");
		operatMenu.add(operatItem5);
		operatItem5.addActionListener(new changeEdgeList());
		JMenuItem operatItem6 = new JMenuItem("Add Cities");
		operatMenu.add(operatItem6);
		operatItem6.addActionListener(new citiesList());
		
		setLayout (new BorderLayout());
		JPanel North = new JPanel();
		add(North, BorderLayout.NORTH);
		
		findButton = new JButton("Hitta väg");
		findButton.addActionListener(new findList());
		North.add(findButton);
		
		showEdgeButton = new JButton("Visa förbindelse");
		showEdgeButton.addActionListener(new showEdgeList());
		North.add(showEdgeButton);
		
		newNodeButton = new JButton("Ny plats");
		newNodeButton.addActionListener(new newNodeList());
		North.add(newNodeButton);
		
		newEdgeButton = new JButton("Ny förbindelse");
		newEdgeButton.addActionListener(new newEdgeList());
		North.add(newEdgeButton);
		
		changeEdgeButton = new JButton("Ändra förbindelse");
		changeEdgeButton.addActionListener(new changeEdgeList());
		North.add(changeEdgeButton);
		
		findButton.setEnabled( false );
		showEdgeButton.setEnabled( false );
		newNodeButton.setEnabled( false );
		newEdgeButton.setEnabled( false );
		changeEdgeButton.setEnabled( false );

		super.setJMenuBar(menubar);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(615,100);
		setVisible(true);
	}
	
	class citiesList implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			try{
				if(display==null)
				throw new NullPointerException("Du måste välja en karta först!");
			}
				catch (NullPointerException e) { JOptionPane.showMessageDialog(null, "Du måste välja en karta först!", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
				return;
				}
			CityPost stad1 = new CityPost(50,50,"Tierp");
			CityPost stad2 = new CityPost(150,150,"Uppsala");
			CityPost stad3 = new CityPost(250,250,"Stockholm");
			CityPost stad4 = new CityPost(350,350,"Lund");
			cities.put(stad1,new Node("Tierp"));
			cities.put(stad2,new Node("Uppsala"));
			cities.put(stad3,new Node("Stockholm"));
			cities.put(stad4,new Node("Lund"));
			CityPost arr[]= {stad1,stad2,stad3,stad4};
			for (CityPost i: arr){
				i.addMouseListener(selectionListener);
				i.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
				display.add(i);
				validate();
				repaint();
			}
			for( Node i : cities.values()){
				g.add(i);
			}
		}
	}
	
	//val av städer
	class SelectionList extends MouseAdapter{
		public void mouseClicked(MouseEvent e){
			CityPost current =(CityPost)e.getSource();
			if( current != cp2 && cp1 == null){
				cp1 = current;
				current.setSelected(true);
			}
			else if( current != cp1 && cp2 == null ){
				cp2 = current;
				current.setSelected(true);
			}
			else if( current == cp1){
				cp1.setSelected(false);
				cp1 = null;
				findButton.setEnabled( false );
				showEdgeButton.setEnabled( false );
				newNodeButton.setEnabled( true );
				newEdgeButton.setEnabled( false );
				changeEdgeButton.setEnabled( false );
			}
			else if( current == cp2){
				cp2.setSelected(false);
				cp2 = null;
				findButton.setEnabled( false );
				showEdgeButton.setEnabled( false );
				newNodeButton.setEnabled( true );
				newEdgeButton.setEnabled( false );
				changeEdgeButton.setEnabled( false );
			}
			if( cp1!= null && cp2!= null){
				findButton.setEnabled( true );
				showEdgeButton.setEnabled( true );
				newNodeButton.setEnabled( true );
				newEdgeButton.setEnabled( true );
				changeEdgeButton.setEnabled( true );
			}
		}
	}

	//skapa nya städer
	class MouseList extends MouseAdapter{
		public void mouseClicked(MouseEvent mev){
			int x = mev.getX();
			int y = mev.getY();
			JPanel form = new JPanel();
			form.setLayout( new BoxLayout( form, BoxLayout.Y_AXIS));
			form.add(new JLabel( "Platsens namn:"));
			JTextField namn = new JTextField(20);
			form.add(namn);
			int value = JOptionPane.showConfirmDialog(null, form, "Ny Plats", JOptionPane.OK_CANCEL_OPTION);
			switch (value){
				case JOptionPane.CANCEL_OPTION:return;
				
				case JOptionPane.OK_OPTION:{
					CityPost stad = new CityPost(x,y,namn.getText());
					cities.put(stad,new Node(namn.getText()));
					stad.addMouseListener(selectionListener);
					stad.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
					display.add(stad);
					validate();
					repaint();
					for( Node i : cities.values()){
						g.add(i);
					}
				}
			}
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			display.removeMouseListener(mouseListener);
			
		}
	}
	
	
	
	class findList implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			try{
				if(cp1==null || cp2==null)
				throw new IllegalArgumentException("Två platser måste vara valda!");
			}
				catch (IllegalArgumentException e) { JOptionPane.showMessageDialog(null, "Två platser måste vara valda!", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
				return;
				}
			JPanel form = new JPanel();
			form.setLayout( new BoxLayout( form, BoxLayout.Y_AXIS));
			JTextArea edges = new JTextArea();
			edges.setText("");
			edges.append( "Förbindelse från " + cities.get(cp1).toString() + " till " + cities.get(cp2).toString()+":\n" );
			edges.setColumns(19);
			edges.setLineWrap(true);
			edges.setRows(18);
			edges.setWrapStyleWord(true);
			edges.setEditable(false);
			form.add(edges);
			GraphMethods<Node> gm = new GraphMethods<Node>();
			List<Edge<Node>> edgesBetween = gm.shortestPath(g,cities.get(cp1),cities.get(cp2));
			for( Edge<Node> i : edgesBetween)
				edges.append(i.toString()+"\n");
			if (gm.pathExists(g,cities.get(cp1),cities.get(cp2))){
				edges.append("\n"+gm.getTotalWeight());}
			else{
				edges.append("Det finns ingen väg");
			}
			try{						
				int value = JOptionPane.showConfirmDialog(null, form, "Visa förbindelse", JOptionPane.OK_OPTION);//snygga till detta!
				switch (value){
				case JOptionPane.NO_OPTION:{
						cp1.setSelected(false); cp1 = null;
						cp2.setSelected(false); cp2 = null;return;
					}
					case JOptionPane.OK_OPTION:{
						cp1.setSelected(false); cp1 = null;
						cp2.setSelected(false); cp2 = null;
					}
				}
			}catch(NumberFormatException e){
				cp1.setSelected(false); cp1 = null;
				cp2.setSelected(false); cp2 = null;
				JOptionPane.showMessageDialog(null, "Fel!", "Fel" , JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class showEdgeList implements ActionListener{
		public void actionPerformed(ActionEvent ave){

			try{
				if(cp1==null || cp2==null)
				throw new IllegalArgumentException("Två platser måste vara valda!");
			}
				catch (IllegalArgumentException e) { JOptionPane.showMessageDialog(null, "Två platser måste vara valda!", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
				return;
				}
			
			JPanel form = new JPanel();
			form.setLayout( new BoxLayout( form, BoxLayout.Y_AXIS));
			form.add(new JLabel());	
			JTextArea edges = new JTextArea();
			edges.setColumns(19);
			edges.setLineWrap(true);
			edges.setRows(18);
			edges.setWrapStyleWord(true);
			edges.setEditable(false);
			form.add(edges);
			edges.setText( "Förbindelse från " + cities.get(cp1).toString() + " till " + cities.get(cp2).toString()+"\n" );
			try {
				List<Edge<Node>> edgesBetween = g.getEdgesBetween(cities.get(cp1), cities.get(cp2));
				if(edgesBetween.get(0)==null)
					throw new NoSuchElementException("Det finns ingen sådan edge");
			}
			catch (NoSuchElementException e) { JOptionPane.showMessageDialog(null, "Det finns ingen sådan förbindelse.", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
			return;
			}
			List<Edge<Node>> edgesBetween = g.getEdgesBetween(cities.get(cp1), cities.get(cp2));
			for( Edge<Node> i : edgesBetween)
				edges.append(i.toString()+"\n");
				try{						
					int value = JOptionPane.showConfirmDialog(null, form, "Visa förbindelse", JOptionPane.OK_OPTION);//snygga till detta!
					switch (value){
					case JOptionPane.NO_OPTION:{
						cp1.setSelected(false); cp1 = null;
						cp2.setSelected(false); cp2 = null;return;
					}
					case JOptionPane.OK_OPTION:{
						cp1.setSelected(false); cp1 = null;
						cp2.setSelected(false); cp2 = null;
					}
				}
			}catch(NumberFormatException e){
				cp1.setSelected(false); cp1 = null;
				cp2.setSelected(false); cp2 = null;
				JOptionPane.showMessageDialog(null, "Fel!", "Fel" , JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	class newNodeList implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			try{
				if(display==null)
				throw new NullPointerException("Du måste välja en karta först!");
			}
				catch (NullPointerException e) { JOptionPane.showMessageDialog(null, "Du måste välja en karta först!", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
				return;
				}
			display.addMouseListener(mouseListener);
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		}
	}

	class newEdgeList implements ActionListener{
		public void actionPerformed(ActionEvent ave){

			try{
				if(cp1==null || cp2==null)
				throw new IllegalArgumentException("Två platser måste vara valda!");
			}
				catch (IllegalArgumentException e) { JOptionPane.showMessageDialog(null, "Två platser måste vara valda!", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
				return;
				}
			
				JPanel form = new JPanel();
				form.setLayout( new BoxLayout( form, BoxLayout.Y_AXIS));
				form.add(new JLabel( "Förbindelse från " + cities.get(cp1).toString() + " till " + cities.get(cp2).toString()+"\n"  ));				
				form.add(new JLabel( "Namn:"));
				JTextField namn = new JTextField(20);
				form.add(namn);
				namn.requestFocusInWindow();
				form.add(new JLabel( "Tid:"));
				JTextField weight = new JTextField(5);
				form.add(weight);
				for(;;){
					try{					
						int value = JOptionPane.showConfirmDialog(null, form, "Ny Förbindelse", JOptionPane.OK_CANCEL_OPTION);//byta till option dialog för att lösa kruxet
						switch (value){
							case JOptionPane.CANCEL_OPTION:{
								cp1.setSelected(false); cp1 = null;
								cp2.setSelected(false); cp2 = null;return;
							}
							
							case JOptionPane.OK_OPTION:{
								try{g.connect( cities.get(cp1),cities.get(cp2), namn.getText(), Integer.parseInt(weight.getText()));
								}catch (IllegalStateException	e){System.out.println("error");}							}
								cp1.setSelected(false); cp1 = null;
								cp2.setSelected(false); cp2 = null;
							}
						
						break;
					}catch(NumberFormatException e){
						JOptionPane.showMessageDialog(null, "Skriv in tiden med siffror!", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
					}
				
				}
			
		}
	}

	class changeEdgeList implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			try{
				if(cp1==null || cp2==null)
				throw new IllegalArgumentException("Två platser måste vara valda!");
			}catch (IllegalArgumentException e) { JOptionPane.showMessageDialog(null, "Två platser måste vara valda!", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
				return;
			}
			try {
				List<Edge<Node>> edgesBetween = g.getEdgesBetween(cities.get(cp1), cities.get(cp2));
				if(edgesBetween.get(0)==null)
					throw new NoSuchElementException("Det finns ingen sådan edge");
			}
			catch (NoSuchElementException e) { JOptionPane.showMessageDialog(null, "Det finns ingen sådan förbindelse", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
			return;
			}
			List<Edge<Node>> edgesBetween = g.getEdgesBetween(cities.get(cp1), cities.get(cp2));
			if(edgesBetween.size()>1){
				JPanel form = new JPanel();
				form.setLayout( new BoxLayout( form, BoxLayout.Y_AXIS));
				form.add(new JLabel(""));	
				
				Edge<Node>[] arr = edgesBetween.toArray(new Edge[edgesBetween.size()]);
				JList<Edge<Node>> edgeList = new JList<Edge<Node>>(arr);
				form.add(edgeList);
				edgeList.setBorder(new LineBorder(Color.BLACK));
				edgeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				//edges.setText( "Förbindelse från " + cities.get(cp1).toString() + " till " + cities.get(cp2).toString() );
				for(;;){
					try{	
						int value = JOptionPane.showConfirmDialog(null, form, "Ändra förbindelse", JOptionPane.OK_OPTION);//snygga till detta!
						switch (value){
						case JOptionPane.NO_OPTION:{
							cp1.setSelected(false); cp1 = null;
							cp2.setSelected(false); cp2 = null;return;
							}
							case JOptionPane.OK_OPTION:{
								
								JPanel form1 = new JPanel();
								form1.setLayout( new BoxLayout( form1, BoxLayout.Y_AXIS));
								form1.add(new JLabel( "Förbindelse från " + cities.get(cp1).toString() + " till " + cities.get(cp2).toString()+"\n" ));				
								form1.add(new JLabel( "Namn:"));
								JTextField namn = new JTextField(((Edge<Node>)edgeList.getSelectedValue()).getName());
								form1.add(namn);
								namn.setEditable(false);
								form1.add(new JLabel( "Tid:"));
								JTextField weight = new JTextField(5);
								weight.setText(""+((Edge<Node>)edgeList.getSelectedValue()).getWeight());
								form1.add(weight);
								for(;;){
									try{					
										int value1 = JOptionPane.showConfirmDialog(null, form1, "Ändra Förbindelse", JOptionPane.OK_CANCEL_OPTION);//byta till option dialog för att lösa kruxet
										switch (value1){
											case JOptionPane.CANCEL_OPTION:{
												cp1.setSelected(false); cp1 = null;
												cp2.setSelected(false); cp2 = null;
												return;
											}
											
											case JOptionPane.OK_OPTION:{
												g.setConnectionWeight( cities.get(cp1),cities.get(cp2), namn.getText(), Integer.parseInt(weight.getText()));
												cp1.setSelected(false); cp1 = null;
												cp2.setSelected(false); cp2 = null;
												return;
											}
										}
										break;
									}catch(NumberFormatException e){
										JOptionPane.showMessageDialog(null, "Skriv in tiden med siffror!", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
									}
								}
							}
						}
					}catch (NullPointerException e) { JOptionPane.showMessageDialog(null, "Du måste välja en förbindelse", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
					}
					
				}
			}//if(edgesBetween.size()>1)
			else{
				
				JPanel form1 = new JPanel();
				form1.setLayout( new BoxLayout( form1, BoxLayout.Y_AXIS));
				form1.add(new JLabel( "Förbindelse från " + cities.get(cp1).toString() + " till " + cities.get(cp2).toString()+"\n" ));				
				form1.add(new JLabel( "Namn:"));
				JTextField namn = new JTextField((edgesBetween.get(0).getName()));
				form1.add(namn);
				namn.setEditable(false);
				form1.add(new JLabel( "Tid:"));
				JTextField weight = new JTextField(5);
				weight.setText(""+(edgesBetween.get(0).getWeight()));
				form1.add(weight);
				for(;;){
					try{					
						int value1 = JOptionPane.showConfirmDialog(null, form1, "Ändra Förbindelse", JOptionPane.OK_CANCEL_OPTION);//byta till option dialog för att lösa kruxet
						switch (value1){
							case JOptionPane.CANCEL_OPTION:{
								cp1.setSelected(false); cp1 = null;
								cp2.setSelected(false); cp2 = null;
								return;
							}
							
							case JOptionPane.OK_OPTION:{
								g.setConnectionWeight( cities.get(cp1),cities.get(cp2), namn.getText(), Integer.parseInt(weight.getText()));
								cp1.setSelected(false); cp1 = null;
								cp2.setSelected(false); cp2 = null;
								return;
							}
						}
						break;
					}catch(NumberFormatException e){
						JOptionPane.showMessageDialog(null, "Skriv in tiden med siffror!", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}
	
	class newBGList implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			int returnVal = fc.showOpenDialog(GUI.this);
			if( returnVal == JFileChooser.APPROVE_OPTION){
				File file = (fc.getSelectedFile());
				cities.clear();
				g.getNodes().clear();
				if(display != null){
					for(int i = 0; i < display.getComponentCount() ;i++){
						 if (display.getComponent(i) instanceof CityPost)
							 display.remove(i);
					}
					remove(display);
				}
				display = new Background(file.getAbsolutePath());
				//har inte hunnit impelemntera så att fönstret skalas automatiskt och sätter det därför till en fix storlek
				findButton.setEnabled( false );
				showEdgeButton.setEnabled( false );
				newNodeButton.setEnabled( true );
				newEdgeButton.setEnabled( false );
				changeEdgeButton.setEnabled( false );
				add(display, BorderLayout.CENTER);
				validate();
				repaint();
				setSize(display.getW()+18,display.getH()+100);
			}else{
				System.out.println("Open command cancelled by user.");
			}
		}
	}

	class avslutaList implements ActionListener{
		public void actionPerformed(ActionEvent ave){
			System.exit(0);
		}
	}
}

/*
class changeEdgeList implements ActionListener{
	public void actionPerformed(ActionEvent ave){
		//har inte hunnit implementera denna del
		try{
			if(cp1==null || cp2==null)
			throw new IllegalArgumentException("Två platser måste vara valda!");
		}
			catch (IllegalArgumentException e) { JOptionPane.showMessageDialog(null, "Två platser måste vara valda!", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
			return;
			}
		JPanel form = new JPanel();
		form.setLayout( new BoxLayout( form, BoxLayout.Y_AXIS));
		form.add(new JLabel(""));	
		JTextArea edges = new JTextArea();
		edges.setColumns(19);
		edges.setLineWrap(true);
		edges.setRows(18);
		edges.setWrapStyleWord(true);
		edges.setEditable(false);
		form.add(edges);
		edges.setText( "Förbindelse från " + cities.get(cp1).toString() + " till " + cities.get(cp2).toString() );

		try {
			List<Edge<Node>> edgesBetween = g.getEdgesBetween(cities.get(cp1), cities.get(cp2));
			if(edgesBetween.get(0)==null)
				throw new NoSuchElementException("Det finns ingen sådan edge");
		}
		catch (NoSuchElementException e) { JOptionPane.showMessageDialog(null, "Det finns ingen sådan förbindelse", "Felmedelande" , JOptionPane.ERROR_MESSAGE);
		return;
		}
		List<Edge<Node>> edgesBetween = g.getEdgesBetween(cities.get(cp1), cities.get(cp2));
		for( Edge<Node> i : edgesBetween)
			edges.append(i.toString()+"\n");
		try{						
			int value = JOptionPane.showConfirmDialog(null, form, "Visa förbindelse", JOptionPane.OK_OPTION);//snygga till detta!
			switch (value){
			case JOptionPane.NO_OPTION:{
				cp1.setSelected(false); cp1 = null;
				cp2.setSelected(false); cp2 = null;return;
			}
				case JOptionPane.OK_OPTION:{
					cp1.setSelected(false); cp1 = null;
					cp2.setSelected(false); cp2 = null;
				}
			}
		}catch(NumberFormatException e){
			cp1.setSelected(false); cp1 = null;
			cp2.setSelected(false); cp2 = null;
			JOptionPane.showMessageDialog(null, "Fel!", "Fel" , JOptionPane.ERROR_MESSAGE);
		}
	}
*/
