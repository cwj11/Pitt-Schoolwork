import java.util.ArrayList;

public class ClothPiece {
	private int width;
	private int height;
	private int value;
	ArrayList<Garment> garments;
	ArrayList<Cut> cuts;
	
	public ClothPiece(int w, int h, int v, ArrayList<Garment> g, ArrayList<Cut> c) {
		width = w;
		height = h;
		value = v;
		garments = g;
		cuts = c;
	}
	
	public ClothPiece(int w, int h, int v) {
		width = w;
		height = h;
		value = v;
		garments = new ArrayList<Garment>();
		cuts = new ArrayList<Cut>();
	}
	
	public int width() {
		return width;
	}
	
	public int height() {
		return height;
	}
	
	public int value() {
		return value;
	}
	
	public void addGarment(Garment g) {
		garments.add(g);
	}
	
	public void addCut(Cut c) {
		cuts.add(c);
	}
	
	public ArrayList<Garment> garments(){
		return garments;
	}
	
	public ArrayList<Cut> cuts(){
		return cuts;
	}
	
	public static ClothPiece addVertical(ClothPiece leftPiece, ClothPiece rightPiece) {
		ClothPiece c = new ClothPiece(leftPiece.width() + rightPiece.width(), leftPiece.height(), leftPiece.value() + rightPiece.value());
		for(Garment g: leftPiece.garments()) {
			c.addGarment(g);
		}
		for(Cut cut: leftPiece.cuts()) {
			c.addCut(cut);
		}
		for(Garment g: rightPiece.garments()) {
			c.addGarment(g);
		}
		for(Cut cut: rightPiece.cuts()) {
			c.addCut(cut);
		}
		return c;
	}
	
	public static ClothPiece addHorizontal(ClothPiece topPiece, ClothPiece bottomPiece) {
		ClothPiece c = new ClothPiece(topPiece.width(), topPiece.height() + bottomPiece.height(), topPiece.value() + bottomPiece.value());
		for(Garment g: topPiece.garments()) {
			c.addGarment(g);
		}
		for(Cut cut: topPiece.cuts()) {
			c.addCut(cut);
		}
		for(Garment g: bottomPiece.garments()) {
			c.addGarment(g);
		}
		for(Cut cut: bottomPiece.cuts()) {
			c.addCut(cut);
		}
		return c;
	}
	
	public static ClothPiece createMemo(ClothPiece piece, int x, int y) {
		ArrayList<Garment> g = new ArrayList<Garment>();
		ArrayList<Cut> c = new ArrayList<Cut>();
		for(Garment temp: piece.garments()) {
			g.add(new Garment(temp.pattern(), temp.x() - x, temp.y() - y));
		}
		for(Cut temp: piece.cuts) {
			c.add(new Cut(temp.x1() - x, temp.y1() - y, temp.x2() - x, temp.y2() - y));
		}
		return new ClothPiece(piece.width(), piece.height(), piece.value(), g, c);
	}
	
	public static ClothPiece memoToPiece(ClothPiece memo, int x, int y) {
		ArrayList<Garment> g = new ArrayList<Garment>();
		ArrayList<Cut> c = new ArrayList<Cut>();
		for(Garment temp: memo.garments()) {
			g.add(new Garment(temp.pattern(), temp.x() + x, temp.y() + y));
		}
		for(Cut temp: memo.cuts) {
			c.add(new Cut(temp.x1() + x, temp.y1() + y, temp.x2() + x, temp.y2() + y));
		}
		return new ClothPiece(memo.width(), memo.height(), memo.value(), g, c);
	}
}
