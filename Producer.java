public class Producer extends Thread {

	private Server server;
	private String threadname;

	public Producer(String threadname, Server server) {
		this.server = server;
		this.threadname = threadname;
	}

	public void run() {
		for (int k = 0; k < 5; k++) {
			this.server.storeMethod(threadname + " writing for the "+"[" + k + "th] time!");
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
