package com.luv2code.aopdemo;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.luv2code.aopdemo.service.TrafficFortuneService;

public class AroundDemoApp {

	public static void main(String[] args) {
		// read the java config class
		AnnotationConfigApplicationContext context = 
				new AnnotationConfigApplicationContext(DemoConfig.class);
		
		//get the bean from spring container
		TrafficFortuneService theFortuneService = 
				context.getBean("trafficFortuneService",TrafficFortuneService.class);
		
		System.out.println("\nMain Program : AroundDemoApp");
		
		System.out.println("calling getFortune");
		
		String theFortune = theFortuneService.getFortune();
		
		System.out.println("\nMy Fortune is :"+theFortune);
		
		System.out.println("Finished!!");
		
		//close the context
		context.close();

	}

}
