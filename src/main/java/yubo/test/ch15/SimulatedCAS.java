package yubo.test.ch15;

import yubo.concurrent.annotations.GuardedBy;

public class SimulatedCAS {

	@GuardedBy("this") 
	private int value;
	
	public synchronized int get()
	{
		return value;
	}
	
	public synchronized int compareAndSwap(int expectedValue, int newValue)
	{
		int oldValue = value;
		if (oldValue == expectedValue)
			value = newValue;
		return oldValue;
	}
	
	public synchronized boolean compareAndSet(int expectedValue, int newValue)
	{
		return (expectedValue == compareAndSwap(expectedValue, newValue));
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
