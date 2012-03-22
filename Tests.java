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
	public void testOfAdding() throws InterruptedException {
		
		ThreadControl tc = new ThreadControl(); //Creates a new instance of ThreadControl Class...
		Server server = new Server();// Creates a new instance of server class. This object has the fundamental methods to the program.
		/*Creates a BlockingQueue object which will serve to catch the data which have been stored on Server Object.*/
		BlockingQueue <List<String>> responseList = new LinkedBlockingQueue<List<String>>(); 
		
		/*gets a object from server, saving it into responseList*/
		server.getObjectList(responseList);
		
		/* Puts the responseList result into  retrieveData variable.*/
		List<String> currentObjects = responseList.take();
		
		//BE CAREFUL HERE...This assertion isn't protected by TC.
		assertTrue(currentObjects.size() ==0); 
		/*Sets a state must be expected by test thread*/
		tc.prepare(getStoringOperationHasTerminated() );
		/*Adds a String into server*/
		server.storeMethod("Test");
		/*Forces the test thread to wait until the state, configured up by prepare method, be reached */
		tc.waitUntilStateIsReached();
		/*gets a object from server, saving it into responseList*/
		server.getObjectList(responseList);
		/*Sets a new state must be expected by test thread*/
		tc.prepare(getRetrievingOperationHasTerminated());
		/*Frees possible thread which were blocked by the first call of waitUntilStateIsReached*/
		tc.proceed();
		
		currentObjects = responseList.take();
		
		/*Forces the test thread to wait until the state, configured up by prepare method, be reached */
		tc.waitUntilStateIsReached();
		/*Performs the JUNIT assertion, verifying if the retrieved data from server object is the same that was added on it*/
		assertTrue(currentObjects.size() == 1);
		/*Frees possible thread which were blocked by the last waitUntilStateIsReached allowing the test to continue*/
		tc.proceed();
		
	}
	
	@Test
	public void testOfRetrieving() throws InterruptedException{
		/*
		Creates a BlockingQueue object which will serve to catch the data which have been stored on Server Object.
		*/
		BlockingQueue <List<String>> responseList = new LinkedBlockingQueue<List<String>>(); 
		/*Creates a new server object*/
		Server server = new Server();
		/*Creates a new ThreadControl object which will serve to perform test primitives of TC approach*/
		ThreadControl tc = new ThreadControl();
		
		/*Sets a state must be expected by test thread*/
		tc.prepare(getStoringOperationHasTerminated());
		/*Adds a String into server*/
		server.storeMethod("This is a test!");
		/*Forces the test thread to wait until the state, configured up by prepare method, be reached */
		tc.waitUntilStateIsReached();
		/*Frees possible thread which were blocked by waitUntilStateIsReached*/
		tc.proceed();
		/*Sets a new state must be expected by test thread*/
		tc.prepare(getRetrievingOperationHasTerminated());
		/*gets a object from server, saving it into responseList*/
		server.getObjectList(responseList);
		/*Forces the test thread to wait until the state, configured up by the last prepare method, be reached */
		tc.waitUntilStateIsReached();
		/*Frees possible thread which were blocked by the last waitUntilStateIsReached*/
		tc.proceed();
		/* Puts the responseList result into  retrieveData variable.*/
		List<String> retrievedData= responseList.take();
		/*Performs the JUNIT assertion, verifying if the retrieved data from server object is the same that was added on it*/
		assertTrue(retrievedData.get(0).equals("This is a test!"));
				
	}
	
	
	/*Sets a wanted state of System*/
	public SystemConfiguration getStoringOperationHasTerminated(){
		ThreadConfiguration config1 = new ThreadConfiguration(Server.threadOfStoring.class.getCanonicalName(), ThreadState.FINISHED, ThreadConfiguration.AT_LEAST_ONCE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config1);
		return sysConfig;
	}
	
	/*Sets a wanted state of System*/
	public SystemConfiguration getRetrievingOperationHasTerminated(){
		ThreadConfiguration config1 = new ThreadConfiguration(Server.threadOfRetrieving.class.getCanonicalName(), ThreadState.FINISHED, ThreadConfiguration.AT_LEAST_ONCE);
		ListOfThreadConfigurations sysConfig = new ListOfThreadConfigurations();
		sysConfig.addThreadConfiguration(config1);
		return sysConfig;
	}
}
