import java.awt.* ;
import javax.swing.* ;

public class Cloth extends JPanel{
	
	public Color BACKGROUND_COLOR  = Color.blue ;
	public Color LINE_COLOR        = Color.black ;
	public Color RECTANGLE_COLOR   = Color.yellow ;
	public Color BORDER_COLOR      = Color.black ;
	public int   TEXT_OFFSET       = 6 ;

	public int width, height, pixels ;

	public Cloth(int w, int h, int p) {
		width = w ;
		height = h ;
		pixels = p ;
		setPreferredSize(new Dimension(width*pixels,height*pixels)) ;
		
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g) ;
		g.setColor(BACKGROUND_COLOR) ;
		g.fillRect(0,0,width*pixels,height*pixels) ;
	}

	public void drawCut(Cut c) {
		Graphics g = this.getGraphics() ;
		g.setColor(LINE_COLOR) ;
		g.drawLine(c.x1()*pixels,c.y1()*pixels,c.x2()*pixels,c.y2()*pixels) ;
	}

	public void drawGarment(Garment garment) {
		int x = garment.x();
		int y = garment.y();
		int h = garment.pattern().height();
		int w = garment.pattern().width();
		String label = garment.pattern().type();
		Graphics g = this.getGraphics() ;
		g.setColor(RECTANGLE_COLOR) ;
		g.fillRect(x*pixels,y*pixels,w*pixels,h*pixels) ;
		g.setColor(BORDER_COLOR) ;
		g.drawRect(x*pixels,y*pixels,w*pixels,h*pixels) ;
		g.drawString(label,x*pixels+TEXT_OFFSET,y*pixels+2*TEXT_OFFSET) ;
	}
	
	public void drawCloth() {
		Graphics g = this.getGraphics();
		g.setColor(RECTANGLE_COLOR);
		g.fillRect(0, 0, width*pixels, height*pixels);
		g.setColor(BORDER_COLOR);
		g.drawRect(0, 0, width*pixels, height*pixels);
	}
}
