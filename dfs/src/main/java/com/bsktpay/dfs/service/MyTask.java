package com.bsktpay.dfs.service;

public class MyTask implements Runnable{
	 @Override
	    public void run() {
		 System.out.println("Testing Async");
		 try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 System.out.println("finished");
		 
	    }

}
