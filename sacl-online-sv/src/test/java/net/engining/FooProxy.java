package net.engining;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class FooProxy implements InvocationHandler{
	
	private Foo foo;
	
	public FooProxy(Foo foo) {
		this.foo = foo;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		
		System.out.println("in proxy invok before");
		Object result = method.invoke(foo, args);
		System.out.println("in proxy invok after");
		return result;
	}
	
}
