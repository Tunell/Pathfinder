import java.awt.*;

import javax.swing.*;


public class CityPost extends JComponent{
	private static final long serialVersionUID = 8888074755716976481L;
	private boolean selected = false;
	private String name;
	public CityPost(int x, int y, String n){
		name = n;
		setBounds(x-14,y-14,400,100);
		setPreferredSize(new Dimension(100,100));
		setMaximumSize(new Dimension(100,100));
		setMinimumSize(new Dimension(100,100));
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
	    g.setColor (Color.BLACK);
	    String city = name;
	    g.setFont (new Font ("Monospaced",Font.BOLD,25));
	    g.drawString (city, 0, 48);
	    g.setColor(Color.BLUE);
		if (selected)
		    g.setColor(Color.RED);
		g.fillOval(0,0,28,28);
	}
	
	public boolean contains( int x, int y ){
		return x>0 && x<28 && y>0 && y<28;
	}
	
	public void setSelected(boolean b){
		selected = b;
		repaint();
	}
}
