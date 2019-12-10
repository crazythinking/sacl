package net.engining;

import java.util.Date;

public class FooImpl implements Foo{

	@Override
	public void ma() {
		System.out.printf("this is methhod ma @ {0} \n", new Date());
		
	}

	@Override
	public void mb() {
		System.out.printf("this is methhod mb @ %s \n", new Date());
		
	}

	
}
