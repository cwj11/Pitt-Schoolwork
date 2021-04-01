
public class MemoryReference {

	public long address;
	public boolean dirty;
	public int next;
	
	public MemoryReference(long addr, boolean d) {
		address = addr;
		dirty = d;
	}
	
	public String toString() {
		return "(Address: " + Long.toHexString(address) + ", Dirty Bit: " + dirty + ", OPT next: " + next + ")";
	}
}
