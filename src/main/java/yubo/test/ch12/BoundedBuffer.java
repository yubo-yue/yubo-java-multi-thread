package yubo.test.ch12;

import java.util.concurrent.Semaphore;

public class BoundedBuffer<E> {

	private final Semaphore availableItems, availableSpaces;
	private final E[] items;
	private int putPosition = 0, takePosition = 0;
	
	public BoundedBuffer(int capacity)
	{
		availableItems = new Semaphore(0);
		availableSpaces = new Semaphore(capacity);
		items = (E[])new Object[capacity];
	}
	
	public boolean isEmpty()
	{
		return availableItems.availablePermits() == 0;
	}
	
	public boolean isFull()
	{
		return availableSpaces.availablePermits() == 0;
	}
	
	public void put(E e) throws InterruptedException
	{
		availableSpaces.acquire();
		doInsert(e);
		availableItems.release();
	}
	
	public E take() throws InterruptedException
	{
		availableItems.acquire();
		E item = doExtract();
		availableSpaces.release();
		return item;
	}
	
	private synchronized void doInsert(E e)
	{
		int i = putPosition;
		items[i] = e;
		putPosition = (++i == items.length) ? 0 : i;
	}
	
	private synchronized E doExtract()
	{
		int i = takePosition;
		E x = items[i];
		items[i] = null;
		takePosition = (++i == items.length) ? 0 : i;
		return x;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {

	}

}
