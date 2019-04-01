package com.shi.method;

import com.shi.inter.FabonacciInter;

public class Fabonacci implements FabonacciInter{

	public int fib(int n){
		if(n==0) return 0;
		if(n==1) return 1;
		return fib(n-1)+fib(n-2);
	}
	
}
