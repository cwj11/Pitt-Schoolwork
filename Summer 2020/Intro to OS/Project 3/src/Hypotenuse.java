
public class Hypotenuse {
	
	public static void main(String[] args) {
		for(int i = -50;i <= 50; i++) {
			for(int k = i; k <= 50; k++) {
				for(int j = k; j<=50; j++) {
					System.out.println(i*i*i + k*k*k + j*j*j);
				}
			}
		}
	}
}
