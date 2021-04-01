
public class LeftPrime {

	public static void main(String[] args) {
		long max = 0;
		long temp = 0;
		temp = largestPrime(2, 0);
		if(temp > max)
			max = temp;
		temp = largestPrime(3, 0);
		if(temp > max)
			max = temp;
		temp = largestPrime(5, 0);
		if(temp > max)
			max = temp;
		temp = largestPrime(7, 0);
		if(temp > max)
			max = temp;
		System.out.println(max);

	}
	
	public static boolean isPrime(long num) {
		long max = (long) Math.sqrt(num);
		for(long i = 2; i < max; i++) {
			if(num % i == 0)
				return false;
		}
		return true;
	}
	
	
	public static long largestPrime(long num, long max) {
		for(long i = 1; i < 10; i++) {
			long test = num * 10 + i;
			if(isPrime(test)) {
				if(test>max)
					max = test;
				test = largestPrime(test, max);
				if(test>max)
					max = test;
			}
			
		}
		return max;
	}

}
