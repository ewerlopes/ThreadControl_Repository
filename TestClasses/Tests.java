import static org.junit.Assert.*;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;

import br.edu.ufcg.threadcontrol.ListOfThreadConfigurations;
import br.edu.ufcg.threadcontrol.SystemConfiguration;
import br.edu.ufcg.threadcontrol.ThreadConfiguration;
import br.edu.ufcg.threadcontrol.ThreadControl;
import br.edu.ufcg.threadcontrol.ThreadState;


public class Tests {

	@Test
	public void addTest() throws InterruptedException {
		
		ThreadControl tc = new ThreadControl(); //It creates a new instance of ThreadControl Class...
		Server server = new Server();// It creates a new instance of server class. This object has the fundamental methods to the program.
		BlockingQueue <List<String>> responseList = new LinkedBlockingQueue<List<String>>(); //it creates a blockingqueue element that will be needed to save the method results
		
		server.getObjectList(responseList);
		
		List<String> currentObjects = responseList.take();
		
		assertTrue(currentObjects.size() ==0);
		tc.prepare(getSavingOperationHasTerminated() );
		server.storeMethod("Test");
		
		tc.waitUntilStateIsReached(); //Faz a thread do teste esperar até que o estado esperado esteja pronto;
		server.getObjectList(responseList);
		tc.prepare(getRetrievingOperationHasTerminated());
		tc.proceed();
		
		currentObjects = responseList.take();
		tc.waitUntilStateIsReached();

		assertTrue(currentObjects.size() == 1);
		assertTrue(currentObjects.get(0).equals("Test"));
		tc.proceed();
		
	}
	
	public SystemConfiguration getSavingOperationHasTerminated(){
		ThreadConfiguration config1 = new ThreadConfiguration(Server.threadOfStoring.class.getCanonicalName(), ThreadState.FINISHED, ThreadConfiguration.AT_LEAST_ONCE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config1);
		return sysConfig;
	}
	
	public SystemConfiguration getRetrievingOperationHasTerminated(){
		ThreadConfiguration config1 = new ThreadConfiguration(Server.threadOfRetrieving.class.getCanonicalName(), ThreadState.FINISHED, ThreadConfiguration.AT_LEAST_ONCE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config1);
		return sysConfig;
	}

}
