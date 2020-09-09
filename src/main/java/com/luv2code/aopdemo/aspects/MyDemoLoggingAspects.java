package com.luv2code.aopdemo.aspects;

import java.util.List;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.luv2code.aopdemo.Account;

@Aspect
@Component
@Order(2)
public class MyDemoLoggingAspects {
	
	//add an around advice for demonstrate exception Handling
	@Around("execution(* com.luv2code.aopdemo.service.TrafficFortuneService.getFortune(Boolean))")
	public Object AroundGetFortuneExceptionHandling(
			ProceedingJoinPoint theProceedingJoinPoint) throws Throwable{
		
		//print out method, we are advising on
		String theMethod = theProceedingJoinPoint.getSignature().toShortString();
		System.out.println("\n========>Executing @Around advise on method :"+theMethod);
		
		//now, let's execute the method
		Object result=null;
		
		try {
			result = theProceedingJoinPoint.proceed();
		} catch (Exception e) {
			//log the exception
			System.out.println(e.getMessage());
			
			//re-throw the exception to the calling program
			throw e;
			
			//give user a custom message
			//result="Major Accident!!, But no worries, your private AOP helicopter is on the way!";
		}
		
		return result;
	}
	
	
	
	
	@Around("execution(* com.luv2code.aopdemo.service.TrafficFortuneService.getFortune())")
	public Object AroundGetFortune(
			ProceedingJoinPoint theProceedingJoinPoint) throws Throwable{
		
		//print out method, we are advising on
		String theMethod = theProceedingJoinPoint.getSignature().toShortString();
		System.out.println("\n========>Executing @Around advise on method :"+theMethod);
		
		//get begin time-stamp
		long begin = System.currentTimeMillis();
		
		//now, let's execute the method
		Object result = theProceedingJoinPoint.proceed();
		
		//get end time-stamp
		long end = System.currentTimeMillis();
		
		//compute the duration and display it
		long duration = end-begin;
		System.out.println("\n=====>duration :"+duration/1000+" seconds");
		
		return result;
	}
	
	@After("execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))")
	public void afterFinallyFindAccountsAdvice(JoinPoint theJoinPoint){
		
		//print out on which method, we are advising on
		String theMethod = theJoinPoint.getSignature().toShortString();
		System.out.println("\n========>Executing @After (finally) advise on method :"+theMethod);
		
	}
	
	@AfterThrowing(pointcut="execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))",
			       throwing="theExc")
	public void afterThrowingFindAccountsAdvice(
			JoinPoint theJoinPoint, Throwable theExc){
		
		//print out on which method, we are advising on
		String theMethod = theJoinPoint.getSignature().toShortString();
		System.out.println("\n========>Executing @AfterThrowing advise on method :"+theMethod);
		
		//log the exception
		System.out.println("\n=========>exception is :"+theExc);
	}
	
	@AfterReturning(
			pointcut="execution(* com.luv2code.aopdemo.dao.AccountDAO.findAccounts(..))",
			returning="result")
	public void afterReturningFindAccountsAdvice(
			JoinPoint theJoinPoint, List<Account> result){
		
		//print out on which method, we are advising on
		String theMethod = theJoinPoint.getSignature().toShortString();
		System.out.println("\n========>Executing @AfterReturning advise on method :"+theMethod);
		
		//print the result of method call
		System.out.println("\n=========>result is :"+result);
		
		//let's post-process the data....let's modify it
		
		//convert the name to upper-case
		convertNamesToUpperCase(result);
		
		System.out.println("\n=========>result is :"+result);
		
	}
	
	private void convertNamesToUpperCase(List<Account> result) {
		// loop through the result
		for(Account theAccount: result){
		//get the upper-case version of name
		String upperName = theAccount.getName().toUpperCase();
		
		//update the account
		theAccount.setName(upperName);
		}
	}

	@Before("com.luv2code.aopdemo.aspects.LuvAOPExpression.forDaoPackageExcludeGetterSetter()")
	public void beforeAddAccountAdvice(JoinPoint theJoinPoint){
		System.out.println("\n====>Executing @Before advice on method()");
		
		//display the method signature
		MethodSignature methodSign = (MethodSignature)theJoinPoint.getSignature();
		System.out.println("Method :"+methodSign);
		
		//display the method parameters
		Object[] args = theJoinPoint.getArgs();
		for(Object tempArg : args){
			System.out.println(tempArg);
			
			if(tempArg instanceof Account){
				//downcast and print account specific info
				Account theAccount=(Account) tempArg;
				System.out.println("Account Name :"+theAccount.getName());
				System.out.println("Account Level :"+theAccount.getLevel());
			}
		}
	}
	
}
