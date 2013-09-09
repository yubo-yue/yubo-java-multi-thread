package yubo.test.ch12;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.junit.Test;

public class BoundedBufferTest {

	@Test
	public void test() {
		BoundedBuffer<Integer> iBuffer = new BoundedBuffer<Integer>(10);
		assertTrue(iBuffer.isEmpty());
		assertFalse(iBuffer.isFull());
	}
	
	@Test
	public void testBlockWhenEmpty()
	{
		final BoundedBuffer<Integer> iBuffer = new BoundedBuffer<Integer>(10);
		Thread taker = new Thread(new Runnable()
		{
			public void run()
			{
				try {
					int unused = iBuffer.take();
					fail();
				} catch (InterruptedException success) {
				}
			}
		});
		
		try {
			taker.start();
			TimeUnit.SECONDS.sleep(100);
			taker.interrupt();
			taker.join(100);
		} catch (InterruptedException e) {
			fail();
		}
		
	}

}
