package yubo.test.ch14;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import yubo.concurrent.annotations.GuardedBy;

public class ConditionBoundedBuffer<T> {

	protected final Lock lock = new ReentrantLock();
	//Condition predicate: notFull count < items.length
	private final Condition notFull = lock.newCondition();
	private final Condition notEmpty = lock.newCondition();
	@GuardedBy("lock") private int tail, head, count;
	
	private final T[] items = (T[]) new Object[100];
	
	public ConditionBoundedBuffer()
	{
		tail = head = count = 0;
	}
	
	public void put(T x) throws InterruptedException
	{
		lock.lock();
		try {
			while (count == items.length)
				notFull.await();
			items[tail] = x;
			if (++tail == items.length)
				tail = 0;
			++count;
			notEmpty.signalAll();
		} finally {
			lock.unlock();
		}
	}
	
	public T take() throws InterruptedException
	{
		lock.lock();
		try {
			while (count == 0)
				notEmpty.await();
			T x = items[head];
			if (++head == items.length)
				head = 0;
			--count;
			notFull.signalAll();
			return x;
		} finally {
			lock.unlock();
		}
	}
	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		final ConditionBoundedBuffer buffer = new ConditionBoundedBuffer<Integer>();
		ExecutorService service = Executors.newCachedThreadPool();
		service.execute(new Runnable() {
			public void run()
			{
				int i = 100;
				Random rnd = new Random(System.currentTimeMillis());
				do {
					try {
						buffer.put(new Integer(i));
						System.out.println("put value : " + i);
						TimeUnit.SECONDS.sleep(rnd.nextInt(10));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (i-- > 0);
			}
		});
		
		service.execute(new Runnable() {
			public void run()
			{
				Random rnd = new Random(System.currentTimeMillis() * 2);
				int i = 100;
				do {
					try {
						Integer v = (Integer) buffer.take();
						System.out.println("get value : " + v);
						TimeUnit.SECONDS.sleep(rnd.nextInt(10));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (i-- > 0);
			}
		});
		service.awaitTermination(1000, TimeUnit.SECONDS);
	}

}
