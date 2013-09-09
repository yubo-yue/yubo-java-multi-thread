package yubo.test.ch10;

import java.util.concurrent.TimeUnit;

public class LeftRightDeadlock implements Runnable{

	private final Object left = new Object();
	private final Object right = new Object();
	
	public void leftRight()
	{
		synchronized(left)
		{
			try {
				TimeUnit.SECONDS.sleep(20);
			} catch (InterruptedException ignore) {
			}
			System.out.print(Thread.currentThread().getName() + " is running");
			synchronized(right)
			{
				
			}
		}
	}
	
	public void rightLeft()
	{
		synchronized(right)
		{
			try {
				TimeUnit.SECONDS.sleep(20);
			} catch (InterruptedException ignore) {
			}
			System.out.print(Thread.currentThread().getName() + " is running");
			synchronized(left)
			{
				
			}
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final LeftRightDeadlock lrLock = new LeftRightDeadlock();
		
		Thread a = new Thread(new Runnable() {
			public void run(){
				lrLock.rightLeft();
			}
		}, "Thread 1");
		
		Thread b = new Thread(new Runnable() {
			public void run() {
				lrLock.leftRight();
			}
		}, "Thread 2");
		
		a.start();
		b.start();
	}

	public void run() {
		leftRight();
	}

}
