import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;


public class Server {
	List <String> dataObjects = new LinkedList<String>();
	
	public void storeMethod(String dataToStore){
		(new threadOfStoring(dataToStore, this)).start();
	}
	
	public void getObjectList(BlockingQueue <List<String>> answer){
		(new threadOfRetrieving(this, answer)).start();
	}
	
	private synchronized List<String> getDataOfServerObjects(){
		return this.dataObjects;
	}
	
	private synchronized void addObject(String dataToStore){
		this.dataObjects.add(dataToStore);
	}
	


class threadOfStoring extends Thread{
	
	String dataToStore;
	Server server;
	
	public threadOfStoring(String dataToStore, Server server){
		this.dataToStore = dataToStore;
		this.server = server;
	}
	public void run(){
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.server.addObject(this.dataToStore);
	}
}

class threadOfRetrieving extends Thread {
	
	private Server server;
	private BlockingQueue <List<String>> currentanswer;
	public threadOfRetrieving(Server server, BlockingQueue <List<String>> answer){
		this.server = server;
		this.currentanswer = answer;
	}
	
	public void run(){
		try {
			currentanswer.put(server.getDataOfServerObjects());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
	
	
}

