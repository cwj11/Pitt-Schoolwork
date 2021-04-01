/** CS 0445 Spring 2018 (Adapted  from Dr. John Ramirez's assignment code)
 This is a partial implementation of the ReallyLongHex class.  You need to
 complete the implementations of the remaining methods.  Also, for this class
 to work, you must complete the implementation of the LinkedDS class.
 See additional comments below.
 */

public class ReallyLongHex 	extends LinkedDS<Character> 
implements Comparable<ReallyLongHex>
{
	// Instance variables are inherited.  You may not add any new instance variables

	// Default constructor
	private ReallyLongHex()
	{
		super();
	}

	// Note that we are adding the digits here in the END. This results in the 
	// MOST significant digit first in the chain.  
	// It is assumed that String s is a valid representation of an
	// unsigned integer with no leading zeros.
	public ReallyLongHex(String s)
	{
		super();
		char c;
		// Iterate through the String, getting each character and adding it 
		// at the end of the list.  
		for (int i = 0; i < s.length(); i++)
		{
			c = s.charAt(i);
			if ((('0' <= c) && (c <= '9')) || (('A' <= c) && (c <= 'F')))
			{
				this.addItem(c);
			}
			else throw new NumberFormatException("Illegal digit " + c);
		}
	}

	// Simple call to super to copy the nodes from the argument ReallyLongHex
	// into a new one.
	public ReallyLongHex(ReallyLongHex rightOp)
	{
		super(rightOp);
	}

	// Method to put digits of number into a String.  We traverse the chain 
	// to add the digits to a StringBuilder. 
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (!empty())
		{
			sb.append("0x");
			for (Node curr = firstNode; curr != null; 
					curr = curr.getNextNode())
			{
				sb.append(curr.getData());
			}
		}
		return sb.toString();
	}

	// You must implement the methods below.  See the descriptions in the
	// assignment sheet

	public ReallyLongHex add(ReallyLongHex rightOp)
	{
		ReallyLongHex ans = new ReallyLongHex();
		reverse();
		rightOp.reverse();
		Node currentNodeThis = this.firstNode, currentNodeRight = rightOp.firstNode;
		int carryOver = 0;
		while(currentNodeThis != null || currentNodeRight != null) {
			int numThis, numRight, nextNum;
			if(currentNodeThis == null)
				numThis = 0;
			else {
				if(currentNodeThis.getData() >= 'A') {
					numThis = currentNodeThis.getData() - 'A' + 10;
				}else {
					numThis = currentNodeThis.getData() - '0';
				}
			}
			if(currentNodeRight == null)
				numRight = 0;
			else {
				if(currentNodeRight.getData() >= 'A') {
					numRight = currentNodeRight.getData() - 'A' + 10;
				}else {
					numRight = currentNodeRight.getData() - '0';
				}
			}
			nextNum = numThis + numRight + carryOver;
			carryOver = nextNum / 16;
			nextNum = nextNum%16;
			char next;
			if(nextNum > 9)
				next = (char) (nextNum - 10 + 'A');
			else
				next = (char) (nextNum + '0');
			ans.addItem(next);
			if(currentNodeThis != null)
				currentNodeThis = currentNodeThis.getNextNode();
			if(currentNodeRight != null)
				currentNodeRight = currentNodeRight.getNextNode();
		}
		if(carryOver > 0)
			ans.addItem('1');
		ans.reverse();
		reverse();
		rightOp.reverse();
		return ans;
	}

	public ReallyLongHex subtract(ReallyLongHex rightOp)
	{
		ReallyLongHex ans = new ReallyLongHex();
		reverse();
		rightOp.reverse();
		Node currentNodeThis = this.firstNode, currentNodeRight = rightOp.firstNode, nextNode;
		int carryOver = 0;
		while(currentNodeThis != null) {
			nextNode = currentNodeThis.getNextNode();
			int numThis, numRight, nextNum;
			if(currentNodeThis.getData() >= 'A') {
				numThis = currentNodeThis.getData() - 'A' + 10;
			}else {
				numThis = currentNodeThis.getData() - '0';
			}

			if(currentNodeRight == null)
				numRight = 0;
			else {
				if(currentNodeRight.getData() >= 'A') {
					numRight = currentNodeRight.getData() - 'A' + 10;
				}else {
					numRight = currentNodeRight.getData() - '0';
				}
				currentNodeRight = currentNodeRight.getNextNode();
			}
			nextNum = numThis - numRight + carryOver; 
			if(nextNum < 0) {
				carryOver = -1;
				nextNum += 16;
			}else
				carryOver = 0;
			char next;
			if(nextNum > 9)
				next = (char) (nextNum - 10 + 'A');
			else
				next = (char) (nextNum + '0');
			ans.addItem(next);
			
			currentNodeThis = nextNode;
		}
		if(currentNodeRight != null || carryOver < 0)
			throw new ArithmeticException();
		reverse();
		rightOp.reverse();
		ans.reverse();
		while(ans.firstNode.getData() == '0')
			ans.shiftLeft();
		return ans;
	}


	public int compareTo(ReallyLongHex rOp)
	{
		int lengthThis = size(), lengthRight = rOp.size();
		if(lengthThis < lengthRight)
			return -1;
		if(lengthThis > lengthRight)
			return 1;
		Node currentNodeThis = firstNode, currentNodeRight = rOp.firstNode;
		while(currentNodeThis != null && currentNodeRight != null) {
			int numThis = currentNodeThis.getData();
			int numRight = currentNodeRight.getData();
			if(numThis > numRight)
				return 1;
			if(numThis < numRight)
				return -1;
			currentNodeThis = currentNodeThis.getNextNode();
			currentNodeRight = currentNodeRight.getNextNode();
		}
		return 0;
	}

	public boolean equals(Object rightOp)
	{
		if(!(rightOp instanceof ReallyLongHex))
			return false;
		return compareTo((ReallyLongHex) rightOp) == 0;
	}

	public void mult16ToThe(int num)
	{
		for(int i = 0; i < num; i++) {
			addItem('0');
		}
	}

	public void div16ToThe(int num)
	{
		rightShift(num);
	}
}
