import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class vmsim {

	public static void main(String[] args) throws FileNotFoundException {
		if(args.length != 7) {
			System.out.println("Usage: ./vmsim -n <numframes> -p <pagesize> -a <opt|lru> <tracefile>");
			return;
		}

		int numframes = Integer.parseInt(args[1]);
		int pagesize = Integer.parseInt(args[3]);
		if(pagesize != 8 && pagesize != 16 && pagesize != 32 && pagesize != 4) {
			System.out.println("Invalid page size.");
			return;
		}
		boolean opt = args[5].equalsIgnoreCase("opt");
		String filename = args[6];
		//System.out.println(numframes + " " + pagesize + " " + opt + " " + filename); 

		Scanner scanner = new Scanner(new File(filename));

		ArrayList<MemoryReference> trace = new ArrayList<MemoryReference>();
		while(scanner.hasNext()) {
			String type = scanner.next();
			long address = Long.parseLong(scanner.next().substring(2).toUpperCase(), 16);
			trace.add(new MemoryReference((address/pagesize)/1024, type.equals("s")));
			//trace.add(new MemoryReference(address, type.equals("s")));
		}
		//System.out.println(trace);
		scanner.close();

		int numAccesses = trace.size();
		int numPageFaults = 0;
		int numWrites = 0;
		if(opt) {
			for(int i = 0; i < trace.size(); i++) {
				long addr = trace.get(i).address;
				int index = i+1;
				while(index < trace.size() && trace.get(index).address != addr) {
					index++;
				}
				trace.get(i).next = index;
			}
			//System.out.println(trace.get(2).next);
			LinkedList<MemoryReference> lru = new LinkedList<MemoryReference>();
			for(int i = 0; i < trace.size(); i++) {
				int index = isInList(lru, trace.get(i));
				if(index > -1) {
					lru.remove(index);
					lru.add(trace.get(i));
				}else {
					if(lru.size() >= numframes) {
						int fault = findOpt(lru);
						if(lru.get(fault).dirty)
							numWrites++;
						lru.remove(fault);
						numPageFaults++;
						lru.add(trace.get(i));
					}else {
						numPageFaults++;
						lru.add(trace.get(i));
					}
				}
				//System.out.println(lru);
			}
		}else {
			LinkedList<MemoryReference> lru = new LinkedList<MemoryReference>();
			for(int i = 0; i < /*trace.size()*/100; i++) {
				int index = isInList(lru, trace.get(i));
				if(index > -1) {
					lru.remove(index);
					lru.add(trace.get(i));
				}else {
					if(lru.size() == numframes) {
						MemoryReference fault = lru.pop();
						System.out.println(fault);
						if(fault.dirty)
							numWrites = numWrites+1;
						numPageFaults++;
						lru.add(trace.get(i));
					}else {
						numPageFaults++;
						lru.add(trace.get(i));
					}
				}
			}
		}

		if(opt)
			System.out.println("Algorithm: OPT");
		else
			System.out.println("Algorithm: LRU");
		System.out.println("Number of frames: " + numframes);
		System.out.println("Page size: " + pagesize + " KB");
		System.out.println("Total memory accesses: " + numAccesses);
		System.out.println("Total page faults: " + numPageFaults);
		System.out.println("Total writes to disk: " + numWrites);

	}





	public static int isInList(LinkedList<MemoryReference> lru, MemoryReference m){
		for(int i = 0; i < lru.size(); i++) {
			if(m.address == lru.get(i).address)
				return i;
		}
		return -1;
	}

	public static int findOpt(LinkedList<MemoryReference> lru) {
		int max = -1;
		int index = -1;
		for(int i = 0; i < lru.size(); i++) {
			if(lru.get(i).next > max) {
				index = i;
				max = lru.get(i).next;
			}
		}
		return index;
	}

}
