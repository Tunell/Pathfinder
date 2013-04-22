import javax.swing.*;
import java.awt.*;

public class Background extends JPanel{
	private static final long serialVersionUID = 8883655096596258223L;
	private Image bild;
	private int width;
	private int height;
	
	public Background(String fil){
		setLayout(null);
		bild = Toolkit.getDefaultToolkit().getImage(fil);
		ImageIcon image = new ImageIcon(fil);
		width = image.getIconWidth();
		height = image.getIconHeight();
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
	    g.setColor(Color.BLUE);
		g.drawImage(bild, 0, 0, width, height, this);
	}
	
	public int getW(){
		return width;
	}
	
	public int getH(){
		return height;
	}

}
