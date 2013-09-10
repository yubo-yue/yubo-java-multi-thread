package yubo.test.ch15;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CasCounter {

	private SimulatedCAS value;
	
	public CasCounter()
	{
		this.value = new SimulatedCAS();
	}
	
	public int getValue()
	{
		return value.get();
	}
	
	public int increment()
	{
		int v;
		do {
			v = value.get();
		} while (v != value.compareAndSwap(v, v + 1));
		return v + 1;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final CasCounter counter = new CasCounter();
		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(new Runnable(){

			public void run() {
				for (int i = 0; i < 10; ++i)
				{
					System.out.println("count : " + counter.getValue());
					counter.increment();
				}
			}
			
		});
		
		executorService.execute(new Runnable(){

			public void run() {
				for (int i = 0; i < 10; ++i)
				{
					System.out.println("count : " + counter.getValue());
					counter.increment();
				}
			}
			
		});
		
		executorService.shutdown();
	}

}
