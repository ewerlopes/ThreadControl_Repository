public class MainProgram {
	
	public static void main(String args[]) throws InterruptedException {
	
		Server serverObject = new Server();
		Producer prod1 = new Producer("Prod1", serverObject);
		Producer prod2 = new Producer("Prod2", serverObject);
		Consumer cons = new Consumer(serverObject);
		
		prod1.start();
		prod2.start();
		cons.start();
		cons.join();
		
		System.out.println("THE PROGRAM HAS FINISHED!");
	}
}
