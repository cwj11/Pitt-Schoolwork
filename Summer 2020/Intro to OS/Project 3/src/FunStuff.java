
public class FunStuff {

	public static void main(String[] args) {
		int stuff[] = new int[10];
		int n = Integer.MAX_VALUE;
		for(int i = 0; i < n; i++) {
			stuff[(int) (Math.random() *10)]++;
		}
		for(int i = 0; i < 10; i++) {
			System.out.println((double)stuff[i]/n);
		}
	}
}
