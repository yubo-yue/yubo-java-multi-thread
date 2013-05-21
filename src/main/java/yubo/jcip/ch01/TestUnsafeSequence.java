package yubo.jcip.ch01;

public class TestUnsafeSequence implements Runnable{

	private UnsafeSequence us;
	
	public TestUnsafeSequence(UnsafeSequence us)
	{
		this.us = us;
	}

	public void run() {
		for (int i = 0; i < 1000; i++) {
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(this.us.getValue());
		}
	}

}
