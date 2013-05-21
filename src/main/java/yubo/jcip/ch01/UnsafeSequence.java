package yubo.jcip.ch01;

public class UnsafeSequence {

	private int value;
	
	public int getValue()
	{
		return value++;
	}
	
	public static void main(String[] args)
	{
		UnsafeSequence us = new UnsafeSequence();
		Thread t1 = new Thread(new TestUnsafeSequence(us));
		Thread t2 = new Thread(new TestUnsafeSequence(us));
		
		t1.start();
		t2.start();
	}
	
}
