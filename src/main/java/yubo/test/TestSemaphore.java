package yubo.test;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class TestSemaphore {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		Pool shared = new Pool();
		for (int i = 0; i < 20; i++) {
			Thread t = new Thread(new Worker(shared));
			t.start();
		}
		
		for (;;)
		{
			Semaphore s = shared.getBlocker();
			TimeUnit.SECONDS.sleep(1);
			System.out.println("Queue length: " + s.getQueueLength());
		}
	}

	public static class Pool {
		private static final int MAX_AVAILABLE = 10;
		private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

		public Object getItem() throws InterruptedException {
			available.acquire();
			return getNextAvailableItem();
		}

		public void putItem(Object x) {
			if (markAsUnused(x))
				available.release();
		}

		public Pool() {
			for (int i = 0; i < items.length; i++)
				items[i] = new Object();
		}

		protected Object[] items = new Object[MAX_AVAILABLE];
		protected boolean[] used = new boolean[MAX_AVAILABLE];

		protected synchronized Object getNextAvailableItem() {
			for (int i = 0; i < MAX_AVAILABLE; ++i) {
				if (!used[i]) {
					used[i] = true;
					return items[i];
				}
			}
			return null; // not reached
		}

		protected synchronized boolean markAsUnused(Object item) {
			for (int i = 0; i < MAX_AVAILABLE; ++i) {
				if (item == items[i]) {
					if (used[i]) {
						used[i] = false;
						return true;
					} else
						return false;
				}
			}
			return false;
		}
		
		public Semaphore getBlocker()
		{
			return available;
		}

	}

	public static class Worker implements Runnable {
		private Pool pool;

		public Worker(Pool pool) {
			this.pool = pool;
		}

		public void run() {
			System.out.println(Thread.currentThread().getName()
					+ " is producing");
			try {
				int delay = new Random().nextInt(30);
				Object o = pool.getItem();
				TimeUnit.SECONDS.sleep(delay);
				pool.putItem(o);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
