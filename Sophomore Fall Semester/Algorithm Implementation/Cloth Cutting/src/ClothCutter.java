
import java.util.ArrayList;
import java.util.Collections;


public class ClothCutter {
	private int width;
	private int height;
	private int value;
	private ArrayList<Pattern> patterns;
	private ArrayList<Garment> garments;
	private ClothPiece[][] memo;
	private ArrayList<Cut> cuts;
	private int minWidth;
	private int minHeight;

	public ClothCutter(int w, int h, ArrayList<Pattern> p) {
		width = w;
		height = h;
		patterns = p;
		value = 0;
		garments = new ArrayList<Garment>();
		cuts = new ArrayList<Cut>();
		minWidth = Integer.MAX_VALUE;
		minHeight = Integer.MAX_VALUE;
		for(int i = 0; i < patterns.size(); i++) {
			if(patterns.get(i).width() < minWidth)
				minWidth = patterns.get(i).width();
			if(patterns.get(i).height() < minHeight)
				minHeight = patterns.get(i).height();
		}
		memo = new ClothPiece[width+1][height+1];


	}

	public int value() {
		return value;
	}

	public ArrayList<Garment> garments(){
		return garments;
	}

	public ArrayList<Cut> cuts(){
		return cuts;
	}


	public void optimize() {
		ClothPiece best = null;
		int xCut1 = 0, yCut1 = 0, xCut2 = 0, yCut2 = 0;
		for(int i = minWidth; i < width; i += 1) {
			ClothPiece leftRect = optimize(i, height, 0, 0);
			ClothPiece rightRect = optimize(width - i, height, i, 0);
			if(value < leftRect.value() + rightRect.value()) {
				value = leftRect.value() + rightRect.value();
				best = ClothPiece.addVertical(leftRect, rightRect);
				xCut1 = i;
				yCut1 = 0;
				xCut2 = i;
				yCut2 = height;
			}
		}
		for(int i = minHeight; i < height; i += 1) {
			ClothPiece topRect = optimize(width, i, 0, 0);
			ClothPiece bottomRect = optimize(width, height - i, 0, i);
			if(value < topRect.value() + bottomRect.value()) {
				value = topRect.value() + bottomRect.value();
				best = ClothPiece.addHorizontal(topRect, bottomRect);
				xCut1 = 0;
				yCut1 = i;
				xCut2 = width;
				yCut2 = i;
			}
		}
		cuts = best.cuts();
		garments = best.garments();
		cuts.add(new Cut(xCut1, yCut1, xCut2, yCut2));
		Collections.sort(garments);
//		Collections.sort(cuts);

	}

	public ClothPiece optimize(int garmentWidth, int garmentHeight, int x, int y) {
		ClothPiece best = new ClothPiece(0,0,0);
		if(garmentHeight < minHeight || garmentWidth < minWidth)
			return new ClothPiece(garmentWidth, garmentHeight, 0);
		
		if(memo[garmentWidth][garmentHeight] != null) {
			return ClothPiece.memoToPiece(memo[garmentWidth][garmentHeight], x, y);
		}
		
		Pattern bestPattern = null;
		for(int i = 0; i < patterns.size(); i++) {
			if(patterns.get(i).width() == garmentWidth && patterns.get(i).height() == garmentHeight) {
				if(patterns.get(i).value() > best.value()) {
					best = new ClothPiece(patterns.get(i).width(), patterns.get(i).height(), patterns.get(i).value());
					bestPattern = patterns.get(i);
				}
			}
		}
		if(bestPattern != null) {
			best.addGarment(new Garment(bestPattern, x, y));
		}
		int xCut1 = 0, yCut1 = 0, xCut2 = 0, yCut2 = 0;
		for(int i = minWidth; i < garmentWidth; i += 1) {
			ClothPiece leftRect = optimize(i, garmentHeight, x, y);
			ClothPiece rightRect = optimize(garmentWidth - i, garmentHeight, x + i, y);
			if(leftRect.value() + rightRect.value() > best.value()) {
				best = ClothPiece.addVertical(leftRect, rightRect);
				xCut1 = x + i;
				yCut1 = y;
				xCut2 = x + i;
				yCut2 = y + garmentHeight;
			}
		}
		for(int i = minHeight; i < garmentHeight; i += 1) {
			ClothPiece topRect = optimize(garmentWidth, i, x, y);
			ClothPiece bottomRect = optimize(garmentWidth, garmentHeight - i, x, y+i);
			if(topRect.value() + bottomRect.value() > best.value()) {
				best = ClothPiece.addHorizontal(topRect, bottomRect);
				xCut1 = x;
				yCut1 = y + i;
				xCut2 = x + garmentWidth;
				yCut2 = y + i;
			}
		}
		best.addCut(new Cut(xCut1, yCut1, xCut2, yCut2));

		memo[garmentWidth][garmentHeight] = ClothPiece.createMemo(best, x, y);

		return best;
	}



}
