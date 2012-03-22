import java.awt.HeadlessException;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Consumer extends Thread {

	private Server server;

	public Consumer(Server server) {
		this.server = server;
	}

	public void run() {

		for (int k = 0; k < 8; k++) {
			BlockingQueue<List<String>> consumerAnswer = new LinkedBlockingQueue<List<String>>();
			server.getObjectList(consumerAnswer);
			try {
//				if (consumerAnswer.take().size() == 0) { //CRAZY DEADLOCK HERE!
//					System.out.println("Empty");
//				} else {
					System.out.println("***** CONSUMING: ");
					System.out.println(consumerAnswer.take().toString());
					System.out.println("\n");
//				}
				Thread.sleep(2000);
			} catch (HeadlessException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
