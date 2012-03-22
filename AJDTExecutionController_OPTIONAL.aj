
public aspect AJDTExecutionController_OPTIONAL {
	
	pointcut RunningRunMethod(): execution(void *.run());
	pointcut RunningTest(): call (* *.assertTrue*(..));
	
	before(): RunningTest(){
		System.out.println("&&&&&Executing test Method: "+" >> "+thisJoinPoint);
	}
	
	before(): RunningRunMethod(){
		System.out.println("#####Starting:"
				+ Thread.currentThread().getName()+" >> "+thisJoinPoint);
	}
}
