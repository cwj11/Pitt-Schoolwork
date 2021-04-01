import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Amazon {

	public static void main(String[] args) {
		int suppliers[] = new int[10000];
		for(int i = 0; i < suppliers.length; i++) {
			suppliers[i] = (int) (Math.random() * 500 + 1000);
		}
		int order = 100000;
		Queue<Integer> que = new PriorityQueue<Integer>((x,y)->Integer.compare(y,x));
		for(int i = 0; i < suppliers.length; i++) {
			que.add(suppliers[i]);
		}
		int ret = 0;
		while(!que.isEmpty() && order-- > 0) {
			int temp = que.poll();
			ret += temp;
			if(temp > 1) {
				que.add(temp-1);
			}
		}
		System.out.println(ret);
	}
}
