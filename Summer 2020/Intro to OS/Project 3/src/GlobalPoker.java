import java.text.DecimalFormat;

public class GlobalPoker {

	public static void main(String[] args) {
		// Gill Global Font
		DecimalFormat df = new DecimalFormat("0.000");
		double fontsize = Math.random() * 3 + 17;
		double spaceRange = (fontsize-17)/30 + 0.7;
		//System.out.println(df.format(fontsize));
		//System.out.println(df.format(Math.random() * 0.1 + spaceRange));
		
		
		// Connor Chumba Font
		fontsize = Math.random() * 3 + 19;
		spaceRange = (fontsize-19)/30 + 0.70;
		System.out.println(df.format(fontsize));
		System.out.println(df.format(Math.random() * 0.1 + spaceRange));
	}

}
