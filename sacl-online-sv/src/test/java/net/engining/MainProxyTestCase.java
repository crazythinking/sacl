package net.engining;

import java.lang.reflect.Proxy;

public class MainProxyTestCase {
	
	public void serviceA(Foo fooImpl) {
		Foo foo = (Foo) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {Foo.class}, new FooProxy(fooImpl));
		foo.mb();
	}
	
	public void serviceB(Foo fooImpl) {
		Foo foo = (Foo) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[] {Foo.class}, new FooProxy(fooImpl));
		foo.mb();
	}
	
	//	动态代理，要有4个要素。 目标接口，InvocationHandler，用Proxy创建一个对象;
	//	还有个第四要素，目标接口的实现类，通常这个类是我们不能改的，而我们有想要增强的；
	//	调用的顺序service (调用方的代码) ->  proxy.bar() -> invoke() -> 自己动过的手脚 -> 真正的FooImpl的方法
	public static void main(String[] args) {
		MainProxyTestCase obj = new MainProxyTestCase();
		obj.serviceA(new FooImpl());
		obj.serviceB(new FooImpl());

	}
}
